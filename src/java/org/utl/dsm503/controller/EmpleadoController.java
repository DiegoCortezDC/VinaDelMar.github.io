package org.utl.dsm503.controller;

import org.utl.dsm503.bd.ConnectionMysql;
import org.utl.dsm503.model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoController {

    public List<Empleado> getAllEmpleados() throws SQLException {
        return getEmpleadosByStatus(1); // Solo activos
    }

    public List<Empleado> getInactivos() throws SQLException {
        return getEmpleadosByStatus(0); // Solo inactivos
    }

    private List<Empleado> getEmpleadosByStatus(int status) throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados WHERE estatus = ?";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(new Empleado(
                            rs.getInt("idEmpleado"),
                            rs.getString("nombre"),
                            rs.getString("apellidoPaterno"),
                            rs.getString("apellidoMaterno"),
                            rs.getDate("fechaIngreso"),
                            rs.getDate("fechaSalida"),
                            rs.getString("curp"),
                            rs.getString("domicilio"),
                            rs.getString("telefono"),
                            rs.getInt("edad"),
                            rs.getInt("estatus"),
                            rs.getString("rol"),
                            rs.getString("email")
                    ));
                }
            }
        }
        return empleados;
    }

    public void updateEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, telefono=?, domicilio=?, curp=?, fechaIngreso=?, edad=?, rol=?, estatus=?, email=? WHERE idEmpleado=?";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellidoPaterno());
            pstmt.setString(3, empleado.getApellidoMaterno());
            pstmt.setString(4, empleado.getTelefono());
            pstmt.setString(5, empleado.getDomicilio());
            pstmt.setString(6, empleado.getCurp());
            pstmt.setDate(7, new java.sql.Date(empleado.getFechaIngreso().getTime()));
            pstmt.setInt(8, empleado.getEdad());
            pstmt.setString(9, empleado.getRol());
            pstmt.setInt(10, empleado.getEstatus());
            pstmt.setString(11, empleado.getEmail());  // Actualizar el correo electrónico
            pstmt.setInt(12, empleado.getIdEmpleado());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating employee failed, no rows affected.");
            }
        }
    }

    public void deleteEmpleado(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estatus = 0, fechaSalida = CURDATE() WHERE idEmpleado = ?";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpleado);
            pstmt.executeUpdate();
        }
    }

    public void addEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, apellidoPaterno, apellidoMaterno, fechaIngreso, curp, domicilio, telefono, edad, estatus, rol, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellidoPaterno());
            pstmt.setString(3, empleado.getApellidoMaterno());
            pstmt.setDate(4, new java.sql.Date(empleado.getFechaIngreso().getTime()));
            pstmt.setString(5, empleado.getCurp());
            pstmt.setString(6, empleado.getDomicilio());
            pstmt.setString(7, empleado.getTelefono());
            pstmt.setInt(8, empleado.getEdad());
            pstmt.setInt(9, empleado.getEstatus());
            pstmt.setString(10, empleado.getRol());
            pstmt.setString(11, empleado.getEmail());  // Añade el correo electrónico
            pstmt.executeUpdate();
        }
    }

    // Reactivar un empleado inactivo
    public void reactivateEmpleado(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estatus = 1, fechaIngreso = CURDATE() WHERE idEmpleado = ?";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpleado);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo reactivar el empleado, ningún registro fue afectado.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            throw e;
        }
    }
}
