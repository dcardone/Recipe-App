package com.example.recipeapp

import java.sql.Time

class Recipe {
    private var name : String = ""
    private var prepTime : Int = 0
    private var totalTime : Int = 0
    private var glutenFree : Boolean = false
    private var vegan : Boolean = false
    private var vegetarian : Boolean = false
    private var nutFree : Boolean = false
    private var dairyFree : Boolean = false
    private var ingredients : Array<Ingredient> = emptyArray()
    private var instructions : Array<String> = emptyArray()
    // some sort of review mechanism, star rating system

    constructor(name : String, prepTime : Int, totalTime : Int, ingredient : Array<Ingredient>, instructions : Array<String>) {
        setName(name)
        setPrepTime(prepTime)
        setTotalTime(totalTime)
        this.ingredients = ingredients
    }

    fun setName(name : String) {
        if (name != null && name != "")
            this.name = name
    }

    fun setPrepTime(time : Int) {
        if (time > 0) {
            prepTime = time
        }
    }

    fun setTotalTime(time : Int) {
        if(time > 0 ) {
            totalTime = time
        }
    }

    fun addIngredients(ingredient : Ingredient) : Recipe{
        if(!(ingredient in ingredients)) {
            ingredients += ingredient
        }
        return this
    }
}