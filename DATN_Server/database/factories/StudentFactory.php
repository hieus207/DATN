<?php

namespace Database\Factories;

use App\Models\Account;
use App\Models\Major;
use Faker\Core\Number;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

class StudentFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array
     */
    public function definition()
    {
        return [
            'student_id' =>  Account::factory()->create()->id,
            'major_id' => Major::all()->random()->id,
            'created_at' => now(),
            'updated_at' => now(),
            'student_name' => $this->faker->name(),
            'email' => $this->faker->unique()->safeEmail(),
            'birthday' => now(),
            'phone'=> $this->faker->phoneNumber(),
            'cv' => $this->faker->words(5, true),
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
