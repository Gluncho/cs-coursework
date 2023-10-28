package com.nglunchadze.memorygame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class MainActivity : AppCompatActivity() {
    private lateinit var cards:ArrayList<Int>
    private lateinit var cardFront:ArrayList<Int>
    private var imageViews:ArrayList<ImageView> = ArrayList()
    private lateinit var successView:TextView
    private lateinit var failView:TextView
    private lateinit var button: Button
    private val delayMillis:Long = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        successView = findViewById(R.id.success)
        failView = findViewById(R.id.fail)
        restartGame()
        for (i in 0..5){
            imageViews[i].setOnClickListener{imageListener(i)}
        }
        button = findViewById(R.id.restart)
        button.setOnClickListener {
            restartGame()
        }
    }

    private fun restartGame(){
        imageViews.clear()
        imageViews.add(findViewById<ImageView>(R.id.image_view1))
        imageViews.add(findViewById<ImageView>(R.id.image_view2))
        imageViews.add(findViewById<ImageView>(R.id.image_view3))
        imageViews.add(findViewById<ImageView>(R.id.image_view4))
        imageViews.add(findViewById<ImageView>(R.id.image_view5))
        imageViews.add(findViewById<ImageView>(R.id.image_view6))
        for (i in 0..5){
            imageViews[i].setImageResource(R.drawable.back)
            imageViews[i].visibility = View.VISIBLE
            imageViews[i].isClickable = true
        }
        successView.text = getString(R.string.success_label_name)
        failView.text = getString(R.string.fail_label_name)
        successView.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        failView.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        generateNewCards()
    }
    private fun generateNewCards() {
        cards = ArrayList<Int>(6).apply {
            repeat(6) {
                add(R.drawable.back)
            }
        }
        cardFront = listOf(R.drawable.apple, R.drawable.apple,
            R.drawable.banana, R.drawable.banana,
            R.drawable.cherry, R.drawable.cherry).shuffled() as ArrayList<Int>
    }
    private fun imageListener(position: Int){
        if(cards[position] != R.drawable.back) return
        val imageView = imageViews[position]
        imageView.setImageResource(cardFront[position])
        cards[position] = cardFront[position]
        for (i in 0..5){
            if(i == position) continue
            if(cards[i] != -1 && cards[i] != R.drawable.back){
                setClickable(false)
                Handler(Looper.getMainLooper()).postDelayed({
                    if(cards[i] == cards[position]){
                        handleCorrectGuess(i, position)
                    }else{
                        handleIncorrectGuess(i, position)
                    }
                    setClickable(true)
                }, delayMillis)
                return
            }
        }
        successView.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        failView.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
    }

    private fun setClickable(is_clickable: Boolean) {
        for(i in 0..5){
            imageViews[i].isClickable = is_clickable
        }
    }

    private fun handleIncorrectGuess(pos1: Int, pos2: Int) {
        imageViews[pos1].setImageResource(R.drawable.back)
        imageViews[pos2].setImageResource(R.drawable.back)
        cards[pos1] = R.drawable.back
        cards[pos2] = R.drawable.back
        failView.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
        incrementTextViewValue(failView)
    }

    private fun handleCorrectGuess(pos1: Int, pos2: Int) {
        imageViews[pos1].visibility = View.INVISIBLE
        imageViews[pos1].isClickable = false
        imageViews[pos2].visibility = View.INVISIBLE
        imageViews[pos2].isClickable = false
        cards[pos1] = -1
        cards[pos2] = -1
        successView.setTextColor(ResourcesCompat.getColor(resources, R.color.green, null))
        incrementTextViewValue(successView)
    }

    private fun incrementTextViewValue(view: TextView) {
        val pattern = Regex("(Success|Fail): (\\d+)")
        view.text = pattern.replace(view.text) {
            val value = it.groupValues[2].toInt() + 1
            "${it.groupValues[1]}: $value"
        }
    }
}