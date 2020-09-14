
<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>
    $(function(){
        //初始化表单属性
        $("#logTable").jqGrid({
            url : "${path}/log/queryLogPage",  //分页查询   page  rows  total recoreds  page  rows
            datatype : "json",
            rowNum : 3,  //每页展示是条数
            rowList : [ 3,5,10, 20, 30 ],
            pager : '#logPager',
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            viewrecords:true,  //是否展示数据总条数
            colNames : [ 'ID', '管理员名称','时间' , '操作', '操作状态'],
            colModel : [
                {name : 'id',width : 55},
                {name : 'admin',width : 90},
                {name : 'date',width : 250,align : "center",edittype:"file"},
                {name : 'option',width : 100},
                {name : 'status',width : 80,align : "center"},
            ]
        });
    });


</script>

<%--初始化一个面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading" align="center">
        <h2>视频管理</h2>
    </div>

    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">视频信息</a></li>
    </div>

    <%--警告提示框--%>
    <div id="deleteMsg" class="alert alert-danger" style="height: 50px;width: 250px;display: none" align="center">
        <span id="showMsg"/>
    </div>

    <%--初始化表单--%>
    <table id="logTable" />

    <%--工具栏--%>
    <div id="logPager" />

</div>