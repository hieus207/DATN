<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TeacherCouncil extends Model
{
    use HasFactory;
    protected $fillable = [
        'teacher_id',
        'council_id'
    ];
}
