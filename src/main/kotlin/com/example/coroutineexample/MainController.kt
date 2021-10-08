package com.example.coroutineexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController(
//        private val webClientBuilder: WebClient.Builder
) {
    private val logger = LoggerFactory.getLogger(MainController::class.java)

    @GetMapping
    suspend fun coroutineStuff(): String {
        logger.info("About to wait")
        delay(1000)
        logger.info("Waiting")
        delay(1000)
        logger.info("Done waiting")
        return "Hello World"
    }


    @GetMapping("flow")
    fun flowStuff() = flow<String> {
        repeat(10) {
            delay(1000)
            emit("Hey! $it")
        }
    }
}