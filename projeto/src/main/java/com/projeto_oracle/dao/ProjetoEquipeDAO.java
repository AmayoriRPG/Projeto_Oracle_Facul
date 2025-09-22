package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projeto_oracle.model.Equipe;
import com.projeto_oracle.model.Projeto;

public class ProjetoEquipeDAO {

    /**
     * Aloca uma equipe a um projeto específico.
     * @param projetoId O ID do projeto.
     * @param equipeId O ID da equipe.
     */
    public void alocarEquipeEmProjeto(int projetoId, int equipeId) {
        String sql = "INSERT INTO TBL_PROJETO_EQUIPES (projeto_id, equipe_id) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projetoId);
            pstmt.setInt(2, equipeId);
            pstmt.executeUpdate();
            System.out.println("Equipe alocada ao projeto com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao alocar equipe ao projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Desaloca uma equipe de um projeto específico.
     * @param projetoId O ID do projeto.
     * @param equipeId O ID da equipe.
     */
    public void desalocarEquipeDeProjeto(int projetoId, int equipeId) {
        String sql = "DELETE FROM TBL_PROJETO_EQUIPES WHERE projeto_id = ? AND equipe_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projetoId);
            pstmt.setInt(2, equipeId);
            pstmt.executeUpdate();
            System.out.println("Equipe desalocada do projeto com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao desalocar equipe do projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lista todas as equipes alocadas em um projeto.
     * @param projetoId O ID do projeto.
     * @return Uma lista de objetos Equipe.
     */
    public List<Equipe> listarEquipesPorProjeto(int projetoId) {
        String sql = "SELECT e.* FROM TBL_EQUIPES e " +
                     "JOIN TBL_PROJETO_EQUIPES pe ON e.equipe_id = pe.equipe_id " +
                     "WHERE pe.projeto_id = ?";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projetoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Equipe equipe = new Equipe();
                    equipe.setId(rs.getInt("equipe_id"));
                    equipe.setNome(rs.getString("nome"));
                    equipe.setDescricao(rs.getString("descricao"));
                    equipes.add(equipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar equipes por projeto: " + e.getMessage());
            e.printStackTrace();
        }
        return equipes;
    }

    public List<Projeto> listarProjetosPorEquipe(int equipeId) {
    String sql = "SELECT p.* FROM TBL_PROJETOS p " +
                 "JOIN TBL_PROJETO_EQUIPES pe ON p.projeto_id = pe.projeto_id " +
                 "WHERE pe.equipe_id = ?";
    List<Projeto> projetos = new ArrayList<>();

    try (Connection conn = Conexao.getConexao();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, equipeId);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("projeto_id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setStatus(rs.getString("status"));
                // Preencha outros campos se necessário para a exibição
                projetos.add(projeto);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar projetos da equipe: " + e.getMessage());
        e.printStackTrace();
    }
    return projetos;
}
}