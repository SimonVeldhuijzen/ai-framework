package ai.framework.entity

import java.util.*

//import java.util.*
//
//abstract class Player(val user: User) {
//    val uuid = UUID.randomUUID()
//
//    fun makeMove(request: MoveRequest): MoveResponse {
//        Thread.sleep(1000)
//        return request.answer(hashMapOf("row" to 1, "column" to 1))
//    }
//}

abstract class Player {
    val uuid = UUID.randomUUID()
    abstract fun makeMove(request: MoveRequest): MoveResponse
    abstract fun name(): String
}