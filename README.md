

### VoiceLine，一个可以根据麦克风音量大小，显示一些波形效果的控件。如图，也有一些自定义属性，包括波形的颜色，灵敏度等。

![image](./line.gif)

引用方法：

```groovy
implementation 'com.github.nanyuweiyi:voiceline:1.0.0'
```
####自定义属性列表如下：
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="voiceView">
        <!--中间线的颜色，就是波形的时候，大家可以看到，中间有一条直线，就是那个-->
        <attr name="middleLine" format="color" />
        <!--中间线的高度，因为宽度是充满的-->
        <attr name="middleLineHeight" format="dimension" />
        <!--波动的线的颜色，如果是距形样式的话，刚是距形的颜色-->
        <attr name="voiceLine" format="color" />
        <!--波动线的横向移动速度，线的速度的反比，即这个值越小，线横向移动越快，越大线移动越慢，默认90-->
        <attr name="lineSpeed" format="integer" />
        <!--所输入音量的最大值-->
        <attr name="maxVolume" format="float" />
        <!--灵敏度，默认值是4-->
        <attr name="sensibility">
            <enum name="one" value="1" />
            <enum name="two" value="2" />
            <enum name="three" value="3" />
            <enum name="four" value="4" />
            <enum name="five" value="5" />
        </attr>
        <!--精细度，绘制曲线的时候，每几个像素绘制一次，默认是1，一般，这个值越小，曲线越顺滑，
            但在一些旧手机上，会出现帧率过低的情况，可以把这个值调大一点，在图片的顺滑度与帧率之间做一个取舍-->
        <attr name="fineness">
            <enum name="one" value="1" />
            <enum name="two" value="2" />
            <enum name="three" value="3" />
        </attr>
    </declare-styleable>
</resources>
```
实际使用过程中，可以这样配置：

```xml
    <com.carlos.voiceline.mylibrary.VoiceLineView
        android:id="@+id/voicLine"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        voiceView:maxVolume="200"
        voiceView:middleLine="@android:color/holo_red_light"
        voiceView:middleLineHeight="1dp"
        voiceView:fineness="three"
        voiceView:rectSpace="2dp"
        voiceView:sensibility="four"
        voiceView:voiceLine="@android:color/holo_red_light" /> 
```

