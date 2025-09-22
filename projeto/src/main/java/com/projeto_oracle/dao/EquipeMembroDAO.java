package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projeto_oracle.model.Usuario;

public class EquipeMembroDAO {

    /**
     * Adiciona um usuário a uma equipe.
     * @param equipeId O ID da equipe.
     * @param usuarioId O ID do usuário.
     */
    public void adicionarMembro(int equipeId, int usuarioId) {
        String sql = "INSERT INTO TBL_EQUIPE_MEMBROS (equipe_id, usuario_id) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipeId);
            pstmt.setInt(2, usuarioId);
            
            pstmt.executeUpdate();
            System.out.println("Membro adicionado à equipe com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar membro à equipe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Remove um usuário de uma equipe.
     * @param equipeId O ID da equipe.
     * @param usuarioId O ID do usuário.
     */
    public void removerMembro(int equipeId, int usuarioId) {
        String sql = "DELETE FROM TBL_EQUIPE_MEMBROS WHERE equipe_id = ? AND usuario_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipeId);
            pstmt.setInt(2, usuarioId);

            pstmt.executeUpdate();
            System.out.println("Membro removido da equipe com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao remover membro da equipe: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lista todos os membros de uma equipe específica.
     * @param equipeId O ID da equipe.
     * @return Uma lista de objetos Usuario.
     */
    public List<Usuario> listarMembros(int equipeId) {
        String sql = "SELECT u.* FROM TBL_USUARIOS u JOIN TBL_EQUIPE_MEMBROS em ON u.usuario_id = em.usuario_id WHERE em.equipe_id = ?";
        List<Usuario> membros = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setLogin(rs.getString("login"));
                    membros.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar membros da equipe: " + e.getMessage());
            e.printStackTrace();
        }
        return membros;
    }
}