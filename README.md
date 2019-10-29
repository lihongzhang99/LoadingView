# LoadingView
 
    主要是三个圆点的角度转换，三个圆点显示隐藏需要根据外环绘制进度来控制
     1 外环绘制stroke如果按view大小走，则会有一半stoke在外面，会显示不完全,处理方式：
      drawArc的Rect上下左右各缩进1/2的storkeWidth
     
     2 UI效果要求 圆环转动的时候，第一个point从显示到隐藏，第二个point在第一个point运行一段时间后，慢慢显示，再慢慢隐藏，
      第三个point在第二个point运行一段时间后也从隐藏到显示，再慢慢隐藏
      
      所有view尊从0～360度为运行周期
      
     3 宽度分成16等份，具体分析见图
      
       
    ![img](https://upload-images.jianshu.io/upload_images/7227181-36d6eda051251b57.gif) 
