@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <div class="row justify-content-center align-middle">
        <div class="col-md-4">
            <img src="{{url('/')."/storage/Yourchoice.png"}}" class="img_nav" srcset="">
        </div>
       <div class="col-md-4">
            <h3>Đăng nhập</h3>
            <form action="{{route('admin.login')}}" method="post">
                @csrf
                <div class="form-group">
                  <label for="">Tên tài khoản</label>
                  <input type="text"
                    class="form-control" name="username" aria-describedby="helpId" placeholder="">
                </div>
                <div class="form-group">
                    <label for="">Mật khẩu</label>
                    <input type="password"
                      class="form-control" name="password" aria-describedby="helpId" placeholder="">
                </div>
                <button type="submit" class="mt-3 btn btn-primary">Đăng nhập</button>
            </form>
       </div>
       <div class="col-md-4">
            <img src="{{url('/')."/storage/Student1.png"}}" class="img_nav" srcset="">
       </div>
    </div>
</div>
@endsection
