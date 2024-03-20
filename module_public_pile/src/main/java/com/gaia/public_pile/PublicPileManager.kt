package com.gaia.public_pile

import android.content.Context
import com.amap.api.maps.MapsInitializer

object PublicPileManager {

    fun authorizePrivacyAgreement(context: Context){
        MapsInitializer.updatePrivacyAgree(context, true)
        MapsInitializer.updatePrivacyShow(context, true, true)
    }
}