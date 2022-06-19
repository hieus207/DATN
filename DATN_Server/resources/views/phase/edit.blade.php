@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <form action="{{ route('mngPhase.update', ['mngPhase' => $mngPhase]) }}" id="form_edit_phase" method="post">
        @method('PUT')
        @csrf
        <label for="">Tên đợt</label>
        <input type="text" class="form-control" name="name" aria-describedby="helpId" value="{{ $mngPhase->name }}">
        <label for="">Ngày bắt đầu</label>
        <input type="date" class="form-control" id="phaseStart" name="startDate" aria-describedby="helpId"
            value="{{ $mngPhase->startDate }}">
        <label for="">Ngày kết thúc</label>
        <input type="date" class="form-control" id="phaseEnd" name="endDate" aria-describedby="helpId" value="{{ $mngPhase->endDate }}">
        <div class="form-check">
            <label class="form-check-label">
                <input type="checkbox" class="form-check-input" name="isEditStage" id="isEditStage" value="checkedValue"
                @if (sizeof($stages)>0)
                    checked
                @endif>
                Sửa giai đoạn trong đợt làm đồ án
            </label>
        </div>
        <div id="stage_edit">
            <div id="stage_input">
                @if (sizeof($stages)>0)
                @foreach ($stages as $stage)
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]" value="{{$stage->name}}">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]" value="{{$stage->startDate}}">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]" value="{{$stage->endDate}}">
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                @endforeach
                @else
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate" value="">
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate" value="">
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                @endif
            </div>
            <button type="button" class="btn btn-primary btn-add-stage" id="btn-add-stage">Thêm đợt</button>
        </div>


        <div class="toast">
            <div class="toast-header">
              Cảnh báo
            </div>
            <div class="toast-body">
              Không được để trống trường dữ liệu
            </div>
          </div>

        <button type="submit" class="btn btn-primary btn-submit mt-3">Sửa</button>
    </form>
</div>
@endsection
