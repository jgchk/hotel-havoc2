package com.jgchk.hotelhavoc.model.ingredients.fries

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Potato() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Potato.prepared = prepared
    }

    override val name = "potato"
    override var prepared = false
    override val action = "chop"
    override fun drawables() = mapOf(R.drawable.ic_potato to 300)
}