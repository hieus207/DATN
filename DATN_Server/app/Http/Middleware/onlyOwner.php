<?php

namespace App\Http\Middleware;

use App\Models\Account;
use Closure;
use Illuminate\Http\Request;

class onlyOwner
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure(\Illuminate\Http\Request): (\Illuminate\Http\Response|\Illuminate\Http\RedirectResponse)  $next
     * @return \Illuminate\Http\Response|\Illuminate\Http\RedirectResponse
     */
    public function handle(Request $request, Closure $next,$role_name)
    {
        if($role_name=="student"){
            $now = now()->toDateString();
            $user = Account::where(["token"=>$request->token,"role"=>0,'id'=>$request->route('student')->student_id])->where("deadline",">=",$now)->first();
            if(!is_null($user))
                return $next($request);
        }
        if($role_name=="teacher"){
            $now = now()->toDateString();
            $user = Account::where(["token"=>$request->token,"role"=>1,'id'=>$request->route('teacher')->teacher_id])->where("deadline",">=",$now)->first();
            if(!is_null($user))
                return $next($request);
        }
        if($role_name=="account"){
            $now = now()->toDateString();
            $user = Account::where(["token"=>$request->token,'id'=>$request->id])->where("deadline",">=",$now)->first();
            if(!is_null($user))
                return $next($request);
        }
        return json_encode(["error"=>"access denied"]);
    }
}
