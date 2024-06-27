package org.utl.dsm503.controller;

import org.utl.dsm503.bd.ConnectionMysql;
import org.utl.dsm503.model.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm503.model.Abono;

public class PrestamosController {

    public void addPrestamo(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamos (idEmpleado, monto, fechaPrestamo) VALUES (?, ?, ?)";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prestamo.getIdEmpleado());
            pstmt.setDouble(2, prestamo.getMonto());
            pstmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            throw e;
        }
    }

    public List<Prestamo> getAllPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.idPrestamo, p.idEmpleado, p.monto, p.fechaPrestamo, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.rol FROM prestamos p JOIN empleados e ON p.idEmpleado = e.idEmpleado";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("idPrestamo"),
                        rs.getInt("idEmpleado"),
                        rs.getDouble("monto"),
                        rs.getDate("fechaPrestamo"),
                        rs.getString("nombre"),
                        rs.getString("apellidoPaterno"),
                        rs.getString("apellidoMaterno"),
                        rs.getString("rol")
                );
                prestamos.add(prestamo);
            }
        }
        return prestamos;
    }

    public boolean realizarAbono(Abono abono) throws SQLException {
    Connection conn = null;
    boolean isPrestamoFullyPaid = false;
    try {
        conn = new ConnectionMysql().open();
        conn.setAutoCommit(false);  // Iniciar transacción

        // Agregar el abono
        String sqlAbono = "INSERT INTO abonos (idPrestamo, monto, fechaAbono, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlAbono)) {
            pstmt.setInt(1, abono.getIdPrestamo());
            pstmt.setDouble(2, abono.getMonto());
            pstmt.setDate(3, new java.sql.Date(abono.getFechaAbono().getTime()));
            pstmt.setString(4, abono.getTipo());
            pstmt.executeUpdate();
        }

        // Actualizar el monto del préstamo
        String sqlUpdatePrestamo = "UPDATE prestamos SET monto = monto - ? WHERE idPrestamo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdatePrestamo)) {
            pstmt.setDouble(1, abono.getMonto());
            pstmt.setInt(2, abono.getIdPrestamo());
            pstmt.executeUpdate();

            // Verificar si el préstamo está completamente pagado
            String sqlCheck = "SELECT monto FROM prestamos WHERE idPrestamo = ?";
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setInt(1, abono.getIdPrestamo());
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next()) {
                    double remainingAmount = rs.getDouble(1);
                    if (remainingAmount <= 0) {
                        isPrestamoFullyPaid = true;
                        eliminarPrestamo(abono.getIdPrestamo(), conn); // Eliminar el préstamo si está completamente pagado
                    }
                }
            }
        }

        conn.commit();  // Finalizar transacción
        return isPrestamoFullyPaid;
    } catch (SQLException e) {
        if (conn != null) {
            conn.rollback();  // Revertir cambios en caso de error
        }
        throw e;
    } finally {
        if (conn != null) {
            conn.close();
        }
    }
}



    public List<Abono> getAbonosPorPrestamo(int idPrestamo) throws SQLException {
        List<Abono> abonos = new ArrayList<>();
        String sql = "SELECT * FROM abonos WHERE idPrestamo = ?";
        try (Connection conn = new ConnectionMysql().open(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPrestamo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                abonos.add(new Abono(
                        rs.getInt("idAbono"),
                        rs.getInt("idPrestamo"),
                        rs.getDouble("monto"),
                        rs.getDate("fechaAbono"),
                        rs.getString("tipo")
                ));
            }
        }
        return abonos;
    }

   public void eliminarPrestamo(int idPrestamo) throws SQLException {
    Connection conn = null;
    try {
        conn = new ConnectionMysql().open();
        conn.setAutoCommit(false); // Inicia la transacción

        // Primero elimina todos los abonos asociados al préstamo
        String sqlDeleteAbonos = "DELETE FROM abonos WHERE idPrestamo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAbonos)) {
            pstmt.setInt(1, idPrestamo);
            pstmt.executeUpdate();
        }

        // Luego elimina el préstamo
        String sqlDeletePrestamo = "DELETE FROM prestamos WHERE idPrestamo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePrestamo)) {
            pstmt.setInt(1, idPrestamo);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo eliminar el préstamo, ningún registro fue afectado.");
            }
        }

        conn.commit(); // Confirma la transacción
    } catch (SQLException e) {
        if (conn != null) {
            conn.rollback(); // Revierte la transacción si algo sale mal
        }
        throw e; // Propaga el error
    } finally {
        if (conn != null) {
            conn.close(); // Cierra la conexión de manera segura
        }
    }
}



private void eliminarPrestamo(int idPrestamo, Connection conn) throws SQLException {
    // Preparamos la consulta SQL para eliminar el préstamo
    String sql = "DELETE FROM prestamos WHERE idPrestamo = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, idPrestamo);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("No se pudo eliminar el préstamo, ningún registro fue afectado.");
        }
    }
}
}