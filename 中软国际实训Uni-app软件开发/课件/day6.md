# 学习和使用uni-app开发可以在手机端运行的应用客户端-3

## 1. 完成注册前后端接口调用

```js
submit() {
				if(this.user.mima != this.user.rmima){
					this.$u.toast("两次输入密码不同")
					return
				}
				uni.request({
				    url: 'http://localhost:8090/doRegister',  
				    data: this.user,
					method:"POST",
				    success: (res) => {//返回的结果（Result）对象 {"code":200,"reslut":...} 在res.data中
				       if(res.data.code*1 == 200){//成功登录 
					   ////1.https://uniapp.dcloud.net.cn/api/router.html#event-channel 
					     uni.navigateBack() //返回登录页面  						  
					   }else{
						   this.$u.toast('注册失败，请更换用户名尝试')  //提示框
					   }
				    }
				}); 	 
			}
```

## 2. 首页完成映射列表

### 2.1新建电影列表页面，打个样

```html
<template>
	<view class="u-wrap">
		<view class="u-demo-wrap">
			<view class="u-demo-title">热映大片</view>
			<view class="u-demo-area">
				<u-toast ref="uToast"></u-toast>
				<u-swiper @change="change" :height="250" :list="list" :title="title" :effect3d="effect3d"
					:indicator-pos="indicatorPos" :mode="mode" :interval="3000" @click="click"></u-swiper>
			</view>
		</view> 
        	<view>
        		<u-grid :col="3">
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film1.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film2.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film3.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film9.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film5.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film6.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film9.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film5.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film6.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film9.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film5.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        			<u-grid-item>
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							src="../../static/filmpics/film6.jpeg" @error="imageError"></image>
        					</view>
        					<view class="image-title">ok电影</view>
        				</view>
        			</u-grid-item>
        		</u-grid>
        	</view>     
		
	</view>
</template>

<script>
	export default {
		data() {
			return {

				//轮播图
				list: [{
						image: '../../static/filmpics/film7.jpeg',
						title: '昨夜星辰昨夜风，画楼西畔桂堂东'
					},
					{
						image: '../../static/filmpics/film8.jpeg',
						title: '身无彩凤双飞翼，心有灵犀一点通'
					},
					{
						image: '../../static/filmpics/film9.jpeg',
						title: '谁念西风独自凉，萧萧黄叶闭疏窗，沉思往事立残阳'
					}
				],
				title: true,
				mode: 'round',
				indicatorPos: 'bottomCenter',
				effect3d: false,
				//结束轮播图
			}
		},
		computed: {

		},
		methods: {

			//轮播图
			click(index) {
				this.$refs.uToast.show({
					title: `点击了第${index + 1}张图片`,
					type: 'success'
				})
			},
			change(index) {
				// console.log(index);
			}
			//结束轮播图

			,
			imageError() {

			}
		}
	}
</script>

<style scoped lang="scss">
	@import "@/common/demo.scss";  
</style>
```

注：新建common 目录，复制demo.scss  style导入demo的样式。filmpics目录的图片，拷贝到static

### 2.2 onLoad事件调用后端,加载数据

```html
<template>
	<view class="u-wrap">
		<view class="u-demo-wrap">
			<view class="u-demo-title">热映大片</view>
			<view class="u-demo-area">
				<u-toast ref="uToast"></u-toast>
				<u-swiper @change="change" :height="250" :list="list" :title="title" :effect3d="effect3d"
					:indicator-pos="indicatorPos" :mode="mode" :interval="3000" @click="click"></u-swiper>
			</view>
		</view>
       
        	<view>
        		<u-grid :col="3">
        			<u-grid-item v-for="(dy,index) in dianyings" :key="dy.id">
        				<view class="image-item">
        					<view class="image-content">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							:src="'../../static/filmpics/film'+(index%9+1)+'.jpeg'" @error="imageError"></image>
        					</view>
        					<view class="image-title">{{dy.ming}}</view>
        				</view>
        			</u-grid-item> 
        		</u-grid>
        	</view>    
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//轮播图
				list: [{
						image: '../../static/filmpics/film7.jpeg',
						title: '昨夜星辰昨夜风，画楼西畔桂堂东'
					},
					{
						image: '../../static/filmpics/film8.jpeg',
						title: '身无彩凤双飞翼，心有灵犀一点通'
					},
					{
						image: '../../static/filmpics/film9.jpeg',
						title: '谁念西风独自凉，萧萧黄叶闭疏窗，沉思往事立残阳'
					}
				],
				title: true,
				mode: 'round',
				indicatorPos: 'bottomCenter',
				effect3d: false,
				dianyings:[]
				//结束轮播图
			}
		},
		computed: {

		},
		methods: {

			//轮播图
			click(index) {
				this.$refs.uToast.show({
					title: `点击了第${index + 1}张图片`,
					type: 'success'
				})
			},
			change(index) {
				// console.log(index);
			}
			//结束轮播图 
			,
			imageError() {

			}
		} ,onLoad() {
	    	uni.request({
	    		url:"http://localhost:8090/dianyings",
				success:(res)=>{
					console.log(res.data)
					this.dianyings = res.data.result;
				}
	    	})
	    }
	}
</script>

<style scoped lang="scss">
	@import "@/common/demo.scss";  
</style>
```

