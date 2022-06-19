<?php

namespace App\Jobs;

use App\Models\Proposal;
use App\Models\StudentPhase;
use App\Models\TeacherPhase;
use App\Models\TeacherReviewer;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldBeUnique;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;

class Review implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    protected $phase_id=-1;
    public function __construct($phase)
    {
        //
        $this->phase_id = $phase;
        $this->queue = "newjob";
    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        //
        $students = StudentPhase::where("phase_id",$this->phase_id)->get();
        foreach($students as $student){
            $dup_std = TeacherReviewer::where(["student_id"=>$student->student_id,"phase_id"=>$this->phase_id])->first();
            if(is_null($dup_std)){
                $tc = Proposal::where(["student_id"=>$student->student_id,"phase_id"=>$this->phase_id])->first();
                $dup_id = -1;
                if(!is_null($tc)){
                    $dup_id=$tc->teacher_id;
                }
                $tp = TeacherPhase::where(["phase_id"=>$this->phase_id])->where("teacher_id","<>",$dup_id)->orderBy('review_std','ASC')->first();
                TeacherReviewer::create(["teacher_id"=>$tp->teacher_id,"student_id"=>$student->student_id,"phase_id"=>$this->phase_id]);
                $tp->review_std +=1;
                $tp->save();
            }
        }
    }
}
