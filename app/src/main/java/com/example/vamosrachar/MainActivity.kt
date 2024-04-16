package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var textToSpeech:TextToSpeech

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

        textToSpeech = TextToSpeech(this){status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "language is not supported", Toast.LENGTH_LONG).show()
                }
            }
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

        findViewById<ImageButton>(R.id.compartilhar).setOnClickListener{
            val v = String.format("R$ %.2f", valorFinal)
            val txt = "Sua parte da conta: $v"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, txt)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        findViewById<ImageButton>(R.id.ouvir).setOnClickListener{
            val v = String.format("R$ %.2f", valorFinal)
            val txt = "Sua parte da conta é $v"
            textToSpeech.speak(txt, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun calculate(value: Double, participants: Int): Double {
        if (value == 0.0 || participants == 0) {
            return 0.0
        }

        return value / participants
    }
}