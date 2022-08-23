package com.boldinov.mviapp.base.state

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
sealed class LoadingState {

    object Idle : LoadingState()

    object Loading : LoadingState()

    data class Error(val throwable: Throwable) : LoadingState()
}