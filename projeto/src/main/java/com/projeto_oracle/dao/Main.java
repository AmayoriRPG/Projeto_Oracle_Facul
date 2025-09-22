package com.projeto_oracle.dao;

import java.sql.Connection;

public class Main {

  public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão direta...");
        try {
            // Tenta obter a conexão usando EXATAMENTE o mesmo método da sua aplicação
            Connection conn = Conexao.getConexao();

            if (conn != null) {
                System.out.println("✅ SUCESSO! A conexão foi estabelecida.");
                System.out.println("URL: " + conn.getMetaData().getURL());
                conn.close();
                System.out.println("Conexão fechada.");
            } else {
                // Este 'else' não deveria acontecer com o código atual de Conexao.java
                System.err.println("❌ FALHA! O método getConexao() retornou nulo.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALHA! Ocorreu uma exceção ao tentar conectar:");
            // Imprime o erro completo (provavelmente o ORA-01017)
            e.printStackTrace();
        }
    }
}