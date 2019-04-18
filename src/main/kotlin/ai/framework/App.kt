package ai.framework

import ai.framework.client.controller.PlayerController
import ai.framework.server.controller.server.TournamentController
import ai.framework.server.controller.server.UserController
import ai.framework.core.test.TestServer
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud

private val userController = UserController()
private val tournamentController = TournamentController()
private val playerController = PlayerController()

fun main(args: Array<String>) {
    if (args.size != 1) {
        return
    }

    if (args[0] == "server") {
        val javalin = Javalin.create()

        generalSettings(javalin, 8080)
        addRestServerApis(javalin)

        javalin.start()
    } else if (args[0] == "testserver") {
        val tester = TestServer()
        tester.test()
    } else if (args[0] == "client") {
        val javalin = Javalin.create()

        generalSettings(javalin, 8081)
        addRestClientApis(javalin)

        javalin.start()
    }
}

private fun generalSettings(javalin: Javalin, port: Int) {
    javalin.apply {
        port(port)
        get("ping") { ctx ->
            ctx.result("pong")
        }
    }
}

private fun addRestServerApis(javalin: Javalin) {
    javalin.apply {
        routes {
            crud("tournaments/:name", tournamentController)
            crud("users/:name", userController)
        }

        post("tournaments/:name/start") { ctx ->
            tournamentController.start(ctx)
        }

        post("tournaments/:name/join/:username") { ctx ->
            tournamentController.addUser(ctx)
        }

        delete("tournaments/:name/leave/:username") { ctx ->
            tournamentController.removeUser(ctx)
        }
    }
}

private fun addRestClientApis(javalin: Javalin) {
    javalin.apply {
        get("move?player=:uuid&timeout=:timeout") {ctx ->
            playerController.move(ctx)
        }
    }
}