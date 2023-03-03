package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.UserRoutes() {
    routing {
        get("/users") {
            call.respondText("all users")
        }
    }
}