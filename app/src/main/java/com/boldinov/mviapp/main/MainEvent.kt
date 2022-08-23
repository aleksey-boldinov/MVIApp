package com.boldinov.mviapp.main

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
sealed class MainEvent {

    object IncreaseClicked : MainEvent()

    object DecreaseClicked : MainEvent()

    object ShareClicked : MainEvent()
}