<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Middleware\Authenticate;
use App\Models\Platform;
use App\Models\Major;
use App\Models\Teacher;
use App\Models\Phase;
use App\Models\Project;
use App\Models\Proposal;
use App\Models\Schedule;
use App\Models\Student;
use App\Models\TeacherPhase;
use App\Models\TeacherPlatform;
use Illuminate\Http\Request;
use Illuminate\Support\Str;
use function PHPSTORM_META\map;

class TeacherController extends Controller
{

    public function getPhase(){
        $phases= Phase::orderBy('startDate','DESC')->get();
        // $phase= Phase::orderBy('startDate','DESC')->first();
        $now = now()->toDateString();
        foreach($phases as $phase){
            if($phase->startDate<=$now&&$now<=$phase->endDate){
                return $phase->id;
            }
        }
        return -1;
    }

    public function getPhaseTeacher(){
        $teacher_id = isset($_GET['teacher_id'])?$_GET['teacher_id']:-1;
        $phase_id = $this->getPhase();
        $tp = TeacherPhase::where(["teacher_id"=>$teacher_id,"phase_id"=>$phase_id])->first();
        if($tp==null)
        $tp =-1;
        else
        $tp = $tp->phase_id;
        return json_encode($tp);
    }

    public function getHomeInfo(){
        $phase_id = $this->getPhase();
        $now = now()->toDateString();
        if(isset($_GET['teacher_id'])){
            $students = Student::whereIn("student_id",Proposal::where(["teacher_id"=>$_GET['teacher_id'],"phase_id"=>$phase_id,"status"=>1])->pluck("student_id"))->get();
            foreach($students as $student){
                $student->project_name = Project::where(["student_id"=>$student->student_id,"phase_id"=>$phase_id])->first()->name;
                $schedule = Schedule::where(["student_id"=>$student->student_id,"phase_id"=>$phase_id,"teacher_id"=>$_GET['teacher_id'],"status"=>2])->where('reportDate','>=',$now)->orderBy('reportDate','ASC')->first();
                $student->reportDate = null;
                $student->reportContent =null;
                if(!is_null($schedule)){
                    $student->reportDate = $schedule->reportDate;
                    $student->reportContent = $schedule->reportContent;
                }
            }
        }
        return json_encode($students);
    }

    public function index()
    {
        //
        $result = [];
        $phase_id = $this->getPhase();

        $teachers = Teacher::whereIn('teacher_id',TeacherPhase::where('phase_id',$phase_id)->pluck('teacher_id'))->get();
        foreach($teachers as $teacher){
            $plf = '';
            $platforms = TeacherPlatform::where('teacher_id',$teacher->teacher_id)->pluck('platform_id');
            foreach($platforms as $platform){
                $plf = $plf.Platform::find($platform)->name.", ";
            }
            $tp = TeacherPhase::where(['teacher_id'=>$teacher->teacher_id,'phase_id'=>$phase_id])->first();
            $teacher->quota = $tp->quota;
            $teacher->available_quota = $tp->available_quota;
            $plf=trim($plf);
            $teacher->platforms = $plf;
            $teacher->major=Major::find($teacher->major_id)->name;
            array_push($result,$teacher);

        }
        return $result;
    }



    public function store(Request $request)
    {
        //
    }

    public function show(Teacher $teacher)
    {
        //
        $status = false;
        $data = [];
        try{
            $teacher->major = Major::find($teacher->major_id)->name;
            $platforms = TeacherPlatform::where('teacher_id',$teacher->teacher_id)->get();
            foreach ($platforms as $platform){
                $teacher->platforms .= Platform::find($platform->platform_id)->name.",";
            }

        }
        catch(\Exception $ex){

        }
        return $teacher;
    }

    public function update(Request $request, Teacher $teacher)
    {
        //
        $status=false;
        try{
            if(!is_null($request->file('avt')))
            {
                $now = now()->valueOf();
                $avt = $now.Str::random(40).'.jpg';
                $avt_url = $request->root()."/storage/avt/".$avt;
                $request->file('avt')->storeAs('public/avt',$avt);
                $teacher->update(array_merge($request->except('avt'),["avt"=> $avt_url]));
            }
            else
                $teacher->update($request->except('platforms','token'));


            if(!is_null($request->platforms)){
                TeacherPlatform::where("teacher_id",$teacher->teacher_id)->delete();
                foreach($request->platforms as $platform_id){
                    TeacherPlatform::create(["platform_id"=>(int)$platform_id,"teacher_id"=>$teacher->teacher_id]);
                }
            }
            $status=true;
        }
        catch(\Exception $e){
        }
        return json_encode($status);
    }

    public function destroy(Teacher $teacher)
    {
        //
    }
}
