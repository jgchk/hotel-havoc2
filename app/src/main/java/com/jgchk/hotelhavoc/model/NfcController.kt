package com.jgchk.hotelhavoc.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NfcController
@Inject constructor(
    private val ingredientParser: IngredientParser,
    private val handController: HandController,
    private val actionParser: ActionParser,
    private val actionController: ActionController,
    private val orderController: OrderController
) {

    fun onNfcScan(tag: String) {

        val type = tag[0]
        val str = tag.substring(1)

        when (type) {
            'i' -> {
                val ingredient = ingredientParser.parseIngredientString(str)
                handController.pickUpIngredient(ingredient)
            }
            'b' -> {
                val ingredients = ingredientParser.parseBeamedIngredientString(str)
                handController.addIngredients(ingredients)
            }
            'a' -> {
                val action = actionParser.parseActionString(str)
                if (handController.canPerformAction(action)) {
                    actionController.startAction(action)
                }
            }
            'r' -> {
                if (orderController.turnIn(handController.hand, str.toInt())) {
                    handController.trash()
                }
            }
            else -> throw IllegalArgumentException("$type is not a valid tag type")
        }
    }

}