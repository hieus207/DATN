<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateStudentsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('students', function (Blueprint $table) {
            $table->timestamps();
            $table->string('student_name');
            $table->string('email')->unique();
            $table->string('birthday')->nullable();
            $table->string('phone')->nullable();
            $table->string('cv')->nullable();
            $table->string('avt')->nullable();
            $table->unsignedBigInteger('student_id');
            $table->unsignedBigInteger('major_id');
            $table->primary('student_id');
            $table->foreign('student_id')->references('id')->on('accounts')->onUpdate('cascade')->onDelete('cascade');
            $table->foreign('major_id')->references('id')->on('majors')->onUpdate('cascade')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('students');
    }
}
