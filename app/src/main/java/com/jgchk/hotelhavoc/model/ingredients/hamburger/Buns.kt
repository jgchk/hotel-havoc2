package com.jgchk.hotelhavoc.model.ingredients.hamburger

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Buns() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Buns.prepared = prepared
    }

    override val name = "buns"
    override var prepared = true
    override val action = ""
    override fun drawables() = mapOf(R.drawable.ic_bun_bottom to 0, R.drawable.ic_bun_top to 99)
}