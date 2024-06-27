package org.utl.dsm503.controller;

import org.utl.dsm503.bd.ConnectionMysql;
import org.utl.dsm503.model.Nomina;
import org.utl.dsm503.utl.MailSender;
import org.utl.dsm503.utl.PdfGenerator;

import java.sql.*;

public class NominaController {

    public void addAndSendNomina(Nomina nomina) throws Exception {
        try (Connection conn = new ConnectionMysql().open()) {
            String sql = "SELECT nombre, apellidoPaterno, apellidoMaterno, curp FROM empleados WHERE idEmpleado = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nomina.getIdEmpleado());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nomina.setNombre(rs.getString("nombre"));
                nomina.setApellidoPaterno(rs.getString("apellidoPaterno"));
                nomina.setApellidoMaterno(rs.getString("apellidoMaterno"));
                nomina.setCurp(rs.getString("curp"));
            }

            sql = "INSERT INTO nominas (idEmpleado, fecha, sueldoBase, imss, retardos, multas, platosRotos, otros, totalPagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, nomina.getIdEmpleado());
            pstmt.setDate(2, new java.sql.Date(nomina.getFecha().getTime()));
            pstmt.setDouble(3, nomina.getSueldoBase());
            pstmt.setDouble(4, nomina.getImss());
            pstmt.setDouble(5, nomina.getRetardos());
            pstmt.setDouble(6, nomina.getMultas());
            pstmt.setDouble(7, nomina.getPlatosRotos());
            pstmt.setDouble(8, nomina.getOtros());
            pstmt.setDouble(9, nomina.getTotalPagar());
            pstmt.executeUpdate();

            // Generar y enviar el PDF
            String pdfPath = PdfGenerator.createPdf(nomina);
            MailSender.sendEmail(nomina.getEmail(), "Viña Del Mar Jr", "Nomina De Pago Viña Del Mar Jr", pdfPath);
        }
    }
}
