<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Major;
use App\Models\Student;
use Illuminate\Http\Request;
use Illuminate\Support\Arr;

class StudentController extends Controller
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
        $students=Student::paginate(12);
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        return view('student.list',compact('students','majors','message'));
    }

    public function search(Request $request){
        return redirect(route('mngStudent.resultSearch',["p_name"=>$request->p_name]));
    }

    public function resultSearch(){
        $p_name = "";
        if(isset($_GET['p_name']))
            $p_name = $_GET['p_name'];
        $students = Student::where('student_name','LIKE','%'.$p_name.'%')->paginate(12);
        $students->appends(['p_name'=>$p_name]);
        $majors = array();
        foreach(Major::all() as $major){
            $majors[$major->id] = $major->name;
        }
        return view('student.list',compact('students','majors'));
    }
    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */

    public function manageAction(Request $request){
        if($request->action == "delete"){
            foreach($request->student_id as $student_id){
                Student::find($student_id)->delete();
            }
            session(["message"=>"Xoá thành công!"]);
            return redirect(route('mngStudent.index'));
        }
        if($request->action == "update"){
            // return redirect(route('mngStudent.edit', ['student' => Student::find($request->student_id[0])]));
            return $this->edit(Student::find($request->student_id[0]));

        }
    }

    public function create()
    {
        //
        $majors = Major::all();
        return view('student.create',compact('majors'));
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $usn = Account::where("username",$request->username)->first();
        $email = Student::where("email",$request->email)->first();
        $majors = Major::all();
        $errors = array();
        if(!is_null($usn)){
            array_push($errors,"Trùng tên tài khoản");
        }
        if(!is_null($email)){
            array_push($errors,"Email đã tồn tại");
        }
        if(sizeof($errors)>0){
            $data = $request;
            return view('student.create',compact('majors','errors','data'));
        }

        if(is_null(Account::where('username',$request->username)->first())){
            $request->password = md5($request->password);
            $account = Account::create(['username'=>$request->username,'password'=>$request->password,'role'=>0]);
            Student::create(array_merge(["student_id"=>$account->id],$request->except(['username','password'])));
            session(["message"=>"Tạo thành công!"]);
            return redirect(route('mngStudent.index'));
        }
        else
            return redirect(route('mngStudent.index'));
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Student  $student
     * @return \Illuminate\Http\Response
     */
    public function show(Student $student)
    {
        //

    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Student  $student
     * @return \Illuminate\Http\Response
     */
    public function edit(Student $student)
    {
        //
        $majors = Major::all();
        $account = Account::find($student->student_id);
        return view("student.edit",compact("student","account","majors"));
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Student  $student
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request,$student_id)
    {
        //

        $student = Student::find($student_id);
        $account = Account::find($student->student_id);
        $majors = Major::all();
        $errors = array();

        if(isset($request->isEditAccount))
        if($request->isEditAccount=="true"){
            $duplicate = Account::where("username",$request->username)->where("id","<>",$student_id)->first();
            if(is_null($duplicate)){
                $request->password = md5($request->password);
                // dd($request->password);
                $account=Account::where('id',$student->student_id)->first()->update(['username'=>$request->username,'password'=>$request->password]);

            }
            else{
                $error = "Tên tài khoản đã bị trùng!";
                array_push($errors,$error);
            }
        }

        // check trung email
        $duplicate = Student::where("email",$request->email)->where("student_id","<>",$student_id)->first();
        if(is_null($duplicate)){
            $student->update($request->except(['username','password','isEditAccount']));
            session(["message"=>"Sửa thành công!"]);
            return redirect(route('mngStudent.index'));
        }
        else{
            $error = "Email đã được sử dụng vui lòng chọn email khác!";
            array_push($errors,$error);
        }
        return view("student.edit",compact("student","account","majors","errors"));
    }



    public function destroy(Student $student)
    {
        //
    }
}
