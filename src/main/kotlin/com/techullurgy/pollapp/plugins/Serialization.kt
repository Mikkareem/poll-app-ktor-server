package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.network.responses.AnsweredPollResponse
import com.techullurgy.pollapp.network.responses.NonAnsweredPollResponse
import com.techullurgy.pollapp.network.responses.PollResponse
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = SerializersModule {
                    polymorphic(PollResponse::class) {
                        subclass(AnsweredPollResponse::class, AnsweredPollResponse.serializer())
                        subclass(NonAnsweredPollResponse::class, NonAnsweredPollResponse.serializer())
                    }
                }
            }
        )
    }
}
