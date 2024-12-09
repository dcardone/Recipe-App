package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences

class RecipeManager {
    private lateinit var recipes : ArrayList<Recipe>
    private lateinit var favorites : ArrayList<Recipe>

    constructor(c : Context) {
        var pref : SharedPreferences = c.getSharedPreferences(c.packageName + "_preferences", Context.MODE_PRIVATE)

    }
}