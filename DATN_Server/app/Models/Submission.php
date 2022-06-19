<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Submission extends Model
{
    use HasFactory;
    protected $fillable = [
        'phase_id',
        'project_id',
        'link'
    ];


    protected function setKeysForSaveQuery($query)
    {
        return $query->where('phase_id', $this->getAttribute('phase_id'))
            ->where('project_id', $this->getAttribute('project_id'));
    }
}
