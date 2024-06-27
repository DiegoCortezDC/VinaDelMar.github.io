package org.utl.dsm503.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm503.controller.NominaController;
import org.utl.dsm503.model.Nomina;

@Path("nominas")
public class RESTNominas {

    private NominaController controller = new NominaController();
    private Gson gson = new Gson();

    @POST
    @Path("saveAndSend")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAndSendNomina(String jsonNomina) {
        try {
            Nomina nomina = gson.fromJson(jsonNomina, Nomina.class);
            controller.addAndSendNomina(nomina);
            return Response.ok(gson.toJson("Nómina guardada y enviada con éxito")).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson("Error al guardar y enviar nómina: " + e.getMessage()))
                    .build();
        }
    }
}
