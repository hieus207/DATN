@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <form action="{{route('mngStudent.update', ['mngStudent' => $student])}}" method="post">
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
          <label for="">Tên sinh viên</label>
          <input type="text" class="form-control" name="student_name" aria-describedby="helpId" value="{{$student->student_name}}">
          <label for="">Email</label>
          <input type="email" class="form-control" name="email" aria-describedby="helpId" value="{{$student->email}}">
          <label for="">Ngày sinh</label>
          <input type="date" class="form-control" name="birthday" aria-describedby="helpId" value="{{$student->birthday}}">
          <label for="">Số điện thoại</label>
          <input type="text" class="form-control" name="phone" aria-describedby="helpId" value="{{$student->phone}}">
          <div class="form-group">
            <label for="">Ngành</label>
            <select class="form-control" name="major_id" id="major_id">
                @foreach ($majors as $major)
                    <option value={{$major->id}} @if ($major->id==$student->major_id)
                        selected
                    @endif>{{$major->name}}</option>
                @endforeach
            </select>
          </div>

        <button type="submit" class="btn btn-primary mt-3">Sửa</button>
    </form>
</div>
@endsection
