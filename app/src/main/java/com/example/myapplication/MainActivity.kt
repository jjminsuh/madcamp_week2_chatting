package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI();
    }

    private fun initUI(){
        val button = findViewById<Button>(R.id.startButton)
        val name = findViewById<EditText>(R.id.testname)
        val num = findViewById<EditText>(R.id.testnum)

        button.setOnClickListener {
            val intent = Intent(this, ChattingActivity::class.java)
            intent.putExtra("username", name.text.toString())
            intent.putExtra("num", num.text.toString())
            this.startActivity(intent)
        }
    }
}