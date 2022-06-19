<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTeacherCouncilsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('teacher_councils', function (Blueprint $table) {
            $table->timestamps();
            $table->unsignedBigInteger('teacher_id');
            $table->unsignedBigInteger('council_id');
            $table->primary(['council_id','teacher_id']);
            $table->foreign('teacher_id')->references('teacher_id')->on('teachers')->onUpdate('cascade')->onDelete('cascade');
            $table->foreign('council_id')->references('id')->on('councils')->onUpdate('cascade')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('teacher_councils');
    }
}
