package ai.framework.core

import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import java.util.*

class Database {
    companion object {
        val tournaments = LinkedList<Tournament>()
        val users = LinkedList<User>()
    }
}