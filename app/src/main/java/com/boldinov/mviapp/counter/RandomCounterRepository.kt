package com.boldinov.mviapp.counter

import java.io.IOException
import kotlin.random.Random

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
class RandomCounterRepository : CounterRepository {

    override fun getCounterLocal(): Int {
        return Random.nextInt()
    }

    override fun getCounterRemote(): Int {
        try {
            Thread.sleep(500)
            return Random.nextInt()
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}