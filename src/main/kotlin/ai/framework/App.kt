package ai.framework

import ai.framework.client.controller.PlayerController
import ai.framework.core.board.VierOpEenRijBoard
import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType
import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import ai.framework.core.helper.logger
import ai.framework.server.controller.TournamentController
import ai.framework.server.controller.UserController
import ai.framework.core.traffic.Arguments
import ai.framework.core.traffic.HttpClient
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import java.util.*

private val userController = UserController()
private val tournamentController = TournamentController()
private val playerController = PlayerController()

private lateinit var arguments: Arguments

fun main(args: Array<String>) {
    arguments = Arguments(args)

    when {
        arguments.hasErrors() -> System.out.println(arguments.error)
        arguments.isClient -> startClient()
        arguments.isServer -> startServer()
    }
}

private fun startServer() {
    val javalin = Javalin.create()

    generalSettings(javalin, arguments.port ?: 8080)
    addRestServerApis(javalin)

    javalin.start()
}

private fun startClient() {
    val javalin = Javalin.create()

    generalSettings(javalin, arguments.port ?: 8081)
    addRestClientApis(javalin)

    javalin.start()
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
        enableStaticFiles("/public")

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

        get("tournament_types") {ctx ->
            ctx.json(BoardType.values())
        }
    }
}

private fun addRestClientApis(javalin: Javalin) {
    javalin.apply {
        post("move") {ctx ->
            playerController.move(ctx)
        }

        post("tournament/:name/join") {ctx ->
            val client = HttpClient(5000, Base64.getEncoder().encodeToString(arguments.serverCredentials.toByteArray()))
            client.post("${arguments.serverEndpoint}/tournaments/${ctx.pathParam("name")}/join/${arguments.name}", arguments.sharedKey)
        }

        post("register") {
            val client = HttpClient(5000, Base64.getEncoder().encodeToString(arguments.serverCredentials.toByteArray()))
            client.post("${arguments.serverEndpoint}/users", User(arguments.name, arguments.sharedKey, arguments.endpoint, arguments.credentials))
        }
    }
}
