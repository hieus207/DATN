@extends('layouts.app')

@section('content')
<div class="container-fluid">
    <div class="row justify-content-center">
       <div class="col-md-3">
           <div class="list-group list-nav shadow">
               <a href="#" class="list-group-item list-group-item-action">Quản lý</a>
               <a href="{{route("mngPhase.index");}}" class="list-group-item list-group-item-action {{str_contains($_SERVER['REQUEST_URI'],"mngPhase")==true?"active":""}}">Quản lý đợt làm đồ án</a>
               <a href="{{route("mngStudent.index");}}" class="list-group-item list-group-item-action  {{str_contains($_SERVER['REQUEST_URI'],"mngStudent")==true?"active":""}}">Quản lý sinh viên</a>
               <a href="{{route("mngTeacher.index");}}" class="list-group-item list-group-item-action  {{str_contains($_SERVER['REQUEST_URI'],"mngTeacher")==true?"active":""}}">Quản lý giảng viên</a>
               <a href="{{route("mngMajor.index");}}" class="list-group-item list-group-item-action  {{str_contains($_SERVER['REQUEST_URI'],"mngMajor")==true?"active":""}}">Quản lý ngành</a>
               <a href="{{route("mngPlatform.index");}}" class="list-group-item list-group-item-action  {{str_contains($_SERVER['REQUEST_URI'],"mngPlatform")==true?"active":""}}">Quản lý nền tảng</a>
               <a href="{{route("logout");}}" class="list-group-item list-group-item-action">Đăng xuất</a>
           </div>
           <img src="{{url('/')."/storage/Yourchoice.png"}}" class="img_nav" srcset="">
       </div>
        <div class="col-md-9">
            @if (isset($errors))
                @foreach ($errors as $error)
                    <div class="mb-3">
                        <span class="error">
                            {{$error}}
                        </span>
                        <div class="clearfix"></div>
                    </div>
                @endforeach
            @endif
            @if (!(empty($message) && !session()->exists('message')))
                <div class="mb-3">
                    <span class="message">
                        {{empty($message)?session()->pull('message'):$message}}
                    </span>
                    <div class="clearfix"></div>
                </div>
            @endif
            @yield('body')
        </div>
    </div>
</div>
@endsection
