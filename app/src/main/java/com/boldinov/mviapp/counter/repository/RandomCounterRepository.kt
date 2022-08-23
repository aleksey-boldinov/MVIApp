package com.boldinov.mviapp.counter.repository

import java.io.IOException
import kotlin.random.Random

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
class RandomCounterRepository : CounterRepository {

    override fun getCounterLocal(): Int {
        return Random.nextInt(10)
    }

    override fun getCounterRemote(): Int {
        try {
            Thread.sleep(1000)
            return Random.nextInt(20)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}