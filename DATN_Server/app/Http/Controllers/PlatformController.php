<?php

namespace App\Http\Controllers;

use App\Models\Platform;
use Illuminate\Http\Request;

class PlatformController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $platforms = Platform::paginate(12);
        $message = isset($_GET['message'])?$_GET['message']:"";
        return view("platform.list",compact('platforms','message'));
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
        Platform::create($request->all());
        session(["message"=>"Thêm thành công!"]);
        return redirect(route("mngPlatform.index"));
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Platform  $platform
     * @return \Illuminate\Http\Response
     */
    public function show(Platform $platform)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Platform  $platform
     * @return \Illuminate\Http\Response
     */
    public function edit(Platform $platform)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Platform  $platform
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Platform $mngPlatform)
    {
        //
        $mngPlatform->update($request->all());
        session(["message"=>"Sửa thành công!"]);
        return redirect(route("mngPlatform.index"));
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Platform  $platform
     * @return \Illuminate\Http\Response
     */

    public function remove(Request $request){
        Platform::where("id",$request->platform_id)->delete();
        session(["message"=>"Xoá thành công!"]);
        return redirect(route("mngPlatform.index"));
    }

    public function destroy(Platform $platform)
    {
        //
    }
}