## 3.修改后端

增加按Id修改用户信息，按id查询用户信息和按Id查询电影信息的功能

```java
  @GetMapping("/queryYongHuById")
    public Result queryYongHuById(Integer id){
        Result res = new Result();//多
        try {
            Yonghu yh = jdbc.queryForObject("select * from yonghu where id=?",new BeanPropertyRowMapper<>(Yonghu.class),id);
            res.setCode(200);
            res.setResult(yh);//用户数据放入结果中
            return res;
        } catch (DataAccessException e) {
            //e.printStackTrace();
            res.setCode(201);
            res.setResult("出现异常"+e.getMessage());//message 异常的信息
            return res;
        }
    }

@PostMapping("/updateyonghu")
    public Result updateYonghu(@RequestBody Yonghu yh){
        Result r = new Result();
        try {
            jdbc.update("UPDATE  yonghu  SET  xingming = ? ,zhanghao = ? , mima =? ,age = ?,leibie = ?  WHERE id = ?",yh.getXingming(),yh.getZhanghao(),yh.getMima(),yh.getAge(),yh.getLeibie(),yh.getId());
            r.setCode(200);
            r.setResult("更新成功");
        } catch (DataAccessException e) {
            r.setCode(201);
            r.setResult("更新失败");
        }
       return r;
    }
    @GetMapping("/dianyings/{id}")
    public Result queryDianYingById(@PathVariable Integer id){
       Result r = new Result();
        try {
             Dianyingxinxi  dy  = jdbc.queryForObject("SELECT * FROM dianyingxinxi where id=?",new BeanPropertyRowMapper<>(Dianyingxinxi.class),id);
             r.setResult(dy);
             r.setCode(200);
        } catch (DataAccessException e) {
            r.setResult("失败");
            r.setCode(201);
        }
        return r;
    }
```



## 4.点击一个电影，进入播放页面（页面传参）

### 4.1新建播放电影页面

见：https://uniapp.dcloud.net.cn/component/video.html

```html
<template>
	<view class="wrap">
		  <video  id="myVideo" src="https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/2minute-demo.mp4"
			  @error="videoErrorCallback" :danmu-list="danmuList" enable-danmu  controls></video>
	 </view>
</template>

<script>
	export default {
	    data() {
	        return {
	            src: '',
	            danmuList: [{
	                    text: '第 1s 出现的弹幕',
	                    color: '#ff0000',
	                    time: 1
	                },
	                {
	                    text: '第 3s 出现的弹幕',
	                    color: '#ff00ff',
	                    time: 3
	                }
	            ],
	            danmuValue: ''
	        }
	    },
	    onReady: function(res) {
	        
	    },
		onLoad(canshu) {
			//this.danmuList=[...this.danmuList] 
		},
	    methods: {  
            getRandomColor: function() {
				const rgb = []
				for (let i = 0; i < 3; ++i) {
					let color = Math.floor(Math.random() * 256).toString(16)
					color = color.length == 1 ? '0' + color : color
					rgb.push(color)
				}
				return '#' + rgb.join('')
			}
            ,
			 sendDanmu: function() {
			    this.videoContext.sendDanmu({
			       text: this.danmuValue,
			       color: this.getRandomColor()
			    }); 
			 }
	    }
	}
</script>

<style>
video{
	width: 100vw;
}
</style
```

