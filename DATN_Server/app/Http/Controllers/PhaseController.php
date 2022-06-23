<?php

namespace App\Http\Controllers;

use App\Imports\StudentImport;
use App\Imports\TeacherImport;
use App\Jobs\Assigment;
use App\Jobs\Assignment;
use App\Jobs\Review;
use App\Models\Council;
use App\Models\CouncilReview;
use App\Models\Major;
use App\Models\Phase;
use App\Models\Project;
use App\Models\Proposal;
use App\Models\Stage;
use App\Models\Student;
use App\Models\StudentPhase;
use App\Models\Submission;
use App\Models\Teacher;
use App\Models\TeacherCouncil;
use App\Models\TeacherPhase;
use App\Models\TeacherReviewer;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Bus;
use Maatwebsite\Excel\Concerns\ToArray;
use Maatwebsite\Excel\Facades\Excel;

use function PHPUnit\Framework\isNull;

class PhaseController extends Controller
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
    public function index()
    {
        //

        $phases=Phase::paginate(12);
        $message = isset($_GET['message'])?$_GET['message']:"";
        return view('phase.list',compact('phases','message'));
    }

    public function search(Request $request){
        $phases = Phase::where('name','LIKE','%'.$request->p_name.'%')->paginate(12);
        return view('phase.list',compact('phases'));
    }

    public function showStudent(Phase $phase){
        $message = isset($_GET['message'])?$_GET['message']:"";
        $students = Student::whereIn("student_id",StudentPhase::where(['phase_id'=>$phase->id])->pluck('student_id'))->paginate(12);
        $Allstudents = Student::whereNotIn("student_id",StudentPhase::where(['phase_id'=>$phase->id])->pluck('student_id'))->get();
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        foreach($students as $student){
            $project = Project::where(["phase_id"=>$phase->id,"student_id"=>$student->student_id])->first();
            $student->link="CHƯA NỘP";
            if(!is_null($project)){
                $submit = Submission::where(["phase_id"=>$phase->id,"project_id"=>$project->id])->first();
                if(!is_null($submit))
                    $student->link = $submit->link;
            }
        }
        return view('phase.student',compact('Allstudents','students','majors','phase','message'));
    }


    public function searchStudent(Request $request){
        return redirect(route('mngPhase.resultSearchStd',["phase_id"=>$request->phase_id,"p_name"=>$request->p_name,"filter"=>$request->filter]));
    }
    // call  search get

    public function resultSearchStd(){
        // get param
        $message = isset($_GET['message'])?$_GET['message']:"";
        $phase_id = isset($_GET['phase_id'])?$_GET['phase_id']:-1;
        $p_name = isset($_GET['p_name'])?$_GET['p_name']:"";
        $filter = isset($_GET['filter'])?$_GET['filter']:"All";

        $Allstudents = Student::whereNotIn("student_id",StudentPhase::where(['phase_id'=>$phase_id])->pluck('student_id'))->get();
        $phase = Phase::find($phase_id);

        if($filter=="All")
            $students = Student::whereIn("student_id",StudentPhase::where(['phase_id'=>$phase_id])->pluck('student_id'))->where("student_name","LIKE","%".$p_name."%")->paginate(12);
        else if($filter=="Not"){
            // $project = array();
            // $project = Project::whereNotIn("id",Submission::where(['phase_id'=>$phase_id])->pluck('project_id'))->pluck('student_id');
            // // SV chua tao du an k search dc

            // $students = Student::whereIn("student_id",$project)->where("student_name","LIKE","%".$p_name."%")->paginate(12);

            $all_student = Student::whereIn("student_id",StudentPhase::where(['phase_id'=>$phase_id])->pluck('student_id'))->where("student_name","LIKE","%".$p_name."%")->pluck('student_id');
            $project = array();
            $project = Project::whereIn("id",Submission::where(['phase_id'=>$phase_id])->pluck('project_id'))->pluck('student_id');

            $submit_students = Student::whereIn("student_id",$project)->where("student_name","LIKE","%".$p_name."%")->pluck('student_id');
            $students = Student::whereIn("student_id",$all_student)->whereNotIn("student_id",$submit_students)->paginate(12);

        } else if($filter == "Submited"){
            $project = array();
            $project = Project::whereIn("id",Submission::where(['phase_id'=>$phase_id])->pluck('project_id'))->pluck('student_id');

            $students = Student::whereIn("student_id",$project)->where("student_name","LIKE","%".$p_name."%")->paginate(12);
        }

        foreach($students as $student){
            $project = Project::where(["phase_id"=>$phase->id,"student_id"=>$student->student_id])->first();
            $student->link="CHƯA NỘP";
            if(!is_null($project)){
                $submit = Submission::where(["phase_id"=>$phase->id,"project_id"=>$project->id])->first();
                if(!is_null($submit))
                    $student->link = $submit->link;
            }
        }

        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        $students->appends(['phase_id' => $phase_id,'p_name'=>$p_name,'filter'=>$filter]);

        return view('phase.student',compact('Allstudents','students','majors','phase','message'));
    }

    public function insertStudent(Request $request){
        foreach($request->import_student_id as $student_id){
            StudentPhase::create(["phase_id"=>$request->phase_id,"student_id"=>$student_id]);
        }
        session(["message"=>"Thêm sinh viên vào đợt thành công!"]);
        return redirect(route("mngPhase.showStudent",['phase'=>$request->phase_id]));
    }
    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */

    public function removeStudent(Request $request){
        foreach($request->student_id as $student_id){
            StudentPhase::where(["student_id"=>$student_id,"phase_id"=>$request->phase_id])->delete();
            // CHECK QUOTA
            $proposal = Proposal::where(["student_id"=>$student_id,"phase_id"=>$request->phase_id,"status"=>1])->first();

            if(!is_null($proposal)){
                $teacher_id = $proposal->teacher_id;
                $tp = TeacherPhase::where(["phase_id"=>$request->phase_id,"teacher_id"=>$teacher_id])->first();
                $tp->available_quota+=1;
                $tp->save();
            }
            Proposal::where(["student_id"=>$student_id,"phase_id"=>$request->phase_id])->delete();
        }
        session(["message"=>"Xoá sinh viên khỏi đợt thành công!"]);
        return redirect(route("mngPhase.showStudent",['phase'=>$request->phase_id]));
    }

    public function showTeacher(Phase $phase){
        $message = isset($_GET['message'])?$_GET['message']:"";
        $teachers = Teacher::whereIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase->id])->pluck('teacher_id'))->paginate(12);
        $Allteachers = Teacher::whereNotIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase->id])->pluck('teacher_id'))->get();
        foreach($teachers as $teacher){
            $tp = TeacherPhase::where(['teacher_id'=>$teacher->teacher_id,"phase_id"=>$phase->id])->first();
            $teacher->available_quota = $tp->available_quota;
            $teacher->quota = $tp->quota;
        }
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        return view('phase.teacher',compact('Allteachers','teachers','majors','phase','message'));
    }


    public function searchTeacher(Request $request){
        return redirect(route('mngPhase.resultSearchTch',["phase_id"=>$request->phase_id,"p_name"=>$request->p_name]));
    }
    // call  search get

    public function resultSearchTch(){
        // get param
        $message = isset($_GET['message'])?$_GET['message']:"";
        $phase_id = isset($_GET['phase_id'])?$_GET['phase_id']:-1;
        $p_name = isset($_GET['p_name'])?$_GET['p_name']:"";
        $Allteachers = Teacher::whereNotIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase_id])->pluck('teacher_id'))->get();
        $phase = Phase::find($phase_id);
        // dd($phase);
        $teachers = Teacher::whereIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase_id])->pluck('teacher_id'))->where("teacher_name","LIKE","%".$p_name."%")->paginate(12);
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        foreach($teachers as $teacher){
            $tp = TeacherPhase::where(['teacher_id'=>$teacher->teacher_id,"phase_id"=>$phase->id])->first();
            $teacher->available_quota = $tp->available_quota;
            $teacher->quota = $tp->quota;
        }
        $teachers->appends(['phase_id' => $phase_id,'p_name'=>$p_name]);

        return view('phase.teacher',compact('Allteachers','teachers','majors','phase','message'));
    }

    public function insertTeacher(Request $request){
        foreach($request->import_teacher_id as $teacher_id){
            $quota = 0;
            switch (Teacher::find($teacher_id)->title) {
                case "GIẢNG VIÊN":
                    $quota = 3;
                   break;
                case "THẠC SĨ":
                    $quota = 4;
                    break;
                case "TIẾN SĨ":
                    $quota = 5;
                    break;
                case "GIÁO SƯ":
                    $quota = 6;
                    break;
                default:
                    $quota = 0;
                    break;
            }
            TeacherPhase::create(["phase_id"=>$request->phase_id,"teacher_id"=>$teacher_id,"quota"=>$quota,"available_quota"=>$quota]);
        }
        session(["message"=>"Thêm giảng viên vào đợt thành công!"]);
        return redirect(route("mngPhase.showTeacher",['phase'=>$request->phase_id]));
    }
    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */

    public function removeTeacher(Request $request){
        foreach($request->teacher_id as $teacher_id){
            TeacherPhase::where(["teacher_id"=>$teacher_id,"phase_id"=>$request->phase_id])->delete();
            Proposal::where(["teacher_id"=>$teacher_id,"phase_id"=>$request->phase_id])->delete();
        }
        session(["message"=>"Xoá giảng viên khỏi đợt thành công!"]);
        return redirect(route("mngPhase.showTeacher",['phase'=>$request->phase_id]));
    }



    public function manageAction(Request $request){
        $errors = array();
        if($request->action == "delete"){
            foreach($request->phase_id as $phase_id){
                Phase::find($phase_id)->delete();
            }
            session(["message"=>"Xoá đợt thành công!"]);
            return redirect(route('mngPhase.index'));
        }
        if($request->action == "update"){
            // return redirect(route('mngStudent.edit', ['mngStudent' => $request->student_id[0]]));
            return $this->edit(Phase::find($request->phase_id[0]));
        }
        if($request->action =="import"){
            if($request->fileStudent)
                Excel::import(new StudentImport($request->phase_id), $request->fileStudent);
            if($request->fileTeacher)
                Excel::import(new TeacherImport($request->phase_id), $request->fileTeacher);
                // sửa lại đường dẫn
            session(["message"=>"Nhập vào đợt thành công!"]);
            return redirect(route('mngPhase.show',["mngPhase"=>$request->phase_id]));
        }
        if($request->action == "assignment"){
            Bus::batch([
                new Assigment($request->phase_id)
            ])->onQueue('newjob')
            ->dispatch();

            // Assigment::dispatch($request->phase_id);
            session(["message"=>"Đã lên lịch phân công hướng dẫn!"]);
            return  redirect(route('mngPhase.show',["mngPhase"=>$request->phase_id]));
        }
        if($request->action == "review"){
            Review::dispatch($request->phase_id);
            session(["message"=>"Đang thực thi phân công phản biện!"]);
            return  redirect(route('mngPhase.show',["mngPhase"=>$request->phase_id]));
        }
    }

    public function create()
    {
        //
        return view('phase.create');
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */

    public function checkPhase($rq,$phase_id=-1){

        $phases = Phase::all();
        if($phase_id!=-1)
            $phases = Phase::where("id","<>",$phase_id)->get();

        $rs = true;
        $error = "";
        $errors = array();
        if($rq->startDate>$rq->endDate)
            {
                $rs = false;
                $error = "Ngày bắt đầu đợt đang lớn hơn ngày kết thúc!";
                array_push($errors,$error);
            }

        if($rs)
        foreach($phases as $phase){
            if($phase->startDate<=$rq->startDate&&$phase->endDate>=$rq->startDate){
                $rs = false;
                $error = "Vui lòng thử lại! Trùng thời gian các đợt khác";
                array_push($errors,$error);
                break;
            }
            if($phase->startDate<=$rq->endDate&&$phase->endDate>=$rq->endDate)
            {
                $rs = false;
                $error = "Vui lòng thử lại! Trùng thời gian các đợt khác";
                array_push($errors,$error);
                break;
            }
        }

        $check = array(
            "rs" => $rs,
            "errors" => $errors
        );
        return $check;
    }
    public function store(Request $request)
    {
        $check =  $this->checkPhase($request);
        if($check['rs'])
            $phase = Phase::create($request->all());
        else{
            $errors = $check['errors'];
            $data = $request->all();
            return view('phase.create',compact('data','errors'));
        }
        if($request->isEditStage=="checkedValue")
            for($i=0;$i<sizeof($request->stageName);$i++){
                Stage::create(array("phase_id"=>$phase->id,"name"=>$request->stageName[$i],"startDate"=>$request->stageStartDate[$i],"endDate"=>$request->stageEndDate[$i]));
            }
        session(["message"=>"Thêm đợt thành công!"]);
        return redirect(route('mngPhase.index'));
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


    public function show(Phase $mngPhase)
    {
        //
        $message = isset($_GET['message'])?$_GET['message']:"";
        $errors = array();
        $error = isset($_GET['error'])?$_GET['error']:"";
        if(!empty($error)){
            array_push($errors,$error);
        }
        // $message = isset($_GET['message'])?$_GET['message']:"";
        $mngPhase->totalQuota = TeacherPhase::where('phase_id',$mngPhase->id)->sum('quota');
        $mngPhase->totalQuotaAvailable = TeacherPhase::where('phase_id',$mngPhase->id)->sum('available_quota');
        $mngPhase->totalTeacher = TeacherPhase::where('phase_id',$mngPhase->id)->count();
        $mngPhase->totalStudent = StudentPhase::where('phase_id',$mngPhase->id)->count();
        $mngPhase->totalStudentAvailable = $mngPhase->totalStudent - ($mngPhase->totalQuota - $mngPhase->totalQuotaAvailable);
        $mngPhase->totalStudentNotReview = $mngPhase->totalStudent - TeacherReviewer::where("phase_id",$mngPhase->id)->count();
        $mngPhase->stage = -1;

        if($this->getPhase()==$mngPhase->id){
            $mngPhase->stage = $this->getStage();
            // session(['message'=>"day la stage ".$this->getStage()]);
        }

        return view('phase.show',compact('mngPhase','message','errors'));
    }


    public function listCouncil(Phase $phase){
        $councils = Council::where("phase_id",$phase->id)->paginate(12);
        // $Allteachers = Teacher::whereIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase->id])->whereNotIn('teacher_id',TeacherCouncil::whereIn("council_id",Council::where("phase_id",$phase->id)->pluck('id'))->pluck('teacher_id'))->pluck('teacher_id'))->get();
        // $majors = array();
        // foreach(Major::all() as $major){
        //     $majors[$major->id] = $major->name;
        // }
        return view('phase.listCouncil',compact('councils','phase'));
    }

    public function showCouncil(Phase $phase,Request $request){
        $council = Council::find($request->council_id);
        $Allteachers = Teacher::whereIn("teacher_id",TeacherPhase::where(['phase_id'=>$phase->id])->whereNotIn('teacher_id',TeacherCouncil::whereIn("council_id",Council::where("phase_id",$phase->id)->pluck('id'))->pluck('teacher_id'))->pluck('teacher_id'))->get();
        // Lay duoc cac project da nop chua phan cong
        $Allprojects = Project::whereIn("id",Submission::where('phase_id',$phase->id)->pluck('project_id'))->whereNotIn('id',CouncilReview::where('phase_id',$phase->id)->pluck('project_id'))->get();
        foreach($Allprojects as $project){
            $project->student_name = Student::find($project->student_id)->student_name;
            $project->link = Submission::where(['project_id'=>$project->id,"phase_id"=>$phase->id])->first()->link;
        }

        $projects = Project::whereIn("id",CouncilReview::where(['council_id'=>$request->council_id,'phase_id'=>$phase->id])->pluck('project_id'))->paginate(15,['*'], 'project_page');
        foreach($projects as $project){
            $project->student_name = Student::find($project->student_id)->student_name;
            $project->link = Submission::where(['project_id'=>$project->id,"phase_id"=>$phase->id])->first()->link;
        }

        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        $teachers = Teacher::whereIn("teacher_id",TeacherCouncil::whereIn("council_id",Council::where('phase_id',$phase->id)->pluck('id'))->pluck('teacher_id'))->paginate(15,['*'], 'teacher_page');

        $teachers->appends(['council_id' => $council->id]);
        $projects->appends(['council_id' => $council->id]);
        return view('phase.showCouncil',compact('phase','council','teachers','majors','projects','Allteachers','Allprojects'));
    }


    public function createCouncil(Request $request){
        Council::create($request->all());
        session(["message"=>"Tạo hội đồng thành công!"]);
        return redirect(route("mngPhase.listCouncil",['phase'=>$request->phase_id]));
    }

    public function deleteCouncil(Request $request){
        foreach($request->council_id as $council_id){
            // StudentPhase::where(["student_id"=>$student_id,"phase_id"=>$request->phase_id])->delete();
            // Proposal::where(["student_id"=>$student_id,"phase_id"=>$request->phase_id])->delete();
            TeacherCouncil::where("council_id",$council_id)->delete();
            Council::find($council_id)->delete();
        }
        session(["message"=>"Xoá hội đồng khỏi đợt thành công!"]);
        return redirect(route("mngPhase.listCouncil",['phase'=>$request->phase_id]));
    }

    public function insertToCouncil(Request $request){
        foreach($request->import_teacher_council_id as $teacher_id){
            TeacherCouncil::create(["council_id"=>$request->council_id,"teacher_id"=>$teacher_id]);
        }
        session(["message"=>"Thêm giảng viên vào hội đồng thành công!"]);
        return redirect(route("mngPhase.showCouncil",['phase'=>$request->phase_id,'council_id'=>$request->council_id]));
    }

    public function removeFromCouncil(Request $request){
        $council = Council::find($request->council_id);
        if($request->action=="delete_teacher"){
            foreach($request->teacher_id as $teacher_id){
                TeacherCouncil::where(["teacher_id"=>$teacher_id,"council_id"=>$request->council_id])->delete();
            }
            session(["message"=>"Xoá giảng viên khỏi hội đồng thành công thành công!"]);
        }
        if($request->action=="delete_project"){
            foreach($request->project_id as $project_id){
                CouncilReview::where(["project_id"=>$project_id,"council_id"=>$request->council_id])->delete();
                $council->maxQuota +=1;
            }
            $council->save();
            session(["message"=>"Xoá đề tài khỏi hội đồng thành công thành công!"]);
        }
        return redirect(route("mngPhase.showCouncil",['phase'=>$request->phase_id,'council_id'=>$request->council_id]));

    }

    public function addProjectToReview(Request $request){
        $council = Council::find($request->council_id);
        if($council->maxQuota>0){
            $council->maxQuota-=1;
            $council->save();
            foreach($request->import_project_id as $project_id){
                CouncilReview::create(["phase_id"=>$request->phase_id,"council_id"=>$request->council_id,"project_id"=>$project_id]);
            }
            session(["message"=>"Nhập đề tài cho hội đồng thành công!"]);
        }else{
            session(["error"=>'Số đề tài phê duyệt cho hội đồng đã đạt tối đa']);
        }

        return redirect(route("mngPhase.showCouncil",['phase'=>$request->phase_id,'council_id'=>$request->council_id]));
    }


    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Phase  $phase
     * @return \Illuminate\Http\Response
     */
    public function edit(Phase $mngPhase)
    {
        //
        $stages = Stage::where('phase_id',$mngPhase->id)->get();
        return view("phase.edit",compact("mngPhase",'stages'));
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Phase  $phase
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Phase $mngPhase)
    {
        $check =  $this->checkPhase($request,$mngPhase->id);

        if($check['rs']){
            $mngPhase->update($request->all());
            if($request->isEditStage=="checkedValue"){
                Stage::where('phase_id',$mngPhase->id)->delete();
                for($i=0;$i<sizeof($request->stageName);$i++){
                    Stage::create(array("phase_id"=>$mngPhase->id,"name"=>$request->stageName[$i],"startDate"=>$request->stageStartDate[$i],"endDate"=>$request->stageEndDate[$i]));
                }
            }
            session(["message"=>"Sửa đợt thành công!"]);
            return redirect(route('mngPhase.index'));
        }
        else{
            $errors = $check['errors'];
            $data = $request->all();
            $stages = Stage::where('phase_id',$mngPhase->id)->get();
            return view('phase.edit',compact('mngPhase','errors','stages'));
        }


    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Phase  $phase
     * @return \Illuminate\Http\Response
     */
    public function destroy(Phase $phase)
    {
        //
    }

}
