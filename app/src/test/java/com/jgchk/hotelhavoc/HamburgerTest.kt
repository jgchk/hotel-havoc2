package com.jgchk.hotelhavoc

import com.jgchk.hotelhavoc.model.ingredients.hamburger.Buns
import com.jgchk.hotelhavoc.model.ingredients.hamburger.Patty
import com.jgchk.hotelhavoc.model.orders.Hamburger
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HamburgerTest {
    @Test
    fun matchesIngredients_matches() {
        val burger = Hamburger.plain()
        val ingredients = listOf(
            Buns(true),
            Patty(true)
        )
        assertTrue(burger.matchesIngredients(ingredients))
    }

    @Test
    fun matchesIngredients_notPrepared_notMatches() {
        val burger = Hamburger.plain()
        val ingredients = listOf(
            Buns(true),
            Patty(false)
        )
        assertFalse(burger.matchesIngredients(ingredients))
    }

    @Test
    fun matchesIngredients_missingIngredients_notMatches() {
        val burger = Hamburger.plain()
        val ingredients = listOf(Buns(true))
        assertFalse(burger.matchesIngredients(ingredients))
    }
}
