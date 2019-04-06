package ai.framework.controller

import ai.framework.entity.User
import io.javalin.websocket.WsSession
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class UserController {
    private val users = ConcurrentHashMap<WsSession, User>()
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    fun create(session: WsSession) {
        val userName = session.pathParam("name")
        val user = User(userName)
        users[session] = user
        session.send("ok")
        logger.info("Created a new user with name '$userName'")
    }

    fun delete(session: WsSession) {
        val user = users[session]
        if (user != null) {
            users.remove(session)
            logger.info("Deleted user with name '${user.name}'")
        }
    }

    fun send(message: String, session: WsSession) {
        logger.info("Sending message to user '${users[session]?.name}'")
        session.send(message)
    }

    fun receive(message: String, session: WsSession) {
        logger.info("Received message from user '${users[session]?.name}'")
        send("received $message", session)
    }
}