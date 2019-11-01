package com.ruanchao.androidblog

import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {


    }
}


class Single{
    companion object {
        val instance:Single by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Single()
        }
    }

    fun test(){
        var list: MutableList<Int> = mutableListOf(1,2,3,4)
        val fold = list.fold(4) { totle, next ->
            totle + next
        }
    }
}
