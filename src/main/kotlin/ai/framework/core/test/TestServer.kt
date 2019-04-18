package ai.framework.core.test

import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger
import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import ai.framework.core.traffic.HttpClient
import io.javalin.HttpResponseException

class TestServer {
    companion object {
        private val logger by logger()
    }

    private val client by lazy { HttpClient(2000) }
    private val userUrl = "http://localhost:8080/users"
    private val tournamentUrl = "http://localhost:8080/tournaments"

    fun test() {
        pong()
        val user1 = User("name1", "shared1", "endpoint1", "creds1")
        val user2 = User("name2", "shared2", "endpoint2", "creds2")
        val user3 = User("name3", "shared3", "endpoint3", "creds3")

        postUser(User("name", "shared", "endpoint", ""), false)
        postUser(User("name", "shared", "", "creds"), false)
        postUser(User("name", "", "endpoint", "creds"), false)
        postUser(User("", "shared", "endpoint", "creds"), false)
        postUser(user1, true)
        postUser(user2, true)
        postUser(user3, true)

        getUser(user1.name, true)
        getUser("nonexistant", false)

        getUsers(3)

        deleteUser(user3.copy(sharedKey = "wrong"), false)
        deleteUser(user3, true)

        getUsers(2)

        val tournament1 = Tournament("name1", BoardType.BOTER_KAAS_EIEREN, "key1")
        val tournament2 = Tournament("name2", BoardType.BOTER_KAAS_EIEREN, "key2")
        val tournament3 = Tournament("name3", BoardType.BOTER_KAAS_EIEREN, "key3")

        postTournament(Tournament("name", BoardType.BOTER_KAAS_EIEREN, ""), false)
        postTournament(Tournament("", BoardType.BOTER_KAAS_EIEREN, "key"), false)
        postTournament(tournament1, true)
        postTournament(tournament2, true)
        postTournament(tournament3, true)

        getTournament(tournament1.name, BoardType.BOTER_KAAS_EIEREN, true)
        getTournament("nonexistant", BoardType.BOTER_KAAS_EIEREN, false)

        getTournaments(3)

        deleteTournament(tournament3.copy(key = "wrong"), false)
        deleteTournament(tournament3, true)

        getTournaments(2)

        joinTournament(tournament1.copy(name = "wrong"), user1.copy(sharedKey = "something"), false)
        joinTournament(tournament1, user1.copy(sharedKey = "something", name = "wrong"), false)
        joinTournament(tournament1, user1.copy(sharedKey = "something"), false)

        joinTournament(tournament1, user1, true)
        joinTournament(tournament1, user2, true)
        joinTournament(tournament2, user1, true)
        joinTournament(tournament2, user1, true)

        getTournament(tournament1.name, 2)
        getTournament(tournament2.name, 1)

        leaveTournament(tournament1.copy(name = "wrong"), user1.copy(sharedKey = "something"), false)
        leaveTournament(tournament1, user1.copy(sharedKey = "something", name = "wrong"), false)
        leaveTournament(tournament1, user1.copy(sharedKey = "something"), false)
        leaveTournament(tournament2, user2, true)
        leaveTournament(tournament2, user1, true)

        getTournament(tournament1.name, 2)
        getTournament(tournament2.name, 0)

        startTournament(Tournament("wrong", BoardType.BOTER_KAAS_EIEREN, "something"), false)
        startTournament(tournament1.copy(key = "something"), false)
    }

    private fun pong() {
        val pong = client.get("http://localhost:8080/ping")
        assert(pong == "pong")
    }

    private fun postUser(user: User, succeeds: Boolean) {
        caught({ client.post(userUrl, user) }, succeeds)
    }

    private fun getUser(name: String, succeeds: Boolean) {
        val user = caughtWithReturn({ client.get("$userUrl/$name", User::class.java) }, succeeds)
        if (succeeds) {
            assert(user != null)
            assert(user!!.name == name)
            assert(user.sharedKey == "")
            assert(user.endpoint == "")
            assert(user.endpointCredentials == "")
        }
    }

    private fun getUsers(count: Int) {
        val users = caughtWithReturn({ client.get(userUrl, Array<User>::class.java) }, true)!!
        assert(users.size == count)
    }

    private fun deleteUser(user: User, succeeds: Boolean) {
        caught({ client.delete("$userUrl/${user.name}", user.sharedKey) }, succeeds)
    }

    private fun postTournament(tournament: Tournament, succeeds: Boolean) {
        caught({ client.post(tournamentUrl, tournament) }, succeeds)
    }

    private fun getTournament(name: String, type: BoardType, succeeds: Boolean) {
        val tournament = caughtWithReturn({ client.get("$tournamentUrl/$name", Tournament::class.java) }, succeeds)
        if (succeeds) {
            assert(tournament != null)
            assert(tournament!!.name == name)
            assert(tournament.type == type)
            assert(tournament.key == "")
        }
    }

    private fun getTournament(name: String, users: Int) {
        val tournament = client.get("$tournamentUrl/$name", Tournament::class.java)
        assert(tournament.users.size == users)
    }

    private fun getTournaments(count: Int) {
        val tournaments = caughtWithReturn({ client.get(tournamentUrl, Array<Tournament>::class.java) }, true)!!
        assert(tournaments.size == count)
    }

    private fun deleteTournament(tournament: Tournament, succeeds: Boolean) {
        caught({ client.delete("$tournamentUrl/${tournament.name}", tournament.key) }, succeeds)
    }

    private fun joinTournament(tournament: Tournament, user: User, succeeds: Boolean) {
        caught({ client.post("$tournamentUrl/${tournament.name}/join/${user.name}", user.sharedKey) }, succeeds)
    }

    private fun leaveTournament(tournament: Tournament, user: User, succeeds: Boolean) {
        caught({ client.delete("$tournamentUrl/${tournament.name}/leave/${user.name}", user.sharedKey) }, succeeds)
    }

    private fun startTournament(tournament: Tournament, succeeds: Boolean) {
        caught({ client.post("$tournamentUrl/${tournament.name}/start", tournament.key) }, succeeds)
    }

    private fun caught(f: () -> Unit,  succeeds: Boolean) {
        try {
            f()
            if (!succeeds) {
                throw Exception()
            }
        } catch(e: HttpResponseException) {
            logger.info(e.toString())
            if (succeeds) {
                throw Exception()
            }
        }
    }

    private fun <T: Any> caughtWithReturn(f: () -> T,  succeeds: Boolean): T? {
        try {
            val value = f()
            if (!succeeds) {
                throw Exception()
            }

            return value
        } catch(e: HttpResponseException) {
            logger.info(e.toString())
            if (succeeds) {
                throw Exception()
            }
        }

        return null
    }
}