package org.utl.dsm503.rest;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.FormParam;
import org.utl.dsm503.controller.ControllerLogin;

import java.util.UUID;

@Path("acceso")
public class RESTAcceso {

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("usuario") String username, @FormParam("password") String password) {
        ControllerLogin controller = new ControllerLogin();
        boolean isAuthenticated = controller.authenticate(username, password);
        if (isAuthenticated) {
            String token = UUID.randomUUID().toString(); // Generar un token simple
            return Response.ok("{\"status\":\"success\", \"message\":\"Autenticación exitosa\", \"token\":\"" + token + "\"}").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"status\":\"error\", \"message\":\"Credenciales inválidas\"}").build();
        }
    }
}
