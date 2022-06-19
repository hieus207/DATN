<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Account extends Model
{
    use HasFactory;

    protected $fillable = [
        'username',
        'role',
        'password',
        'token'
    ];
    protected $hidden = [
        'password'
    ];

    public function student(){
        return $this->hasOne(Student::class,'student_id','id');
    }
}
