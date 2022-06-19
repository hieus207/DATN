<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TeacherReviewer extends Model
{
    use HasFactory;
    protected $fillable = [
        'teacher_id',
        'student_id',
        'phase_id'
    ];
    protected function setKeysForSaveQuery($query)
    {
        return $query->where('teacher_id', $this->getAttribute('teacher_id'))
            ->where('phase_id', $this->getAttribute('phase_id'))->where('student_id',$this->getAttribute('student_id'));
    }
}
