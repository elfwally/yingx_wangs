<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>yingx Login</title>
    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/form-elements.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="shortcut icon" href="assets/ico/favicon.png">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${path}/login/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${path}/login/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${path}/login/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="${path}/login/assets/ico/apple-touch-icon-57-precomposed.png">
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/login/assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="${path}/login/assets/js/jquery.backstretch.min.js"></script>
    <script src="${path}/login/assets/js/scripts.js"></script>
    <script src="${path}/login/assets/js/jquery.validate.min.js"></script>
    <script>
        $(function () {

            //点击切换验证码  为第一种方式
            $("#captchaImage").click(function(){
                $("#captchaImage").prop("src","${path}/admin/code?d="+new Date().getTime())
            })
            //表单验证
            $.extend($.validator.messages, {
                required: "<span style='color:red' >这是必填字段</span>",  //再需要验证的input上加入 required属性
                minlength: $.validator.format("<span style='color:red' >最少要输入 4 个字符</span>"),  //再需要验证的input上加入  minlength="4"
            });

            //异步登录
            $("#loginButtonId").click(function () {
                //先判断表单验证是否通过
                var isOk=$("#loginForm").valid(); //开启表单验证
                if (isOk){
                    //获取表单数据
                    //提交表单数据
                    $.ajax({
                        type:"POST",
                        url:"${path}/admin/login",
                        data:$("#loginForm").serialize(), //表单序列化
                        dataType:"json",  //返回的是string字符串 用text  如果用json转换的字符串 用json
                        success:function (data) {
                            //判断是否登录成功  返回的是json形式的 map集合对象
                            if(data.state){
                                //alert("登陆成功")
                                window.location.href = "${path}/main/main.jsp";
                                //parent.document.location.href = "/main/main.jsp";
                            }else {
                               // alert("登录没有跳转");
                                $("#msgDiv").html(data.message);
                            }

                        },
                        error:function () {
                            alert("出错了");
                            $("#msgDiv").html("后台数据出错啦！");
                        }
                    });
                }
               /* //获取数据值  另一种发送ajax方式  但是无法跳转页面 使用上面方法 返回 map对象 可以进行判断跳转页面
               var username= $("input[id='form-username']").val();  //获取数据信息方式
               var password= $("input[id='form-password']").val();
               var code =  $("input[id='form-code']").val();
               //发送ajax
              $.ajax({
                  type:"POST",
                  url:"${path}/admin/login",
                  data:{"username":username,"password":password,"code":code},
                  dataType:"text",  //返回的是string字符串 用text  如果用json转换的字符串 用json
                  success:function (data) {
                      //  alert("返回数据");
                     // window.location.href = "${path}/main/main.jsp";
                      if(data.eq("success")){
                          alert("登陆成功")
                          window.location.href = "/main/main.jsp";
                          //parent.document.location.href = "/main/main.jsp";
                      }else {
                          alert("登录没有跳转");
                          $("#msgDiv").html(data);
                      }

                  },
                  error:function () {
                      alert("出错了");
                      $("#msgDiv").html("后台数据出错啦！");
                  }
              });*/
            });
        })
    </script>
</head>

<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>YINGX</strong> Login Form</h1>
                    <div class="description">
                        <p>
                            <a href="#"><strong>YINGX</strong></a>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top" style="width: 450px">
                        <div class="form-top-left">
                            <h3>Login to showAll</h3>
                            <p>Enter your username and password to log on:</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom" style="width: 450px">
                        <form role="form" action="" method="post" class="login-form" id="loginForm">
                            <span style="color:red" id="msgDiv"></span>
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <input type="text" name="username" placeholder="请输入用户名..." class="form-username form-control" required id="form-username">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="form-password">Password</label>
                                <input type="password" name="password"  placeholder="请输入密码..." class="form-password form-control" required id="form-password">
                            </div>
                            <div class="form-group">
                                <%--使用以下两种方式 第一种要用上面的 获取方式来更换验证码   第二种直接点击下一张来换验证码--%>
                                <%--第一种--%>
                                <label class="sr-only" for="form-code">Code</label>
                                <img id="captchaImage" style="height: 48px" class="captchaImage" src="${path}/admin/code">
                                <input style="width: 250px;height: 50px;border:3px solid #ddd;border-radius: 4px;" required type="test"  name="code" id="form-code">
                                  <%-- &lt;%&ndash;第二种&ndash;%&gt;
                                    <a class="code_pic" id="vcodeImgWrap" name="change_code_img" href="javascript:void(0);">
                                        <img id="num" src="${path}/admin/code" class="Ucc_captcha Captcha-image"
                                             typeof="">
                                    </a>
                                    <a id="vcodeImgBtn" name="change_code_link" class="code_picww" href="javascript:changeImage()"
                                       onClick="document.getElementById('num').src='${path}/admin/code?dt='+(new Date()).getTime()">换张图</a>--%>
                            </div>
                            <input type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="loginButtonId" value="登录">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="copyrights">Collect from <a href="http://www.cssmoban.com/" title="网站模板">网站模板</a></div>


<!-- Javascript -->

<!--[if lt IE 10]>
<script src="assets/js/placeholder.js"></script>
<![endif]-->

</body>

</html>