<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class Proposal extends Model
{
    use HasFactory;

    protected $fillable = [

        'student_id',
        'teacher_id',
        'project_id',
        'phase_id',
        'status'
    ];

    protected function setKeysForSaveQuery($query)
    {
        return $query->where('student_id', $this->getAttribute('student_id'))
            ->where('teacher_id', $this->getAttribute('teacher_id'))
            ->where('project_id', $this->getAttribute('project_id'))
            ->where('phase_id', $this->getAttribute('phase_id'));
    }
}
