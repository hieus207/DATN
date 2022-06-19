<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CouncilReview extends Model
{
    use HasFactory;
    protected $fillable = [
        'council_id',
        'project_id',
        'phase_id',
    ];

}
