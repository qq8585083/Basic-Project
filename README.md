Basic-Projectject是一个简易的Android基础项目，方便您快速进行开发。
包含以下内容：
- BaseActivity、BaseFragment
- Activity栈管理
- 异常信息收集
- 日志打印
- 丰富的工具类

通用适配器请参考: [CommonAdapter](https://github.com/qq8585083/CommonAdapter)

##图片展示
![image](https://raw.githubusercontent.com/qq8585083/Basic-Project/master/Quick basic project.gif)
##使用步骤
第一步：
```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/qq8585083/maven/'
    }
}
....
dependencies {
    compile 'com.hy.basicproject:basicproject:1.0.0'
}
```
第二步：
```java
public class YourApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        /**
         * 默认配置
         * 内部调用了: initDir() initLog(false) initExceptionHandler()三个方法
         */
        BasicConfig.getInstance(this).init();

//        or

        /**
         * 自定义配置
         * initDir() 初始化SDCard缓存目录
         * initLog() 初始化日志打印
         * initExceptionHandler() 初始化异常信息收集
         */
        BasicConfig.getInstance(this)
                   .initDir() // or initDir(rootDirName)
                   .initExceptionHandler()
                   .initLog(true); 

                    //其它初始化日志方法：
                    /**
                     * @param tag 日志标示
                     */
                    initLog(tag)
                            
                    /**
                     * @param tag 日志标示
                     * @param isDebug true:打印全部日志，false:不打印日志
                     */
                    initLog(tag, isDebug)
            
                    /**
                     * @param tag 日志标示，可以为空
                     * @param methodCount 显示方法行数，默认为：2
                     * @param isHideThreadInfo 是否显示线程信息，默认显示
                     * @param logTool 自定义日志打印，可以为空
                     * @param isDebug true:打印全部日志，false:不打印日志
                     */
                    initLog(tag, methodCount, isHideThreadInfo, logTool, isDebug)
    }
}
```

##代码示例

Activity示例
```java
public class TestActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    //初始化一些数据
    @Override public void initData() {
        super.initData();
        Intent intent = getIntent();
        params = intent.getStringExtra(...);
    }

    //初始化view
    @Override public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        /**
         * 使用Fragment
         * 参数1：被替换为Fragment的视图id
         * 参数2：BaseFragment对象
         */
        changeFragment(R.id.fragment_layout, new ImageFragment());
    }


    //以下为可选方法，根据需要进行重载.
    //方法执行顺序：
    //initPre() --> initData() --> initView(Bundle savedInstanceState) --> register()

    //这里可以注册一些广播、服务
    @Override public void register() { }
    //注销广播、服务,在onDestroy()内部执行
    @Override public void unRegister() { }
    //只有第一次才会执行，这里可以做一些界面功能引导
    @Override public void onFirst() { }
    //这个方法会在setContentView(...)方法之前执行
    @Override public void initPre() { }
    //view点击事件统一处理
    @Override public void viewClick(View v) { }
    @Override public void showProgress() { }
    @Override public void hideProgress() { }
}
```

Fragment示例
```java
public class TestFragment extends BaseFragment {
    private TextView mTitle;

    @Override public int getLayoutResId() {
        return R.layout.activity_listview_item;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        mTitle = (TextView) parentView.findViewById(R.id.item_title_tv);
    }

    //以下为可选方法，根据需要进行重载.
    //方法执行顺序：
    //initData() --> initView(View parentView, Bundle savedInstanceState) --> register()

    //这里可以注册一些广播、服务
    @Override public void register() { }
    //注销广播、服务, 在onDestroyView()内部执行
    @Override public void unRegister() { }
    //只有第一次才会执行，这里可以做一些界面功能引导
    @Override public void onFirst() { }
    @Override public void initData() { }
    @Override public void onChange() { }
    @Override public void onHidden() { }
    //view点击事件统一处理
    @Override public void viewClick(View v) { }
    @Override public void showProgress() { }
    @Override public void hideProgress() { }
}
```

启动页示例
```java
public class SplashActivity extends BaseSplashActivity {

    @Override protected void setSplashResources(List<SplashImgResource> resources) {
        /**
         * SplashImgResource参数:
         * mResId - 图片资源的ID。
         * playerTime - 图片资源的播放时间，单位为毫秒。。
         * startAlpha - 图片资源开始时的透明程度。0-255之间。
         * isExpand - 如果为true，则图片会被拉伸至全屏幕大小进行展示，否则按原大小展示。
         */
        resources.add(new SplashImgResource(R.mipmap.splash,1500,100f,true));
        resources.add(new SplashImgResource(R.mipmap.splash1,1500,100f,true));
        resources.add(new SplashImgResource(R.mipmap.splash2,1500,100f,true));
    }
    
    @Override protected boolean isAutoStartNextActivity() {
        return false;
    }
    @Override protected Class<?> nextActivity() {
        return null;
        //如果isAutoStartNextActivity设置为true,这里需要指定跳转的activity
        //return MainActivity.class;
    }
    
    @Override protected void runOnBackground() {
        //这里可以执行耗时操作、初始化工作
        //请注意：如果执行了耗时操作，那么启动页会等到耗时操作执行完才会进行跳转
        //try {
        //  Thread.sleep(15 * 1000);
        //} catch (InterruptedException e) {
        //  e.printStackTrace();
        //}
    }
}
```

打印日志
```java
Logger.d(content);
Logger.e(content);
Logger.w(content);
Logger.v(content);
Logger.wtf(content);
//打印json数据
Logger.json(jsonContent);
//打印xml数据
Logger.xml(xmlContent);
```

##工具类
* [AppInfoUtil - 应用程序相关信息](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/AppInfoUtil.java) <br/>
* [BitmapUtil - 图像处理](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/BitmapUtil.java) <br/>
* [CloseUtil - 实现Closeable对象关闭工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/CloseUtil.java)<br/>
* [ConversionUtil - 单位转换](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/ConversionUtil.java)<br/>
* [DataUtil - 数据非空判断工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/DataUtil.java)<br/>
* [DateUtil - 日期操作](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/DateUtil.java)<br/>
* [DeviceUtil - 设备信息](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/DeviceUtil.java)<br/>
* [DoubleClickExitHelper - 双击退出应用程序](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/DoubleClickExitHelper.java)<br/>
* [EditTextUtil - 文本输入框工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/EditTextUtil.java)<br/>
* [FileUtil - 文件操作](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/FileUtil.java)<br/>
* [HtmlUtil - HTML处理](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/HtmlUtil.java)<br/>
* [IntentUtil - 常用系统Intent](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/IntentUtil.java)<br/>
* [IpUtil - IP工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/IpUtil.java)<br/>
* [KeyBoardUtil - 键盘工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/KeyBoardUtil.java)<br/>
* [MatcherUtil - 正则表达式](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/MatcherUtil.java)<br/>
* [MoneyUtil - 高精度数据计算](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/MoneyUtil.java)<br/>
* [NetworkUtil - 网络状态](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/NetworkUtil.java)<br/>
* [PackageUtil - 包管理](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/PackageUtil.java)<br/>
* [ResourceUtil - 资源文件操作](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/ResourceUtil.java)<br/>
* [SDcardUtil - 存储卡工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/SDcardUtil.java)<br/>
* [SharedPreferencesUtil - 偏好参数存储工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/SharedPreferencesUtil.java)<br/>
* [StringUtil - 字符串处理工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/StringUtil.java)<br/>
* [ToastUtil - Toast工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/ToastUtil.java)<br/>
* [ViewHolder - View复用工具类](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/ViewHolder.java)<br/>
* [WifiHelper - wifi管理](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/WifiHelper.java)<br/>
* [WindowUtil - 屏幕管理](https://github.com/qq8585083/Basic-Project/blob/master/basicproject/src/main/java/com/hy/basicproject/utils/WindowUtil.java)<br/>

##感谢
[logger](https://github.com/orhanobut/logger)
