package com.jgchk.hotelhavoc.core.di

import com.jgchk.hotelhavoc.AndroidApplication
import com.jgchk.hotelhavoc.core.di.viewmodel.ViewModelModule
import com.jgchk.hotelhavoc.ui.game.GameFragment
import com.jgchk.hotelhavoc.ui.menu.MenuFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)

    fun inject(gameFragment: GameFragment)
    fun inject(menuFragment: MenuFragment)
}
