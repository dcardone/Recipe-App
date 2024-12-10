package com.example.recipeapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var adView : AdView

    private lateinit var dishNameET : EditText

    private lateinit var ingredientsLayout : LinearLayout

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

    private lateinit var instructionsET : EditText

    private lateinit var addIngredientBtn: Button
    private lateinit var submitRecipeBtn: Button

    private lateinit var currRecipe: Recipe

    private lateinit var recipeManager: RecipeManager

    private lateinit var makeRecipeBtn : Button
    private lateinit var viewMyRecipesBtn : Button
    private lateinit var searchRecipesBtn : Button



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

        instructionsET = findViewById(R.id.etInstructions)

        addIngredientBtn = findViewById(R.id.btnAddIngredient)
        submitRecipeBtn = findViewById(R.id.btnSubmit)

        ingredientsLayout = findViewById(R.id.ingredientList)

        makeRecipeBtn = findViewById(R.id.btnMakeRecipe)
        viewMyRecipesBtn = findViewById(R.id.btnViewMyRecipes)
        searchRecipesBtn = findViewById(R.id.btnSearchRecipes)


        /** SET UP EVENT HANDLING **/
        addIngredientBtn.setOnClickListener{ addIngredient() }
        submitRecipeBtn.setOnClickListener{ submitRecipe() }
        makeRecipeBtn.setOnClickListener{ makeRecipeViewChange() }
        viewMyRecipesBtn.setOnClickListener{ viewMyRecipesViewChange() }
        searchRecipesBtn.setOnClickListener{ searchRecipesViewChange() }



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

            val textView = TextView(this).apply {
                text = name
                textSize = 16f
                setPadding(16, 16, 16, 16)
            }

            ingredientsLayout.addView(textView)

            ingredientET.setText("")
            amountET.setText("")
            unitET.setText("")
            Log.w("MainActivity", currRecipe.getIngredients().toString())

        }
    }

    fun submitRecipe() {
        var prepTime : String = prepTimeET.text.toString()
        var totalTime : String = totalTimeET.text.toString()
        currRecipe.addInstruction("Cook it")

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

            var prepTimeVal : Int = prepTime.toInt()
            var totalTimeVal : Int = totalTime.toInt()

            currRecipe.setPrepTime(prepTimeVal)
            currRecipe.setTotalTime(totalTimeVal)

            /**         ADD RECIPE TO FAVORITES                  **
             **         PUSH RECIPE TO FIREBASE                  **
             ** UPDATE SHARED PREFERENCES (user created recipes) **/



            var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
            val recipesRef = firebase.getReference("recipes")

            val recipeId = recipesRef.push().key // Generate unique key
            if (recipeId != null) {
                currRecipe.setID(recipeId)
                recipesRef.child(recipeId).setValue(currRecipe)
            }

            dishNameET.setText("")
            prepTimeET.setText("")
            totalTimeET.setText("")
            dairyFreeBox.isChecked = false
            nutFreeBox.isChecked = false
            veganBox.isChecked = false
            vegetarianBox.isChecked = false
            glutenFreeBox.isChecked = false
        }
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

    inner class DataListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.w( "MainActivity", "Inside onDataChange" )
            if( snapshot.value != null ) {
                Log.w( "MainActivity", "value read is: " + snapshot.value.toString( ) )
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w( "MainActivity", "error: " + error.toString() )
        }

    }

    fun makeRecipeViewChange() {
        setContentView(R.layout.activity_main)
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

        instructionsET = findViewById(R.id.etInstructions)

        addIngredientBtn = findViewById(R.id.btnAddIngredient)
        submitRecipeBtn = findViewById(R.id.btnSubmit)

        ingredientsLayout = findViewById(R.id.ingredientList)

        makeRecipeBtn = findViewById(R.id.btnMakeRecipe)
        viewMyRecipesBtn = findViewById(R.id.btnViewMyRecipes)
        searchRecipesBtn = findViewById(R.id.btnSearchRecipes)


        /** SET UP EVENT HANDLING **/
        addIngredientBtn.setOnClickListener{ addIngredient() }
        submitRecipeBtn.setOnClickListener{ submitRecipe() }
        makeRecipeBtn.setOnClickListener{ makeRecipeViewChange() }
        viewMyRecipesBtn.setOnClickListener{ viewMyRecipesViewChange() }
        searchRecipesBtn.setOnClickListener{ searchRecipesViewChange() }



        /** CREATE EMPTY RECIPE TO BE FILLED **/
        currRecipe = Recipe()
    }

    fun viewMyRecipesViewChange() {
        setContentView(R.layout.view_my_recipes)

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

        makeRecipeBtn = findViewById(R.id.btnMakeRecipe)
        viewMyRecipesBtn = findViewById(R.id.btnViewMyRecipes)
        searchRecipesBtn = findViewById(R.id.btnSearchRecipes)


        /** SET UP EVENT HANDLING **/
        makeRecipeBtn.setOnClickListener{ makeRecipeViewChange() }
        viewMyRecipesBtn.setOnClickListener{ viewMyRecipesViewChange() }
        searchRecipesBtn.setOnClickListener{ searchRecipesViewChange() }
    }

    fun searchRecipesViewChange() {

        setContentView(R.layout.search_recipes)

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

        makeRecipeBtn = findViewById(R.id.btnMakeRecipe)
        viewMyRecipesBtn = findViewById(R.id.btnViewMyRecipes)
        searchRecipesBtn = findViewById(R.id.btnSearchRecipes)


        /** SET UP EVENT HANDLING **/
        makeRecipeBtn.setOnClickListener{ makeRecipeViewChange() }
        viewMyRecipesBtn.setOnClickListener{ viewMyRecipesViewChange() }
        searchRecipesBtn.setOnClickListener{ searchRecipesViewChange() }
    }
}