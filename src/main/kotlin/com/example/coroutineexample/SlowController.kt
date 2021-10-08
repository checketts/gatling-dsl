package com.example.coroutineexample

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SlowController {

    @GetMapping("/slow")
    fun slow(){
        Thread.sleep(5000)
    }

}