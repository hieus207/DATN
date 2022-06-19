<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Student extends Model
{
    use HasFactory;

    public $primaryKey = 'student_id';

    protected $fillable = [
        'student_id',
        'student_name',
        'email',
        'birthday',
        'phone',
        'cv',
        'avt',
        'major_id'
    ];

    public function marjor()
    {
        return $this->belongsTo(Major::class,'id','major_id');
    }

    public function account(){
        return $this->belongsTo(Account::class,'id','student_id');
    }
}
