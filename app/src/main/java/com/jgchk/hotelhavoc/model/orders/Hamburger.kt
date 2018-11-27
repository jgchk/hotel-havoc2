package com.jgchk.hotelhavoc.model.orders

import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Buns
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Lettuce
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Patty
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Tomato
import kotlin.random.Random

class Hamburger private constructor(override val score: Int, vararg required: Ingredient) : Order() {

    override val requiredIngredients = required.toList()

    companion object : HasRandomGenerator {

        private const val PLAIN_CHANCE = 0.3
        private const val LETTUCE_CHANCE = 0.3
        private const val TOMATO_CHANCE = 0.3

        private const val PLAIN_SCORE = 40
        private const val LETTUCE_SCORE = 50
        private const val TOMATO_SCORE = 50
        private const val FULLY_LOADED_SCORE = 60

        override fun random(): Hamburger {
            val randomDouble = Random.nextDouble()
            return when {
                randomDouble < PLAIN_CHANCE -> plain()
                randomDouble < PLAIN_CHANCE + LETTUCE_CHANCE -> lettuce()
                randomDouble < PLAIN_CHANCE + LETTUCE_CHANCE + TOMATO_CHANCE -> tomato()
                else -> fullyLoaded()
            }
        }

        fun plain() = Hamburger(
            PLAIN_SCORE,
            Buns(true),
            Patty(true)
        )

        fun lettuce() = Hamburger(
            LETTUCE_SCORE,
            Buns(true),
            Patty(true),
            Lettuce(true)
        )

        fun tomato() = Hamburger(
            TOMATO_SCORE,
            Buns(true),
            Patty(true),
            Tomato(true)
        )

        fun fullyLoaded() = Hamburger(
            FULLY_LOADED_SCORE,
            Buns(true),
            Patty(true),
            Lettuce(true),
            Tomato(true)
        )
    }
}