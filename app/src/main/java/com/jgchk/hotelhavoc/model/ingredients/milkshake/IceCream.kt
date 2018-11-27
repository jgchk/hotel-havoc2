package com.jgchk.hotelhavoc.model.ingredients.milkshake

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class IceCream() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@IceCream.prepared = prepared
    }

    override val name = "ice cream"
    override val action = "blend"
    override var prepared = false
    override fun drawables() = mapOf(R.drawable.ic_ice_cream to 100)
}