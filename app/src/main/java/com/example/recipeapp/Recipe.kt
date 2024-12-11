package com.example.recipeapp

import android.util.Log
import java.sql.Time
import org.json.JSONArray
import org.json.JSONObject

class Recipe {
    private var recipeID : String = ""
    private var name : String = ""
    private var prepTime : Int = 0
    private var totalTime : Int = 0
    private var glutenFree : Boolean = false
    private var vegan : Boolean = false
    private var vegetarian : Boolean = false
    private var nutFree : Boolean = false
    private var dairyFree : Boolean = false
    private var ingredients : ArrayList<Ingredient> = ArrayList<Ingredient>()
    private var instructions : String = ""
    // some sort of review mechanism, star rating system

    constructor(name : String, prepTime : Int, totalTime : Int, ingredient : ArrayList<Ingredient>, instructions : String) {
        setName(name)
        setPrepTime(prepTime)
        setTotalTime(totalTime)
        addIngredients(ingredients)
        this.instructions = instructions
    }

    constructor() {

    }

    fun setID(id : String) {
        recipeID = id
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
        ingredients.add(newIngredient)

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

    fun setInstruction(newInstruction : String) : Recipe {
        if(newInstruction.isNullOrBlank()) {
            Log.w("MainActivity", "Invalid instruction")
        } else {
            instructions = newInstruction
        }
        return this
    }

//    fun addInstructions(newInstructions: ArrayList<String>) : Recipe {
//        for(instruction in newInstructions) {
//            if(!instruction.isNullOrBlank()) {
//                instructions.add(instruction)
//            }
//        }
//        return this
//    }

    fun makeGF() {
        glutenFree = true
    }

    fun makeVegan() {
        vegan = true
    }

    fun makeVegetarian() {
        vegetarian = true
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

    fun getInstructions() : String {
        return instructions
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        json.put("recipeID", recipeID)
        json.put("name", name)
        json.put("prepTime", prepTime)
        json.put("totalTime", totalTime)
        json.put("glutenFree", glutenFree)
        json.put("vegan", vegan)
        json.put("vegetarian", vegetarian)
        json.put("nutFree", nutFree)
        json.put("dairyFree", dairyFree)

        // Convert ingredients to JSON
        val ingredientsArray = JSONArray()
        for (ingredient in ingredients) {
            ingredientsArray.put(ingredient.toJSON())
        }
        json.put("ingredients", ingredientsArray)

        // Convert instructions to JSON

        json.put("instructions", instructions)

        return json
    }

    fun getTotalTime(): Int {
        return totalTime
    }

    fun getName() : String {
        return name
    }

}