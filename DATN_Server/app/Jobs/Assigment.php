<?php

namespace App\Jobs;

use Illuminate\Bus\Queueable;
use App\Models\TeacherPhase;
use App\Models\StudentPhase;
use App\Models\Proposal;
use App\Models\Project;
use Illuminate\Contracts\Queue\ShouldBeUnique;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Http\Request;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Bus\Batchable;

class Assigment implements ShouldQueue
{
    use Batchable,Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    protected $phase_id;

    public function __construct($phase)
    {
        //
        $this->phase_id = $phase;
        // $this->queue = "newjob";

    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        //
        $students =StudentPhase::where('phase_id',$this->phase_id)->whereNotIn('student_id',Proposal::where(['phase_id'=>$this->phase_id,"status"=>1])->pluck("student_id"))->get();
            $slot = TeacherPhase::where(['phase_id'=>$this->phase_id])->sum('available_quota');
            if(count($students)>$slot){
                $error = "Không đủ hạn ngạch cho sinh viên! Vui lòng bổ sung giảng viên";
                return  redirect(route('mngPhase.show',["mngPhase"=>$this->phase_id,"error"=>$error]));
            }
// LAY DS GV TRONG DOT
// LAY DS PROJECT TRONG DOT
// SHORT GV
            foreach($students as $student){
                // LAY GV O DAU
                // CHECK PROJECT FIND STUDENT ID IF NULL THI TAO
                $teacher = TeacherPhase::where(['phase_id'=>$this->phase_id])->where('available_quota','>',0)->orderBy('available_quota','DESC')->first();
                // check project hien tai
                $project = Project::where(["student_id"=>$student->student_id,'phase_id'=>$this->phase_id])->first();
                if(is_null($project))
                    $project = Project::create([
                        'name'=>"Chưa tạo dự án",
                        'quest'=>"Chưa tạo dự án",
                        'solution'=>"Chưa tạo dự án",
                        'tech'=>"Chưa tạo dự án",
                        'func'=>"Chưa tạo dự án",
                        'result'=>"Chưa tạo dự án",
                        'student_id'=>$student->student_id,
                        'phase_id'=>$this->phase_id
                    ]);


                $proposal = Proposal::where([
                    'student_id'=>$student->student_id,
                    'phase_id'=>$this->phase_id,
                    'project_id'=>$project->id,
                    'teacher_id'=>$teacher->teacher_id
                ])->first();

                if(is_null($proposal)){
                    $proposal = Proposal::create([
                        'student_id'=>$student->student_id,
                        'phase_id'=>$this->phase_id,
                        'project_id'=>$project->id,
                        'teacher_id'=>$teacher->teacher_id,
                        'status'=>1
                    ]);
                }else{
                    $proposal->status = 1;
                    $proposal->save();
                }

                // xoa proposal
                Proposal::where([
                    'student_id'=>$student->student_id,
                    'phase_id'=>$this->phase_id,
                    'project_id'=>$project->id
                ])->where('status','<>',1)->delete();
                // giam quota teacher, cap nhat bang teacher
                $teacher->available_quota -= 1;
                $teacher->save();
            }

    }
}
