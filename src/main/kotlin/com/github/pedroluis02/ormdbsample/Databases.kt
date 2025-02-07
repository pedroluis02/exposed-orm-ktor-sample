package com.github.pedroluis02.ormdbsample

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    val userService = UserService(database)

    routing {
        route("/users") {
            post {
                val user = call.receive<ExposedUser>()
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }

            get("/{id}") {
                val id = call.extractIDOrThrow()
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("/{id}") {
                val id = call.extractIDOrThrow()
                val user = call.receive<ExposedUser>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}") {
                val id = call.extractIDOrThrow()
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private fun RoutingCall.extractIDOrThrow(): Int {
    return parameters["id"]?.toInt() ?: throw InvalidIDArgumentException()
}