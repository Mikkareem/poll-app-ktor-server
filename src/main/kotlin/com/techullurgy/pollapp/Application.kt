package com.techullurgy.pollapp

import com.techullurgy.pollapp.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureStatusPages()
    configureSecurity()
    configureSerialization()
    configureDatabase()
    configureRouting()
}
