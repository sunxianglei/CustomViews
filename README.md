# CustomViews

这个库包括了各种自定义View，目前有直方图、饼图、悬浮按钮。出现什么bug都会及时更新并且如果有新增的自定义View都会加入此库中。

### 直方图

![直方图](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E7%9B%B4%E6%96%B9%E5%9B%BE.gif)

直方图使用方法：

```
    //在xml布局文件中定义Histogram
    <com.xianglei.customviews.view.Histogram
        android:id="@+id/histogram"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:padding="30dp"
        app:histogram_textSize="12sp"
        app:histogram_textColor="@color/gray"
        app:histogram_scaleTextSize="10sp"
        app:histogram_scaleTextColor="@color/blue">
    </com.xianglei.customviews.view.Histogram>
    
    //在java代码中传入名称、颜色、数值
    public static final String[] HISTOGRAM_NAMES = {"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
    public static final int[] HISTOGRAM_COLORS = {Color.BLUE, Color.GREEN, Color.RED,
            Color.BLACK, Color.GREEN, Color.BLUE, Color.RED};
    public static final int[] HISTOGRAM_VALUES = {20 ,30, 100, 60, 80, 230, 50};
    mHistogram.setData(HISTOGRAM_NAMES, HISTOGRAM_COLORS, HISTOGRAM_VALUES);
```

直方图支持自定义文字大小、颜色，刻度文字大小、颜色等自定义属性：

```
    <declare-styleable name="Histogram">
        <attr name="histogram_textSize" format="dimension"></attr>
        <attr name="histogram_textColor" format="color"></attr>
        <attr name="histogram_scaleTextSize" format="dimension"></attr>
        <attr name="histogram_scaleTextColor" format="color"></attr>
    </declare-styleable>
```

动态支持隐藏Y轴刻度和矩形上方的值：

```
    mHistogram.hideValue(true);//隐藏矩形上方的值
    mHistogram.hideScale(true);//隐藏Y轴刻度
```



### 饼图

![饼图1](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E9%A5%BC%E5%9B%BE1.png)

![饼图2](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E9%A5%BC%E5%9B%BE2.png)

饼图使用方法：

```
    //xml布局文件定义PieChart
    <com.xianglei.customviews.view.PieChart
        android:id="@+id/pie_chart"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:background="@color/deep_gray"
        app:pie_textColor="@color/white"
        app:pie_lineColor="@color/white">
    </com.xianglei.customviews.view.PieChart>
    
    //java代码里传入名称、颜色、数值
    public static final String[] PIE_NAMES = {"Lollopop", "KitKat", "Jelly", "SandWich", "Ginger","FroYo", "Marsh"};
    public static final int[] PIE_COLORS = {Color.parseColor("#0a77f4"), Color.parseColor("#000000"), Color.parseColor("#c91313"),Color.parseColor("#206b05"), Color.parseColor("#0a77f4"), Color.parseColor("#ffd000"), Color.parseColor("#206b05")};
    public static final int[] PIE_VALUES = {20 ,25, 40, 40, 15, 35, 10};
    mPieChart.setData(PIE_NAMES, PIE_COLORS, PIE_VALUES);
```

饼图支持自定义文字大小、颜色，延伸线与半径的比例、延伸线颜色等自定义属性：

```
    <declare-styleable name="PieChart">
        <attr name="pie_textSize" format="dimension"></attr>
        <attr name="pie_textColor" format="color"></attr>
        <attr name="pie_lineRatio" format="float"></attr>
        <attr name="pie_lineColor" format="color"></attr>
    </declare-styleable>
```

动态支持两种模式，第一种是将文字显示在内部，第二种是将文字显示在外部，默认是第一种：

```
mPieChart.setMode(PieChart.INSIDE_MODE);
```



### 悬浮可拖拽按钮

![悬浮按钮](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E6%82%AC%E6%B5%AE%E6%8C%89%E9%92%AE.gif)

悬浮可拖拽按钮使用方法较简单，直接在布局中加入以下代码即可：

