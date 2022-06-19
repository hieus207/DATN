@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">

            <div class="modal-content">

                {{-- FORM --}}
                <form action="{{ route('mngPhase.insertToCouncil') }}" id="form_insert_council" method="post">
                    @csrf
                    <input type="hidden" name="phase_id" value="{{ $phase->id }}">
                    <input type="hidden" name="council_id" value="{{ $council->id }}">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Danh sách giảng viên</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="text" class="form-control" id="import_council_s" name="p_name"
                            placeholder="Nhập tên giảng viên" aria-describedby="basic-addon2">
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
                            <tbody class="body_import_council">
                                @foreach ($Allteachers as $teacher)
                                    <tr>
                                        <td scope="row"><input type="checkbox"
                                                class="form-check-input import_checkbox_id"
                                                name="import_teacher_council_id[]" value="{{ $teacher->teacher_id }}">
                                        </td>
                                        <td>{{ $teacher->teacher_id }}</td>
                                        <td>{{ $teacher->teacher_name }}</td>
                                        <td>{{ $teacher->title }}</td>
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
    <div class="modal fade" id="import_project" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-lg modal-fix" role="document">

        <div class="modal-content">

            {{-- FORM --}}
            <form action="{{ route('mngPhase.addProjectToReview') }}" id="form_insert_project" method="post">
                @csrf
                <input type="hidden" name="phase_id" value="{{ $phase->id }}">
                <input type="hidden" name="council_id" value="{{ $council->id }}">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Danh sách đề tài</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="text" class="form-control" id="import_project_s" name="p_name"
                        placeholder="Nhập tên đề tài" aria-describedby="basic-addon2">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <input type="checkbox" class="form-check-input" id="import_checkAll_2">
                                </th>
                                <th>Mã đề tài</th>
                                <th>Tên đề tài</th>
                                <th>Đường dẫn</th>
                                <th>Mã sinh viên</th>
                                <th>Tên sinh viên</th>
                            </tr>
                        </thead>
                        <tbody class="body_import_project">
                            @foreach ($Allprojects as $project)
                                <tr>
                                    <td scope="row"><input type="checkbox"
                                            class="form-check-input import_checkbox_id_2"
                                            name="import_project_id[]" value="{{ $project->id }}">
                                    </td>
                                    <td>{{ $project->id }}</td>
                                    <td>{{ $project->name }}</td>
                                    <td><a href="{{$project->link}}"> {{ substr($project->link,0,15) }}</a></td>
                                    <td>{{ $project->student_id }}</td>
                                    <td>{{ $project->student_name }}</td>
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



    <form action="{{ route('mngPhase.removeFromCouncil') }}" method="post">
        @csrf
        <input type="hidden" name="phase_id" value="{{ $phase->id }}">
        <input type="hidden" name="council_id" value="{{ $council->id }}">
        <!-- Button trigger modal -->
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
            Thêm giảng viên
        </button>

        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#import_project">
            Nhập đề tài
        </button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal"
            data-target="#confirm-delete">Xoá Giảng viên</button>

        <button type="button" class="btn btn-danger btn_del_form_2" data-toggle="modal"
            data-target="#confirm-delete-2">Xoá Đề tài</button>
        <div class="row">
            <div class="col-md-6">
                <table class="table">
                    <thead>
                        <tr>
                            <th>
                                <input type="checkbox" class="form-check-input" id="checkAll">
                            </th>
                            <th>Mã giảng viên</th>
                            <th>Tên giảng viên</th>
                            {{-- <th>Chức danh</th>
                            <th>Email</th> --}}
                            <th>Ngành</th>
                        </tr>
                    </thead>
                    <tbody class="body_delete_teacher">
                        @foreach ($teachers as $teacher)
                            <tr>
                                <td scope="row"><input type="checkbox" class="form-check-input checkbox_id"
                                        name="teacher_id[]" value="{{ $teacher->teacher_id }}"></td>
                                <td>{{ $teacher->teacher_id }}</td>
                                <td>{{ $teacher->teacher_name }}</td>
                                {{-- <td>{{ $teacher->title }}</td>
                                <td>{{ $teacher->email }}</td> --}}
                                <td>{{ $majors[$teacher->major_id] }}</td>
                            </tr>
                        @endforeach

                    </tbody>
                </table>
                <div class="paginate">
                    {{ $teachers->onEachSide(2)->links() }}
                </div>
            </div>
            <div class="col-md-6">
                <table class="table">
                    <thead>
                        <tr>
                            <th>
                                <input type="checkbox" class="form-check-input" id="checkAll_2">
                            </th>
                            <th>Mã đề tài</th>
                            <th>Tên đề tài</th>
                            <th>Đường dẫn</th>
                            {{-- <th>Chức danh</th>
                            <th>Email</th> --}}
                            <th>Tên sinh viên</th>
                        </tr>
                    </thead>
                    <tbody class="body_delete_teacher">
                        @foreach ($projects as $project)
                            <tr>
                                <td scope="row"><input type="checkbox" class="form-check-input checkbox_id_2"
                                        name="project_id[]" value="{{ $project->id }}"></td>
                                <td>{{ $project->id }}</td>
                                <td>{{ $project->name }}</td>
                                <td><a href="{{$project->link}}"> {{ substr($project->link,0,15) }}</a></td>
                                <td>{{ $project->student_name }}</td>

                            </tr>
                        @endforeach

                    </tbody>
                </table>
                <div class="paginate">
                    {{ $projects->onEachSide(2)->links() }}
                </div>
            </div>
        </div>


        <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
            aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5>Xác nhận xoá</h3>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc muốn xoá Giảng viên khỏi hội đồng?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-danger btn_del_form" name="action"
                            value="delete_teacher">Xoá</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confirm-delete-2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5>Xác nhận xoá</h3>
                </div>
                <div class="modal-body">
                    Bạn có chắc muốn xoá Đề tài này khỏi hội đồng?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-danger btn_del_form_2" name="action"
                        value="delete_project">Xoá</button>
                </div>
            </div>
        </div>
    </div>

    </form>
</div>
@endsection

