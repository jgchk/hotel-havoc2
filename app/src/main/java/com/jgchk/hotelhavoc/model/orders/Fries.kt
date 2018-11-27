package com.jgchk.hotelhavoc.model.orders

import com.jgchk.hotelhavoc.model.ingredients.fries.FriesIngredient
import com.jgchk.hotelhavoc.model.ingredients.fries.Potato

class Fries private constructor() : Order() {

    override val requiredIngredients = listOf(
        FriesIngredient(true)
    )
    override val score = 40

    companion object : HasRandomGenerator {
        override fun random(): Fries {
            return Fries()
        }

        val ingredients = setOf(
            Potato(true)
        )
    }

}