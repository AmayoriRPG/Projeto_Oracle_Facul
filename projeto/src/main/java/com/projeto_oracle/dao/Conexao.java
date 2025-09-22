package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    public static Connection getConexao() throws SQLException {
    
        try {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String URL = "jdbc:oracle:thin:@//localhost:1521/freepdb1";
        String USUARIO = "PROJETO";
        String SENHA = "a1234";    
   
            return DriverManager.getConnection(URL, USUARIO, SENHA);

    } catch (ClassNotFoundException e) {
        // Transforma ClassNotFoundException em uma SQLException para simplificar o tratamento de erro
        throw new SQLException("Driver Oracle JDBC não encontrado.", e);
    }
    }


    public static void testarConexao() {
 
        try (Connection conn = getConexao()) {
            if (conn != null) {
                System.out.println("✅ Conexão com o banco de dados Oracle estabelecida com sucesso!");
                System.out.println("URL: " + conn.getMetaData().getURL());
            } else {
                System.err.println("❌ Falha ao obter a conexão. O objeto de conexão é nulo.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Falha ao conectar com o banco de dados Oracle.");

            e.printStackTrace();
        }
    }
}