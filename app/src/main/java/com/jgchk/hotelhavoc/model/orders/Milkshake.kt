package com.jgchk.hotelhavoc.model.orders

import com.jgchk.hotelhavoc.model.ingredients.milkshake.IceCream
import com.jgchk.hotelhavoc.model.ingredients.milkshake.Milk
import com.jgchk.hotelhavoc.model.ingredients.milkshake.MilkshakeIngredient

class Milkshake private constructor() : Order() {

    override val requiredIngredients = listOf(
        MilkshakeIngredient(true)
    )
    override val score = 30

    companion object : HasRandomGenerator {
        override fun random(): Milkshake {
            return Milkshake()
        }

        val ingredients = setOf(
            Milk(true),
            IceCream(true)
        )
    }

}