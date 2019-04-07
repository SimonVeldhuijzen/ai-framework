package ai.framework

import ai.framework.controller.UserController
import io.javalin.Javalin

private val userController = UserController()

fun main() {
    val javalin = Javalin.create()

    generalSettings(javalin)
    addUserRegistration(javalin)

    javalin.start()
}

private fun generalSettings(javalin: Javalin) {
    javalin.apply {
        port(8443)
        get("ping") {ctx ->
            ctx.result("pong")
        }
    }
}

private fun addUserRegistration(javalin: Javalin) {
    javalin.apply {
        ws("/register/:name") { ws ->
            ws.onConnect { session ->
                userController.create(session)
            }
            ws.onClose { session, _, _ ->
                userController.delete(session)
            }
            ws.onMessage { session, message ->
                userController.receive(message, session)
            }
        }
    }
}