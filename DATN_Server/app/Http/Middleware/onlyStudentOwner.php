<?php

namespace App\Http\Middleware;

use App\Models\Account;
use App\Models\Project;
use App\Models\Schedule;
use Closure;
use Illuminate\Http\Request;

class onlyStudentOwner
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure(\Illuminate\Http\Request): (\Illuminate\Http\Response|\Illuminate\Http\RedirectResponse)  $next
     * @return \Illuminate\Http\Response|\Illuminate\Http\RedirectResponse
     */
    public function handle(Request $request, Closure $next)
    {
        $status = true;
        if(!is_null($request->id)||!is_null($request->student_id)){
            if(!is_null($request->project_id)){
                $now = now()->toDateString();
                $user = Account::where(["token"=>$request->token,'role'=>0])->where("deadline",">=",$now)->first();
                $prj = Project::where(["id"=>$request->project_id,"student_id"=>$user->id])->first();
                if(!is_null($prj))
                    $status=true;
                else
                    $status=false;
            }

            if(!is_null($request->id)&&!is_null($request->token)){
                // hoac project hoac lich
                $now = now()->toDateString();
                $user = Account::where(["token"=>$request->token,'role'=>0])->where("deadline",">=",$now)->first();
                $prj = Project::where(["id"=>$request->id,"student_id"=>$user->id])->first();
                $sch = Schedule::where(["id"=>$request->id,"student_id"=>$user->id])->first();
                if(!is_null($prj)||!is_null($sch)){
                    $status = true;
                }
                else
                    $status = false;
            }
        }
        if($status)
            return $next($request);
        else
            return json_encode(["error"=>"access denied"]);

    }
}
