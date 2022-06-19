<?php

namespace App\Http\Controllers;

use App\Models\Teacher;
use App\Models\Account;
use App\Models\Major;
use App\Models\Student;
use Illuminate\Http\Request;

class TeacherController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $message = isset($_GET['message'])?$_GET['message']:"";

        $teachers=Teacher::paginate(12);
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        return view('teacher.list',compact('teachers','majors','message'));
    }

    public function search(Request $request){


        return redirect(route('mngTeacher.resultSearch',["p_name"=>$request->p_name]));
    }

    public function resultSearch(){
        $p_name = "";
        if(isset($_GET['p_name']))
            $p_name = $_GET['p_name'];
            $teachers = Teacher::where('teacher_name','LIKE','%'.$p_name.'%')->paginate(12);
            $teachers->appends(['p_name'=>$p_name]);
            $majors = array();
            foreach(Major::all() as $major){
                $majors[$major->id] = $major->name;
            }
            return view('teacher.list',compact('teachers','majors'));
    }


    public function manageAction(Request $request){
        if($request->action == "delete"){
            foreach($request->teacher_id as $teacher_id){
                Teacher::find($teacher_id)->delete();
            }
            session(["message"=>"Xoá thành công!"]);
            return redirect(route('mngTeacher.index'));
        }
        if($request->action == "update"){
            // return redirect(route('mngStudent.edit', ['mngStudent' => $request->student_id[0]]));
            return $this->edit(Teacher::find($request->teacher_id[0]));
        }
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
        $majors = Major::all();
        return view('teacher.create',compact('majors'));
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
        $usn = Account::where("username",$request->username)->first();
        $email = Teacher::where("email",$request->email)->first();
        $majors = Major::all();
        $errors = array();
        if(is_null($request->teacher_name)){
            array_push($errors,"Không được để trống tên");
        }
        if(!is_null($usn)){
            array_push($errors,"Trùng tên tài khoản");
        }
        if(!is_null($email)){
            array_push($errors,"Email đã tồn tại");
        }
        if(sizeof($errors)>0){
            $data = $request;
            return view('teacher.create',compact('majors','errors','data'));
        }

        if(is_null(Account::where('username',$request->username)->first())){
            $request->password = md5($request->password);
            $account = Account::create(array_merge(["role"=>1],['username'=>$request->username,'password'=>$request->password]));
            Teacher::create(array_merge(["teacher_id"=>$account->id],$request->except(['username','password'])));
            session(["message"=>"Tạo thành công!"]);
            return redirect(route('mngTeacher.index'));
        }
        else
            return redirect(route('mngTeacher.index'));
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Teacher  $teacher
     * @return \Illuminate\Http\Response
     */
    public function show(Teacher $teacher)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Teacher  $teacher
     * @return \Illuminate\Http\Response
     */
    public function edit(Teacher $teacher)
    {
        $majors = Major::all();
        $account = Account::find($teacher->teacher_id);
        return view("teacher.edit",compact("teacher","account","majors"));
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Teacher  $teacher
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request,$teacher_id)
    {
        //

        $email = Teacher::where("email",$request->email)->where("teacher_id","<>",$teacher_id)->first();
        $majors = Major::all();
        $errors = array();
        if(is_null($request->teacher_name)){
            array_push($errors,"Không được để trống tên");
        }
        if(!is_null($email)){
            array_push($errors,"Email đã tồn tại");
        }


        $teacher = Teacher::find($teacher_id);
        $account = Account::find($teacher_id);
        if(isset($request->isEditAccount))
        if($request->isEditAccount=="true"){
            $usn = Account::where("username",$request->username)->where("id","<>",$teacher_id)->first();
            if(!is_null($usn)){
                array_push($errors,"Trùng tên tài khoản");
            }
            if(sizeof($errors)==0)
            {
                $request->password = md5($request->password);
                Account::where('id',$teacher->teacher_id)->first()->update(['username'=>$request->username,'password'=>$request->password]);
            }
        }
        if(sizeof($errors)>0){
            $teacher = $request;
            $teacher->teacher_id = $teacher_id;
            return view('teacher.edit',compact('teacher','majors','account','errors'));
        }
        $teacher->update($request->except(['username','password','isEditAccount']));
        session(["message"=>"Sửa thành công!"]);
        return redirect(route('mngTeacher.index'));
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Teacher  $teacher
     * @return \Illuminate\Http\Response
     */
    public function destroy(Teacher $teacher)
    {
        //
    }
}
