package com.jgchk.hotelhavoc

import android.app.Application
import com.jgchk.hotelhavoc.core.di.ApplicationComponent
import com.jgchk.hotelhavoc.core.di.ApplicationModule
import com.jgchk.hotelhavoc.core.di.DaggerApplicationComponent

class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
    }

    private fun injectMembers() = appComponent.inject(this)
}