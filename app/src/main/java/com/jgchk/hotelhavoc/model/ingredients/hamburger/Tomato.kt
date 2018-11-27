package com.jgchk.hotelhavoc.model.ingredients.hamburger

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Tomato() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Tomato.prepared = prepared
    }

    override val name = "tomato"
    override val action = "chop"
    override var prepared = false
    override fun drawables(): Map<Int, Int> {
        return if (prepared) {
            mapOf(R.drawable.ic_tomato_chopped to 30)
        } else {
            mapOf(R.drawable.ic_tomato to 30)
        }
    }
}