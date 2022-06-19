@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <form action="{{route('mngPhase.store')}}" id="form_create_phase" method="post">
        @csrf
        <label for="">Tên đợt</label>

        <input type="text" class="form-control" name="name" aria-describedby="helpId"
        @if (isset($data))
        value="{{$data['name']}}"
        @endif
        >

        <label for="">Ngày bắt đầu</label>
        <input type="date" id="phaseStart" class="form-control" name="startDate" aria-describedby="helpId"
        @if (isset($data))
        value="{{$data['startDate']}}"
        @endif
        >
        <label for="">Ngày kết thúc</label>
        <input type="date" id="phaseEnd" class="form-control" name="endDate" aria-describedby="helpId"
        @if (isset($data))
        value="{{$data['endDate']}}"
        @endif
        >

        <div class="form-check">
            <label class="form-check-label">
                <input type="checkbox" class="form-check-input" name="isEditStage" id="isEditStage" value="checkedValue" checked>
                Chỉnh sửa giai đoạn trong đợt làm đồ án (Mặc định 6 giai đoạn)
            </label>
        </div>
        <div id="stage_edit">
            <div id="stage_input">
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][0]))
                            value="{{$data['stageName'][0]}}"
                        @else
                            value="Công bố danh sách sinh viên"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][0]))
                        value="{{$data['stageStartDate'][0]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][0]))
                        value="{{$data['stageEndDate'][0]}}"
                        @endif
                        >
                    </div>
                </div>
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][1]))
                            value="{{$data['stageName'][1]}}"
                        @else
                            value="Đề xuất đề tài"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][1]))
                        value="{{$data['stageStartDate'][1]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][1]))
                        value="{{$data['stageEndDate'][1]}}"
                        @endif
                        >
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][2]))
                            value="{{$data['stageName'][2]}}"
                        @else
                            value="Xét duyệt đề tài"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][2]))
                        value="{{$data['stageStartDate'][2]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][2]))
                        value="{{$data['stageEndDate'][2]}}"
                        @endif
                        >
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][3]))
                            value="{{$data['stageName'][3]}}"
                        @else
                            value="Giao nhiệm vụ học phần"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][3]))
                        value="{{$data['stageStartDate'][3]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][3]))
                        value="{{$data['stageEndDate'][3]}}"
                        @endif
                        >
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][4]))
                            value="{{$data['stageName'][4]}}"
                        @else
                            value="Thực hiện nhiệm vụ học phần"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][4]))
                        value="{{$data['stageStartDate'][4]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][4]))
                        value="{{$data['stageEndDate'][4]}}"
                        @endif
                        >
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
                <div class="input-group mb-3 form-inline">
                    <div class="form-group mb-2">
                        <label for="">Tên giai đoạn</label>
                        <input type="text" class="form-control " name="stageName[]"
                        @if (isset($data['stageName'][5]))
                            value="{{$data['stageName'][5]}}"
                        @else
                            value="Chấm và bảo vệ đồ án tốt nghiệp"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày bắt đầu</label>
                        <input type="date" class="form-control stageStart" name="stageStartDate[]"
                        @if (isset($data['stageStartDate'][5]))
                        value="{{$data['stageStartDate'][5]}}"
                        @endif
                        >
                    </div>
                    <div class="form-group mx-sm-3 mb-2">
                        <label for="">Ngày kết thúc</label>
                        <input type="date" class="form-control stageEnd" name="stageEndDate[]"
                        @if (isset($data['stageEndDate'][5]))
                        value="{{$data['stageEndDate'][5]}}"
                        @endif
                        >
                    </div>
                    {{-- <div class="form-group mx-sm-3 mb-2 mt-3 p-1">
                        <button class="btn btn-outline-danger remove_stage" type="button">Remove</button>
                    </div> --}}
                </div>
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

        <button type="submit" class="btn btn-primary btn-submit mt-3">Tạo</button>
    </form>
</div>
@endsection
