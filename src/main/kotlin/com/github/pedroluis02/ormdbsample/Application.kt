package com.github.pedroluis02.ormdbsample

import com.github.pedroluis02.ormdbsample.plugins.configureDatabases
import com.github.pedroluis02.ormdbsample.plugins.configureSerialization
import com.github.pedroluis02.ormdbsample.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
}
