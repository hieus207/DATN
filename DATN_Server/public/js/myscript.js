$(document).ready(function(){
    $("#import_checkAll").on("click",function(){
        $(".import_checkbox_id").prop('checked', $("#import_checkAll").is(":checked"));
        checkCb();
    })

    $("#import_checkAll_2").on("click",function(){
        $(".import_checkbox_id_2").prop('checked', $("#import_checkAll_2").is(":checked"));
        checkCb();
    })

    $("#checkAll").on("click",function(){
        $(".checkbox_id").prop('checked', $("#checkAll").is(":checked"));
        checkCb();
    })

    $("#checkAll_2").on("click",function(){
        $(".checkbox_id_2").prop('checked', $("#checkAll_2").is(":checked"));
        checkCb2();
    })


    function checkCb(){
        if($(".checkbox_id").is(":checked")){
            $(".btn_edit_form").prop('disabled',false)
            $(".btn_del_form").prop('disabled',false)

        }
        else
        {
            $(".btn_edit_form").prop('disabled',true)
            $(".btn_del_form").prop('disabled',true)
        }
    }

    function checkCb2(){
        if($(".checkbox_id_2").is(":checked")){
            $(".btn_del_form_2").prop('disabled',false)

        }
        else
        {
            $(".btn_del_form_2").prop('disabled',true)
        }
    }

    checkCb()
    checkCb2()
    $(".checkbox_id").on("click",checkCb)
    $(".checkbox_id_2").on("click",checkCb2)

    if(!$("#isEditStage").is(":checked")){
        $("#stage_edit").hide();
    }

    $("#isEditStage").on("click",function(){
        if($(this).is(":checked")){
            $("#stage_edit").show();
        }
        else
            $("#stage_edit").hide();
    })

    $("#btn-add-stage").on("click",addStage);

    $(document).on('click', '.remove_stage', rmStage)

    function addStage(){
        let input =
        `
        <div class="input-group mb-3 form-inline">
                <div class="form-group mb-2">
                    <label for="">Tên giai đoạn</label>
                    <input type="text" class="form-control " name="stageName[]" >
                </div>
                <div class="form-group mx-sm-3 mb-2">
                    <label for="">Ngày bắt đầu</label>
                    <input type="date" class="form-control" name="stageStartDate[]" value="">
                </div>
                <div class="form-group mx-sm-3 mb-2">
                    <label for="">Ngày kết thúc</label>
                    <input type="date" class="form-control" name="stageEndDate[]" value="">
                </div>
                <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                    <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                  </div>
            </div>
        `
        $("#stage_input").append(input);
    }

    function rmStage(){
        $(this).parent().parent().remove();
    }

    function today(){
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        if (dd < 10) {
           dd = '0' + dd;
        }
        if (mm < 10) {
           mm = '0' + mm;
        }
        today = yyyy + '-' + mm + '-' + dd;
        return today;
    }
    $("#phaseStart").attr("min",today())
    $(".stageStart").attr("min",today())
    $(".stageEnd").attr("min",today())

    $("#phaseStart").on("change",()=>{
        $(".stageStart").attr("min",$("#phaseStart").val())
        $(".stageEnd").attr("min",$("#phaseStart").val())
        $("#phaseEnd").attr("min",$("#phaseStart").val())
    })
    $("#phaseEnd").on("change",()=>{
        $(".stageStart").attr("max",$("#phaseEnd").val())
        $(".stageEnd").attr("max",$("#phaseEnd").val())
        $("#phaseStart").attr("max",$("#phaseEnd").val())

    })

    function handleStageDate(){
        $(".stageStart").on("change",function(){

            $(this).parent().parent().children().eq(2).children().eq(1).attr("min",$(this).val())

            console.log($(this).val())
        })
        $(".stageEnd").on("change",function(){
            let cha = $(this).parent().parent().parent();
            let con = $(this)[0];
            let vitri = -1;
            for(var i=0;i<cha.children().length;i++){
                if($(".stageEnd")[i]==con)
                    vitri=i
            }
            if(vitri!=cha.children().length-1)
                $(".stageStart")[vitri+1].setAttribute("min",$(".stageEnd")[vitri].value)

        })
    }

    function checkNullData(){
        let dd=false
        $("input").each(function(ele){
            if(this.value==""){
                dd= true;
            }
        })
        return dd;
    }

    $("#form_create_phase").submit(function(e){
        if(checkNullData()){
            $('.toast').toast('show');
            e.preventDefault();
        }
    });
    $("#form_edit_phase").submit(function(e){
        if(checkNullData()){
            $('.toast').toast('show');
            e.preventDefault();
        }
    });


    handleStageDate();


    $('.paginate').each(function(){
        $(this).children().eq(0).children().eq(0).hide()
    })
    $('.paginate').each(function(){
        $(this).children().eq(0).children().eq(1).children().eq(0).hide()
    })

    $("#import_student_s").on("change",function(){
        var name = $(this).val();
        $(".body_import_student tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })

    $("#import_teacher_s").on("change",function(){
        var name = $(this).val();
        $(".body_import_teacher tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })

    $("#import_council_s").on("change",function(){
        var name = $(this).val();
        $(".body_import_council tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })

    $("#import_project_s").on("change",function(){
        var name = $(this).val();
        $(".body_import_project tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })

    function checkNullStudent(){
        if($(".import_checkbox_id").is(":checked"))
            return true;

        return false;
    }


    $("#form_insert_student").submit(function(e){
        if(!checkNullStudent()){
            e.preventDefault();
        }
    });
    $("#form_insert_teacher").submit(function(e){
        if(!checkNullStudent()){
            e.preventDefault();
        }
    });
    // KEY ENTER---------------------------------------------
    $(document).on("keydown", "form", function(event) {
        return event.key != "Enter";
    });

    $("#delete_student_s").on("change",function(){
        var name = $(this).val();
        $(".body_delete_student tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })
    $("#delete_teacher_s").on("change",function(){
        var name = $(this).val();
        $(".body_delete_teacher tr").each(function(){
            if($(this).children().eq(2).text().toUpperCase().includes(name.toUpperCase())){
                $(this).show()
            }
            else
                $(this).hide()
        });
    })

    $(".btn_edit_major").on("click",function(){
        $(".checkbox_id").each(function(){
            if($(this).is(":checked")){
                $("#edt_major").val($(this).parent().parent().children().eq(2).text());
                $("#form_edit_major").attr("action",origin+location.pathname+"/"+$(this).val());
            }
        })
    })

    $(".btn_edit_platform").on("click",function(){
        $(".checkbox_id").each(function(){
            if($(this).is(":checked")){
                $("#edt_platform").val($(this).parent().parent().children().eq(2).text());
                $("#form_edit_platform").attr("action",origin+location.pathname+"/"+$(this).val());
            }
        })
    })

    try {
        setTimeout(()=>{
            $(".error").hide();
        },3700)
    } catch (error) {

    }
    try {
        setTimeout(()=>{
            $(".message").hide();
        },2000)
    } catch (error) {

    }
});
