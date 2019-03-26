package aiframework.controller

import aiframework.entity.Player
import aiframework.persistency.PlayerRepository
import aiframework.persistency.players
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PlayerController {
    private val repository: PlayerRepository = PlayerRepository()

    @GET
    fun all(): List<Player> {
        return repository.all()
    }

    @GET
    @Path("/{name}")
    fun byName(@PathParam("name") name: String): Player? {
        return repository.byName(name)
    }

    @POST
    fun create(player: Player) {
        repository.create(player)
    }

    @DELETE
    @Path("/{name}")
    fun delete(@PathParam("name") name: String) {
        repository.delete(name)
    }
}