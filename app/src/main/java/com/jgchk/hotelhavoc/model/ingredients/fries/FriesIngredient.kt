package com.jgchk.hotelhavoc.model.ingredients.fries

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class FriesIngredient() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@FriesIngredient.prepared = prepared
    }

    override val name = "fries"
    override var prepared = false
    override val action = "oven"
    override fun drawables(): Map<Int, Int> {
        return if (prepared)
            mapOf(R.drawable.ic_fries_cooked to 310)
        else
            mapOf(R.drawable.ic_fries to 310)
    }
}