package ai.framework.controller

import ai.framework.core.logger
import ai.framework.entity.User
import io.javalin.websocket.WsSession
import java.util.concurrent.ConcurrentHashMap

class UserController {
    companion object {
        private val logger by logger()
        private val users = ConcurrentHashMap<WsSession, User>()
    }

    fun create(session: WsSession) {
        val userName = session.pathParam("name")
        val user = User(userName)
        users[session] = user
        send("ok", session)
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