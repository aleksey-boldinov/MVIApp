package com.boldinov.mviapp.base.router

import androidx.lifecycle.LifecycleOwner

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
interface Navigator<ENTITY : LifecycleOwner> {

    fun execute(command: (ENTITY) -> Unit)
}