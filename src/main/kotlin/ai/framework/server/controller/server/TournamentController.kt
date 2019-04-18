package ai.framework.server.controller.server

import ai.framework.core.constant.GameState
import ai.framework.core.helper.cleanStringBody
import ai.framework.core.helper.logger
import ai.framework.core.entity.Tournament
import ai.framework.server.repository.TournamentRepository
import ai.framework.server.repository.UserRepository
import io.javalin.BadRequestResponse
import io.javalin.Context
import io.javalin.MethodNotAllowedResponse
import io.javalin.apibuilder.CrudHandler

class TournamentController: CrudHandler {
    companion object {
        private val logger by logger()
    }

    private val repository = TournamentRepository()
    private val userRepository = UserRepository()

    override fun create(ctx: Context) {
        val tournament = ctx.body<Tournament>()
        logger.info("Creating tournament: ${tournament.name}")

        ctx.json(repository.create(ctx.body<Tournament>()))
        ctx.status(201)
    }

    override fun delete(ctx: Context, resourceId: String) {
        logger.info("Deleting tournament: $resourceId")

        val tournament = repository.get(resourceId)
        val key = cleanStringBody(ctx.body())

        if (tournament.key != key) {
            throw BadRequestResponse("Key not valid")
        }

        repository.delete(resourceId)
    }

    override fun getAll(ctx: Context) {
        logger.info("Getting all tournaments")
        ctx.json(repository.getAll())
    }

    override fun getOne(ctx: Context, resourceId: String) {
        logger.info("Getting tournament: $resourceId")
        ctx.json(repository.get(resourceId))
    }

    override fun update(ctx: Context, resourceId: String) {
        throw MethodNotAllowedResponse("", HashMap())
    }

    fun addUser(ctx: Context) {
        val tournamentName = ctx.pathParam("name")
        val userName = ctx.pathParam("username")
        logger.info("Adding user '$userName' to tournament: $tournamentName")

        val user = userRepository.get(userName)
        val key = cleanStringBody(ctx.body())
        if (key != user.sharedKey) {
            logger.info("key: $key, sharedKey: ${user.sharedKey}")
            logger.info(key.length.toString())
            logger.info(user.sharedKey.length.toString())
            throw BadRequestResponse("Key not valid")
        }

        repository.join(tournamentName, user)
        ctx.status(201)
    }

    fun removeUser(ctx: Context) {
        val tournamentName = ctx.pathParam("name")
        val userName = ctx.pathParam("username")
        logger.info("Removing user '$userName' from tournament: $tournamentName")

        val user = userRepository.get(userName)
        val key = cleanStringBody(ctx.body())
        if (key != user.sharedKey) {
            throw BadRequestResponse("Key not valid")
        }

        repository.leave(tournamentName, user)
    }

    fun start(ctx: Context) {
        val name = ctx.pathParam("name")
        logger.info("Starting tournament: $name")

        val key = cleanStringBody(ctx.body())

        val tournament = repository.get(name)

        if (tournament.state == GameState.CALCULATING) {
            throw BadRequestResponse("Tournament already under way")
        }

        if (tournament.state == GameState.FINISHED) {
            throw BadRequestResponse("Tournament already finished")
        }

        if (tournament.key != key) {
            throw BadRequestResponse("Key not valid")
        }

        tournament.start()
    }
}