<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Teacher extends Model
{
    use HasFactory;

    protected $fillable = [
        'teacher_id',
        'teacher_name',
        'email',
        'birthday',
        'phone',
        'topic',
        'avt',
        'title',
        'major_id'
    ];

    protected $primaryKey = 'teacher_id';
}
