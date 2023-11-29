package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule)
    }
}