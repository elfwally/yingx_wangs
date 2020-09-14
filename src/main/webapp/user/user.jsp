<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    $(function () {
        $("#userTable").jqGrid(
            {
                /*
         后台以下方式返回
       *  page: 当前页
       *  total: 总页数
       *  records: 总条数
       *  rows: 返回的数据
       * */
                url: "${path}/user/querypage",//发送请求 传递什么参数？  page页码    rows每页展示多少条
                editurl: "${path}/user/edit", //oper  三个值  edit add del  后台接收名称必为 oper
                datatype: "json",    //响应  拿到的返回值？page页码   rows当前页的数据  total总页数    records总条数
                rowNum: 2,
                rowList: [2, 10, 20, 30],
                pager: '#userPage',
                styleUI: "Bootstrap",
                height: "auto",
                autowidth: true,
                viewrecords: true,    //展示总条数
                colNames: ['Id', '用户名', '手机号', '微信', '头像', '签名', '状态', '注册时间'],
                colModel: [
                    {name: 'id', width: 55},  //每个名字 'id' 'username' 。。。都要对应后台 实体类属性的名 （要保持一致否则数据不展示）
                    {name: 'username', editable: true, width: 90},  //editable:true  允许进行增删改操作
                    {name: 'phone', editable: true, width: 100},
                    {name: 'wechat', editable: true, align: "center"},
                    {
                        name: 'headimg', editable: true, edittype: "file", align: "center",
                        formatter: function (cellvalue, options, rowObject) {
                            //三个参数  列的值 ，操作 ，行对象
                            return "<img width='120px' height='80px' src=' " + cellvalue + "'/>";
                            // console.log(cellvalue);
                        }
                    },
                    {name: 'sign', editable: true, align: "center"},
                    {
                        name: 'status', width: 150, sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            //三个参数  列的值 ，操作 ，行对象（一条数据）
                            if (cellvalue == "正常") {
                                return "<a class='btn btn-success' onclick='updatestatus(\"" + cellvalue + "\",\"" + rowObject.id + "\")' >正常</a>"
                            } else {
                                return "<a class='btn btn-danger' onclick='updatestatus(\"" + cellvalue + "\",\"" + rowObject.id + "\")' >冻结</a>"
                            }
                        }
                    },
                    {name: 'createdate', align: "center"},
                ]
            });
        //编辑工具栏
        $("#userTable").jqGrid('navGrid', '#userPage', {edit: false, add: true, del: false},
            {},//操作修改之后的额外操作
            {
                //操作添加之后的额外操作
                //关闭文本框
                closeAfterAdd: true,//关闭文本框要写对应的关闭窗口  Add  Edit  Del
                //手动文件上传
                afterSubmit: function (response) {  //后台添加操作之后 返回json数据 其中包括 用户id 然后在进行此操作
                    var id = response.responseJSON.id;//获取后台存入map集合 然后返回json数据类型的map对象 在map中获取id
                    //异步文件上传
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadheadimg",  //走后台上传文件的方法
                        type: "post",
                        dataType: "text",
                        fileElementId: "headimg",//上传的文件域id   此id名称必须和上方jqgrid 表格的名字 一致  {name : 'headimg',editable:true,edittype:"file",align : "center",
                        data: {id: id}, //发送用户id
                        success: function () {
                            // alert("上传头像成功返回！");
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");

                        },
                        error: function () {
                            alert("上传头像出错！")
                        }
                    });
                    // afterSubmit 必须要有返回值  返回值 随便（返回值随便什么都行 不影响程序运行 ）
                    return "123";
                }
            },
            {}//操作删除之后的额外操作
        );

    });

    //修改用户状态
    function updatestatus(status, id) {
        // alert("出发ajax!");
        if (status == "正常") {
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"status": "冻结", "id": id, "oper": "edit"},  //通过传入指定的数据 进行访问后台edit方法 省去了写新的修改方法和判断用户状态，从而直接修改  减少对数据库的访问
                success: function () {
                    //刷新表单
                    $("#userTable").trigger("reloadGrid");
                }
            })
        } else {
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"status": "正常", "id": id, "oper": "edit"},
                success: function () {
                    //刷新表单
                    $("#userTable").trigger("reloadGrid");
                }
            })
        }
    }

    /*点击发送验证码*/
    $("#sendPhoneCode").click(function () {
        //获取手机号
        var phone = $("#phone").val();
        //发送ajax
        $.ajax({
            type: "post",
            data: {"phone": phone},
            dataType: "JSON",
            url: "${path}/user/phoneSend",
            success: function (data) {
                //获取验证码
                $("#showMsgs").html("验证码为：" + data.code);
                //展示警告框
                $("#delMsgs").show();
                ////10秒自动消失
                setTimeout(function () {
                    //关闭提示框
                    $("#delMsgs").hide();
                }, 10000);
            }
        })
    })

    //导出用户数据
    $("#ExportUser").click(function () {
        //发送ajax
        $.ajax({
            url: "${path}/user/ExportUser",
            type: "post",
            dataType: "JSON",
            success: function (data) {

                //弹出提示信息
                $("#showMsgs").html("导出用户" + data.message);
                //展示警告框
                $("#delMsgs").show();
                //三秒后消失警告框
                setTimeout(function () {
                    //关闭提示框
                    $("#delMsgs").hide();
                }, 3000);
            },
            error: function (data) {
                //弹出提示信息
                $("#showMsgs").html("导出用户" + data.message);
                //展示警告框
                $("#delMsgs").show();
                //三秒后消失警告框
                setTimeout(function () {
                    //关闭提示框
                    $("#delMsgs").hide();
                }, 3000);
            }
        })

    })
    //上传文件 导入excel表格
    function doUpload() {

        var formData = new FormData($( "#uploadForm" )[0]);
        $.ajax({
            url: '${path}/user/ImportUser' ,
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                //获取map集合信息
                $("#showMsgs").html( data.message);
                //展示警告框
                $("#delMsgs").show();
                ////3秒自动消失
                setTimeout(function () {
                    //关闭提示框
                    $("#delMsgs").hide();
                }, 3000);
            },
            error: function (data) {
                //获取map集合信息
                $("#showMsgs").html( data.message);
                //展示警告框
                $("#delMsgs").show();
                ////3秒自动消失
                setTimeout(function () {
                    //关闭提示框
                    $("#delMsgs").hide();
                }, 3000);
            },

        });
    }
</script>

<%--初始化面板--%>
<div class="panel panel-info">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>
    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a>用户信息</a></li>
    </div>
    <%--验证码输入框--%>
    <div class="input-group" style="width: 100px;float: right">
        <input type="text" id="phone" class="form-control" style="width: 124px" placeholder="请输入手机号" aria-describedby="basic-addon2">
        <span class="btn btn-danger" id="sendPhoneCode">点击发送验证码</span>
    </div>
    <%--按钮--%>
    <div class="panel panel-body">
        <a class="btn btn-success" id="ExportUser">导出用户数据</a>
    </div>
        <form id= "uploadForm" >
            <p >上传文件： <input type="file" name="files" /></p>
            <a class="btn btn-success" onclick="doUpload()">导入用户数据</a>
        </form>
    <%--警告框--%>
    <div id="delMsgs" style="width: 300px;display: none" class="alert alert-warning alert-dismissible" role="alert">
        <strong id="showMsgs"/>
    </div>
    <table id="userTable">

    </table>

    <div id="userPage"></div>
</div>