### 4.2 主页面增加点击电影的事件，跳转到播放

```html
... 
<scroll-view scroll-y="true" >
        	<view>
        		<u-grid :col="3">
        			<u-grid-item v-for="(dy,index) in dianyings" :key="dy.id">
        				<view class="image-item">
        					<view class="image-content" @click="bofang(dy.id)">
        						<image style="width: 120px; height: 150px; background-color: #eeeeee;" mode="aspectFill"
        							:src="'../../static/filmpics/film'+(index%9+1)+'.jpeg'" @error="imageError"></image>
        					</view>
        					<view class="image-title">{{dy.ming}}</view>
        				</view>
        			</u-grid-item> 
        		</u-grid>
        	</view>        	
        </scroll-view>
...
methods: {//添加跳转到播放页面的 bofang,并传递参数电影id
          ......
			bofang(dyid){
			   uni.navigateTo({
			   	 url:"/pages/gehm/bofang?id="+dyid
			   })
			}
          ......
	 } 
```

### 4.3 修改电影播放页面

  onLoad中接受传入的参数，电影id,并根据电影id，从后端得到电影信息，变为弹幕，显示

```html
<template>
	<view class="wrap">
		<video id="myVideo" src="https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/2minute-demo.mp4"
			@error="videoErrorCallback" :danmu-list="danmuList" enable-danmu controls></video>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				src: '',
				danmuList: [{
                    text: '影片开始',
                    color: '#ff0000',
                    time: 1
                },
                {
                    text: '倒计时Go',
                    color: '#ff00ff',
                    time: 3
                },
				],
				danmuValue: ''
			}
		},
		onReady: function(res) {
            this.videoContext = uni.createVideoContext('myVideo')
		},
		onLoad(canshu) {
			// canshu.id   得到请求中的 bofang?id=1   id变量值
			let strUrl = `http://localhost:8090/dianyings/${canshu.id}`;
			let _this = this //延时发弹幕 不能访问this，定义个变量
			console.log("url",strUrl)
			uni.request({
				url: strUrl,
				success: (res) => { 
					if (res.data.code * 1 == 200) {
						let dy = res.data.result; //把电影对象 给一个变量，写代码 字母少 
						setInterval(function(){
							_this.danmuValue = "电影："+dy.ming
							_this.sendDanmu()
						},5000) //5秒发一个单秒
						setInterval(function(){
							_this.danmuValue = "导演："+dy.daoyan
							_this.sendDanmu()
						},6000) //6秒发一个单秒
						setInterval(function(){
							_this.danmuValue = "主演："+dy.zhuyan
							_this.sendDanmu()
						},7000)
						setInterval(function(){
							_this.danmuValue = "发布日："+dy.faburi
							_this.sendDanmu()
						},8000)
						setInterval(function(){
							_this.danmuValue = "票价："+dy.piaojia
							_this.sendDanmu()
						},9000)						
					}
				}
			}) 
		},
		methods: {
			getRandomColor: function() {
				const rgb = []
				for (let i = 0; i < 3; ++i) {
					let color = Math.floor(Math.random() * 256).toString(16)
					color = color.length == 1 ? '0' + color : color
					rgb.push(color)
				}
				return '#' + rgb.join('')
			},
			 sendDanmu: function() {
			    this.videoContext.sendDanmu({
			       text: this.danmuValue,
			       color: this.getRandomColor()
			    }); 
			 }
		}
	}
</script>

<style>
	video {
		width: 100vw; //视频窗口宽度全屏宽
	}
