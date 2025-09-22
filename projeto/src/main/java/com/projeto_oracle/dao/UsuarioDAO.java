package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projeto_oracle.model.Usuario;

public class UsuarioDAO {

    /**
     * Valida as credenciais de um usuário no banco de dados.
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return Um objeto Usuario se as credenciais forem válidas, ou null caso contrário.
     */
    public Usuario validarLogin(String login, String senha) {
        // TODO: A comparação de senha em texto plano é insegura.
        // Em um projeto real, armazene um hash da senha e compare o hash.
        String sql = "SELECT * FROM TBL_USUARIOS WHERE login = ? AND senha = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, senha);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCargo(rs.getString("cargo"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setPerfil(rs.getString("perfil"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar login do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario O objeto Usuario a ser inserido.
     */
    public void cadastrar(Usuario usuario) {
        String sql = "INSERT INTO TBL_USUARIOS (nome_completo, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getCargo());
            pstmt.setString(5, usuario.getLogin());
            pstmt.setString(6, usuario.getSenha()); // Lembre-se de usar hash
            pstmt.setString(7, usuario.getPerfil());
            
            pstmt.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna uma lista com todos os usuários do banco de dados.
     * @return Uma List<Usuario>.
     */
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM TBL_USUARIOS ORDER BY nome_completo";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usuario_id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuario.setLogin(rs.getString("login"));
                usuario.setPerfil(rs.getString("perfil"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }
        public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM TBL_USUARIOS WHERE usuario_id = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCargo(rs.getString("cargo"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setPerfil(rs.getString("perfil"));
                    // Não populamos a senha por segurança
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Atualiza os dados de um usuário existente no banco de dados.
     * @param usuario O objeto Usuario com os dados atualizados.
     */
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE TBL_USUARIOS SET nome_completo = ?, cpf = ?, email = ?, cargo = ?, login = ?, senha = ?, perfil = ? WHERE usuario_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getCargo());
            pstmt.setString(5, usuario.getLogin());
            pstmt.setString(6, usuario.getSenha()); // Lembre-se de usar hash
            pstmt.setString(7, usuario.getPerfil());
            pstmt.setInt(8, usuario.getId()); // ID para a cláusula WHERE
            
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exclui um usuário do banco de dados pelo seu ID.
     * @param id O ID do usuário a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM TBL_USUARIOS WHERE usuario_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Usuário excluído com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

      public List<Usuario> listarGerentes() throws SQLException {
        List<Usuario> gerentes = new ArrayList<>();
        String sql = "SELECT * FROM TBL_USUARIOS WHERE perfil = 'GERENTE' OR perfil = 'ADMINISTRADOR' ORDER BY nome_completo";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usuario_id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setPerfil(rs.getString("perfil"));
                gerentes.add(usuario);
            }
        }
        return gerentes;
    }
}