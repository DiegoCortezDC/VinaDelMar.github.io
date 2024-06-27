package org.utl.dsm503.bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMysql {
    Connection conn;

    public Connection open()
    {
        String user = "root";
        String password = "12345";
        String url = "jdbc:mysql://localhost:3306/VDMJR";

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (ClassNotFoundException ex)
        {
            throw new RuntimeException(ex);
        } catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void close()
    {
        if(conn!=null)
        {
            try
            {
                conn.close();
            } catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}



