<?php

namespace Database\Factories;

use App\Models\Platform;
use App\Models\Teacher;
use Faker\Core\Number;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

class TeacherPlatformFactory extends Factory
{
    /**
     * Co the bi trung du lieu nhung thap
     *
     * @return array
     */
    public function definition()
    {
        return [
            'platform_id' => Platform::inRandomOrder()->take(1)->first()->id,
            'teacher_id' => Teacher::inRandomOrder()->take(1)->first()->teacher_id
        ];
    }

    /**
     * Indicate that the model's email address should be unverified.
     *
     * @return \Illuminate\Database\Eloquent\Factories\Factory
     */
    public function unverified()
    {
        return $this->state(function (array $attributes) {
            return [
                'email_verified_at' => null,
            ];
        });
    }
}
