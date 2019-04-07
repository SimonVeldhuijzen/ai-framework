package ai.framework.traffic

import ai.framework.constant.MessageType
import ai.framework.entity.User
import com.fasterxml.jackson.databind.ObjectMapper

class Dispatcher {
    fun handleIncoming(message: String, user: User) {
        if (message.isEmpty() || message.startsWith('|')) {
            return
        }

        val type: MessageType
        try {
            type = MessageType.valueOf(message.substringBefore('|'))
        } catch (e: IllegalArgumentException) {
            return
        }

        val result = ObjectMapper().readValue(message.substringAfter('|'), type.clazz)
        passMessage(type, result, user)
    }

    private fun passMessage(type: MessageType, result: Any, user: User) {

    }
}