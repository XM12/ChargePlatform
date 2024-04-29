package com.pinyou.public_pile.cluster

import android.text.TextUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker

class Cluster(private val latLng: LatLng?) {
    private var id: String? = null
    private var clusterItems = mutableListOf<ClusterItem>()
    private var marker: Marker? = null

    init {
        if (latLng != null) {
            id = (latLng.latitude + latLng.longitude).toString() + ""
        }
    }

    override fun equals(other: Any?): Boolean {
        val cluster = other as Cluster?
        return if (cluster == null || TextUtils.isEmpty(id)) {
            false
        } else TextUtils.equals(id, other!!.id)
    }

    override fun hashCode(): Int {
        return if (!TextUtils.isEmpty(id)) id.hashCode() else 0
    }

    fun addClusterItem(clusterItem: ClusterItem) {
        clusterItems.add(clusterItem)
    }

    fun getClusterCount(): Int {
        return clusterItems.size
    }

    fun getCenterLatLng(): LatLng? {
        return latLng
    }

    fun setMarker(marker: Marker?) {
        this.marker = marker
    }

    fun getMarker(): Marker? {
        return marker
    }

    fun getClusterItems(): List<ClusterItem> {
        return clusterItems
    }

}