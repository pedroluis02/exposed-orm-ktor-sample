package com.github.pedroluis02.ormdbsample.routing

import com.github.pedroluis02.ormdbsample.service.User
import com.github.pedroluis02.ormdbsample.service.UserService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userService: UserService) {
    post {
        val dto = call.receive<UserDto>()
        val user = userService.create(dto.toModel(0))
        call.respond(HttpStatusCode.Created, user.toDto())
    }

    get {
        val users = userService.readAll()
        call.respond(HttpStatusCode.OK, users.map { it.toDto() })
    }

    get("/{id}") {
        val id = call.extractIDOrThrow()
        val user = userService.read(id)
        if (user != null) {
            call.respond(HttpStatusCode.OK, user.toDto())
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    put("/{id}") {
        val id = call.extractIDOrThrow()
        val dto = call.receive<UserDto>()

        val user = dto.toModel(id)
        userService.update(user)
        call.respond(HttpStatusCode.OK)
    }

    delete("/{id}") {
        val id = call.extractIDOrThrow()
        userService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}

private fun RoutingCall.extractIDOrThrow(): Int {
    return parameters["id"]?.toInt() ?: throw InvalidIDArgumentException()
}

private fun UserDto.toModel(id: Int) = User(id, name, age)

private fun User.toDto() = UserDto(id, name, age)
