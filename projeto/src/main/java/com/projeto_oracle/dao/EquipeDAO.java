package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projeto_oracle.model.Equipe;

public class EquipeDAO {

    /**
     * Insere uma nova equipe no banco de dados.
     * @param equipe O objeto Equipe a ser inserido.
     */
    public void cadastrar(Equipe equipe) {
        String sql = "INSERT INTO TBL_EQUIPES (nome) VALUES (?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipe.getNome());
            
            pstmt.executeUpdate();
            System.out.println("Equipe cadastrada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar equipe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna uma lista com todas as equipes do banco de dados.
     * @return Uma List<Equipe>.
     */
    public List<Equipe> listarTodos() {
        String sql = "SELECT * FROM TBL_EQUIPES ORDER BY nome";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setId(rs.getInt("equipe_id"));
                equipe.setNome(rs.getString("nome"));
                equipes.add(equipe);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar equipes: " + e.getMessage());
            e.printStackTrace();
        }
        return equipes;
    }

    /**
     * Busca uma equipe no banco de dados pelo seu ID.
     * @param id O ID da equipe a ser buscada.
     * @return Um objeto Equipe, ou null se não for encontrada.
     */
    public Equipe buscarPorId(int id) {
        String sql = "SELECT * FROM TBL_EQUIPES WHERE equipe_id = ?";
        Equipe equipe = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    equipe = new Equipe();
                    equipe.setId(rs.getInt("equipe_id"));
                    equipe.setNome(rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipe por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return equipe;
    }

    /**
     * Atualiza os dados de uma equipe existente no banco de dados.
     * @param equipe O objeto Equipe com os dados atualizados.
     */
    public void atualizar(Equipe equipe) {
        String sql = "UPDATE TBL_EQUIPES SET nome = ? WHERE equipe_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipe.getNome());
            pstmt.setInt(2, equipe.getId());

            pstmt.executeUpdate();
            System.out.println("Equipe atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar equipe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exclui uma equipe do banco de dados pelo seu ID.
     * @param id O ID da equipe a ser excluída.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM TBL_EQUIPES WHERE equipe_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Equipe excluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir equipe: " + e.getMessage());
            e.printStackTrace();
        }
    }
}