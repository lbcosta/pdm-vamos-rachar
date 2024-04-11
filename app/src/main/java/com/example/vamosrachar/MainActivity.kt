package com.example.vamosrachar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var valorDaConta = 0.0
    private var pessoas = 0
    private var valorFinal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<EditText>(R.id.valorDaConta).addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("NotImplemented", "beforeTextChanged: ")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty()) {
                        valorDaConta = 0.0
                        return
                    }
                }

                val str = s.toString()
                val value = str.toDouble()
                valorDaConta = value
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) {
                    return
                }

                valorFinal = if (s.isEmpty()) 0.0 else calculate(valorDaConta, pessoas)
                val valorFinalStr = String.format("R$ %.2f", valorFinal)
                findViewById<TextView>(R.id.valorFinal).text = valorFinalStr
            }
        })

        findViewById<EditText>(R.id.pessoas).addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("NotImplemented", "beforeTextChanged: ")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty()) {
                        pessoas = 0
                        return
                    }
                }

                val str = s.toString()
                val value = str.toInt()
                pessoas = value
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) {
                    return
                }

                valorFinal = if (s.isEmpty()) 0.0 else calculate(valorDaConta, pessoas)
                val valorFinalStr = String.format("R$ %.2f", valorFinal)
                findViewById<TextView>(R.id.valorFinal).text = valorFinalStr
            }
        })
    }

    fun calculate(value: Double, participants: Int): Double {
        if (value == 0.0 || participants == 0) {
            return 0.0
        }

        return value / participants
    }
}