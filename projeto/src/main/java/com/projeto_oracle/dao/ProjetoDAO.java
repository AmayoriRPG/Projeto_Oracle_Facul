package com.projeto_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.projeto_oracle.model.Projeto;

public class ProjetoDAO {

    /**
     * Insere um novo projeto no banco de dados.
     *
     * @param projeto O objeto Projeto a ser inserido.
     */
    public void cadastrar(Projeto projeto) {
        String sql = "INSERT INTO TBL_PROJETOS (nome, descricao, data_inicio, data_termino_prevista, status, gerente_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setDate(3, new java.sql.Date(projeto.getDataInicio().getTime()));
            pstmt.setDate(4, new java.sql.Date(projeto.getDataTerminoPrevista().getTime()));
            pstmt.setString(5, projeto.getStatus());
            pstmt.setInt(6, projeto.getGerenteId());

            pstmt.executeUpdate();
            System.out.println("Projeto cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna uma lista com todos os projetos do banco de dados.
     *
     * @return Uma List<Projeto>.
     */
    public List<Projeto> listarTodos() {
        String sql = "SELECT * FROM TBL_PROJETOS ORDER BY nome";
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("projeto_id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDescricao(rs.getString("descricao"));
                projeto.setDataInicio(rs.getDate("data_inicio"));
                projeto.setDataTerminoPrevista(rs.getDate("data_termino_prevista"));
                projeto.setStatus(rs.getString("status"));
                projeto.setGerenteId(rs.getInt("gerente_id"));
                projetos.add(projeto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar projetos: " + e.getMessage());
            e.printStackTrace();
        }
        return projetos;
    }

    /**
     * Busca um projeto no banco de dados pelo seu ID.
     *
     * @param id O ID do projeto a ser buscado.
     * @return Um objeto Projeto, ou null se não for encontrado.
     */
    public Projeto buscarPorId(int id) {
        String sql = "SELECT * FROM TBL_PROJETOS WHERE projeto_id = ?";
        Projeto projeto = null;

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    projeto = new Projeto();
                    projeto.setId(rs.getInt("projeto_id"));
                    projeto.setNome(rs.getString("nome"));
                    projeto.setDescricao(rs.getString("descricao"));
                    projeto.setDataInicio(rs.getDate("data_inicio"));
                    projeto.setDataTerminoPrevista(rs.getDate("data_termino_prevista"));
                    projeto.setStatus(rs.getString("status"));
                    projeto.setGerenteId(rs.getInt("gerente_id"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar projeto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return projeto;
    }

    /**
     * Atualiza os dados de um projeto existente no banco de dados.
     *
     * @param projeto O objeto Projeto com os dados atualizados.
     */
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE TBL_PROJETOS SET nome = ?, descricao = ?, data_inicio = ?, data_termino_prevista = ?, status = ?, gerente_id = ? WHERE projeto_id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setDate(3, new java.sql.Date(projeto.getDataInicio().getTime()));
            pstmt.setDate(4, new java.sql.Date(projeto.getDataTerminoPrevista().getTime()));
            pstmt.setString(5, projeto.getStatus());
            pstmt.setInt(6, projeto.getGerenteId());
            pstmt.setInt(7, projeto.getId());

            pstmt.executeUpdate();
            System.out.println("Projeto atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exclui um projeto do banco de dados pelo seu ID.
     *
     * @param id O ID do projeto a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM TBL_PROJETOS WHERE projeto_id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Projeto excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getContagemStatusProjetos() throws SQLException {
        String sql = "SELECT status, COUNT(*) as total FROM TBL_PROJETOS GROUP BY status";
        Map<String, Integer> contagem = new HashMap<>();

        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                int total = rs.getInt("total");
                contagem.put(status, total);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter contagem de status dos projetos: " + e.getMessage());
            e.printStackTrace();
            throw e; // Relança a exceção para o servlet tratar
        }
        return contagem;
    }
}
