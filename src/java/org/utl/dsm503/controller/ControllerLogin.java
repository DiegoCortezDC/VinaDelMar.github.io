package org.utl.dsm503.controller;

import org.utl.dsm503.bd.ConnectionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerLogin {

    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM usuario WHERE nombreUsuario = ? AND contrasena = ?";
        try (Connection conn = new ConnectionMysql().open();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Retorna true si hay un match
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;  // En caso de error, retorna false
        }
    }
}
