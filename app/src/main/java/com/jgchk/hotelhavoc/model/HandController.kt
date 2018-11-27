package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import com.jgchk.hotelhavoc.model.actions.Action
import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import com.jgchk.hotelhavoc.model.ingredients.fries.FriesIngredient
import com.jgchk.hotelhavoc.model.ingredients.milkshake.MilkshakeIngredient
import com.jgchk.hotelhavoc.model.orders.Fries
import com.jgchk.hotelhavoc.model.orders.Milkshake
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandController
@Inject constructor() {

    val handData = MutableLiveData<List<Ingredient>>()

    var hand
        get() = handData.value!!
        private set(value) {
            handData.value = value
        }

    init {
        hand = emptyList()
    }

    fun pickUpIngredient(ingredient: Ingredient) {
        hand = hand.plus(ingredient)
    }

    fun addIngredients(ingredients: List<Ingredient>) {
        hand = hand.plus(ingredients)
    }

    fun trash() {
        hand = emptyList()
    }

    fun onBeamIngredient() {
        handData.postValue(emptyList())
    }

    fun canPerformAction(action: Action): Boolean {
        return hand.isNotEmpty() && hand.all { it.action == action.name }
    }

    fun doAction() {
        var preparedIngredients = hand.apply { forEach { it.prepare() } }

        if (Milkshake.ingredients == hand.toSet()) {
            preparedIngredients = listOf(MilkshakeIngredient(true))
        } else if (Fries.ingredients == hand.toSet()) {
            preparedIngredients = listOf(FriesIngredient(false))
        }

        hand = preparedIngredients
    }
}