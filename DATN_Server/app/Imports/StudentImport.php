<?php

namespace App\Imports;

use App\Models\Account;
use App\Models\Major;
use App\Models\Student;
use App\Models\StudentPhase;
use Maatwebsite\Excel\Concerns\ToModel;

class StudentImport implements ToModel
{

    private $phase_id;

    public function __construct($phase_id)
    {
        $this->phase_id = $phase_id;
    }

    public function model(array $row)
    {
        if(!is_null($row[3])){
            if($row[3]!="Mã sinh viên"){
                $UNIX_DATE = ($row[8] - 25569) * 86400;
                $birthday = gmdate("Y-m-d", $UNIX_DATE);
                $account = Account::firstOrCreate(
                    ['username' => "sv_".$row[3]],
                    ['role' => 0,
                    'password' => md5($birthday),
                    'token' => ""]
                );

                $major = Major::firstOrCreate(["name"=>$row[2]])->first();
                // if(is_null($major))
                // {
                //     $major = Major::create(["name"=>$row[2]]);
                // }

                $student = Student::firstOrCreate(
                    ["email" => $row[3]."@e.tlu.edu.vn",
                    "student_id"=>$account->id],
                    [
                    "major_id" =>$major->id,
                    "student_name" =>$row[4],
                    "avt" => "",
                    'birthday'=>$birthday,
                    'phone'=>"",
                    'cv'=>"",
                ]);

                // ADD TO PHASE
                StudentPhase::firstOrCreate([
                    "student_id"=>$account->id,
                    "phase_id"=>$this->phase_id
                ]);
            }
        }
    }
}
