# 一：Apache ECharts

一个基于 JavaScript 的开源可视化图表库

官网：https://echarts.apache.org/zh/index.html

> 超文本标记语言（英语：HyperText Markup Language，简称：HTML）是一种用于创建网页的标准标记语言。您可以使用 HTML 来建立自己的 WEB 站点，HTML 运行在浏览器上，由浏览器来解析。
>
> ![img](https://www.runoob.com/wp-content/uploads/2013/06/02A7DD95-22B4-4FB9-B994-DDB5393F7F03.jpg)

## 1.快速上手

### 1.1 获取 Apache ECharts

Apache ECharts 支持多种下载方式，可以在下一篇教程[安装](https://echarts.apache.org/handbook/zh/basics/download)中查看所有方式。这里，我们以从 [jsDelivr](https://www.jsdelivr.com/package/npm/echarts) CDN 上获取为例，介绍如何快速安装。

在 https://www.jsdelivr.com/package/npm/echarts 选择 echarts.min.js，点击并保存为 `echarts.min.js` 文件。

### 1.2 新建文件夹，按如图创建

![image-20230701201225783](image-20230701201225783.png)

### 1.3页面study.html，如下编写

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"> 
    <title>ECharts</title>
    <script src="js/echarts.min.js"> </script>
</head>
<body>
    <div id="main" style="width: 600px;height:400px;"></div>  
</body>
</html>
```

### 1.4 [echarts.init](https://echarts.apache.org//api.html#echarts.init) 方法初始化一个 echarts 实例并通过 [setOption](https://echarts.apache.org//api.html#echartsInstance.setOption) 方法生成一个简单的柱状图，下面是完整代码。

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>ECharts</title>
    <!-- 引入刚刚下载的 ECharts 文件 -->
    <script src="js/echarts.min.js"> </script>
  </head>
  <body>
    <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>
    <script type="text/javascript">
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('main'));

      // 指定图表的配置项和数据
      var option = {
        title: {
          text: 'ECharts 入门示例'
        },
        tooltip: {},
        legend: {
          data: ['销量']
        },
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
        },
        yAxis: {},
        series: [
          {
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
          }
        ]
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    </script>
  </body>
</html>
```

### 1.5 小贴士：setOption 

设置图表实例的配置项以及数据，它是万能接口，所有参数和数据的修改都可以通过 `setOption` 完成，ECharts 会合并新的参数和数据，然后刷新图表。如果开启动画(https://echarts.apache.org/zh/option.html#option.animation)的话，ECharts 找到两组数据之间的差异然后通过合适的动画去表现数据的变化。

## 2.数据集

### 2.1简介

`数据集（dataset）`是专门用来管理数据的组件。虽然每个系列都可以在 `series.data` 中设置数据，但是从 ECharts4 支持数据集开始，更推荐使用数据集来管理数据。因为这样，数据可以被多个组件复用，也方便进行 “数据和其他配置” 分离的配置风格。毕竟，在运行时，数据是最常改变的，而其他配置大多并不会改变。

见：https://echarts.apache.org/handbook/zh/concepts/dataset

### 2.2数组作为数据源的数据集

```js
option = {
  legend: {},
  tooltip: {},
  dataset: {
    // 提供一份数据。
    source: [
      ['product', '2015', '2016', '2017'],
      ['Matcha Latte', 43.3, 85.8, 93.7],
      ['Milk Tea', 83.1, 73.4, 55.1],
      ['Cheese Cocoa', 86.4, 65.2, 82.5],
      ['Walnut Brownie', 72.4, 53.9, 39.1]
    ]
  },
  // 声明一个 X 轴，类目轴（category）。默认情况下，类目轴对应到 dataset 第一列。
  xAxis: { type: 'category' },
  // 声明一个 Y 轴，数值轴。
  yAxis: {},
  // 声明多个 bar 系列，默认情况下，每个系列会自动对应到 dataset 的每一列。
  series: [{ type: 'bar' }, { type: 'bar' }, { type: 'bar' }]
};
```

![image-20230701222353548](image-20230701222353548.png)

### 2.3对象数组作为数据集的（最常见）

```js
option = {
  legend: {},
  tooltip: {},
  dataset: {
    // 用 dimensions 指定了维度的顺序。直角坐标系中，如果 X 轴 type 为 category，
    // 默认把第一个维度映射到 X 轴上，后面维度映射到 Y 轴上。
    // 如果不指定 dimensions，也可以通过指定 series.encode
    // 完成映射。
    dimensions: ['product', '2015', '2016', '2017'],
    source: [
      { product: 'Matcha Latte', '2015': 43.3, '2016': 85.8, '2017': 93.7 },
      { product: 'Milk Tea', '2015': 83.1, '2016': 73.4, '2017': 55.1 },
      { product: 'Cheese Cocoa', '2015': 86.4, '2016': 65.2, '2017': 82.5 },
      { product: 'Walnut Brownie', '2015': 72.4, '2016': 53.9, '2017': 39.1 }
    ]
  },
  xAxis: { type: 'category' },
  yAxis: {},
  series: [{ type: 'bar' }, { type: 'bar' }, { type: 'bar' }]
};
```

![image-20230701222425979](image-20230701222425979.png)

### 2.4数据到图形的映射

如上所述，数据可视化的一个常见思路是：（I）提供数据，（II）指定数据到视觉的映射。

简而言之，可以进行这些映射的设定：

- 指定 `数据集` 的列（column）还是行（row）映射为 `系列（series）`。这件事可以使用 [series.seriesLayoutBy](https://echarts.apache.org/option.html#series.seriesLayoutBy) 属性来配置。默认是按照列（column）来映射。
- 指定维度映射的规则：如何从 dataset 的维度（一个“维度”的意思是一行/列）映射到坐标轴（如 X、Y 轴）、提示框（tooltip）、标签（label）、图形元素大小颜色等（visualMap）。这件事可以使用 [series.encode](https://echarts.apache.org/option.html#series.encode) 属性，以及 [visualMap](https://echarts.apache.org/option.html#visualMap) 组件来配置（如果有需要映射颜色大小等视觉维度的话）。
- **<font color='red'>上面的例子中，没有给出这种映射配置，那么 ECharts 就按最常见的理解进行默认映射：X 坐标轴声明为类目轴，默认情况下会自动对应到 `dataset.source` 中的第一列；三个柱图系列，一一对应到 `dataset.source` 中后面每一列。</font>**

## 3.数据的视觉映射

**数据可视化是数据到视觉元素的映射过程（这个过程也可称为视觉编码，视觉元素也可称为视觉通道）。**

**<font color='red'>ECharts 的每种图表本身就内置了这种映射过程，比如折线图把数据映射到“线”，柱状图把数据映射到“长度”。一些更复杂的图表，如关系图、事件河流图、树图也都会做出各自内置的映射</font>**。

此外，ECharts 还提供了 [visualMap 组件](https://echarts.apache.org/option.html#visualMap) 来提供通用的视觉映射。`visualMap` 组件中可以使用的视觉元素有：

- 图形类别（symbol）、图形大小（symbolSize）
- 颜色（color）、透明度（opacity）、颜色透明度（colorAlpha）、
- 颜色明暗度（colorLightness）、颜色饱和度（colorSaturation）、色调（colorHue）



## 4.常见的图表类型

见：https://echarts.apache.org/handbook/zh/how-to/chart-types/bar/basic-bar/

## 二：使用axios请求数据（ajax框架）

见：https://www.axios-http.cn/docs/intro

## 1.Axios 简介

Axios 是一个基于 promise （见：https://www.runoob.com/js/js-promise.html）网络请求库，作用于node.js 和浏览器中。 它是 isomorphic 的(即同一套代码可以运行在浏览器和node.js中)。在服务端它使用原生 node.js http 模块, 而在客户端 (浏览端) 则使用 XMLHttpRequests。

## 2. 请求数据

### 2.1 POST请求

```js
axios.post('http://localhost:8090/doLogin', {
    zhanghao: 'zhangsan',
    mima: '123'
  })
  .then(function (response) { //then 回调，返回数据传入response
    console.log(response);
  })
  .catch(function (error) {//如果出现异常 这个异常处理会执行
    console.log(error);
  });
```

### 2.2 GET请求

```js
axios.get('http://localhost:8090/dianyings?leiXingId=1')
.then(function (response) { //then 回调，返回数据传入response
    console.log(response);
 })
 .catch(function (error) {//如果出现异常 这个异常处理会执行
    console.log(error);
 });
```

### 2.3 编码测试

```html
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="js/axios.min.js"></script>
    <script>
        function queryDianYing() {
            axios.get('http://localhost:8090/dianyings?leiXingId=1')
                .then(function (response) { //then 回调，返回数据传入response
                    console.log(response);
                })
        }
        function login() {
            axios.post('http://localhost:8090/doLogin', {
                zhanghao: 'zhangsan',
                mima: '123'
            })
            .then(function (response) { //then 回调，返回数据传入response
                    console.log(response);
             })
        }
    </script>
</head> 
<body>
    <button type="button" onclick="queryDianYing()">电影列表</button>
    <button type="button" onclick="login()">登录</button>
</body>

</html>
```

# 三：可视化大屏

## 1.用EChart展示各类电影的数量饼图

### 1.1后端

数据集数据源格式： {
          value: 335,
          name: 'A'
        },
        {
          value: 234,
          name: 'B'
        },

所以 统计的Sql（后端要按照数据集数据的格式返回相应数据，有时候连属性名都确定 ）

```sql
select lx.id,lx.leixingming name,count(dy.id) value
from dianyingxinxi dy inner join leixing lx
on dy.leixingid = lx.id
GROUP BY lx.id,lx.leixingming
```

因此，实体类：

```java
@Data
public class LeiXingTongJi { //类型的id就不要了，丢弃
    private String name;
    private Integer value;
}
```

控制器：

```java
  @GetMapping("/leixingtongji")
    public Result tongjileixing(){
        Result r = new Result();
        try {
            List<LeiXingTongJi> tjs  = jdbc.query("select lx.id,lx.leixingming name,count(dy.id)  value  from dianyingxinxi dy inner join leixing lx  on dy.leixingid = lx.id  GROUP BY lx.id,lx.leixingming",new BeanPropertyRowMapper<>(LeiXingTongJi.class));
            r.setResult(tjs);
            r.setCode(200);
        } catch (DataAccessException e) {
            r.setResult("失败");
            r.setCode(201);
        }
        return r;
    }
//返回值 以json 格式描绘  刚好符合饼图要求
```

### 1.2前端

```html
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8" />
  <title>ECharts</title>
  <!-- 引入 ECharts 文件 -->
  <script src="js/echarts.min.js"> </script>    
  <script src="js/axios.min.js"> </script>
</head>

<body>
  <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
  <div id="main" style="width: 600px;height:400px;margin: 100px auto;"></div>
  <script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));
    // 指定图表的配置项和数据
    option = {
      title: {
        text: '各类电影占比',
        left: 'center',
        top: 'center'
      },
      legend:{
        show:true
      },
      dataset:{
        source: []  
      },
      series: [
        {
          type: 'pie',          
          radius: ['40%', '70%'],
          label:{
            show:true,
            formatter:'{b}:{d}%'    //https://echarts.apache.org/zh/option.html#series-pie.label.formatter
          }
        }
      ]
    };
    // 使用刚指定的配置项和数据显示图表。先设置 
    myChart.setOption(option);
    axios.get("http://localhost:8090/leixingtongji") //请求展示数据
    .then(function(response){
      if(response.data.code*1==200){
         let dataSourceoption={ //一个只有数据集的option 展示数据  就是这样
          dataset:{
            source:response.data.result
          }
         }  
         myChart.setOption(dataSourceoption)
      }
    })
  </script>
</body>

</html>
```

## 2.在大屏的现有模板，js中找到setOption,那么......

见：001

