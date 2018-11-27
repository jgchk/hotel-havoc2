package com.jgchk.hotelhavoc.model.ingredients.milkshake

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class MilkshakeIngredient() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@MilkshakeIngredient.prepared = prepared
    }

    override val name = "milkshake"
    override val action = ""
    override var prepared = true
    override fun drawables() = mapOf(R.drawable.ic_milkshake to 100)
}