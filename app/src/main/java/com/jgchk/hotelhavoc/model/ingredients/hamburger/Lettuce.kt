package com.jgchk.hotelhavoc.model.ingredients.hamburger

import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.model.ingredients.Ingredient

class Lettuce() : Ingredient() {

    constructor(prepared: Boolean) : this() {
        this@Lettuce.prepared = prepared
    }

    override val name = "lettuce"
    override val action = "chop"
    override var prepared = false
    override fun drawables(): Map<Int, Int> {
        return if (prepared) {
            mapOf(R.drawable.ic_lettuce_chopped to 20)
        } else {
            mapOf(R.drawable.ic_lettuce to 20)
        }
    }
}