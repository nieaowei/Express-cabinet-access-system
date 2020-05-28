package com.naw.ecas;

import android.app.Application
import com.naw.ecas.data.AppContainer
import com.naw.ecas.data.AppContainerImpl

class ECASApplication :Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
