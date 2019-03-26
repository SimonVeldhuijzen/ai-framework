package aiframework

import javax.ws.rs.core.MediaType
import javax.ws.rs.*

@Path("reverse-arrays")
class ArraysReverserEndpoints {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    fun reverseArrays(sequences: List<List<Int>>): List<List<Int>> {
        return ArraysReverser.reverse(sequences)
    }
}
