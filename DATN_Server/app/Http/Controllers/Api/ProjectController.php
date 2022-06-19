<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Project;
use App\Models\Phase;
use App\Models\Platform;
use App\Models\ProjectPlatform;
use App\Models\Schedule;
use App\Models\Stage;
use App\Models\StudentPhase;
use App\Models\Submission;
use App\Models\Teacher;
use Illuminate\Http\Request;

class ProjectController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
    }

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

    public function getProject(Request $request){
        // request->id token
        $status = false;
        $data = null;
        $plf = '';
        $phase_id = $this->getPhase();
        if($phase_id>0){
            $project = Project::where(['student_id'=>$request->id,'phase_id'=>$phase_id])->first();
            if(!is_null($project)){
                $platforms = ProjectPlatform::where('project_id',$project->id)->pluck('platform_id');
                foreach($platforms as $platform){
                    $plf = $plf.Platform::find($platform)->name.", ";
                }
                $plf=trim($plf);
                $status=true;
                $project->platforms = $plf;
                $submit = Submission::where(['project_id'=>$project->id])->first();
                $project->link = $submit==null?"": $submit->link;
                $data=$project;
            }
        }

        $res = array(
            'status'=>$status,
            'data' => $data
        );

        return json_encode($res);
    }

    public function getHomeProject(){
        $phase_id = $this->getPhase();

        $now = now()->toDateString();
        $schedule = null;
        $teacher = null;
        $project = null;
        $sp = StudentPhase::where(['phase_id'=>$phase_id,"student_id"=>$_GET['student_id']])->first()==null?false:true;
        if(isset($_GET['student_id'])){
            $project = Project::where(['student_id'=>$_GET['student_id'],'phase_id'=>$phase_id])->first();
            $schedule = Schedule::where(['student_id'=>$_GET['student_id'],'phase_id'=>$phase_id,'status'=>2])->where('reportDate','>=',$now)->orderBy('reportDate','ASC')->first();
            if(!is_null($schedule)){
                $teacher = Teacher::find($schedule->teacher_id);
            }
        }
        $res = array(
            'project' =>$project,
            'teacher' =>$teacher,
            'schedule' =>$schedule,
            'phase' => $sp,
        );

        return json_encode($res);
    }
    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        // onlyStudent
        // CHeck student project exist
        $status=false;
        try{
            $phase_id = $this->getPhase();
            $project = Project::create(array_merge($request->except('platforms','token'),["phase_id"=>$phase_id]));
            foreach($request->platforms as $platform_id){
                ProjectPlatform::create(["platform_id"=>(int)$platform_id,"project_id"=>$project->id]);

            }
            $status=true;
        }
        catch(\Exception $e){
        }
        return json_encode($status);

    }

    public function show(Project $project)
    {
        //


    }

    public function checkPhase(){
        $phase_id = $this->getPhase();
        if($phase_id!=-1){
            $stages = Stage::where("phase_id",$phase_id)->orderBy('startDate','ASC')->get();
            $now = now()->toDateString();
            $stage = null;
            $stt = -1;
            foreach($stages as $key=>$value){
                if($value->startDate<=$now){
                    $stt = $key;
                    $stage = $value;
                }
            }
            if($stt>2){
                return false;
            }
        }
        return true;
    }

    public function update(Request $request, Project $project)
    {
        //onlyStudentOwner
        $status=false;
        if($this->checkPhase())
        try{
            // CHECK stage of phase <3

            $project->update($request->except('platforms','token'));
            if(!is_null($request->platforms)){
                ProjectPlatform::where("project_id",$project->id)->delete();
                foreach($request->platforms as $platform_id){
                    ProjectPlatform::create(["platform_id"=>(int)$platform_id,"project_id"=>$project->id]);
                }
            }
            $status=true;
        }
        catch(\Exception $e){
        }
        return json_encode($status);
    }

    public function destroy(Project $project)
    {
        //
    }
}
