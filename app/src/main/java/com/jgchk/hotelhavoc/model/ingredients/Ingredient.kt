package com.jgchk.hotelhavoc.model.ingredients

import com.jgchk.hotelhavoc.model.ingredients.fries.Potato
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Buns
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Lettuce
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Patty
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Tomato
import com.jgchk.hotelhavoc.model.ingredients.milkshake.IceCream
import com.jgchk.hotelhavoc.model.ingredients.milkshake.Milk

abstract class Ingredient {

    companion object {
        val ingredientMap = listOf(
            Buns(),
            Lettuce(),
            Patty(),
            Tomato(),
            IceCream(),
            Milk(),
            Potato()
        ).associateBy({ it.name }, { it.javaClass })
    }

    abstract var prepared: Boolean
    abstract val name: String
    fun prepare() {
        prepared = true
    }

    abstract val action: String
    abstract fun drawables(): Map<Int, Int>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ingredient

        if (name != other.name) return false
        if (prepared != other.prepared) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + prepared.hashCode()
        return result
    }
}