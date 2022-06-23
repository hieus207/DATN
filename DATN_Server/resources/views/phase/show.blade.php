@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
    <div class="page-table shadow">
        <div class="row">
            <div class="col-md-4">
                <a class="nav-link" href="{{ route('mngPhase.index') }}">
                    < Trở lại</a>
            </div>
            <div class="col-md-4">
                <div class="card phase-name text-dark mt-3 mb-1">
                    <div class="card-header">Tên đợt</div>
                    <div class="card-body">{{ $mngPhase->name }}</div>
                </div>
            </div>
            <div class="col-md-4">
            </div>
        </div>


        <div class="row mb-5 mt-5">
            <div class="col-xl-1">{{ date('d-m-Y', strtotime($mngPhase->startDate)) }}</div>
            <div class="col-xl-10 m-auto">
                {{-- <progress id="progress" max="30" value="14">abcv</progress> --}}
                @php
                    $total = Carbon::parse($mngPhase->endDate)->diffInDays(date($mngPhase->startDate));
                    if (now() >= date($mngPhase->startDate)) {
                        $current = now()->diffInDays(date($mngPhase->startDate));
                    } else {
                        $current = 0;
                    }
                @endphp
                <div class="progress">
                    <div class="progress-bar" role="progressbar" style="width: {{ ($current * 100) / $total }}%;"
                        aria-valuenow="{{ $current }}" aria-valuemin="0" aria-valuemax="{{ $total }}">
                        {{ date('d-m-Y') }}</div>
                </div>
            </div>
            <div class="col-xl-1">{{ date('d-m-Y', strtotime($mngPhase->endDate)) }}</div>
        </div>

        <div class="row">

            <div class="col-xl-3 col-md-6">
                <div class="card phase-start text-dark mb-4">
                    <div class="card-header">Ngày bắt đầu</div>
                    <div class="card-body"> {{ date('d-m-Y', strtotime($mngPhase->startDate)) }}</div>
                </div>
            </div>

            <div class="col-xl-3 col-md-6">
                <div class="card phase-end text-dark mb-4">
                    <div class="card-header">Ngày kết thúc</div>
                    <div class="card-body">{{ date('d-m-Y', strtotime($mngPhase->endDate)) }}</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="card phase-total-tc text-dark mb-4">
                    <div class="card-header">
                        Tổng Số giảng viên
                        <span class="float-right">
                            <a class="text-dark" href="{{ route('mngPhase.showTeacher', ['phase' => $mngPhase]) }}">Xem thêm
                                ></a>
                        </span>
                    </div>
                    <div class="card-body"> {{ $mngPhase->totalTeacher }}</div>
                </div>
            </div>

            <div class="col-xl-3 col-md-6">
                <div class="card phase-total-std text-dark mb-4">
                    <div class="card-header">
                        Tổng Số sinh viên
                        <span class="float-right">
                            <a class="text-dark" href="{{ route('mngPhase.showStudent', ['phase' => $mngPhase]) }}">Xem thêm
                                ></a>
                        </span>
                    </div>
                    <div class="card-body"> {{ $mngPhase->totalStudent }}</div>
                </div>
            </div>
        </div>

        {{-- ccusss --}}
        <div class="row">
            <div class="col-xl-3"></div>

            <div class="col-xl-3"></div>
        </div>

        <div class="row">
            <div class="col-xl-3 col-md-6">
                <div class="card phase-quota text-dark mb-4">
                    <div class="card-header">Số hạn ngạch còn lại</div>
                    <div class="card-body"> {{ $mngPhase->totalQuotaAvailable }}/{{ $mngPhase->totalQuota }}</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="card phase-avb-std text-dark mb-4">
                    <div class="card-header">Số sinh viên chưa nhận hướng dẫn</div>
                    <div class="card-body"> {{ $mngPhase->totalStudentAvailable }}</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="card phase-avb-std text-dark mb-4">
                    <div class="card-header">Số sinh viên chưa nhận phản biện</div>
                    <div class="card-body"> {{ $mngPhase->totalStudentNotReview }}</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="card phase-avb-std text-dark mb-4">
                    <div class="card-header">Danh sách hội đồng</div>
                    <div class="card-body"> <a class="text-dark"
                            href="{{ route('mngPhase.listCouncil', ['phase' => $mngPhase]) }}">Xem thêm</a></div>
                </div>
            </div>
        </div>





        <div class="row">

            <div class="col-xl-4 col-md-6">
                <div class="card phase-import-std text-dark mb-4">
                    <form action="{{ route('mngPhase.manageAction') }}" method="post" enctype="multipart/form-data">
                        <div class="card-header">Nhập giảng viên vào đợt</div>
                        <div class="card-body">
                            @csrf
                            <input type="hidden" name="phase_id" value="{{ $mngPhase->id }}">
                            <div class="form-group">
                                <input type="file" class="form-control-file" name="fileTeacher">
                            </div>
                            <button type="submit" class="btn phase-import-std btn-block mt-3" name="action"
                                value="import">Nhập</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-xl-4 col-md-6">
                <div class="card phase-import-tc text-dark mb-4">
                    <form action="{{ route('mngPhase.manageAction') }}" method="post" enctype="multipart/form-data">
                        <div class="card-header">Nhập sinh viên vào đợt</div>
                        <div class="card-body">
                            @csrf
                            <input type="hidden" name="phase_id" value="{{ $mngPhase->id }}">
                            <div class="form-group">
                                <input type="file" class="form-control-file" name="fileStudent">
                            </div>
                            <button type="submit" class="btn phase-import-tc btn-block mt-3" name="action"
                                value="import">Nhập</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col-xl-4"></div>
        </div>
    </div>


    <form action="{{ route('mngPhase.manageAction') }}" class="float-right" method="post"
        enctype="multipart/form-data">
        @csrf
        <input type="hidden" name="phase_id" value="{{ $mngPhase->id }}">
        <a name="" id="" class="mt-3 button-28 btn-phase shadow"
            href="{{ route('mngPhase.edit', ['mngPhase' => $mngPhase]) }}" role="button">Sửa</a>
        @if ($mngPhase->stage==2)
            <button type="submit" class="mt-3 btn-phase button-28 shadow" role="button" name="action" value="assignment">Phân
                công hướng dẫn</button>
        @endif
        @if ($mngPhase->stage==5)
        <button type="submit" class="mt-3 btn-phase button-28 shadow" name="action" value="review">Phân công phản
            biện</button>
        @endif
    </form>
@endsection
