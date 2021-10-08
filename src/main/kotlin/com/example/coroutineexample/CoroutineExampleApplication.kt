package com.example.coroutineexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
class CoroutineExampleApplication

fun main(args: Array<String>) {
	runApplication<CoroutineExampleApplication>(*args)
}
