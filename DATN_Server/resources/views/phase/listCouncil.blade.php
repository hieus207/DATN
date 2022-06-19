@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
    <!-- Modal -->
<div class="page-table shadow">
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <form action="{{ route('mngPhase.createCouncil') }}" method="post">

                @csrf
                <input type="hidden" name="phase_id" value="{{ $phase->id }}">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Tạo hội đồng</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="text" class="form-control" name="name"
                            placeholder="Nhập tên hội đồng" aria-describedby="basic-addon2">

                            <input type="number" class="form-control mt-3" name="maxQuota"
                            placeholder="Số lượng duyệt đề tài tối đa" aria-describedby="basic-addon2">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Tạo</button>
                    </div>
                </div>
            </form>
        </div>
    </div>



    <form action="{{route('mngPhase.deleteCouncil')}}" method="post">

        @csrf
        <input type="hidden" name="phase_id" value="{{ $phase->id }}">
        <!-- Button trigger modal -->
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
            Tạo hội đồng
        </button>
        <button type="button" class="btn btn-danger btn_del_form" data-toggle="modal" data-target="#confirm-delete">Xoá</button>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <input type="checkbox" class="form-check-input" id="checkAll">
                    </th>
                    <th>Mã hội đồng</th>
                    <th>Tên hội đồng</th>
                </tr>
            </thead>
            <tbody class="body_delete_student">
                @foreach ($councils as $council)
                    <tr>
                        <td scope="row"><input type="checkbox" class="form-check-input checkbox_id" name="council_id[]"
                                value="{{ $council->id }}"></td>
                        <td>{{ $council->id }}</td>
                        <td><a href="{{route('mngPhase.showCouncil',['council_id'=>$council->id,'phase'=>$phase])}}">{{ $council->name }}</a></td>
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
                        Bạn có chắc muốn xoá Hội đồng khỏi đợt?
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
    {{ $councils->onEachSide(2)->links() }}
</div>

@endsection
