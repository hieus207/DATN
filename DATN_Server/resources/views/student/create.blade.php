@extends('layouts.body')
{{-- STUDENT --}}
@section('body')
<div class="page-table shadow">
    <form action="{{ route('mngStudent.store') }}" method="post">

        @csrf
        @if (isset($data))
            <label for="">Tên tài khoản</label>
            <input type="text" class="form-control" name="username" aria-describedby="helpId" placeholder=""
                value="{{ $data->username }}">
            <label for="">Mật khẩu</label>
            <input type="password" class="form-control" name="password" aria-describedby="helpId" placeholder=""
                value="{{ $data->password }}">
            <label for="">Tên sinh viên</label>
            <input type="text" class="form-control" name="student_name" aria-describedby="helpId" placeholder=""
                value="{{ $data->student_name }}">
            <label for="">Email</label>
            <input type="email" class="form-control" name="email" aria-describedby="helpId" placeholder=""
                value="{{ $data->email }}">
            <label for="">Ngày sinh</label>
            <input type="date" class="form-control" name="birthday" aria-describedby="helpId" placeholder=""
                value="{{ $data->birthday }}">
            <label for="">Số điện thoại</label>
            <input type="text" class="form-control" name="phone" aria-describedby="helpId" placeholder=""
                value="{{ $data->phone }}">

            <div class="form-group">
                <label for="">Ngành</label>
                <select class="form-control" name="major_id" id="major_id">
                    @foreach ($majors as $major)
                        <option value={{ $major->id }} @if ($major->id == $data->major_id) selected @endif>
                            {{ $major->name }}</option>
                    @endforeach
                </select>
            </div>
        @else
            <label for="">Tên tài khoản</label>
            <input type="text" class="form-control" name="username" aria-describedby="helpId" placeholder="">
            <label for="">Mật khẩu</label>
            <input type="password" class="form-control" name="password" aria-describedby="helpId" placeholder="">
            <label for="">Tên sinh viên</label>
            <input type="text" class="form-control" name="student_name" aria-describedby="helpId" placeholder="">
            <label for="">Email</label>
            <input type="email" class="form-control" name="email" aria-describedby="helpId" placeholder="">
            <label for="">Ngày sinh</label>
            <input type="date" class="form-control" name="birthday" aria-describedby="helpId" placeholder="">
            <label for="">Số điện thoại</label>
            <input type="text" class="form-control" name="phone" aria-describedby="helpId" placeholder="">
            <div class="form-group">
                <label for="">Ngành</label>
                <select class="form-control" name="major_id" id="major_id">
                    @foreach ($majors as $major)
                        <option value={{ $major->id }} checked>{{ $major->name }}</option>
                    @endforeach
                </select>
            </div>
        @endif
        <button type="submit" class="btn btn-primary mt-3">Tạo</button>
    </form>
</div>
@endsection
