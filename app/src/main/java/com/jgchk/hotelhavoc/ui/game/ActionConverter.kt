package com.jgchk.hotelhavoc.ui.game

import com.jgchk.hotelhavoc.model.actions.Action

object ActionConverter {

    fun actionToActionView(action: Action?): ActionView {
        return ActionView(action?.drawable)
    }

}