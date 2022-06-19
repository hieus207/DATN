<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
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
        $status = false;
        $data = null;
        $acc = Account::all()->first();
        if(!is_null($acc)){
            $status=true;
            $data = $acc;
        }
        $res = array(
            'status'=>$status,
            'data' => $data
        );
        return json_encode($res);
        //
    }



    public function login(Request $request)
    {
        $status = false;
        $data = null;
        $acc = Account::where('username',$request->username)->where('password',$request->password)->first();
        if(!is_null($acc)){
            // CHECK TIME TOKEN
            if($acc->deadline<now()->toDateString()){
                $acc->token=$this->genToken($acc);
                $acc->deadline=now()->addDay()->toDateString();
                $acc->save();
            }

            $status=true;
            $data = $acc;
        }
        $res = array(
            'status'=>$status,
            'data' => $data
        );

        return json_encode($res);
    }

    public function changePwd(Request $request){
        $account = Account::where(['id'=>$request->id,'password'=>md5($request->oldPwd)])->first();
        $account->password = md5($request->newPwd);
        $account->save();
        return json_encode(true);
    }

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
