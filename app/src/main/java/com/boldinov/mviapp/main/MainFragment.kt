package com.boldinov.mviapp.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.boldinov.mviapp.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainController.attachTo(
            fragment = this,
            viewFactory = {
                MainViewImpl(it)
            }
        )
    }
}