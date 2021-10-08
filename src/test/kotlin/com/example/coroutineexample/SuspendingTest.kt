package com.example.coroutineexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SuspendingTest {

    @Test
    fun testIt() = suspendingTest {
        fail("On purpose ${thing()}")
    }

    @Test
    fun testItGood() = runBlockingTest {
        assertThat(thing()).isEqualTo("a string")
    }

    suspend fun thing() = "a string"

    fun suspendingTest(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Any): Unit {
        runBlocking(context, block)
        Unit
    }



}