package com.pinyou.public_pile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.Marker
import com.pinyou.basic.log.LogUtils
import com.pinyou.common.base.BaseViewModel
import com.pinyou.common.base.ui.BaseFragment
import com.pinyou.db.AppDbManager
import com.pinyou.public_pile.cluster.Cluster
import com.pinyou.public_pile.cluster.ClusterItem
import com.pinyou.public_pile.cluster.ClusterOverlay
import com.pinyou.public_pile.databinding.PublicFragmentMapBinding
import com.pinyou.public_pile.databinding.PublicLayoutClusterMarkerBinding
import kotlinx.coroutines.launch
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class MapFragment : BaseFragment<PublicFragmentMapBinding, BaseViewModel>(), ClusterOverlay.ClusterRender,
    ClusterOverlay.OnMapCameraChangeListener, ClusterOverlay.ClusterClickListener, AMap.OnMapLoadedListener {
    private val TAG = "MapFragment"

    //默认缩放比例
    private val DEFAULT_ZOOM = 14

    //范围阶段，单位km，如果阶段范围内电站少于10个，则扩大范围搜索，最大阶段20km
    private val SCOPE_STAGE_1 = 10
    private val SCOPE_STAGE_2 = 15
    private val SCOPE_STAGE_3 = 20

    //扩大范围搜索条件，小于10个电站扩大范围搜索
    private val EXTENDED_SCOPE_CONDITIONS = 10

    //默认经纬度信息
    private val DEFAULT_LATITUDE = 39.91059256625662
    private val DEFAULT_LONGITUDE = 116.39710555031172

    private val savedInstanceState: Bundle? = null
    private lateinit var aMap: AMap
    private lateinit var clusterOverlay: ClusterOverlay

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): PublicFragmentMapBinding {
        return PublicFragmentMapBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {
        binding.mapview.onCreate(savedInstanceState)
        aMap = binding.mapview.map

        aMap.mapType = AMap.MAP_TYPE_NORMAL
        aMap.uiSettings.isZoomControlsEnabled = false
        aMap.uiSettings.isRotateGesturesEnabled = false
        aMap.uiSettings.isGestureScaleByMapCenter = true

        clusterOverlay = ClusterOverlay(aMap)
        clusterOverlay.setClusterRenderer(this)
        clusterOverlay.setOnMapCameraChangeListener(this)
        clusterOverlay.setOnClusterClickListener(this)

        aMap.setOnMapLoadedListener(this)
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onMapLoaded() {
        val visibleRegion = aMap.projection.visibleRegion
        val visibleDistance = AMapUtils.calculateLineDistance(visibleRegion.farLeft, visibleRegion.farRight).toDouble()
        LogUtils.d(TAG, "onMapCameraChange, visual range distance = $visibleDistance")
        val visibleBounds = aMap.projection.visibleRegion.latLngBounds
        val northeastLatLng = visibleBounds.northeast
        val southwestLatLng = visibleBounds.southwest
        var maxLatitude = max(northeastLatLng.latitude, southwestLatLng.latitude)
        var maxLongitude = max(northeastLatLng.longitude, southwestLatLng.longitude)
        var minLatitude = min(northeastLatLng.latitude, southwestLatLng.latitude)
        var minLongitude = min(northeastLatLng.longitude, southwestLatLng.longitude)
        //限定范围为中国
        if (minLatitude < 3) {
            minLatitude = 3.0
        }
        if (minLongitude < 72) {
            minLongitude = 72.0
        }
        if (maxLatitude > 54) {
            maxLatitude = 54.0
        }
        if (maxLongitude > 135) {
            maxLongitude = 135.0
        }
        LogUtils.d(TAG, "onMapCameraChange minLatLng $minLatitude,$minLongitude | maxLatLng $maxLatitude,$maxLongitude")
        val southwest = LatLng(minLatitude, minLongitude)
        val northeast = LatLng(maxLatitude, maxLongitude)
        val latLngBounds = LatLngBounds(southwest, northeast)
        val sqlStr =
            "SELECT * FROM CHARGING_STATION_MODEL WHERE " +
                    "LONGITUDE > $minLongitude AND " +
                    "LONGITUDE < $maxLongitude AND " +
                    "LATITUDE > $minLatitude AND " +
                    "LATITUDE < $maxLatitude"
        lifecycleScope.launch {
            val sqLiteQuery = SimpleSQLiteQuery(sqlStr)
            val stationDao = AppDbManager.instance.getStationDb().createStationDao()
            val stationEntities = stationDao.queryBySql(sqLiteQuery)
            LogUtils.i(TAG, "query station ${stationEntities.size}")
        }
    }

    /**
     * 初始化聚合点样式
     * */
    override fun getDrawableLayout(cluster: Cluster): View {
        val binding = PublicLayoutClusterMarkerBinding.inflate(LayoutInflater.from(context), null, false)
        binding.tvClusterNumber.text = cluster.getClusterCount().toString()
        return binding.root
    }

    /**
     * 地图移动状态监听
     * */
    override fun onMapCameraChange(touchStatus: ClusterOverlay.ClusterLoadType) {

    }

    /**
     * 聚合点击事件
     * */
    override fun onClusterClick(marker: Marker, clusterItems: List<ClusterItem>) {

    }


    /**
     * 计算搜索范围
     * 推荐搜索电站规则，不参与用户手动缩放搜索
     * 无法准确计算出比例尺
     */
    private fun calculateExtendedScope(latLng: LatLng, scope: Int): LatLngBounds {
        //可视区域中心坐标
        var scopeDistance = 0.0
        when (scope) {
            SCOPE_STAGE_1 -> {
                scopeDistance = SCOPE_STAGE_1.toDouble()
            }

            SCOPE_STAGE_2 -> {
                scopeDistance = SCOPE_STAGE_2.toDouble()
            }

            SCOPE_STAGE_3 -> {
                scopeDistance = SCOPE_STAGE_3.toDouble()
            }
        }
        scopeDistance /= 2
        //先计算查询点的经纬度范围
        val r = 6378.137 //地球半径千米  
        var dLng = 2 * asin(sin(scopeDistance / (2 * r)) / cos(latLng.latitude * Math.PI / 180))
        dLng = dLng * 180 / Math.PI //角度转为弧度  
        var dLat = scopeDistance / r
        dLat = dLat * 180 / Math.PI
        val minLat = latLng.latitude - dLat
        val maxLat = latLng.latitude + dLat
        val minLng = latLng.longitude - dLng
        val maxLng = latLng.longitude + dLng
        val southwest = LatLng(minLat, minLng)
        val northeast = LatLng(maxLat, maxLng)
        return LatLngBounds(southwest, northeast)
    }

    override fun onPause() {
        super.onPause()
        binding.mapview.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapview.onDestroy()
    }

}