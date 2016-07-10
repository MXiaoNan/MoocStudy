# AndroidManifest.xml配置文件介绍

* 本质：AndroidManifest.xml是整个应用的主配置清单文件
* 包含：该应用的包名，版本号，组件，权限等信息
* 作用：记录该应用的相关的配置信息

创建完工程后，AndroidManifest.xml被自动创建

## AndroidManifest.xml常用标签解读
* （1）全局篇（包名，版本信息）
* （2）组件篇（四大组件）
* （3）权限篇（申请权限和定义权限）

### 一：全局篇

* （1）应用的包名以及版本信息的管理

		<?xml version="1.0" encoding="utf-8"?>
		<manifest 
			xmlns:android="http://schemas.android.com/apk/res/android"
		    package="com.xn.test">

* （2）控制android版本信息（可以支持的最低版本、你期望的系统版本）（在Android studio中，版本信息是在每个app下的**build.gradle**中设置的）

		defaultConfig {
			applicationId "com.xn.test"
			minSdkVersion 15
			targetSdkVersion 23
			versionCode 1
			versionName "1.0"
		}
<br>

### 二：组件篇

		<application
	        android:allowBackup="true"
	        android:icon="@mipmap/ic_launcher"
	        android:label="@string/app_name"
	        android:supportsRtl="true"
	        android:theme="@style/AppTheme">
	        <activity android:name=".MainActivity">
	            <intent-filter>
	                <action android:name="android.intent.action.MAIN" />
	                <category android:name="android.intent.category.LAUNCHER" />
	            </intent-filter>
	        </activity>
    	</application>

其属性可以设置：

	图标：android:icon
	标题：android:label
	主题样式：android:theme

**注意：** 一个清单文件里面只能包含一个**application**节点

#### 1、Activity（活动）

		<activity android:name=".MainActivity">
			<intent-filter>
		    	<action android:name="android.intent.action.MAIN" />
		    	<category android:name="android.intent.category.LAUNCHER" />
			</intent-filter>
		</activity>

&nbsp;&nbsp;&nbsp;&nbsp;每个活动必须要有**activity**标签，如果启动一个没有在清单中定义的Activity会抛出运行时异常。

每个activity中可以使用**<intent-filter>**子标签决定启动哪个活动。还可以进行活动间的通信。

#### 2、Service（服务）

		<service android:name="com.xn.test.TestService">
            <intent-filter>
                <action android:name="com.xn.myservice">

                </action>
            </intent-filter>
        </service>

每个使用Service的都必须在清单中进行注册。

**与Activity的区别：Activity用来显示界面，而Service负责后台代码逻辑的处理**

#### 3、Content Provider（内容提供者）

		<provider
            android:authorities="com.xn.test.MainActivity"
            android:name="com.xn.test.TestProvider">
        </provider>

**作用：主要用来管理数据库的访问以及程序内和程序间的数据的共享**

#### 4、Broadcast Receiver（广播接收者）

		<receiver android:name="com.xn.test.TestReceiver">
		    <intent-filter>
		    	<action android:name="test.app.receiver"/>
		    </intent-filter>
		</receiver>

可以理解为**全局事件监听器**


### 三：权限篇

Android studio添加方法：

* 直接打开AndroidManifest.xml文件，在mainfest标签以及application标签之间进行添加。

		<uses-permission android:name="android.permission.INTERNET"/>
		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

Eclipse添加方法：

* 先打开AndroidManifest.xml文件，然后在屏幕下方点击Permissions选项卡，点击Add添加，选择Uses Persission，在右侧下拉列表中选择相应的权限。

##### （1）使用系统权限

	 <uses-permission>申请权限

	声明了哪些是由你定义的权限，而这些权限是应用程序正常执行所必需的。在安装程序的时候，你设定的所有权限将会告诉给用户，由他们来决定同意与否。

	对很多本地Android服务来说，权限都是必需的，特别是那些需要付费或者有安全问题的服务（例如：拨号、接收SMS或者使用基于位置的服务）

##### （2）自定义权限

		<activity android:name=".MainActivity"
            android:permission="xn.test.per">

在activity标签中添加permission属性后，在mainfest标签以及application标签之间添加如下属性：

		<permission 
	        android:name="xn.test.PER"
	        android:protectionLevel="normal">
    	</permission>

如果有程序想访问添加过自定义权限的程序时，需要在配置清单中添加使用权限<uses-permission>为自定义的权限，否则会出现权限拒绝。

自定义权限的使用，是为了保护应用的某一个组件，不被轻易的被其它组件所访问。

#注意事项：

1. 每个组件必须包含android:name这个属性，推荐用全名称（包名.类名），intent-filter（过滤器）可以选写
2. 四大组件中除了BroadcasrReceiver可以使用代码声明注册之外，其它组件必须要在Manifest文件中进行声明配置，否则会报错
3. 权限的名字不用全部记忆，只需要记住个别单词，进行代码提示就可以了