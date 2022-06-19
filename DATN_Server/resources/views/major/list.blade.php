@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
    <!-- Button trigger modal -->

<div class="page-table shadow">
    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <form action="{{route('mngMajor.store')}}" method="post">
            @csrf
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Thêm ngành</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="text" class="form-control" name="name" placeholder="Nhập tên ngành"  aria-describedby="basic-addon2">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Thêm</button>
                </div>
            </div>
            </form>
        </div>
    </div>

    <div class="modal fade" id="exampleModa2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <form action="" id="form_edit_major" method="post">
            @csrf
            @method('put')
            <div class="modal-content">
                <div class="modal-header">

                    <h5 class="modal-title" id="exampleModalLabel">Sửa ngành</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="text" class="form-control" id="edt_major" name="name" placeholder="Nhập tên ngành"  aria-describedby="basic-addon2">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Sửa</button>
                </div>
            </div>
            </form>
        </div>
    </div>

    <form action="{{ route('mngMajor.remove') }}" method="post">
        @csrf

        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
            Thêm
        </button>
        <button type="button" class="btn btn-primary btn_edit_form btn_edit_major" data-toggle="modal" data-target="#exampleModa2">
            Sửa
        </button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">
            Xoá
        </button>

        <table class="table">
            <thead>
                <tr>
                    <th>
                        <input type="checkbox" class="form-check-input" id="checkAll">
                    </th>
                    <th>Mã ngành</th>
                    <th>Tên ngành</th>
                </tr>
            </thead>
            <tbody>
                @foreach ($majors as $major)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="major_id[]"
                                value="{{ $major->id }}"></td>
                        <td>{{ $major->id }}</td>
                        <td>{{ $major->name }}</td>
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
                        <button type="submit" class="btn btn-danger">Xoá</button>
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>
    <div class="paginate">
        {{ $majors->onEachSide(2)->links() }}
    </div>


@endsection
