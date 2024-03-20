package com.gaia.public_pile.cluster

import com.amap.api.maps.model.LatLng

abstract class ClusterItem {

    abstract fun getPosition(): LatLng

}