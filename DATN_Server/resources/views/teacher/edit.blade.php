@extends('layouts.body')
{{-- Teacher --}}
@section('body')
<div class="page-table shadow">
    <form action="{{route('mngTeacher.update', ['mngTeacher' => $teacher->teacher_id])}}" method="post">
        @method('PUT')
        @csrf

          <label for="">Tên tài khoản</label>
          <input type="text" class="form-control" name="username" aria-describedby="helpId" value="{{$account->username}}">
          <label for="">Mật khẩu</label>
          <input type="password" class="form-control" name="password" aria-describedby="helpId" value="{{$account->password}}">
          <div class="form-check">
            <label class="form-check-label">
                <input type="checkbox" class="form-check-input" name="isEditAccount" value="true">
                Sửa tài khoản (Mặc định không sửa)
            </label>
          </div>
          <label for="">Tên giảng viên</label>
          <input type="text" class="form-control" name="teacher_name" aria-describedby="helpId" value="{{$teacher->teacher_name}}">
          <label for="">Email</label>
          <input type="email" class="form-control" name="email" aria-describedby="helpId" value="{{$teacher->email}}">
          <label for="">Ngày sinh</label>
          <input type="date" class="form-control" name="birthday" aria-describedby="helpId" value="{{$teacher->birthday}}">
          <label for="">Số điện thoại</label>
          <input type="text" class="form-control" name="phone" aria-describedby="helpId" value="{{$teacher->phone}}">
          <label for="">Chức danh</label>
          <select class="form-control" name="title">

            <option value="GIẢNG VIÊN"
            @if ($teacher->title=="GIẢNG VIÊN")
                selected
            @endif>GIẢNG VIÊN</option>
            <option value="THẠC SĨ"
            @if ($teacher->title=="THẠC SĨ")
                selected
            @endif
            >THẠC SĨ</option>
            <option value="TIẾN SĨ"
            @if ($teacher->title=="TIẾN SĨ")
                selected
            @endif
            >TIẾN SĨ</option>
            <option value="PHÓ GIÁO SƯ"
            @if ($teacher->title=="PHÓ GIÁO SƯ")
                selected
            @endif
            >PHÓ GIÁO SƯ</option>
            <option value="GIÁO SƯ"
            @if ($teacher->title=="GIÁO SƯ")
                selected
            @endif
            >GIÁO SƯ</option>
          </select>
          <div class="form-group">
            <label for="">Ngành</label>
            <select class="form-control" name="major_id" id="major_id">
                @foreach ($majors as $major)
                    <option value={{$major->id}}
                        @if ($major->id==$teacher->major_id)
                        selected
                        @endif
                    >{{$major->name}}</option>
                @endforeach
            </select>
          </div>

        <button type="submit" class="btn btn-primary mt-3">Sửa</button>
    </form>
</div>
@endsection