```
    <com.xianglei.customviews.view.FloatView
        android:id="@+id/view_float"
        android:layout_height="50dp"
        android:layout_width="50dp">
    </com.xianglei.customviews.view.FloatView>
```



### 机械表

![机械表](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E6%9C%BA%E6%A2%B0%E8%A1%A8.gif)

机械表使用方法：

~~~
    //xml布局文件
    <com.xianglei.customviews.view.MyClockView
        android:id="@+id/custom_clock"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:clock_hour_pointer_color="@color/blue"
        app:clock_minute_pointer_out_length="20dp"
        app:clock_text_size="15sp"
        app:clock_scale_length="9dp"
        app:clock_center_circle_radius="10dp"/>
~~~

机械表支持一下自定义属性：

```
    <declare-styleable name="MyClockView">
        <!--中心圆半径-->
        <attr name="clock_center_circle_radius" format="dimension"></attr>
        <!--时间数字字体大小-->
        <attr name="clock_text_size" format="dimension"/>
        <!--时针宽度-->
        <attr name="clock_hour_pointer_width" format="dimension"/>
        <!--分针宽度-->
        <attr name="clock_minute_pointer_width" format="dimension"/>
        <!--秒针宽度-->
        <attr name="clock_second_pointer_width" format="dimension"/>
        <!--指针圆角半径-->
        <attr name="clock_pointer_corner_radius" format="dimension"/>
        <!--时针超出中心点宽度-->
        <attr name="clock_hour_pointer_out_length" format="dimension"/>
        <!--分针超出中心点宽度-->
        <attr name="clock_minute_pointer_out_length" format="dimension"/>
        <!--秒针超出中心点宽度-->
        <attr name="clock_second_pointer_out_length" format="dimension"/>
        <!--短刻度长度(长刻度是短刻度的1.5倍)-->
        <attr name="clock_scale_length" format="dimension"></attr>
        <!--长刻度颜色-->
        <attr name="clock_scale_long_color" format="color"/>
        <!--短刻度颜色-->
        <attr name="clock_scale_short_color" format="color"/>
        <!--时针颜色-->
        <attr name="clock_hour_pointer_color" format="color"/>
        <!--分针颜色-->
        <attr name="clock_minute_pointer_color" format="color"/>
        <!--秒针颜色-->
        <attr name="clock_second_pointer_color" format="color"/>
    </declare-styleable>

```



### 加载框

![加载框](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E5%8A%A0%E8%BD%BD%E6%A1%86.gif)

加载框使用方法（可以定义圆圈颜色和文字颜色）：

```
<com.xianglei.customviews.view.LoadingView
    android:id="@+id/view_loading"
    android:layout_height="70dp"
    android:layout_width="70dp"
    app:loading_circle_color="@color/green"
    app:loading_text_color="@color/blue"/>
```



### 滑动卷尺

![滑动卷尺](https://github.com/sunxianglei/ImageLibrary/blob/master/images/%E6%BB%91%E5%8A%A8%E5%8D%B7%E5%B0%BA.gif)

滑动卷尺使用方法：

```
<com.xianglei.customviews.view.RuleView
    android:id="@+id/view_rule"
    android:layout_width="match_parent"
    android:layout_height="130dp"
    android:layout_marginTop="30dp"
    android:background="@color/light_gray"
    app:rule_middle_line_color="@color/blue"
    app:rule_min_unit_value="0.1"
    app:rule_max_value="100"
    app:rule_default_value="30"/>
```

滑动卷尺自定义属性：

```
<declare-styleable name="RuleView">
    <attr name="rule_middle_line_color" format="color"></attr>
    <attr name="rule_scale_text_color" format="color"></attr>
    <attr name="rule_scale_line_color" format="color"></attr>
    <attr name="rule_scale_text_size" format="dimension"></attr>
    <attr name="rule_min_unit_value" format="float"></attr>
    <attr name="rule_max_value" format="float"></attr>
    <attr name="rule_max_show_value" format="float"></attr>
    <attr name="rule_default_value" format="float"></attr>
</declare-styleable>
```



###未完待续。。。

