<?php

namespace App\Http\Controllers;

use App\Models\Major;
use Illuminate\Http\Request;

class MajorController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $majors = Major::paginate(12);
        $message = isset($_GET['message'])?$_GET['message']:"";
        return view('major.list',compact('majors','message'));
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
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
        Major::create($request->all());
        session(["message"=>"Thêm thành công!"]);
        return redirect(route("mngMajor.index"));
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Major  $major
     * @return \Illuminate\Http\Response
     */
    public function show(Major $major)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Major  $major
     * @return \Illuminate\Http\Response
     */
    public function edit(Major $major)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Major  $major
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Major $mngMajor)
    {
        // dd("abc");
        //
        $mngMajor->update($request->all());
        session(["message"=>"Sửa thành công!"]);
        return redirect(route("mngMajor.index"));
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Major  $major
     * @return \Illuminate\Http\Response
     */
    public function remove(Request $request){
        Major::where("id",$request->major_id)->delete();
        session(["message"=>"Xoá thành công!"]);
        return redirect(route("mngMajor.index"));
    }

    public function destroy(Major $major)
    {
        //

    }
}
