package ai.framework

import ai.framework.client.controller.PlayerController
import ai.framework.core.constant.BoardType
import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import ai.framework.core.helper.logger
import ai.framework.server.controller.TournamentController
import ai.framework.server.controller.UserController
import ai.framework.core.test.TestServer
import ai.framework.core.traffic.HttpClient
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud

private val userController = UserController()
private val tournamentController = TournamentController()
private val playerController = PlayerController()

fun main(args: Array<String>) {
    if (args.size != 1) {
        return
    }

    when {
        args[0] == "server" -> {
            val javalin = Javalin.create()

            generalSettings(javalin, 8080)
            addRestServerApis(javalin)

            javalin.start()
        }
        args[0] == "testServer" -> {
            val tester = TestServer()
            tester.test()
        }
        args[0] == "client" -> {
            val javalin = Javalin.create()

            generalSettings(javalin, 8081)
            addRestClientApis(javalin)

            javalin.start()
        }
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
        post("move") {ctx ->
            playerController.move(ctx)
        }

        get("test") {ctx ->
            val client = HttpClient(5000)
            client.post("http://localhost:8080/tournaments", Tournament("tournament", BoardType.BOTER_KAAS_EIEREN, 50, 2000, "key"))
            client.post("http://localhost:8080/users", User("user1", "key1", "http://localhost:8081", "creds1"))
            client.post("http://localhost:8080/users", User("user2", "key2", "http://localhost:8081", "creds2"))
            client.post("http://localhost:8080/tournaments/tournament/join/user1", "key1")
            client.post("http://localhost:8080/tournaments/tournament/join/user2", "key2")
            client.post("http://localhost:8080/tournaments/tournament/start", "key")
        }
    }
}