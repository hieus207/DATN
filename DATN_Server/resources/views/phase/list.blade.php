@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
        <div class="page-table shadow">
            <form action="{{route("mngPhase.search")}}" method="post">
                @csrf
                <div class="input-group w-25 float-right">
                    <input type="text" class="form-control" name="p_name" placeholder="Nhập tên đợt"  aria-describedby="basic-addon2">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
                    </div>
                </div>
            </form>
    <form action="{{route('mngPhase.manageAction')}}" method="post">
            @csrf


            <div class="nav_btn">
                <a class="btn btn-primary" href="{{route("mngPhase.create")}}" role="button">Thêm</a>

                <button type="submit" class="btn btn-primary btn_edit_form" name="action" value="update">Sửa</button>
                <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>

            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" class="form-check-input" id="checkAll">
                        </th>
                        <th>Mã đợt</th>
                        <th>Tên đợt</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                    </tr>
                </thead>
                <tbody>
                    @foreach ($phases as $phase)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="phase_id[]" value="{{$phase->id}}"></td>
                        <td>{{$phase->id}}</td>
                        <td><a href="{{route('mngPhase.show',['mngPhase'=>$phase])}}">{{$phase->name}}</a></td>
                        <td>{{$phase->startDate}}</td>
                        <td>{{$phase->endDate}}</td>
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
        </div>


    </form>
    <div class="paginate">
        {{$phases->onEachSide(2)->links()}}
    </div>
@endsection
