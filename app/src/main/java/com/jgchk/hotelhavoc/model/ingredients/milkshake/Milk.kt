package com.jgchk.hotelhavoc.model.ingredients.milkshake

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Milk() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Milk.prepared = prepared
    }

    override val name = "milk"
    override val action = "blend"
    override var prepared = false
    override fun drawables() = mapOf(R.drawable.ic_milk to 110)
}