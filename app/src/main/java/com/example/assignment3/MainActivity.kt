package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnNext : Button = findViewById(R.id.btnNext)
        val editTextName : EditText = findViewById(R.id.editTextName)
        val editTextLast : EditText = findViewById(R.id.editTextLast)

        btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java).apply {
                putExtra("name", editTextName.text.toString())
                putExtra("last", editTextLast.text.toString())
            }
            startActivity(intent)
        }

    }
}