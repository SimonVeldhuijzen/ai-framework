package ai.framework

import ai.framework.client.controller.PlayerController
import ai.framework.core.constant.BoardType
import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import ai.framework.server.controller.TournamentController
import ai.framework.server.controller.UserController
import ai.framework.core.test.TestServer
import ai.framework.core.traffic.HttpClient
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import java.util.*

private val userController = UserController()
private val tournamentController = TournamentController()
private val playerController = PlayerController()

private var credentials = ""
private var serverCredentials = ""
private var serverEndpoint = ""
private var endpoint = ""

fun main(args: Array<String>) {
    val port: Int?
    if (args.contains("--port")) {
        val portString = args[args.indexOf("--port") + 1]
        if (portString.any { c -> !c.isDigit() }) {
            System.out.println("Invalid port: $portString")
            return
        }

        port = portString.toInt()
    } else {
        port = null
    }

    when {
        args[0] == "server" -> {
            startServer(args, port)
        }
        args[0] == "testServer" -> {
            val tester = TestServer()
            tester.test()
        }
        args[0] == "client" -> {
            startClient(args, port)
        }
    }
}

private fun startServer(args: Array<String>, port: Int?) {
    val javalin = Javalin.create()

    generalSettings(javalin, port ?: 8080)
    addRestServerApis(javalin)

    javalin.start()
}

private fun startClient(args: Array<String>, port: Int?) {
    if (args.contains("--credentials")) {
        credentials = args[args.indexOf("--credentials") + 1]
        if (credentials.count { c -> c == ':' } != 1 || credentials.startsWith(":") || credentials.endsWith(":")) {
            System.out.println("Invalid credentials: $credentials")
            return
        }
    } else {
        System.out.println("No credentials given. Use --credentials")
        return
    }

    if (args.contains("--server-credentials")) {
        serverCredentials = args[args.indexOf("--server-credentials") + 1]
        if (serverCredentials.count { c -> c == ':' } != 1 || serverCredentials.startsWith(":") || serverCredentials.endsWith(":")) {
            System.out.println("Invalid credentials: $serverCredentials")
            return
        }
    } else {
        System.out.println("No server credentials given. Use --server-credentials")
        return
    }

    if (args.contains("--endpoint")) {
        endpoint = args[args.indexOf("--endpoint") + 1]
    } else {
        System.out.println("No endpoint given. Use --endpoint")
        return
    }

    if (args.contains("--server-endpoint")) {
        serverEndpoint = args[args.indexOf("--server-endpoint") + 1]
    } else {
        System.out.println("No server endpoint given. Use --server-endpoint")
        return
    }

    val javalin = Javalin.create()

    generalSettings(javalin, port ?: 8081)
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
            val client = HttpClient(5000, Base64.getEncoder().encodeToString(serverCredentials.toByteArray()))
            client.post("$serverEndpoint/tournaments", Tournament("tournament", BoardType.BOTER_KAAS_EIEREN, 50, 2000, "key"))
            client.post("$serverEndpoint/users", User("user1", "key1", endpoint, credentials))
            client.post("$serverEndpoint/users", User("user2", "key2", endpoint, credentials))
            client.post("$serverEndpoint/users", User("user3", "key3", endpoint, credentials))
            client.post("$serverEndpoint/users", User("user4", "key4", endpoint, credentials))
            client.post("$serverEndpoint/users", User("user5", "key5", endpoint, credentials))
            client.post("$serverEndpoint/tournaments/tournament/join/user1", "key1")
            client.post("$serverEndpoint/tournaments/tournament/join/user2", "key2")
            client.post("$serverEndpoint/tournaments/tournament/join/user3", "key3")
            client.post("$serverEndpoint/tournaments/tournament/join/user4", "key4")
            client.post("$serverEndpoint/tournaments/tournament/join/user5", "key5")
            client.post("$serverEndpoint/tournaments/tournament/start", "key")
        }
    }
}
