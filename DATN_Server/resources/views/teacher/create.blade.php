@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <form action="{{route('mngTeacher.store')}}" method="post">
        @csrf
          <label for="">Tên tài khoản</label>
          <input type="text" class="form-control" name="username" aria-describedby="helpId" placeholder="">
          <label for="">Mật khẩu</label>
          <input type="password" class="form-control" name="password" aria-describedby="helpId" placeholder="">
          <label for="">Tên giảng viên</label>
          <input type="text" class="form-control" name="teacher_name" aria-describedby="helpId" placeholder="">
          <label for="">Email</label>
          <input type="email" class="form-control" name="email" aria-describedby="helpId" placeholder="">
          <label for="">Ngày sinh</label>
          <input type="date" class="form-control" name="birthday" aria-describedby="helpId" placeholder="">
          <label for="">Số điện thoại</label>
          <input type="text" class="form-control" name="phone" aria-describedby="helpId" placeholder="">
          <label for="">Chức danh</label>
          <select class="form-control" name="title">
            <option value="GIẢNG VIÊN">GIẢNG VIÊN</option>
            <option value="THẠC SĨ">THẠC SĨ</option>
            <option value="TIẾN SĨ">TIẾN SĨ</option>
            <option value="PHÓ GIÁO SƯ">PHÓ GIÁO SƯ</option>
            <option value="GIÁO SƯ">GIÁO SƯ</option>
          </select>
          <div class="form-group">
            <label for="">Ngành</label>
            <select class="form-control" name="major_id" id="major_id">
                @foreach ($majors as $major)
                    <option value={{$major->id}} checked>{{$major->name}}</option>
                @endforeach
            </select>
          </div>

        <button type="submit" class="btn btn-primary mt-3">Tạo</button>
    </form>
</div>
@endsection
