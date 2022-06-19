@extends('layouts.app')

@section('content')
<div class="container">
    <div class="row justify-content-center">
       <div class="col-md-3">
            <h3>Login</h3>
            <form action="{{route('admin.login')}}" method="post">
                @csrf
                <div class="form-group">
                  <label for="">Username</label>
                  <input type="text"
                    class="form-control" name="username" aria-describedby="helpId" placeholder="">
                </div>
                <div class="form-group">
                    <label for="">Password</label>
                    <input type="password"
                      class="form-control" name="password" aria-describedby="helpId" placeholder="">
                </div>
                <button type="submit" class="mt-3 btn btn-primary">Login</button>
            </form>
       </div>
    </div>
</div>
@endsection
