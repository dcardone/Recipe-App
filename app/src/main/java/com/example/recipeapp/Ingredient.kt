package com.example.recipeapp

import org.json.JSONArray
import org.json.JSONObject

class Ingredient {
    private var name : String = ""
    private var amt : Float = 0.0f
    private var unit : String = ""

    constructor(name : String, amt : Float, unit : String) {
        setName(name)
        setAmt(amt)
        setUnit(unit)
    }

    fun setName(name : String) {
        if(name != null && name != " ") {
            this.name = name
        }
    }

    fun setAmt(amt : Float) {
        if(amt > 0) {
            this.amt = amt
        }
    }

    fun setUnit(unit : String) {
        if(unit != null && unit != " ") {
            this.unit = unit
        }
    }

    fun getName() : String {
        return name
    }

    fun getAmt() : Float {
        return amt
    }

    fun getUnit() : String {
        return unit
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("name", name)
        json.put("amt", amt)
        json.put("unit", unit)
        return json
    }
}