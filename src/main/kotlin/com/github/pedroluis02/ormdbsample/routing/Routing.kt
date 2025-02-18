package com.github.pedroluis02.ormdbsample.routing

import com.github.pedroluis02.ormdbsample.service.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userService = UserService()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/users") {
            userRoute(userService)
        }
    }
}
