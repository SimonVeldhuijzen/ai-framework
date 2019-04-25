package ai.framework.core.board

import java.util.*
import kotlin.reflect.KClass

class Move(val board: Board, val params: MutableMap<String, Any>) {
    val uuid = UUID.randomUUID()

    fun checkParam(param: String, clazz: KClass<*>): Boolean {
        return params.containsKey(param) && params[param]!!::class == clazz
    }

    fun <T>getParam(param: String): T {
        return params[param]!! as T
    }
}