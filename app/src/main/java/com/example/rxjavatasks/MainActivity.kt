package com.example.rxjavatasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavatasks.tasks1.task1
import com.example.rxjavatasks.tasks1.task2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        invokeTasks1()
    }

    private fun invokeTasks1() {
        task1()
        task2()
    }
}
