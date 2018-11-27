package com.jgchk.hotelhavoc.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientParser
@Inject constructor(private val gson: Gson) {

    fun parseIngredientString(ingredientString: String): Ingredient {
        if (Ingredient.ingredientMap.containsKey(ingredientString)) {
            return Ingredient.ingredientMap[ingredientString]!!.newInstance()
        } else {
            throw IllegalArgumentException("$ingredientString is not an ingredient")
        }
    }

    private val ingredientsListType = object : TypeToken<List<Ingredient>>() {}.type

    fun parseBeamedIngredientString(ingredientString: String): List<Ingredient> {
        return gson.fromJson<List<Ingredient>>(ingredientString, ingredientsListType)
    }
}