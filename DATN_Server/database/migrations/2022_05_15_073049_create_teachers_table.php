<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTeachersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('teachers', function (Blueprint $table) {
            $table->timestamps();
            $table->string('teacher_name');
            $table->string('email')->unique();
            $table->date('birthday')->nullable();
            $table->string('phone')->nullable();
            $table->text('topic')->nullable();
            $table->string('avt')->nullable();
            $table->string('title');
            $table->unsignedBigInteger('teacher_id');
            $table->unsignedBigInteger('major_id');
            $table->primary('teacher_id');
            $table->foreign('teacher_id')->references('id')->on('accounts')->onUpdate('cascade')->onDelete('cascade');
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
        Schema::dropIfExists('teachers');
    }
}
