<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AccountController;
use App\Http\Controllers\MajorController;
use App\Http\Controllers\StudentController;
use App\Http\Controllers\TeacherController;
use App\Http\Controllers\PhaseController;
use App\Http\Controllers\PlatformController;
use App\Models\Platform;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});


Route::get('/login', function () {
    return view('admin.login');
});
Route::post('/login',[AccountController::class,'login'])->name('admin.login');

Route::get('/logout',[AccountController::class,'logout'])->name("logout");


Route::group(['middleware'=>'onlyAdmin'],function(){
    Route::resource('/admin',AccountController::class);
    Route::resource('/mngStudent',StudentController::class);
    Route::resource('/mngTeacher',TeacherController::class);
    Route::resource('/mngPhase',PhaseController::class);
    Route::resource('/mngMajor',MajorController::class);
    Route::resource('/mngPlatform',PlatformController::class);

    Route::post('/mngStudent/manageAction',[StudentController::class,'manageAction'])->name('mngStudent.manageAction');
    Route::post('/mngTeacher/manageAction',[TeacherController::class,'manageAction'])->name('mngTeacher.manageAction');
    Route::post('/mngPhase/manageAction',[PhaseController::class,'manageAction'])->name('mngPhase.manageAction');

    Route::post('/mngPhase/search',[PhaseController::class,'search'])->name('mngPhase.search');

    Route::post('/mngPhase/createCouncil',[PhaseController::class,'createCouncil'])->name('mngPhase.createCouncil');
    Route::post('/mngPhase/deleteCouncil',[PhaseController::class,'deleteCouncil'])->name('mngPhase.deleteCouncil');
    Route::post('/mngPhase/insertToCouncil',[PhaseController::class,'insertToCouncil'])->name('mngPhase.insertToCouncil');
    Route::post('/mngPhase/removeFromCouncil',[PhaseController::class,'removeFromCouncil'])->name('mngPhase.removeFromCouncil');
    Route::post('/mngPhase/addProjectToReview',[PhaseController::class,'addProjectToReview'])->name('mngPhase.addProjectToReview');


    Route::post('/mngPhase/insertStudent',[PhaseController::class,'insertStudent'])->name('mngPhase.insertStudent');
    Route::post('/mngPhase/searchStudent',[PhaseController::class,'searchStudent'])->name('mngPhase.searchStudent');
    Route::get('/mngPhase/searchStudent/resultSearchStd',[PhaseController::class,'resultSearchStd'])->name('mngPhase.resultSearchStd');
    Route::post('/mngPhase/removeStudent',[PhaseController::class,'removeStudent'])->name('mngPhase.removeStudent');

    Route::post('/mngPhase/insertTeacher',[PhaseController::class,'insertTeacher'])->name('mngPhase.insertTeacher');
    Route::post('/mngPhase/searchTeacher',[PhaseController::class,'searchTeacher'])->name('mngPhase.searchTeacher');
    Route::get('/mngPhase/searchTeacher/resultSearchTch',[PhaseController::class,'resultSearchTch'])->name('mngPhase.resultSearchTch');
    Route::post('/mngPhase/removeTeacher',[PhaseController::class,'removeTeacher'])->name('mngPhase.removeTeacher');


    Route::post('/mngStudent/search',[StudentController::class,'search'])->name('mngStudent.search');
    Route::get('/mngStudent/search/resultSearch',[StudentController::class,'resultSearch'])->name('mngStudent.resultSearch');

    Route::post('/mngTeacher/search',[TeacherController::class,'search'])->name('mngTeacher.search');
    Route::get('/mngTeacher/search/resultSearch',[TeacherController::class,'resultSearch'])->name('mngTeacher.resultSearch');

    Route::get('/mngPhase/showStudent/{phase}',[PhaseController::class,'showStudent'])->name('mngPhase.showStudent');
    Route::get('/mngPhase/showTeacher/{phase}',[PhaseController::class,'showTeacher'])->name('mngPhase.showTeacher');
    Route::get('/mngPhase/listCouncil/{phase}',[PhaseController::class,'listCouncil'])->name('mngPhase.listCouncil');
    Route::get('/mngPhase/showCouncil/{phase}',[PhaseController::class,'showCouncil'])->name('mngPhase.showCouncil');



    Route::post('/mngMajor/remove',[MajorController::class,'remove'])->name('mngMajor.remove');
    Route::post('/mngPlatform/remove',[PlatformController::class,'remove'])->name('mngPlatform.remove');

});

