package ai.framework.entity

import ai.framework.constant.MessageType
import ai.framework.controller.UserController
import io.javalin.websocket.WsSession
import java.util.*

class User(private val session: WsSession, val name: String = "") {
    companion object {
        private val userController = UserController()
    }

    val uuid = UUID.randomUUID()

    fun send(message: String, type: MessageType) {
        userController.send(message, session, type)
    }
}