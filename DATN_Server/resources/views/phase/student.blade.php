@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <a class="nav-link" href="{{route('mngPhase.show',$phase->id)}}">< Trở lại</a>
    <form action="{{ route('mngPhase.searchStudent') }}" method="post">
        {{-- custom lai w --}}
        <div class="input-group w-25 float-right">
            @csrf
            <input type="hidden" name="phase_id" value="{{ $phase->id }}">
            <select class="form-control" name="filter">
            <option value="All" selected>Tất cả</option>
            <option value="Not" >Chưa nộp</option>
            <option value="Submited" >Đã nộp</option>
            </select>
            <input type="text" class="form-control" id="delete_student_s" name="p_name" placeholder="Nhập tên sinh viên"
                aria-describedby="basic-addon2">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
            </div>
        </div>
    </form>

    <form action="{{ route('mngPhase.removeStudent') }}" method="post">
        @csrf
        <input type="hidden" name="phase_id" value="{{ $phase->id }}">
        <!-- Button trigger modal -->
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
            Thêm sinh viên
        </button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <input type="checkbox" class="form-check-input" id="checkAll">
                    </th>
                    <th>Mã sinh viên</th>
                    <th>Tên sinh viên</th>
                    <th>Nộp bài</th>
                    <th>Email</th>
                    {{-- <th>Số điện thoại</th> --}}
                    <th>Ngành</th>
                </tr>
            </thead>
            <tbody class="body_delete_student">
                @foreach ($students as $student)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="student_id[]"
                                value="{{ $student->student_id }}"></td>
                        <td>{{ $student->student_id }}</td>
                        <td>{{ $student->student_name }}</td>
                        <td>
                            @if ($student->link!="CHƯA NỘP")
                                <a href="{{$student->link}}"> {{ substr($student->link,0,15) }}</a>
                            @else
                                {{"CHƯA NỘP"}}
                            @endif
                        </td>
                        <td>{{ $student->email }}</td>
                        {{-- <td>{{ $student->phone }}</td> --}}
                        <td>{{ $majors[$student->major_id] }}</td>
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
                        Bạn có chắc muốn xoá sinh viên khỏi đợt?
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
    {{ $students->onEachSide(2)->links() }}
</div>



    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">

                {{-- FORM --}}
                <form action="{{ route('mngPhase.insertStudent') }}" id="form_insert_student" method="post">
                    @csrf
                    <input type="hidden" name="phase_id" value="{{ $phase->id }}">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Danh sách sinh viên</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="text" class="form-control" id="import_student_s" name="p_name"
                            placeholder="Nhập tên sv" aria-describedby="basic-addon2">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" class="form-check-input" id="import_checkAll">
                                    </th>
                                    <th>Mã sinh viên</th>
                                    <th>Tên sinh viên</th>
                                    <th>Email</th>
                                    <th>Ngành</th>
                                </tr>
                            </thead>
                            <tbody class="body_import_student">
                                @foreach ($Allstudents as $student)
                                    <tr>
                                        <td scope="row"><input type="checkbox" class="form-check-input import_checkbox_id"
                                                name="import_student_id[]" value="{{ $student->student_id }}"></td>
                                        <td>{{ $student->student_id }}</td>
                                        <td>{{ $student->student_name }}</td>
                                        <td>{{ $student->email }}</td>
                                        <td>{{ $majors[$student->major_id] }}</td>
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
