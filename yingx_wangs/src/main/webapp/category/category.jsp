
<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        //父表格
        $("#categoryTable").jqGrid(
            {
                url: "${path}/category/category",//父表格提供数据
                editurl: "${path}/category/edit", //oper  三个值  edit add del  后台接收名称必为 oper
                datatype: "json",
                height: "auto",
                styleUI: "Bootstrap",
                autowidth: true,
                rowNum: 2,
                rowList: [2, 8, 10, 20, 30],
                pager: '#categoryPage',
                viewrecords: true,
                subGrid: true,//添加subgird支持
                caption: "展示类别信息",
                colNames: ['id', '类别名', '级别', '父类id'],
                colModel: [
                    {name: 'id', width: 55},
                    {name: 'name', editable: true, width: 90},
                    {name: 'levels', width: 100},
                    {name: 'parentId', width: 80, align: "right"},
                ],


                //子表格
                subGridRowExpanded: function (subgrid_id, row_id) {//第一个参数 子容器的id  第二个参数  当前行id
                    var subgrid_table_id = subgrid_id + "_t";
                    var pager_id = "p_" + subgrid_table_id;
                    //在子容器中，创建了一个子表格和子表格的分页工具
                    $("#" + subgrid_id).html(
                        "<table id='" + subgrid_table_id + "' class='scroll'></table>" +
                        "<div id='" + pager_id + "' class='scroll'></div>");
                    //对子表格进行初始化
                    $("#" + subgrid_table_id).jqGrid(
                        {
                            url: "${path}/category/twoCategory?id=" + row_id,//子表格提供数据
                            editurl: "${path}/category/edit?parentId=" + row_id, //oper  三个值  edit add del  后台接收名称必为 oper
                            datatype: "json",
                            rowNum: 2,
                            rowList: [2, 8, 10, 20, 30],
                            pager: pager_id,
                            styleUI: "Bootstrap",
                            autowidth: true,
                            height: '100%',
                            colNames: ['id', '类别名', '级别', '父类id'],
                            colModel: [
                                {name: 'id', width: 55},
                                {name: 'name', editable: true, width: 90},
                                {name: 'levels', width: 100},
                                {name: 'parentId', width: 80, align: "center"},
                            ]
                        });
                    //子类别编辑工具
                    $("#" + subgrid_table_id).jqGrid('navGrid',
                        "#" + pager_id, {edit: true, add: true, del: true},
                        {//修改操作之后执行的额外操作
                            closeAfterEdit: true  //关闭文本框
                        },
                        {//添加操作之后执行的额外操作
                            closeAfterAdd: true,//关闭文本框
                        },
                        {//删除操作之后执行的额外操作
                            closeAfterDel: true,  //关闭文本框
                            afterSubmit: function (data) {

                                //将提示信息放入提示框
                                $("#showMsg").html(data.responseJSON.message);
                                //展示提示框
                                $("#delMsg").show();

                                //3秒自动消失
                                setTimeout(function () {
                                    //关闭提示框
                                    $("#delMsg").hide();
                                }, 3000);
                                //刷新表单
                                $("#userTable").trigger("reloadGrid");
                                return "hello";

                            }
                        }
                    );
                },
            }); //父类别编辑工具
        $("#categoryTable").jqGrid('navGrid', '#categoryPage', {add: true,edit: true, del: true},//运行执行添加 修改 删除 操作 下面的额外操作顺序是修改 添加  删除 无法更改额外操作的修 加 删 位置
            {//修改操作之后执行的额外操作    三个执行的额外顺序无法更改  修 加 删
                closeAfterEdit: true //关闭文本框
            },
            {//添加操作之后执行的额外操作

                closeAfterAdd: true,  //执行完添加之后 自动关闭添加窗口 //关闭文本框
            },
            {//删除操作之后执行的额外操作
                closeAfterDel: true, //关闭文本框
                afterSubmit: function (data) {

                    //将提示信息放入提示框
                    $("#showMsg").html(data.responseJSON.message);
                    //展示提示框
                    $("#delMsg").show();

                    //3秒自动消失
                    setTimeout(function () {
                        //关闭提示框
                        $("#delMsg").hide();
                    }, 3000);
                    //刷新表单
                    $("#userTable").trigger("reloadGrid");
                    return "hello";

                }

            }
        );


    });
</script>

<%--初始化面板--%>
<div class="panel panel-warning">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>类别信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a>类别信息</a></li>
    </div>
    <%--警告框--%>
    <div id="delMsg" style="width: 300px;display: none" class="alert alert-warning alert-dismissible" role="alert">
        <strong id="showMsg"/>
    </div>
        <%--表格--%>
    <table id="categoryTable">

    </table>
    <div id="categoryPage"></div>
</div>