<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script src="${path}/bootstrap/js/jquery.min.js"></script>
<script src="${path}/bootstrap/js/echarts.js"></script>
<script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.17.js"></script>
<script type="text/javascript">
    /*初始化goeasy对象*/
    var goEasy = new GoEasy({
        host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
        appkey: "BC-eedfe1730ba14d7d9741c8d33048f862", //替换为您的应用appkey
    });
    $(function(){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        /*接收消息*/
        goEasy.subscribe({
            channel: "yingx_wang", //替换为您自己的channel 管道名称
            onMessage: function (message) {
                //将json字符串转换为javascript对象
                var data=JSON.parse(message.content);
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '用户各月份注册分析图',
                        link:"${path}/main/main.jsp",
                        subtext:"纯属虚构"
                    },
                    tooltip: {},//鼠标提示
                    legend: {
                        data:['男','女']//选项卡
                    },
                    xAxis: {
                        data: data.month
                    },
                    yAxis: {},//自适应
                    series: [{
                        name: '男',//跟选项卡是一一对应的
                        type: 'bar',
                        data:  data.boys
                    },{
                        name: '女',//跟选项卡是一一对应的
                        type: 'bar',
                        data:  data.girls
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        });

    })
</script>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>