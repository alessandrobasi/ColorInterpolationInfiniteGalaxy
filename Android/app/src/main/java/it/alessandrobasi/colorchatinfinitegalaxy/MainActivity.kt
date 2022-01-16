package it.alessandrobasi.colorchatinfinitegalaxy

import android.R.attr
import android.animation.ArgbEvaluator
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.graphics.Color
import android.util.Log
import android.widget.EditText
import androidx.annotation.ColorInt
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import androidx.core.content.ContextCompat.getSystemService
import java.security.AccessController.getContext
import android.R.attr.text

import android.R.attr.label

import android.content.ClipData
import androidx.core.graphics.toColorInt


var colore1 = Color.WHITE
var colore2 = Color.BLACK



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val color1 = findViewById<Button>(R.id.color1)
        val color2 = findViewById<Button>(R.id.color2)
        val inTesto = findViewById<EditText>(R.id.inTesto)
        val copyBtn = findViewById<Button>(R.id.copyBtn)

        color1.setOnClickListener {
            openColorPicker(color1,0, colore1)
        }

        color2.setOnClickListener {
            openColorPicker(color2,1, colore2)
        }

        copyBtn.setOnClickListener {
            copyTesto(inTesto.text.toString(), colore1, colore2)
        }

    }



    fun openColorPicker(btn: Button , x: Int, colore: Int) {

        //val cp = ColorPicker(this@MainActivity, Color.red(colore), Color.green(colore), Color.blue(colore))
        ColorPickerDialog
            .Builder(this)        				// Pass Activity Instance
            .setTitle("Pick Theme")           	// Default "Choose Color"
            .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
            .setDefaultColor(colore)     // Pass Default Color
            .setColorListener { color, colorHex ->
                // Handle Color Selection
                // Do whatever you want
                // Examples
                //Log.d("Alpha", Integer.toString(Color.alpha(color)))
                //Log.d("Red", Integer.toString(Color.red(color)))
                //Log.d("Green", Integer.toString(Color.green(color)))
                //Log.d("Blue", Integer.toString(Color.blue(color)))
                Log.d("Pure Hex", Integer.toHexString(color))
                Log.d("#Hex no alpha", String.format("%06X", 0xFFFFFF and color))
                Log.d("#Hex with alpha", String.format("#%08X", -0x1 and color))
                Log.d("colorHex", colorHex)
                Log.d("test",Integer.toHexString(0xFFFFFF and color))

                if(x == 0){
                    colore1 = color
                }
                else{
                    colore2 = color
                }

                btn.setBackgroundColor(color)
            }
            .show()



    }

    fun removeAlpha(x:Int):String{
        return String.format("%06X", 0xFFFFFF and x)
    }

    fun colorInterpolation(color1: Int, color2: Int, num: Float, q:Float):Int{
        val step = { _num:Float, _q:Float -> _q/_num }

        return ArgbEvaluator().evaluate(step(num,q), color1, color2) as Int

    }


    fun copyTesto(testo: String, color1: Int, color2: Int){
        var testoClipboard = ""
        val num = testo.length.toFloat()
        testo.forEachIndexed { index, c ->
            val colore = colorInterpolation(color1,color2, num,index+0.0f)

            testoClipboard += "["+removeAlpha(colore)+"]"+c+"[-]"
        }

        val clipboard: ClipboardManager? = getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("InfiniteGalaxy color command", testoClipboard)
        clipboard?.setPrimaryClip(clip)

    }

}