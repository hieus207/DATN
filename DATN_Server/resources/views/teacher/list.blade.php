@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
    <div class="page-table shadow">
        <form action="{{route("mngTeacher.search")}}" method="post">
            @csrf
            <div class="input-group w-25 float-right">
                <input type="text" class="form-control" name="p_name" placeholder="Nhập tên đợt"  aria-describedby="basic-addon2">
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
                </div>
            </div>
        </form>
        <form action="{{route('mngTeacher.manageAction')}}" method="post">
        @csrf
        <a class="btn btn-primary" href="{{route("mngTeacher.create")}}" role="button">Thêm</a>
        <button type="submit" class="btn btn-primary btn_edit_form" name="action" value="update">Sửa</button>

        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>
            <table class="table">
                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" class="form-check-input" id="checkAll">
                        </th>
                        <th>Mã giảng viên</th>
                        <th>Tên giảng viên</th>
                        <th>Ngày sinh</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Chức danh</th>
                        <th>Ngành</th>
                    </tr>
                </thead>
                <tbody>
                    @foreach ($teachers as $teacher)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="teacher_id[]" value="{{$teacher->teacher_id}}"></td>
                        <td>{{$teacher->teacher_id}}</td>
                        <td>{{$teacher->teacher_name}}</td>
                        <td>{{$teacher->birthday}}</td>
                        <td>{{$teacher->email}}</td>
                        <td>{{$teacher->phone}}</td>
                        <td>{{$teacher->title}}</td>
                        <td>{{$majors[$teacher->major_id]}}</td>
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
            {{$teachers->onEachSide(2)->links()}}
        </div>
@endsection
