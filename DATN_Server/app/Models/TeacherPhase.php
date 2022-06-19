<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TeacherPhase extends Model
{
    use HasFactory;
    protected $fillable = [
        'teacher_id',
        'phase_id',
        'quota',
        'available_quota',
        'review_std'
    ];
    protected function setKeysForSaveQuery($query)
    {
        return $query->where('teacher_id', $this->getAttribute('teacher_id'))
            ->where('phase_id', $this->getAttribute('phase_id'));
    }
}
