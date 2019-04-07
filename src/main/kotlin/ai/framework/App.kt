package ai.framework

import ai.framework.controller.UserController
import ai.framework.entity.*
import ai.framework.game.BoterKaasEierenBoard
import ai.framework.game.Game
import io.javalin.Javalin

private val userController = UserController()

fun main() {
    val smart = SmartPlayer(User("smart"))
    val slow = SlowPlayer(User("slow"))
    val slow2 = SlowPlayer(User("slow2"))
    val random = RandomPlayer(User("random"))
    val board = BoterKaasEierenBoard(listOf(slow2, slow))
    val game = Game(board)
    System.out.println("starting")
    game.start(2000)

    System.out.println("finished")


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