</style>
```

## 5.修改个人信息

### 5.1复制注册页面为修改信息页面

复制页面名：editmyinfo.vue ,增加隐藏的id输入框，按id修改，

```html
<template>
	<view class="wrap">
		<u-form :model="user" ref="uForm">
            <!--小技巧 增加一个隐藏的输入框 存id-->
			<u-input v-show="false"  v-model="user.id" type="text"></u-input>
			<u-form-item label-width="140" label-position="left" label="姓名" prop="xingming">  
				<u-input :border="border"  placeholder="请输入姓名" v-model="user.xingming" type="text"></u-input>
			</u-form-item>
			<u-form-item label-width="140" label-position="left" label="用户名" prop="zhanghao">
				<u-input :border="border" placeholder="请输入用户名" v-model="user.zhanghao" type="text"></u-input>
			</u-form-item>
			<u-form-item label-width="140" label-position="left" label="密码" prop="mima">
				<u-input :password-icon="true" :border="border" type="password" v-model="user.mima"
					placeholder="请输入密码"></u-input>
			</u-form-item>
			<u-form-item label-width="140" label-position="left" label="确认密码" prop="rmima">
				<u-input :password-icon="true" :border="border" type="password" v-model="user.rmima"
					placeholder="再输一次密码"></u-input>
			</u-form-item>
			<u-form-item label-width="140" label-position="left" label="年龄" prop="age">
				<u-input :border="border" placeholder="请输入年龄" v-model="user.age" type="text"></u-input>
			</u-form-item>
			<u-form-item label-width="140" label-position="left" label="用户类别" prop="leibie">
				<u-radio-group v-model="user.leibie" @change="radioGroupChange">
							<u-radio 
								@change="radioChange" 
								v-for="(item, index) in list" :key="index" 
								:name="item.name"
								:disabled="item.disabled"
							>
								{{item.txt}}
							</u-radio>
				 </u-radio-group>
			</u-form-item>
		</u-form>
		<u-button @click="submit">提交</u-button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				border: true,
				user: { },
				list:[{
					name: '1',
					txt:"超级用户",
					disabled: false
				},
				{
					name: '2',
					txt:"普通用户",
					disabled: false
				},
				{
					name: '3',
					txt:"电影推介",
					disabled: false
				}]
			}
		},
		methods: {
			submit() {
				if(this.user.mima != this.user.rmima){
					this.$u.toast("两次输入密码不同")
					return
				}
				uni.request({
				    url: 'http://localhost:8090/updateyonghu',  
				    data: this.user,
					method:"POST",
				    success: (res) => {//返回的结果（Result）对象 {"code":200,"reslut":...} 在res.data中
				       if(res.data.code*1 == 200){//成功 
					  
					     uni.navigateBack() //返回  						  
					   }else{
						   this.$u.toast('失败，请更换用户名尝试')  //提示框
					   }
				    }
				}); 	 
			},
			radioChange(e) { 
					console.log(e,"radio") 
			},
			radioGroupChange(e){
				console.log(e,"group")
			}
		}
	}
</script>

<style>

</style>
```



pages.json

```json
{
		    "path" : "pages/gehm/editmyinfo",
		    "style" :                                                                                    
		    {
		        "navigationBarTitleText": "设置个人信息",
		        "enablePullDownRefresh": false
		    }
		    
		}
```

### 5.2 个人信息页面点击跳转到修改，传参用户id

![image-20230701010349819](image-20230701010349819.png)

```html
<view class="u-flex user-box u-p-l-30 u-p-r-20 u-p-b-30">
				<view class="u-m-r-10">
					<u-avatar src="../../static/images/personal.png" size="140"></u-avatar>
				</view>
				<view class="u-flex-1">
					<view class="u-font-18 u-p-b-20">{{yonghu.xingming}}</view>
					<view class="u-font-14 u-tips-color">粉丝数：11</view>
				</view>
				<view class="u-m-l-10 u-p-10">
					<u-icon name="scan" color="#969799" size="28"></u-icon>
				</view>
				<view class="u-m-l-10 u-p-10" @click="editit(yonghu.id)">
					<u-icon name="arrow-right" color="#969799" size="28"></u-icon>
				</view>
			</view>
```

```js
methods: {
			......
			editit(yhid){
				uni.navigateTo({
					url:`/pages/gehm/editmyinfo?id=${yhid}`
				})
			}
		}
```

### 5.3 修改页面中，onLoad事件，得到传入的Id查询用户信息展示

```js
onLoad(canshu) { 
			uni.request({
				url:`http://localhost:8090/queryYongHuById?id=${canshu.id}`,
				success:(res) => {
					if(res.data.code*1==200){
						this.user = res.data.result
						this.user.rmima = this.user.mima
					}
				}
			})
		}
```

