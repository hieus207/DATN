<?php

namespace App\Http\Middleware;

use Closure;
use App\Models\Account;
use App\Models\Project;
use App\Models\Proposal;
use App\Models\Schedule;
use Illuminate\Http\Request;

class onlyTeacherOwner
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
        if(!is_null($request->id)||!is_null($request->teacher_id)){
            if(!is_null($request->id)&&!is_null($request->token)){
                // hoac proposal hoac lich
                $now = now()->toDateString();
                $user = Account::where(["token"=>$request->token,"role"=>1])->where("deadline",">=",$now)->first();
                $prp = Proposal::where(["id"=>$request->id,"teacher_id"=>$user->id])->first();
                $sch = Schedule::where(["id"=>$request->id,"teacher_id"=>$user->id])->first();
                if(!is_null($prp)||!is_null($sch)){
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
