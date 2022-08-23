package com.boldinov.mviapp.counter

import androidx.annotation.WorkerThread
import java.io.IOException

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
@WorkerThread
interface CounterRepository {

    fun getCounterLocal(): Int

    @Throws(IOException::class)
    fun getCounterRemote(): Int
}