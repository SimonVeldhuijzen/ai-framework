package ai.framework.server.repository

import ai.framework.core.Database
import ai.framework.core.entity.Tournament
import ai.framework.core.entity.User
import io.javalin.ConflictResponse
import io.javalin.NotFoundResponse

class TournamentRepository: Repository() {
    fun getAll(): List<Tournament> = Database.tournaments

    fun get(name: String): Tournament = Database.tournaments.find { t -> t.name == name } ?: throw NotFoundResponse("Tournament with name '$name' not found")

    fun delete(name: String) = Database.tournaments.removeIf { t -> t.name == name }

    fun create(tournament: Tournament): Tournament {
        throwIfEmpty(tournament.name, "name")
        throwIfEmpty(tournament.key, "key")
        throwIfOutOfBounds(tournament.repetitions, "repetitions", 1)
        throwIfOutOfBounds(tournament.timeout, "timeout", 500)

        if (Database.tournaments.find { t -> t.name == tournament.name } != null) {
            throw ConflictResponse("Tournament '${tournament.name}' already exists")
        }

        Database.tournaments.add(tournament)
        return tournament
    }

    fun join(name: String, user: User) {
        val tournament = get(name)
        if (tournament.users.none { u -> u.name == user.name }) {
            tournament.users.add(user)
        }
    }

    fun leave(name: String, user: User) {
        val tournament = get(name)
        tournament.users.removeIf { u -> u.name == user.name }
    }
}