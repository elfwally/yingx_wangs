<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script src="${path}/bootstrap/js/jquery.min.js"></script>
<script src="${path}/bootstrap/js/echarts.js"></script><%--导入echarts 的jar--%>
<script src="${path}/bootstrap/js/china.js"></script><%--导入echarts 中国地图的jar--%>
<script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.17.js"></script><%--导入goeasyjar--%>
<script type="text/javascript">
    $(function(){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        $.get("${path}/echarts/queryUserCity",function (data) {
            // 指定图表的配置项和数据

            var series=[];
            for(var i = 0;i < data.length;i ++){
                var e = data[i];
                alert(e.title)
                alert(e.city)
                series.push(
                    {
                        name: e.title,
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        data:e.city
                    }
                );
            }
            var option = {
                title : {
                    text: '用户地图分布',
                    subtext: '纯属虚构',
                    left: 'center'
                },
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data:['男','女']
                },
                visualMap: {
                    min: 0,
                    max: 200,
                    left: 'left',
                    top: 'bottom',
                    text:['高','低'],           // 文本，默认为数值文本
                    calculable : true
                },
                toolbox: {
                    show: true,
                    orient : 'vertical',
                    left: 'right',
                    top: 'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                series : series
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        },"json")

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