@extends('layouts.body')
{{-- teacher --}}
@section('body')
<div class="page-table shadow">
    <a class="nav-link" href="{{route('mngPhase.show',$phase->id)}}">< Trở lại</a>
    <form action="{{ route('mngPhase.searchTeacher') }}" method="post">
        <div class="input-group w-25 float-right">
            @csrf
            <input type="hidden" name="phase_id" value="{{ $phase->id }}">
            <input type="text" class="form-control" id="delete_teacher_s" name="p_name" placeholder="Nhập tên giảng viên"
                aria-describedby="basic-addon2">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
            </div>
        </div>
    </form>

    <form action="{{ route('mngPhase.removeTeacher') }}" method="post">
        @csrf
        <input type="hidden" name="phase_id" value="{{ $phase->id }}">
        <!-- Button trigger modal -->
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
            Thêm giảng viên
        </button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <input type="checkbox" class="form-check-input" id="checkAll">
                    </th>
                    <th>Mã giảng viên</th>
                    <th>Tên giảng viên</th>
                    <th>Chức danh</th>
                    <th>Hạn ngạch</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Ngành</th>
                </tr>
            </thead>
            <tbody class="body_delete_teacher">
                @foreach ($teachers as $teacher)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="teacher_id[]"
                                value="{{ $teacher->teacher_id }}"></td>
                        <td>{{ $teacher->teacher_id }}</td>
                        <td>{{ $teacher->teacher_name }}</td>
                        <td>{{ $teacher->title}}</td>
                        <td>{{ $teacher->available_quota."/".$teacher->quota }}</td>
                        <td>{{ $teacher->email }}</td>
                        <td>{{ $teacher->phone }}</td>
                        <td>{{ $majors[$teacher->major_id] }}</td>
                    </tr>
                @endforeach

            </tbody>
        </table>

        <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5>Xác nhận xoá</h3>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc muốn xoá giảng viên khỏi đợt?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-danger btn_del_form" name="action" value="delete">Xoá</button>
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>

<div class="paginate">
    {{ $teachers->onEachSide(2)->links() }}
</div>



    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">

                {{-- FORM --}}
                <form action="{{ route('mngPhase.insertTeacher') }}" id="form_insert_teacher" method="post">
                    @csrf
                    <input type="hidden" name="phase_id" value="{{ $phase->id }}">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Danh sách giảng viên</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="text" class="form-control" id="import_teacher_s" name="p_name"
                            placeholder="Nhập tên sv" aria-describedby="basic-addon2">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" class="form-check-input" id="import_checkAll">
                                    </th>
                                    <th>Mã giảng viên</th>
                                    <th>Tên giảng viên</th>
                                    <th>Chức danh</th>
                                    <th>Email</th>
                                    <th>Ngành</th>
                                </tr>
                            </thead>
                            <tbody class="body_import_teacher">
                                @foreach ($Allteachers as $teacher)
                                    <tr>
                                        <td scope="row"><input type="checkbox" class="form-check-input import_checkbox_id"
                                                name="import_teacher_id[]" value="{{ $teacher->teacher_id }}"></td>
                                        <td>{{ $teacher->teacher_id }}</td>
                                        <td>{{ $teacher->teacher_name }}</td>
                                        <td>{{ $teacher->title}}</td>
                                        <td>{{ $teacher->email }}</td>
                                        <td>{{ $majors[$teacher->major_id] }}</td>
                                    </tr>
                                @endforeach

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Thêm</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
@endsection
