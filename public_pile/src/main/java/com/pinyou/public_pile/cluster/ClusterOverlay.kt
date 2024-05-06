package com.pinyou.public_pile.cluster

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMarkerClickListener
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.AMapGestureListener
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.pinyou.basic.log.LogUtils
import com.pinyou.basic.utils.ScreenUtils

class ClusterOverlay(private val aMap: AMap) : OnMarkerClickListener {
    private val TAG = "ClusterOverlay"

    //聚合总数据
    private val totalClusterItems = LinkedHashSet<ClusterItem>()

    //移动地图聚合加载辅助
    private val translationClusterItems = LinkedHashSet<ClusterItem>()

    //缩放地图聚合加载辅助
    private val zoomClusters = LinkedHashSet<Cluster>()

    //已加载的聚合点
    private val alreadyLoadClusters = LinkedHashSet<Cluster>()

    //已加载的Marker
    private val alreadyLoadMarker = LinkedHashSet<Marker>()

    private var clusterSize = 0
    private var clusterClickListener: ClusterClickListener? = null
    private var clusterRender: ClusterRender? = null
    private var clusterDistance = 0.0
    private val markerHandlerThread = HandlerThread("addMarker")
    private val signClusterThread = HandlerThread("calculateCluster")
    private var markerHandler: Handler? = null
    private var signClusterHandler: Handler? = null
    private var pxInMeters = 0f
    private var currentZoom = 0f
    private var onMapCameraChangeListener: OnMapCameraChangeListener? = null
    private var currentLatLng: LatLng? = null
    private var isTouch = false

    enum class ClusterLoadType {
        DEFAULT, TRANSLATION, ZOOM, RECOMMEND_SCOPE_STAGE_1, RECOMMEND_SCOPE_STAGE_2, RECOMMEND_SCOPE_STAGE_3
    }

    companion object {
        const val ADD_CLUSTER_LIST = 0
        const val ADD_SINGLE_CLUSTER = 1
        const val UPDATE_SINGLE_CLUSTER = 2

        const val CALCULATE_CLUSTER = 10
        const val CALCULATE_SINGLE_CLUSTER = 11
    }

    init {
        clusterSize = ScreenUtils.dp2px(50f)
        pxInMeters = aMap.scalePerPixel
        clusterDistance = (pxInMeters * clusterSize).toDouble()
        aMap.setOnMarkerClickListener(this)
        initThreadHandler()
        //初始化默认地图缩放信息
        if (currentZoom == 0f) {
            currentZoom = aMap.cameraPosition.zoom
        }
        if (currentLatLng == null) {
            currentLatLng = aMap.cameraPosition.target
        }
        aMap.setAMapGestureListener(object : AMapGestureListener {
            override fun onDoubleTap(v: Float, v1: Float) {
                LogUtils.d(TAG, "AMapGestureListener map touch status onDoubleTap")
            }

            override fun onSingleTap(v: Float, v1: Float) {
                LogUtils.d(TAG, "AMapGestureListener map touch status onSingleTap")
            }

            override fun onFling(v: Float, v1: Float) {}
            override fun onScroll(v: Float, v1: Float) {}
            override fun onLongPress(v: Float, v1: Float) {
                LogUtils.d(TAG, "AMapGestureListener map touch status onLongPress")
            }

            override fun onDown(v: Float, v1: Float) {
                isTouch = true
                LogUtils.d(TAG, "AMapGestureListener map touch status onDown")
            }

            override fun onUp(v: Float, v1: Float) {
                LogUtils.d(TAG, "AMapGestureListener map touch status onUp")
            }

            override fun onMapStable() {
                LogUtils.d(TAG, "AMapGestureListener map touch status onMapStable")
                val cameraPosition = aMap.cameraPosition
                pxInMeters = aMap.scalePerPixel
                clusterDistance = (pxInMeters * clusterSize).toDouble()
                val lastZoom = currentZoom
                currentZoom = cameraPosition.zoom
                val lastLatLng = currentLatLng
                currentLatLng = cameraPosition.target
                var loadType = ClusterLoadType.DEFAULT
                if (lastZoom != 0f && lastZoom != currentZoom) {
                    //缩放地图
                    loadType = ClusterLoadType.ZOOM
                } else if (lastLatLng != null && (lastLatLng.latitude != currentLatLng?.latitude || lastLatLng.longitude != currentLatLng?.longitude)) {
                    //滑动地图
                    loadType = ClusterLoadType.TRANSLATION
                }
                LogUtils.i(TAG, "onCameraChangeFinish camera change, touchStatus = $loadType, is touch map $isTouch")
                if (onMapCameraChangeListener != null && isTouch) {
                    onMapCameraChangeListener!!.onMapCameraChange(loadType)
                }
                isTouch = false
            }
        })
    }

