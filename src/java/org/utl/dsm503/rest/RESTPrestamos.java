package org.utl.dsm503.rest;

import org.utl.dsm503.model.Prestamo;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm503.controller.PrestamosController;
import java.util.List;
import org.utl.dsm503.model.Abono;

@Path("prestamos")
public class RESTPrestamos {

    private PrestamosController controller = new PrestamosController();
    private Gson gson = new Gson();

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePrestamo(String jsonPrestamo) {
        try {
            Prestamo prestamo = gson.fromJson(jsonPrestamo, Prestamo.class);
            controller.addPrestamo(prestamo);
            return Response.ok(gson.toJson("Préstamo registrado con éxito")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson("Error al registrar préstamo: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrestamos() {
        try {
            List<Prestamo> prestamos = controller.getAllPrestamos();
            return Response.ok(gson.toJson(prestamos)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson("Error al recuperar préstamos: " + e.getMessage()))
                    .build();
        }
    }

 @POST
@Path("realizarAbono")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response realizarAbono(String jsonAbono) {
    try {
        Abono abono = gson.fromJson(jsonAbono, Abono.class);
        if (abono.getMonto() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson("El monto debe ser positivo")).build();
        }
        boolean completado = controller.realizarAbono(abono);
        if (completado) {
            return Response.ok(gson.toJson("completamente pagado")).build();
        } else {
            return Response.ok(gson.toJson("Abono realizado pero el préstamo no está completamente pagado")).build();
        }
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson("Error al realizar abono: " + e.getMessage())).build();
    }
}


    @GET
    @Path("abonos/{idPrestamo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbonos(@PathParam("idPrestamo") int idPrestamo) {
        try {
            List<Abono> abonos = controller.getAbonosPorPrestamo(idPrestamo);
            return Response.ok(gson.toJson(abonos)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson("Error al recuperar abonos: " + e.getMessage())).build();
        }
    }

    @DELETE
    @Path("delete/{idPrestamo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPrestamo(@PathParam("idPrestamo") int idPrestamo) {
        try {
            controller.eliminarPrestamo(idPrestamo);
            return Response.ok(gson.toJson("Préstamo eliminado con éxito")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson("Error al eliminar préstamo: " + e.getMessage()))
                    .build();
        }
    }
}
