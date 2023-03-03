package com.example.plugins

import com.example.DB.Pdo
import com.example.Model.User
import com.example.Model.UserRequest
import com.example.Model.UserRespond
import com.example.Tables.UserEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.UserRoutes() {
    routing {
        val db = Pdo.connector

//        return all user from db
        get("/users") {
            val allStudents = db.from(UserEntity).select().map {
                val id = it[UserEntity.id]
                val login = it[UserEntity.login]
                val password = it[UserEntity.password]
                val role = it[UserEntity.role]
                User(id?:-1, login?:"", password?:"",role?:"")
            }
            call.respond(allStudents)
        }
//        add new user to db
        post("/adduser") {
            val request = call.receive<UserRequest>()
            val result = db.insert(UserEntity){
                set(it.login, request.login)
                set(it.password, request.password)
                set(it.role, request.role)
            }
            if(result == 1) {
                call.respond(HttpStatusCode.OK, UserRespond(true, "success"))
            }else {
                call.respond(HttpStatusCode.BadRequest, UserRespond(false, "Failed"))
            }
        }
//            delete user
        delete("/user/{id}") {
            val id = call.parameters["id"]?.toInt()?:-1
            val deleteuser = db.delete(UserEntity){
                it.id eq id
            }
            if(deleteuser == 1 ) {
                call.respond(HttpStatusCode.OK, UserRespond(true , "Success"))
            }else {
                call.respond(HttpStatusCode.NotFound, UserRespond(false, "Failed"))
            }
        }
//         route update user
        put("/user/{id}"){
            val id = call.parameters["id"]?.toInt()?:-1
            val updateUser = call.receive<UserRequest>()
            val updateuser = db.update(UserEntity){
                set(it.login, updateUser.login)
                set(it.password, updateUser.password)
                set(it.role, updateUser.role)
                where { it.id eq id }
            }
            if(updateuser == 1) {
                call.respond(HttpStatusCode.OK, UserRespond(true , "Success"))
            }else {
                call.respond(HttpStatusCode.NotFound, UserRespond(false , "Failed"))
            }
        }
//        route get user by id
        get("/user/{id}") {
            var id = call.parameters["id"]?.toInt() ?:-1
            val student = db.from(UserEntity).select()
                .where(UserEntity.id eq id)
                .map {
                    val id = it[UserEntity.id]
                    val login = it[UserEntity.login]
                    val password = it[UserEntity.password]
                    val role = it[UserEntity.role]
                    User(id?:-1, login?:"", password?:"",role?:"")
                }.firstOrNull()
            if(student == null) {
                call.respond(HttpStatusCode.NotFound, UserRespond(false , "user not exist"))
            }else {
                call.respond(HttpStatusCode.OK, UserRespond(true, student))
            }
        }
    }
}