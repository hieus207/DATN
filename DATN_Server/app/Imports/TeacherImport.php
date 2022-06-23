<?php

namespace App\Imports;

use App\Models\Teacher;
use App\Models\Account;
use App\Models\Major;
use App\Models\TeacherPhase;
use Maatwebsite\Excel\Concerns\ToModel;

class TeacherImport implements ToModel
{
    private $phase_id;

    public function __construct($phase_id)
    {
        $this->phase_id = $phase_id;
    }

    public function model(array $row)
    {
        if(!is_null($row[3])){
            if($row[3]!="Mã giảng viên"){
                $account = Account::firstOrCreate(
                    //
                    ['username' => "gv_".$row[3]],
                    ['role' => 1,
                    // Ngày sinh gì đấy
                    'password' => md5($row[8]),
                    'token' => ""]
                );

                $major = Major::firstOrCreate(["name"=>$row[2]])->first();
                // $time = strtotime($row[8]);
                $UNIX_DATE = ($row[8] - 25569) * 86400;
                $teacher = Teacher::firstOrCreate(
                    ["email" => $row[3]."@gv.tlu.edu.vn",
                    "teacher_id"=>$account->id],
                    [
                    "major_id" =>$major->id,
                    "teacher_name" =>$row[4],
                    "avt" =>"",
                    "birthday"=>gmdate("Y-m-d", $UNIX_DATE),
                    "phone"=>"",
                    "title"=>trim(mb_strtoupper($row[7],'UTF-8')),
                    "topic" =>""
                ]);

                // ADD TO PHASE
                $quota = 0;
                switch (trim(mb_strtoupper($row[7],'UTF-8'))) {
                    case "GIẢNG VIÊN":
                        $quota = 3;
                       break;
                    case "THẠC SĨ":
                        $quota = 4;
                        break;
                    case "TIẾN SĨ":
                        $quota = 5;
                        break;
                    case "PHÓ GIÁO SƯ":
                        $quota = 5;
                        break;
                    case "GIÁO SƯ":
                        $quota = 6;
                        break;
                    default:
                        $quota = 0;
                        break;
                }
                TeacherPhase::firstOrCreate([
                    "teacher_id"=>$account->id,
                    "phase_id"=>$this->phase_id],
                    [
                    "quota" => $quota,
                    "available_quota" => $quota,
                    "review_std" => 0,
                ]);
            }
        }

    }
}
