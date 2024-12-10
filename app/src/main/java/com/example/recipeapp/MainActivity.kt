package com.example.recipeapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() {
    private lateinit var adView : AdView

    private lateinit var dishNameET : EditText

    private lateinit var ingredientET : EditText
    private lateinit var amountET : EditText
    private lateinit var unitET : EditText

    private lateinit var dairyFreeBox : CheckBox
    private lateinit var nutFreeBox : CheckBox
    private lateinit var veganBox : CheckBox
    private lateinit var vegetarianBox : CheckBox
    private lateinit var glutenFreeBox : CheckBox

    private lateinit var prepTimeET : EditText
    private lateinit var totalTimeET : EditText

    private lateinit var addIngredientBtn: Button
    private lateinit var submitRecipeBtn: Button

    private lateinit var currRecipe: Recipe

    private lateinit var recipeManager: RecipeManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** AD CREATION **/
        adView = AdView(this)
        var adSize : AdSize = AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT)
        adView.setAdSize(adSize)
        var adUnitId : String = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId

        var builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("cooking").addKeyword("food").addKeyword("recipe").addKeyword("baking").addKeyword("kitchen")
        var request : AdRequest = builder.build()

        var adLayout : LinearLayout = findViewById(R.id.ad_view)
        adLayout.addView(adView)
        adView.loadAd(request)

        /** VIEW INSTANTIATION **/
        dishNameET = findViewById(R.id.etDishName)
        ingredientET = findViewById(R.id.tvIngredientName)
        amountET = findViewById(R.id.tvIngredientAmountTv)
        unitET = findViewById(R.id.unitTv)

        dairyFreeBox = findViewById(R.id.checkDairyFree)
        nutFreeBox = findViewById(R.id.checkNutFree)
        veganBox = findViewById(R.id.checkVegan)
        vegetarianBox = findViewById(R.id.checkVegetarian)
        glutenFreeBox = findViewById(R.id.checkGlutenFree)

        prepTimeET = findViewById(R.id.prepTimeTv)
        totalTimeET = findViewById(R.id.tvTotalTime)

        addIngredientBtn = findViewById(R.id.btnAddIngredient)
        submitRecipeBtn = findViewById(R.id.btnSubmit)

        /** SET UP EVENT HANDLING **/
        addIngredientBtn.setOnClickListener{ addIngredient() }
        submitRecipeBtn.setOnClickListener{ submitRecipe() }


        /** CREATE EMPTY RECIPE TO BE FILLED **/
        currRecipe = Recipe()



    }

    fun addIngredient() {
        var name : String = ingredientET.text.toString()
        var amount : String = amountET.text.toString()
        var unit : String = unitET.text.toString()
        if(name.isNullOrBlank()) {
            var toast : Toast = Toast.makeText( this, "Please enter a valid ingredient name", Toast.LENGTH_SHORT )
            toast.show()
        }
        else if (amount.isNullOrBlank()) {
            var toast : Toast = Toast.makeText( this, "Please enter a valid ingredient amount", Toast.LENGTH_SHORT )
            toast.show()
        }
        else if(unit.isNullOrBlank()) {
            var toast : Toast = Toast.makeText( this, "Please enter a valid unit name", Toast.LENGTH_SHORT )
            toast.show()
        } else {
            var amountVal : Float = amount.toFloat()
            var newIngredient = Ingredient(name, amountVal, unit)
            currRecipe.addIngredients(newIngredient)
        }
    }

    fun submitRecipe() {
        var prepTime : String = prepTimeET.text.toString()
        var totalTime : String = totalTimeET.text.toString()

        if (currRecipe.getIngredients().size <= 0) {
            var toast : Toast = Toast.makeText( this, "A recipe must have at least one ingredient", Toast.LENGTH_SHORT )
            toast.show()
        } else if (currRecipe.getInstructions().size <= 0) {
            var toast: Toast = Toast.makeText(
                this,
                "A recipe must have at least one instruction",
                Toast.LENGTH_SHORT
            )
            toast.show()
        } else if (prepTime.isNullOrBlank()) {
            var toast : Toast = Toast.makeText( this, "Please fill the prep time", Toast.LENGTH_SHORT )
            toast.show()
        } else if (totalTime.isNullOrBlank()) {
            var toast : Toast = Toast.makeText( this, "Please fill the total time", Toast.LENGTH_SHORT )
            toast.show()
        } else {
            if(dairyFreeBox.isChecked)
                currRecipe.makeDF()
            if(nutFreeBox.isChecked)
                currRecipe.makeNF()
            if(veganBox.isChecked)
                currRecipe.makeVegan()
            if(vegetarianBox.isChecked)
                currRecipe.makeVegetarian()
            if(glutenFreeBox.isChecked)
                currRecipe.makeGF()
        }

        var prepTimeVal : Int = prepTime.toInt()
        var totalTimeVal : Int = totalTime.toInt()

        currRecipe.setPrepTime(prepTimeVal)
        currRecipe.setTotalTime(totalTimeVal)

        /**         ADD RECIPE TO FAVORITES                  **
         **         PUSH RECIPE TO FIREBASE                  **
         ** UPDATE SHARED PREFERENCES (user created recipes) **/

    }


    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }
}