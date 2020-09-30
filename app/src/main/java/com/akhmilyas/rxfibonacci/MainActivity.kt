package com.akhmilyas.rxfibonacci

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..30) {
            val txt1 = TextView(this)
            txt1.text = "$i"
            fib_output.addView(txt1)
        }
    }
}