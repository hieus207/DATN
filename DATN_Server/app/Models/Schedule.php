<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Schedule extends Model
{
    use HasFactory;
    protected $fillable = [
        'student_id',
        'teacher_id',
        'phase_id',
        'reportDate',
        'reportContent'
    ];

    protected $primaryKey = 'id';
}
