@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
    <div class="page-table shadow">
        <form action="{{route("mngStudent.search")}}" method="post">
            @csrf
            <div class="input-group w-25 float-right">
                <input type="text" class="form-control" name="p_name" placeholder="Nhập tên sinh viên"  aria-describedby="basic-addon2">
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
                </div>
            </div>
        </form>
        <form action="{{route('mngStudent.manageAction')}}" method="post">
        @csrf
        <a class="btn btn-primary" href="{{route("mngStudent.create")}}" role="button">Thêm</a>
        <button type="submit" class="btn btn-primary btn_edit_form" name="action" value="update">Sửa</button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>
            <table class="table">
                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" class="form-check-input" id="checkAll">
                        </th>
                        <th>Mã sinh viên</th>
                        <th>Tên sinh viên</th>
                        <th>Ngày sinh</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Ngành</th>
                    </tr>
                </thead>
                <tbody>
                    @foreach ($students as $student)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="student_id[]" value="{{$student->student_id}}"></td>
                        <td>{{$student->student_id}}</td>
                        <td>{{$student->student_name}}</td>
                        <td>{{$student->birthday}}</td>
                        <td>{{$student->email}}</td>
                        <td>{{$student->phone}}</td>
                        <td>{{$majors[$student->major_id]}}</td>
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
                            Bạn có chắc muốn xoá?
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
            {{$students->onEachSide(2)->links()}}
        </div>
@endsection
