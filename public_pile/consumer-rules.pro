-dontwarn com.amap.api.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**

#地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.alibaba.idst.nui.* {*;}
-keep class com.google.**{*;}