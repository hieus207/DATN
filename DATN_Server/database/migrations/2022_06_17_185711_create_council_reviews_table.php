<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCouncilReviewsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('council_reviews', function (Blueprint $table) {
            $table->timestamps();
            $table->unsignedBigInteger('phase_id');
            $table->unsignedBigInteger('project_id');
            $table->unsignedBigInteger('council_id');
            $table->primary(['council_id','project_id','phase_id']);
            $table->foreign('phase_id')->references('id')->on('phases')->onUpdate('cascade')->onDelete('cascade');
            $table->foreign('project_id')->references('id')->on('projects')->onUpdate('cascade')->onDelete('cascade');
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
        Schema::dropIfExists('council_reviews');
    }
}