    //初始化Handler
    private fun initThreadHandler() {
        markerHandlerThread.start()
        signClusterThread.start()
        markerHandler = MarkerHandler(markerHandlerThread.looper)
        signClusterHandler = SignClusterHandler(signClusterThread.looper)
    }

    //点击事件
    override fun onMarkerClick(marker: Marker): Boolean {
        if (clusterClickListener == null) {
            return true
        }
        val cluster = marker.getObject() as Cluster?
        if (cluster != null) {
            clusterClickListener!!.onClusterClick(marker, cluster.getClusterItems())
            return true
        }
        return false
    }

    /**
     * 添加一个聚合点
     */
    fun addClusterItem(item: ClusterItem) {
        val message = Message.obtain()
        message.what = CALCULATE_SINGLE_CLUSTER
        message.obj = item
        signClusterHandler!!.sendMessage(message)
    }

    /**
     * 初始化聚合点，用于第一次加载聚合
     */
    fun initClusters(visibleBounds: LatLngBounds, data: List<ClusterItem>) {
        translationClusterItems.clear()
        alreadyLoadClusters.clear()
        totalClusterItems.clear()
        totalClusterItems.addAll(data)
        calculateClusters(visibleBounds, totalClusterItems, true)
    }

    /**
     * 更新聚合点，用于缩放地图加载聚合（缩放后需要根据比例尺重新加载聚合）
     */
    fun updateClustersAfterZoom(visibleBounds: LatLngBounds, data: List<ClusterItem>) {
        translationClusterItems.clear()
        alreadyLoadClusters.clear()
        totalClusterItems.addAll(data)
        calculateClusters(visibleBounds, totalClusterItems, true)
    }

    /**
     * 更新聚合点，用于移动地图加载聚合（去除重复数据避免多次加载）
     */
    fun updateClustersAfterTranslation(visibleBounds: LatLngBounds, data: List<ClusterItem>) {
        if (data.isEmpty()) {
            return
        }
        totalClusterItems.addAll(data)
        val clusterItems = LinkedHashSet(data)
        //求差集，去掉已经加载过的电站，避免多次添加造成的marker闪动
        if (translationClusterItems.size > 0) {
            clusterItems.removeAll(translationClusterItems)
        }
        calculateClusters(visibleBounds, clusterItems, false)
    }

