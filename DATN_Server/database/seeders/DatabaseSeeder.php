<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        \App\Models\Major::factory(2)->create();
        \App\Models\Platform::factory(5)->create();
        \App\Models\Student::factory(10)->create();
        \App\Models\Teacher::factory(10)->create();
        \App\Models\TeacherPlatform::factory(5)->create();



    }
}
