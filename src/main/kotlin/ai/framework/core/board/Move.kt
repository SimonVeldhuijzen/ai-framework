package ai.framework.core.board

import kotlin.reflect.KClass

class Move(val params: MutableMap<String, Any>) {
    fun checkParam(param: String, clazz: KClass<*>): Boolean {
        return params.containsKey(param) && params[param]!!::class == clazz
    }

    fun <T>getParam(param: String): T {
        return params[param]!! as T
    }
}