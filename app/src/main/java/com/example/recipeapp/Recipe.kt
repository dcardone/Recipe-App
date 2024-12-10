package com.example.recipeapp

import android.util.Log
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
    private var ingredients : ArrayList<Ingredient> = ArrayList<Ingredient>()
    private var instructions : ArrayList<String> = ArrayList<String>()
    // some sort of review mechanism, star rating system

    constructor(name : String, prepTime : Int, totalTime : Int, ingredient : ArrayList<Ingredient>, instructions : ArrayList<String>) {
        setName(name)
        setPrepTime(prepTime)
        setTotalTime(totalTime)
        addIngredients(ingredients)
        this.instructions = instructions
    }

    constructor() {

    }

    fun setName(name : String) {
        if (!name.isNullOrBlank())
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

    fun addIngredients(newIngredient : Ingredient) : Recipe {
        for(ingredient in ingredients) {
            if(newIngredient.getName() == ingredient.getName()) {
                Log.w("MainActivity", "Duplicate ingredient: " + newIngredient.getName())
                return this
            }
        }

        return this
    }

    fun addIngredients(newIngredients : ArrayList<Ingredient>) : Recipe {
        var shouldAdd : Boolean = true
        for (ingredient in newIngredients) {
            shouldAdd = true
            for (currIngredient in ingredients) {
                if(ingredient.getName() == currIngredient.getName()) {
                    Log.w("MainActivity", "Duplicate ingredient: " + ingredient.getName())
                    shouldAdd = false
                }

            }
            if(shouldAdd)
                ingredients.add(ingredient)
        }
        return this
    }

    fun addInstruction(newInstruction : String) : Recipe {
        if(newInstruction.isNullOrBlank()) {
            Log.w("MainActivity", "Invalid instruction")
        } else {
            instructions.add(newInstruction)
        }
        return this
    }

    fun addInstructions(newInstructions: ArrayList<String>) : Recipe {
        for(instruction in newInstructions) {
            if(!instruction.isNullOrBlank()) {
                instructions.add(instruction)
            }
        }
        return this
    }

    fun makeGF() {
        glutenFree = true
    }

    fun makeVegan() {
        vegan = true
    }

    fun makeVegetarian() {
        vegetarian = false
    }

    fun makeNF() {
        nutFree = true
    }

    fun makeDF() {
        dairyFree = true
    }

    fun getGF() : Boolean {
        return glutenFree
    }

    fun getVegan() : Boolean {
        return vegan
    }

    fun getVegetarian() : Boolean {
        return vegetarian
    }

    fun getNF() : Boolean {
        return nutFree
    }

    fun getDF() : Boolean {
        return dairyFree
    }

    fun getIngredients() : ArrayList<Ingredient> {
        return ingredients
    }

    fun getInstructions() : ArrayList<String> {
        return instructions
    }
}