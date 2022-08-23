package com.boldinov.mviapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var controller: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = MainController(
            DefaultStoreFactory(),
            instanceKeeper()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.onViewCreated(MainViewImpl(view), viewLifecycleOwner.essentyLifecycle())
    }
}