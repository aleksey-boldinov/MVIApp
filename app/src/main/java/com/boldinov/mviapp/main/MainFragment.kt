package com.boldinov.mviapp.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.boldinov.mviapp.R
import com.boldinov.mviapp.base.router.WeakLifecycleNavigator
import com.boldinov.mviapp.counter.CounterRouterImpl
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var controller: MainController

    private val counterRouter = CounterRouterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counterRouter.attachNavigator(WeakLifecycleNavigator(this))
        controller = MainController(
            DefaultStoreFactory(),
            counterRouter,
            instanceKeeper()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.onViewCreated(MainViewImpl(view), viewLifecycleOwner.essentyLifecycle())
    }
}