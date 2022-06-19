<?php

namespace App\Http\Controllers;

use App\Models\Account;
use Illuminate\Http\Request;

class AccountController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    private function genToken($acc){
        return substr(md5($acc->password.$acc->username.$acc->role.now()->toDateString()),12);
    }

    public function index()
    {
        //
    }

    public function login(Request $request){
        $account = Account::where(["username"=>$request->username,"password"=>md5($request->password),"role"=>2])->first();
        if(!is_null($account)){
            if($account->deadline<now()->toDateString()){
                $account->token=$this->genToken($account);
                $account->deadline=now()->addDay()->toDateString();
                $account->save();
            }

            session(["token"=>$account->token]);
            session(["role"=>$account->role]);

            return view('admin.dashboard');
        }
        else
            return view('admin.login');
    }

    public function logout(){
        session()->forget("token");
        session()->forget("role");
        return view('admin.login');
    }

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
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Account  $account
     * @return \Illuminate\Http\Response
     */
    public function show(Account $account)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Account  $account
     * @return \Illuminate\Http\Response
     */
    public function edit(Account $account)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Account  $account
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Account $account)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Account  $account
     * @return \Illuminate\Http\Response
     */
    public function destroy(Account $account)
    {
        //
    }
}
