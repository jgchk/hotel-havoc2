package com.jgchk.hotelhavoc.model

import com.jgchk.hotelhavoc.model.actions.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionParser
@Inject constructor(chop: Chop, cook: Cook, blend: Blend, oven: Oven) {

    private val actionMap = listOf(
        chop,
        cook,
        blend,
        oven
    ).associateBy({ it.name }, { it })

    fun parseActionString(actionString: String): Action {
        if (actionMap.containsKey(actionString)) {
            return actionMap[actionString]!!
        } else {
            throw IllegalArgumentException("$actionString is not an action")
        }
    }
}