package com.github.pedroluis02.ormdbsample

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        driverClassName = "org.h2.Driver"
        username = "root"
        password = ""
        maximumPoolSize = 6
        isReadOnly = false
    }
    val dataSource = HikariDataSource(config)

    val database = Database.connect(
        datasource = dataSource,
        databaseConfig = DatabaseConfig()
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