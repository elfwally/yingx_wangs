<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script src="${path}/bootstrap/js/jquery.min.js"></script>
<script src="${path}/bootstrap/js/echarts.js"></script>
<script type="text/javascript">
   $(function () {
       // 基于准备好的dom，初始化echarts实例
       var myChart = echarts.init(document.getElementById('main'));

        //发送ajax
       $.get("${path}/echarts/queryUserNum",function (data) {
           //设置颜色配置
           var colors = ['#5793f3', '#d14a61', '#675bba'];
           // 指定图表的配置项和数据
           option = {
               color: colors,
               title: {//标题名称
                   text: '用户注册分析图',
                   link:"${path}/main/main.jsp", //跳转页面链接
                   subtext:"纯属虚构"
               },
               tooltip: {//鼠标放上显示提示信息
                   trigger: 'axis',
                   axisPointer: {
                       type: 'cross'
                   }
               },
               grid: {
                   right: '20%'
               },
               toolbox: {
                   feature: {
                       dataView: {show: true, readOnly: false},
                       restore: {show: true},
                       saveAsImage: {show: true}
                   }
               },
               legend: {//属性 选项卡 可以展示与隐藏 相关信息
                   data: ['男', '女']
               },
               xAxis: [//X轴数据 横轴
                   {
                       type: 'category',
                       axisTick: {
                           alignWithLabel: true
                       },
                       data: data.month
                   }
               ],
               yAxis: [//Y轴数据  纵轴 图形大小自适应
                   {
                       type: 'value',
                       name: '男',
                       min: 0,
                       max: 250,
                       posio: 'left',
                       axisLine: {
                           lineStyle: {
                               color: colors[0]
                           }
                       },
                       axisLabel: {
                           formatter: '{value} ml'
                       }
                   },
                   {
                       type: 'value',
                       name: '女',
                       min: 0,
                       max: 250,
                       position: 'right',
                       offset: 80,
                       axisLine: {
                           lineStyle: {
                               color: colors[1]
                           }
                       },
                       axisLabel: {
                           formatter: '{value} ml'
                       }
                   },
               ],
               series: [//跟选项卡数据时一一对应的  具体数据
                   {
                       name: '男',
                       type: 'bar',
                       data: data.boys
                   },
                   {
                       name: '女',
                       type: 'bar',
                       yAxisIndex: 1,
                       data: data.girls
                   },

               ]
           };
           myChart.setOption(option);
       },"json")

   })




</script>
<html>

<body>
<%--初始化面板
<div class="panel panel-info">
    &lt;%&ndash;面板头&ndash;%&gt;
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>
    &lt;%&ndash;标签页&ndash;%&gt;
    <div class="nav nav-tabs">
        <li class="active"><a>用户信息</a></li>
    </div>

        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
        <div id="main" style="width: 600px;height:400px;"></div>

    <div id="userPage"></div>
</div>--%>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>