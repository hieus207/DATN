<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Account;
use App\Models\Major;
use App\Models\Student;
use Illuminate\Http\Request;
use Illuminate\Support\Str;

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
        $acc = Account::create($request->only(['username', 'password']));

        return $acc->student()->create(array_merge(['student_id'=>$acc->id],$request->all()));
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
        $student->major=Major::find($student->major_id)->name;
        return $student;
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Student  $student
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Student $student)
    {
        //
        $status = false;
        try{
            if(!is_null($request->phone))
                $student->update(["phone"=>$request->phone]);
            if(!is_null($request->cv))
                $student->update(["cv"=>$request->cv]);
            $avt_url = '';
            $status = true;
            if(!is_null($request->file('avt')))
            {
                $now = now()->valueOf();
                $avt = $now.Str::random(40).'.jpg';
                $avt_url = $request->root()."/storage/avt/".$avt;
                $request->file('avt')->storeAs('public/avt',$avt);
                $student->update(array_merge($request->except('avt'),["avt"=> $avt_url]));
            }
            else
                $student->update($request->all());

        }catch(\Exception $e){

        }

        return json_encode($status);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Student  $student
     * @return \Illuminate\Http\Response
     */
    public function destroy(Student $student)
    {
        //
        return $student->delete();
    }
}