    /**
     * 计算聚合数据
     */
    private fun calculateClusters(visibleBounds: LatLngBounds, data: LinkedHashSet<ClusterItem>, isClearMarker: Boolean) {
        val clusters = LinkedHashSet<Cluster>()
        val needToKeepClusters = LinkedHashSet<Cluster>()
        for (clusterItem in data) {
            val latlng = clusterItem.getPosition()
            if (visibleBounds.contains(latlng)) {
                var cluster = getCluster(latlng, clusters)
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem)
                } else {
                    cluster = Cluster(latlng)
                    clusters.add(cluster)
                    cluster.addClusterItem(clusterItem)
                }
                //缩放的时候查询当前显示范围内数量为1的聚合点，避免其重复加载引起Maker闪烁
                if (zoomClusters.contains(cluster)) {
                    if (cluster.getClusterCount() == 1) {
                        needToKeepClusters.add(cluster)
                    } else {
                        needToKeepClusters.remove(cluster)
                    }
                }
                translationClusterItems.add(clusterItem)
            }
        }
        //更新缩放地图聚合辅助数据
        zoomClusters.clear()
        zoomClusters.addAll(needToKeepClusters)
        //复制一份数据，规避同步
        val copyClusters = LinkedHashSet<Cluster>(clusters)
        addClusterToMap(copyClusters, needToKeepClusters, isClearMarker)
    }

    /**
     * 将聚合元素添加至地图上
     */
    private fun addClusterToMap(clusters: LinkedHashSet<Cluster>, needToKeepClusters: LinkedHashSet<Cluster>, isClearMarker: Boolean) {
        //移除已经存在marker
        if (isClearMarker) {
            val iterator = alreadyLoadMarker.iterator()
            while (iterator.hasNext()) {
                val marker = iterator.next()
                val cluster = marker.getObject() as Cluster
                if (!needToKeepClusters.contains(cluster)) {
                    marker.remove()
                    iterator.remove()
                }
            }
        }
        //添加新的marker
        for (cluster in clusters) {
            if (!needToKeepClusters.contains(cluster)) {
                addSingleClusterToMap(cluster)
            }
        }
    }

    /**
     * 将单个聚合元素添加至地图显示
     */
    private fun addSingleClusterToMap(cluster: Cluster) {
        val latlng = cluster.getCenterLatLng()
        val markerOptions = MarkerOptions()
        markerOptions.anchor(0.5f, 0.5f)
            .icon(BitmapDescriptorFactory.fromView(clusterRender!!.getDrawableLayout(cluster))).position(latlng)
        val marker = aMap.addMarker(markerOptions)
        marker.setObject(cluster)
        cluster.setMarker(marker)
        alreadyLoadClusters.add(cluster)
        if (cluster.getClusterCount() == 1) {
            zoomClusters.add(cluster)
        }
        alreadyLoadMarker.add(marker)
    }

    /**
     * 在已有的聚合基础上，对添加的单个元素进行聚合
     */
    private fun calculateSingleCluster(clusterItem: ClusterItem) {
        val visibleBounds = aMap.projection.visibleRegion.latLngBounds
        val latlng = clusterItem.getPosition()
        if (!visibleBounds.contains(latlng)) {
            return
        }
        var cluster = getCluster(latlng, alreadyLoadClusters)
        if (cluster != null) {
            cluster.addClusterItem(clusterItem)
            val message = Message.obtain()
            message.what = UPDATE_SINGLE_CLUSTER
            message.obj = cluster
            markerHandler!!.removeMessages(UPDATE_SINGLE_CLUSTER)
            markerHandler!!.sendMessageDelayed(message, 5)
        } else {
            cluster = Cluster(latlng)
            alreadyLoadClusters.add(cluster)
            cluster.addClusterItem(clusterItem)
            val message = Message.obtain()
            message.what = ADD_SINGLE_CLUSTER
            message.obj = cluster
            markerHandler!!.sendMessage(message)
        }
    }

    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     */
    private fun getCluster(latLng: LatLng, clusters: LinkedHashSet<Cluster>): Cluster? {
        for (cluster in clusters) {
            val clusterCenterPoint = cluster.getCenterLatLng()
            val distance = AMapUtils.calculateLineDistance(latLng, clusterCenterPoint).toDouble()
            val zoom = aMap.cameraPosition.zoom
            if (distance < clusterDistance && zoom < 13) {
                return cluster
            }
        }
        return null
    }

    /**
     * 更新已加入地图聚合点的样式
     */
    private fun updateCluster(cluster: Cluster) {
        val marker = cluster.getMarker()
        marker!!.setIcon(BitmapDescriptorFactory.fromView(clusterRender!!.getDrawableLayout(cluster)))
    }

    /**
     * 处理market添加，更新等操作
     */
    inner class MarkerHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                ADD_CLUSTER_LIST -> {}
                ADD_SINGLE_CLUSTER -> {
                    val cluster = message.obj as Cluster
                    addSingleClusterToMap(cluster)
                }

                UPDATE_SINGLE_CLUSTER -> {
                    val updateCluster = message.obj as Cluster
                    updateCluster(updateCluster)
                }
            }
        }
    }

    /**
     * 处理聚合点算法线程
     */
    inner class SignClusterHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                CALCULATE_CLUSTER -> {}
                CALCULATE_SINGLE_CLUSTER -> {
                    val item = message.obj as ClusterItem
                    totalClusterItems.add(item)
                    calculateSingleCluster(item)
                }
            }
        }
    }

    private val gestureListener: AMapGestureListener = object : AMapGestureListener {
        override fun onDoubleTap(v: Float, v1: Float) {}
        override fun onSingleTap(v: Float, v1: Float) {}
        override fun onFling(v: Float, v1: Float) {}
        override fun onScroll(v: Float, v1: Float) {}
        override fun onLongPress(v: Float, v1: Float) {}
        override fun onDown(v: Float, v1: Float) {}
        override fun onUp(v: Float, v1: Float) {}
        override fun onMapStable() {}
    }

    /**
     * 地图移动、缩放状态监听
     */
    interface OnMapCameraChangeListener {
        fun onMapCameraChange(touchStatus: ClusterLoadType)
    }

    fun setOnMapCameraChangeListener(onMapCameraChangeListener: OnMapCameraChangeListener) {
        this.onMapCameraChangeListener = onMapCameraChangeListener
    }

    /**
     * 聚合点的点击事件
     */
    interface ClusterClickListener {
        fun onClusterClick(marker: Marker, clusterItems: List<ClusterItem>)
    }

    fun setOnClusterClickListener(clusterClickListener: ClusterClickListener) {
        this.clusterClickListener = clusterClickListener
    }

    /**
     * 聚合渲染样式
     */
    interface ClusterRender {
        fun getDrawableLayout(cluster: Cluster): View
    }

    fun setClusterRenderer(render: ClusterRender) {
        clusterRender = render
    }

    fun destroy() {
        signClusterHandler?.removeCallbacksAndMessages(null)
        markerHandler?.removeCallbacksAndMessages(null)
        signClusterThread.quit()
        markerHandlerThread.quit()
        for (marker in alreadyLoadMarker) {
            marker.remove()
        }
        totalClusterItems.clear()
        translationClusterItems.clear()
        zoomClusters.clear()
        alreadyLoadClusters.clear()
        alreadyLoadMarker.clear()
    }

}