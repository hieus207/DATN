<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Phase;
use App\Models\Project;
use App\Models\Stage;
use App\Models\Submission;
use Illuminate\Http\Request;

class SubmissionController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
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
        return $stt;
    }

    public function index()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
        $result = false;

        if($this->getStage()==4)
        try{
            $phase_id = $this->getPhase();

            $project = Project::where(['student_id'=>$request->student_id,"phase_id"=>$phase_id])->first();

            if(!is_null($project)){

                Submission::updateOrCreate(
                    ['project_id' => $project->id, 'phase_id' => $phase_id],
                    ['link' => $request->link]
                );
            }
            $result = true;
        }
        catch(\Exception $e){

        }
        return json_encode($result);
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Submission  $submission
     * @return \Illuminate\Http\Response
     */
    public function show(Submission $submission)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Submission  $submission
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Submission $submission)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Submission  $submission
     * @return \Illuminate\Http\Response
     */
    public function destroy(Submission $submission)
    {
        //
    }
}
