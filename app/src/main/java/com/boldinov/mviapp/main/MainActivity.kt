package com.boldinov.mviapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.boldinov.mviapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    MainFragment(),
                    MainFragment::class.java.canonicalName
                ).commit()
        }
    }
}