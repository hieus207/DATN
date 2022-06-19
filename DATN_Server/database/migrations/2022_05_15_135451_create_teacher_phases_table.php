<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTeacherPhasesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('teacher_phases', function (Blueprint $table) {
            $table->timestamps();
            $table->unsignedBigInteger('quota')->default(1);
            $table->unsignedBigInteger('available_quota')->default(1);
            $table->unsignedBigInteger('review_std')->default(0);
            $table->unsignedBigInteger('teacher_id');
            $table->unsignedBigInteger('phase_id');
            $table->primary(['teacher_id','phase_id']);
            $table->foreign('teacher_id')->references('teacher_id')->on('teachers')->onUpdate('cascade')->onDelete('cascade');
            $table->foreign('phase_id')->references('id')->on('phases')->onUpdate('cascade')->onDelete('cascade');

        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('teacher_phases');
    }
}
