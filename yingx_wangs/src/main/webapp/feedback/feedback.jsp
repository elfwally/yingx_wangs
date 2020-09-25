
<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function(){
        $("#feedbackTable").jqGrid(
            {
                /*
         后台以下方式返回
       *  page: 当前页
       *  total: 总页数
       *  records: 总条数
       *  rows: 返回的数据
       * */
                url :"${path}/feedback/querypage",//发送请求 传递什么参数？  page页码    rows每页展示多少条
                editurl:"${path}/feedback/edit", //oper  三个值  edit add del  后台接收名称必为 oper
                datatype : "json",    //响应  拿到的返回值？page页码   rows当前页的数据  total总页数    records总条数
                rowNum : 2,
                rowList : [ 2,10, 20, 30 ],
                pager : '#feedbackPage',
                styleUI:"Bootstrap",
                height:"auto",
                autowidth:true,
                viewrecords : true,    //展示总条数
                colNames : [ 'Id', '标题', '内容', '用户ID', '反馈时间' ],
                colModel : [
                    {name : 'id',width : 55},  //每个名字 'id' 'username' 。。。都要对应后台 实体类属性的名 （要保持一致否则数据不展示）
                    {name : 'title',editable:true,width : 90},  //editable:true  允许进行增删改操作
                    {name : 'content',editable:true,width : 100},
                    {name : 'userID',editable:true,align : "center"},
                    {name : 'saveDate',align : "center"},
                ]
            });
        //编辑工具栏
        $("#feedbackTable").jqGrid('navGrid', '#feedbackPage', {edit : false,add : false,del : true,deltext:"删除"},
            {},//操作修改之后的额外操作
            {},
            {
                closeAfterDel:true,





            },//操作删除之后的额外操作
            $("#feedbackTbale").trigger("reloadGrid")
        );
    });
</script>

<%--初始化面板--%>
<div class="panel panel-info">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>反馈信息</h2>
    </div>
    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a>反馈信息</a></li>
    </div>

    <table id="feedbackTable">

    </table>

    <div id="feedbackPage"></div>
</div>