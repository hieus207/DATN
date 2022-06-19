<?php

namespace App\Http\Middleware;

use App\Models\Account;
use Closure;
use Illuminate\Http\Request;

class onlyStudent
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
        if(!is_null($request->token)){
            $now = now()->toDateString();
            $user = Account::where(["token"=>$request->token,"role"=>0])->where("deadline",">=",$now)->first();
            if(!is_null($user))
                return $next($request);
        }
        else
            return json_encode(["error"=>"access denied"]);
    }
}
