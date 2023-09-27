package com.gaia.basic.net

class NetWork private constructor() {

    companion object {
        val instance: NetWork by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWork()
        }
    }

}