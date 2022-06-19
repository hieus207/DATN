<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Council;
use App\Models\CouncilReview;
use App\Models\Proposal;
use App\Models\Project;
use App\Models\Phase;
use App\Models\Teacher;
use App\Models\Student;
use App\Models\TeacherPhase;
use App\Models\StudentPhase;
use App\Models\TeacherCouncil;
use App\Models\TeacherReviewer;
use Illuminate\Http\Request;

class ProposalController extends Controller
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

    public function index()
    {
        //
        $result=[];
        if(!is_null($_GET['id'])){
            $phase_id = $this->getPhase();
            if($_GET['type']=="student")
                $proposals = Proposal::where(["student_id"=>$_GET['id'],"phase_id"=>$phase_id])->get();
            else if($_GET['type']=="teacher")
                    if($_GET['status']==1)
                        $proposals = Proposal::where(["teacher_id"=>$_GET['id'],"phase_id"=>$phase_id,"status"=>1])->get();
                    else
                        $proposals = Proposal::where(["teacher_id"=>$_GET['id'],"phase_id"=>$phase_id])->where("status","<>",1)->get();

            foreach($proposals as $proposal){
                $proposal->project_name = Project::find($proposal->project_id)->name;
                $proposal->teacher_name = Teacher::where(["teacher_id"=>$proposal->teacher_id])->first()->teacher_name;
                $proposal->student_name = Student::find($proposal->student_id)->student_name;
                array_push($result,$proposal);
            }
        }
        return json_encode($result);
    }

    public function getStudentReview(){
        $result=[];
        $phase_id = $this->getPhase();
        if(!is_null($_GET['teacher_id'])){
            $tr = TeacherReviewer::where(["phase_id"=>$phase_id,"teacher_id"=>$_GET['teacher_id']])->pluck("student_id");
            $project_name = "";
            $teacher_name = "";
            $student_name = "";
            $status = 5;
            foreach($tr as $student_id){
                $project_name = Project::where(["student_id"=>$student_id,"phase_id"=>$phase_id])->first()->name;
                $teacher_name = Teacher::find($_GET['teacher_id'])->teacher_name;
                $student = Student::find($student_id);
                if(!is_null($project_name)&&!is_null($teacher_name)&&!is_null($student_name)){
                    $rs = array(
                        "project_name"=>$project_name,
                        "teacher_name"=>$teacher_name,
                        "student_id" =>$student->student_id,
                        "student_name"=>$student->student_name,
                        "status"=>$status
                    );
                    array_push($result,$rs);
                }
            }

        }
        return json_encode($result);
    }

    public function getTeacherCouncil(){
        $result=[];
        $phase_id = $this->getPhase();
        if(!is_null($_GET['teacher_id'])){
            $councils = Council::where(["phase_id"=>$phase_id])->get();
            $tc = TeacherCouncil::where("teacher_id",$_GET['teacher_id'])->whereIn('council_id',$councils->pluck('id'))->first();
            // dd($phase_id);
            if(is_null($tc))
                return json_encode($result);

            $cr = CouncilReview::where("council_id",$tc->council_id)->pluck('project_id');

            $project_name = "";
            $teacher_name = "";
            $student_name = "";
            $status = 5;

            foreach($cr as $project_id){
                $project = Project::find($project_id);
                $project_name = $project->name;
                $teacher_name = Teacher::find($_GET['teacher_id'])->teacher_name;
                $student = Student::find($project->student_id);
                if(!is_null($project_name)&&!is_null($teacher_name)&&!is_null($student_name)){
                    $rs = array(
                        "project_name"=>$project_name,
                        "teacher_name"=>$teacher_name,
                        "student_id" =>$student->student_id,
                        "student_name"=>$student->student_name,
                        "status"=>$status
                    );
                    array_push($result,$rs);
                }
            }

        }
        return json_encode($result);
    }

    public function applyProject(Request $request){
        // onlystudentOwner
        $result=[];
        if(!is_null($_GET['id'])){
            $phase_id = $this->getPhase();
            $project = Project::where(['student_id'=>$_GET['id'],'phase_id'=>$phase_id])->first();
            if(!is_null($project)){
                $result=[
                    'project_name'=>$project->name,
                    'project_id' =>$project->id
                ];
            }
        }
        return json_encode($result);
    }


    public function beforeProposal($request){
        $phase_id = $this->getPhase();
        // Check sv
        $proposal = Proposal::where(["student_id"=>$request->student_id,"phase_id"=>$phase_id,"status"=>1])->first();
        if(!is_null($proposal))
           return false;
        $teacherPhase = TeacherPhase::where(["teacher_id"=>$request->teacher_id,"phase_id"=>$phase_id])->first();
        $studentPhase = StudentPhase::where(["student_id"=>$request->student_id,"phase_id"=>$phase_id])->first();
        if(!is_null($teacherPhase)&&!is_null($studentPhase))
            if($teacherPhase->available_quota>0)
                if(!is_null(Project::where(["student_id"=>$request->student_id,"phase_id"=>$phase_id])))
                    return true;
        return false;
        // CHECK GIANG VIEN
        // CON HAN NGACH VA TRONG PHASE
        // CHECK STUDENT
        // CHU CUA PROJECT VA PROJECT TRONG PHASE STUDENT TRONG PHASE
        // CHECK PROJECT
    }
    public function store(Request $request)
    {
        // onlyStudent
        $status = false;
        $phase_id = $this->getPhase();

        try{
            if($this->beforeProposal($request)){
                Proposal::create(array_merge(["phase_id"=>$phase_id],$request->except('token')));
                $status = true;
            }

        }
        catch(\Exception $ex){

        }
        return json_encode($status);
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Proposal  $proposal
     * @return \Illuminate\Http\Response
     */
    public function show(Proposal $proposal)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Proposal  $proposal
     * @return \Illuminate\Http\Response
     */

    public function acceptProject(Request $request){
        // onlyteacherowner
        $status = false;
        try{
            $phase_id = $this->getPhase();
            $proposal = Proposal::where(["student_id"=>$request->student_id,"teacher_id"=>$request->teacher_id,"phase_id"=>$phase_id,"project_id"=>$request->project_id])->first();
            if($request->type=="accept")
            {
                if($this->beforeProposal($request)){
                    $proposal->status = 1;
                    $proposal->save();
                    Proposal::where(["student_id"=>$request->student_id,"phase_id"=>$phase_id])->where('status','<>',1)->delete();
                    $tphase = TeacherPhase::where(["teacher_id"=>$request->teacher_id,"phase_id"=>$phase_id])->first();
                    $tphase->available_quota -=1;
                    $tphase->save();
                }
            }
            else if($request->type == "refuse"){
                $proposal->status = 2;
                $proposal->save();
            }
            $status = true;
        }
        catch(\Exception $ex){

        }
        return json_encode($status);
    }

    public function update(Request $request, Proposal $proposal)
    {
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Proposal  $proposal
     * @return \Illuminate\Http\Response
     */
    public function destroy(Proposal $proposal)
    {
        //
    }
}
