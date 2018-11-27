package com.jgchk.hotelhavoc.model.ingredients.hamburger

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Patty() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Patty.prepared = prepared
    }

    override val name = "patty"
    override val action = "cook"
    override var prepared = false
    override fun drawables(): Map<Int, Int> {
        return if (prepared) {
            mapOf(R.drawable.ic_patty_cooked to 10)
        } else {
            mapOf(R.drawable.ic_patty to 10)
        }
    }
}