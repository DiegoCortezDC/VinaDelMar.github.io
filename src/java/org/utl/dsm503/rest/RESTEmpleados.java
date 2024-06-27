package org.utl.dsm503.rest;

import org.utl.dsm503.model.Empleado;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm503.controller.EmpleadoController;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Path("empleados")
public class RESTEmpleados {

    private EmpleadoController controller = new EmpleadoController();
    private Gson gson = new Gson();

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveEmpleado(String jsonEmpleado) {
        try {
            Empleado empleado = gson.fromJson(jsonEmpleado, Empleado.class);
            controller.addEmpleado(empleado);
            return Response.ok(gson.toJson(empleado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error al guardar empleado: " + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmpleados() {
        try {
            return Response.ok(gson.toJson(controller.getAllEmpleados())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error al recuperar empleados: " + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmpleado(String jsonEmpleado) {
        try {
            Empleado empleado = gson.fromJson(jsonEmpleado, Empleado.class);
            controller.updateEmpleado(empleado);
            return Response.ok("{\"success\":\"Empleado actualizado correctamente.\"}").build();
        } catch (SQLException sqlException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error SQL al actualizar empleado: " + sqlException.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error al actualizar empleado: " + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("delete")
    public Response deleteEmpleado(@QueryParam("id") int idEmpleado) {
        try {
            controller.deleteEmpleado(idEmpleado);
            return Response.ok("{\"success\":\"Empleado eliminado correctamente.\"}").build();
        } catch (SQLException sqlException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error SQL al eliminar empleado: " + sqlException.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error al eliminar empleado: " + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getInactivos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInactivos() {
        try {
            List<Empleado> inactivos = controller.getInactivos();
            return Response.ok(gson.toJson(inactivos)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al recuperar empleados inactivos: " + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("reactivate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) // Asegúrate de tener esta línea si esperas una respuesta JSON
    public Response reactivateEmpleado(String jsonEmpleado) {
        Gson gson = new Gson();
        try {
            Empleado empleado = gson.fromJson(jsonEmpleado, Empleado.class);
            controller.reactivateEmpleado(empleado.getIdEmpleado());
            return Response.ok(gson.toJson("Empleado reactivado exitosamente")).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson("Error al reactivar empleado: " + e.getMessage()))
                    .build();
        }
    }

}
