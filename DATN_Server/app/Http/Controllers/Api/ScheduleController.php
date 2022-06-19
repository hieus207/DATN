<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Proposal;
use App\Models\Schedule;
use App\Models\Phase;
use App\Models\Stage;
use App\Models\Student;
use Illuminate\Http\Request;

class ScheduleController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        // THIEU STATUS
        $result=[];
        $phase_id =$this->getPhase();

        if(!is_null($_GET['student_id'])){
            $teacher_id = Proposal::where(['student_id'=>$_GET['student_id'],'phase_id'=>$phase_id,"status"=>1])->first()->teacher_id;

            if($_GET['type']=="all")
                $result = Schedule::where(['student_id'=>$_GET['student_id'],'phase_id'=>$phase_id,"teacher_id"=>$teacher_id])->orderBy('reportDate','ASC')->get();
            else
            if($_GET['type']=="pending")
                $result = Schedule::where(['student_id'=>$_GET['student_id'],'phase_id'=>$phase_id,"teacher_id"=>$teacher_id])->where('status',">",0)->where('status',"<",3)->orderBy('reportDate','ASC')->get();
        }
        return json_encode($result);
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

    public function get_Phase(){
        return json_encode(Phase::find($this->getPhase()));
    }

    public function getStage(){
        $phase_id = $this->getPhase();
        $stt = -1;
        if($phase_id!=-1){
            $stages = Stage::where("phase_id",$phase_id)->orderBy('startDate','ASC')->get();
            $now = now()->toDateString();
            foreach($stages as $key=>$value){
                if($value->startDate<=$now){
                    $stt = $key;
                }
            }
        }
        return json_encode($stt);
    }

    public function proposeSchedule(Request $request){
        // CO TOKEN
        // onlystudentOwner
        $phase_id=$this->getPhase();
        $status = false;
        try{
            Schedule::where(["student_id"=>$request->student_id,"phase_id"=>$phase_id])->where("status","<>",2)->update(array('status' => 1));
            $status=true;
        }
        catch(\Exception $ex){

        }

        return json_encode($status);
    }

    function checkDuplicate($student_id,$phase_id,$reportDate){
        $schedule = Schedule::where(['student_id'=>$student_id,'phase_id'=>$phase_id,'reportDate'=>$reportDate])->first();
        return is_null($schedule);
    }

    public function store(Request $request)
    {
        // onlystudent
        $status = false;
        $phase_id =$this->getPhase();
        if($this->checkDuplicate($request->student_id,$phase_id,$request->reportDate))
        try{
            $teacher_id = Proposal::where(['student_id'=>$request->student_id,'phase_id'=>$phase_id,'status'=>1])->first()->teacher_id;

            Schedule::create(array_merge(["teacher_id"=>$teacher_id,"phase_id"=>$phase_id],$request->except('token')));
            $status = true;
        }
        catch(\Exception $ex){

        }
        return json_encode($status);

    }

    public function getTeacherSchedule(){
        $phase_id =$this->getPhase();
        $result = [];
        $schedules = Schedule::where(["teacher_id"=>$_GET["teacher_id"],"phase_id"=>$phase_id,"status"=>2])->orderBy('reportDate','ASC')->get();
        foreach($schedules as $schedule){
            $schedule->student_name = Student::where("student_id",$schedule->student_id)->first()->student_name;
            array_push($result,$schedule);
        }
        return json_encode($result);
    }


    public function getPhaseTime(){
        return Phase::find($this->getPhase());
    }

    public function show(Schedule $schedule)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Schedule  $schedule
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Schedule $schedule)
    {
        //onlystudentowner
        // CHECK ROLE BEFORE UPDATE
        $status = false;
        $check = true;
        $phase_id =$this->getPhase();
        if($request->reportDate!=$schedule->reportDate){
            if(!$this->checkDuplicate($schedule->student_id,$phase_id,$request->reportDate))
                $check = false;
        }
        if($check)
        try{
            if($schedule->status!=2){
                $schedule->update($request->except('token'));
                $status=true;
            }
        }
        catch(\Exception $ex){

        }

        return json_encode($status);
    }


    public function teacherUpdate(Request $request, Schedule $schedule){
        // onlyteacherowner
        $status = false;
        try{
            if($schedule->status!=2){
                $schedule->status = $request->status;
                $schedule->refuseContent = $request->refuseContent;
                $schedule->save();
                $status=true;
            }
        }
        catch(\Exception $ex){

        }
        return json_encode($status);
    }

    public function acceptAllSchedule(Request $request){
        // onlyteacherowner
        $status = false;
        try{
            $schedules = Schedule::where(["student_id"=>$request->student_id,"teacher_id"=>$request->teacher_id,"status"=>1])->get();
            foreach($schedules as $schedule){
                $schedule->status = 2;
                $schedule->save();
            }
            $status = true;
        }
        catch(\Exception $ex){

        }
        return json_encode($status);
    }

    public function refuseAllSchedule(Request $request){
        // onlyteacherowner
        $status = false;
        try{
            $schedules = Schedule::where(["student_id"=>$request->student_id,"teacher_id"=>$request->teacher_id,"status"=>1])->get();
            foreach($schedules as $schedule){
                $schedule->status = 3;
                $schedule->refuseContent = $request->refuseContent;
                $schedule->save();
            }
            $status = true;
        }
        catch(\Exception $ex){

        }
        return json_encode($status);
    }
    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Schedule  $schedule
     * @return \Illuminate\Http\Response
     */
    public function destroy(Request $request, Schedule $schedule)
    {
        //

        $status = false;
        try{
            $schedule->token = $request->token;
            $schedule->delete();
            $status=true;
        }
        catch(\Exception $ex){

        }

        return json_encode($status);
    }
}
