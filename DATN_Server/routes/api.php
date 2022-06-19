<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\StudentController;
use App\Http\Controllers\Api\ProjectController;
use App\Http\Controllers\Api\PlatformController;
use App\Http\Controllers\Api\MajorController;
use App\Http\Controllers\Api\TeacherController;
use App\Http\Controllers\Api\ProposalController;
use App\Http\Controllers\Api\ScheduleController;
use App\Http\Controllers\Api\SubmissionController;
use App\Models\Schedule;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::apiResource('account',AccountController::class);
Route::post('account/changePwd',[AccountController::class,'changePwd'])->middleware('onlyOwner:account');
Route::post('account/login',[AccountController::class,'login']);


Route::apiResource('student',StudentController::class)->except(['update']);
Route::apiResource('student',StudentController::class)->only(['update'])->middleware('onlyOwner:student');

Route::apiResource('project',ProjectController::class)->except(['update','destroy','store']);
Route::apiResource('project',ProjectController::class)->only(['update','destroy'])->middleware('onlyStudentOwner');
Route::apiResource('project',ProjectController::class)->only(['store'])->middleware('onlyStudent');

Route::post('project/getProject',[ProjectController::class,'getProject']);
// FOR TEST
Route::post('checkPhase',[ProjectController::class,'checkPhase']);

Route::get('getHomeProject',[ProjectController::class,'getHomeProject']);
Route::get('getPhaseTeacher',[TeacherController::class,'getPhaseTeacher']);

Route::get('getHomeInfo',[TeacherController::class,'getHomeInfo']);
Route::get('getPhase',[ScheduleController::class,'get_Phase']);
Route::get('getStage',[ScheduleController::class,'getStage']);



Route::apiResource('platform',PlatformController::class);
// TRUOC KHI MD
Route::apiResource('major',MajorController::class)->only(['index']);

Route::apiResource('submit',SubmissionController::class);
Route::apiResource('teacher',TeacherController::class)->except(['update']);
Route::apiResource('teacher',TeacherController::class)->only(['update'])->middleware('onlyOwner:teacher');

Route::apiResource('proposal',ProposalController::class)->except(['store']);
Route::apiResource('proposal',ProposalController::class)->only(['store'])->middleware('onlyStudent');

Route::post('proposal/accept',[ProposalController::class,'acceptProject'])->middleware('onlyTeacherOwner');

Route::get('applyProject',[ProposalController::class,'applyProject']);
Route::get('getStudentReview',[ProposalController::class,'getStudentReview']);
Route::get('getTeacherCouncil',[ProposalController::class,'getTeacherCouncil']);

Route::apiResource('schedule',ScheduleController::class)->except(['update','destroy','store']);

Route::apiResource('schedule',ScheduleController::class)->only(['update','destroy'])->middleware('onlyStudentOwner');
Route::apiResource('schedule',ScheduleController::class)->only(['store'])->middleware('onlyStudent');
Route::post('schedule/proposeSchedule',[ScheduleController::class,'proposeSchedule'])->middleware('onlyStudentOwner');

Route::get('teacherSchedule',[ScheduleController::class,'getTeacherSchedule']);
Route::get('getPhaseTime',[ScheduleController::class,'getPhaseTime']);
Route::put('schedule/teacherUpdate/{schedule}',[ScheduleController::class,'teacherUpdate'])->middleware('onlyTeacherOwner');
Route::post('schedule/acceptAllSchedule',[ScheduleController::class,'acceptAllSchedule'])->middleware('onlyTeacherOwner');
Route::post('schedule/refuseAllSchedule',[ScheduleController::class,'refuseAllSchedule'])->middleware('onlyTeacherOwner');
