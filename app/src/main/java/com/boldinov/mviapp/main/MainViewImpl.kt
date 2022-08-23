package com.boldinov.mviapp.main

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.boldinov.mviapp.R

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
class MainViewImpl(
    root: View
) : BaseMviView<MainViewModel, MainEvent>(), MainView {

    private val counterView = root.findViewById<TextView>(R.id.counter_text)

    private var dialog: AlertDialog? = null

    override val renderer: ViewRenderer<MainViewModel> =
        diff {
            diff(MainViewModel::isLoading) {
                dialog = if (it) {
                    AlertDialog.Builder(root.context)
                        .setMessage("Loading...").show()
                } else {
                    dialog?.hide()
                    null
                }
            }
            diff(MainViewModel::counter) {
                counterView.text = it
            }
        }

    init {
        root.findViewById<View>(R.id.counter_increase_button).apply {
            setOnClickListener {
                dispatch(MainEvent.IncreaseClicked)
            }
        }
        root.findViewById<View>(R.id.counter_decrease_button).apply {
            setOnClickListener {
                dispatch(MainEvent.DecreaseClicked)
            }
        }
    }
}