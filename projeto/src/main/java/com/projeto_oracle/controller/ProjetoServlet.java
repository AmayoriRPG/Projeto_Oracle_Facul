package com.projeto_oracle.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.projeto_oracle.dao.EquipeDAO;
import com.projeto_oracle.dao.ProjetoDAO;
import com.projeto_oracle.dao.ProjetoEquipeDAO; // NOVO DAO
import com.projeto_oracle.dao.UsuarioDAO;
import com.projeto_oracle.model.Equipe;
import com.projeto_oracle.model.Projeto;
import com.projeto_oracle.model.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/projetos")
public class ProjetoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // --- DAOs ---
    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;
    private ProjetoEquipeDAO projetoEquipeDAO; // NOVO DAO
    private EquipeDAO equipeDAO;             // NOVO DAO

    public void init() {
        projetoDAO = new ProjetoDAO();
        usuarioDAO = new UsuarioDAO();
        projetoEquipeDAO = new ProjetoEquipeDAO(); // INICIALIZA O NOVO DAO
        equipeDAO = new EquipeDAO();             // INICIALIZA O NOVO DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (lógica de verificação de login e permissão continua a mesma) ...
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
            return;
        }


        try {
            switch (action) {
                case "list":
                    listProjetos(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProjeto(request, response);
                    break;
                // --- NOVAS AÇÕES ---
                case "detail":
                    showDetalhe(request, response);
                    break;
                case "desalocarEquipe":
                    desalocarEquipe(request, response);
                    break;
                default:
                    listProjetos(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (lógica de verificação de login e permissão continua a mesma) ...
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp"); return;
        }
        if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado."); return;
        }

        // --- LÓGICA DE AÇÃO ATUALIZADA PARA POST ---
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("projetos"); // Redireciona para a lista se nenhuma ação for especificada
            return;
        }

        try {
            switch (action) {
                case "save":
                    saveProjeto(request, response);
                    break;
                case "alocarEquipe":
                    alocarEquipe(request, response);
                    break;
                default:
                    response.sendRedirect("projetos");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // --- MÉTODOS DE AÇÃO (EXISTENTES E NOVOS) ---

    // O método de detalhe foi ATUALIZADO para carregar as equipes
    private void showDetalhe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Projeto projeto = projetoDAO.buscarPorId(id);

        if (projeto == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Projeto não encontrado.");
            return;
        }
        
        Usuario gerente = usuarioDAO.buscarPorId(projeto.getGerenteId());
        
        // --- LÓGICA NOVA: BUSCAR EQUIPES ---
        List<Equipe> equipesNoProjeto = projetoEquipeDAO.listarEquipesPorProjeto(id);
        List<Equipe> todasAsEquipes = equipeDAO.listarTodos();

        // Filtra para mostrar apenas as equipes que ainda NÃO estão no projeto
        List<Integer> idsEquipesNoProjeto = equipesNoProjeto.stream().map(Equipe::getId).collect(Collectors.toList());
        List<Equipe> equipesDisponiveis = todasAsEquipes.stream()
                .filter(e -> !idsEquipesNoProjeto.contains(e.getId()))
                .collect(Collectors.toList());

        request.setAttribute("projeto", projeto);
        request.setAttribute("gerente", gerente);
        request.setAttribute("equipesNoProjeto", equipesNoProjeto);         // Envia a lista de equipes já alocadas
        request.setAttribute("equipesDisponiveis", equipesDisponiveis);   // Envia a lista de equipes que podem ser alocadas

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/projetos/detalhe-projeto.jsp");
        dispatcher.forward(request, response);
    }
    
    // --- NOVO MÉTODO PARA ALOCAR EQUIPE (CHAMADO VIA POST) ---
    private void alocarEquipe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int projetoId = Integer.parseInt(request.getParameter("projetoId"));
        int equipeId = Integer.parseInt(request.getParameter("equipeId"));

        projetoEquipeDAO.alocarEquipeEmProjeto(projetoId, equipeId);

        // Redireciona de volta para a mesma página de detalhes
        response.sendRedirect("projetos?action=detail&id=" + projetoId);
    }
    
    // --- NOVO MÉTODO PARA DESALOCAR EQUIPE (CHAMADO VIA GET) ---
    private void desalocarEquipe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int projetoId = Integer.parseInt(request.getParameter("projetoId"));
        int equipeId = Integer.parseInt(request.getParameter("equipeId"));

        projetoEquipeDAO.desalocarEquipeDeProjeto(projetoId, equipeId);
        
        // Redireciona de volta para a mesma página de detalhes
        response.sendRedirect("projetos?action=detail&id=" + projetoId);
    }

    // Método saveProjeto ATUALIZADO para ser chamado com ?action=save
    private void saveProjeto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String idParam = request.getParameter("id");
        int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;
        
        // ... (o resto do método saveProjeto continua exatamente igual)
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String dataInicioStr = request.getParameter("dataInicio");
        String dataTerminoStr = request.getParameter("dataTerminoPrevista");
        String status = request.getParameter("status");
        int gerenteId = Integer.parseInt(request.getParameter("gerenteId"));

        Date dataInicio = null;
        Date dataTermino = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = formatter.parse(dataInicioStr);
            }
            if (dataTerminoStr != null && !dataTerminoStr.isEmpty()) {
                dataTermino = formatter.parse(dataTerminoStr);
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao converter datas", e);
        }

        Projeto projeto = new Projeto(id, nome, descricao, dataInicio, dataTermino, status, gerenteId);

        if (id == 0) {
            projetoDAO.cadastrar(projeto);
        } else {
            projetoDAO.atualizar(projeto);
        }

        response.sendRedirect("projetos?action=list");
    }

    // ... (os outros métodos como listProjetos, showNewForm, showEditForm, deleteProjeto continuam os mesmos)
    private void listProjetos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        List<Projeto> listaProjetos = projetoDAO.listarTodos();
        request.setAttribute("listaProjetos", listaProjetos);

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/projetos/lista-projetos.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {

        List<Usuario> gerentes = usuarioDAO.listarGerentes();
        request.setAttribute("gerentes", gerentes);
        
        request.getRequestDispatcher("WEB-INF/jsp/projetos/form-projeto.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Projeto projetoExistente = projetoDAO.buscarPorId(id);

        List<Usuario> gerentes = usuarioDAO.listarGerentes();
        request.setAttribute("gerentes", gerentes);

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/projetos/form-projeto.jsp");
        request.setAttribute("projeto", projetoExistente);
        dispatcher.forward(request, response);
    }

    private void deleteProjeto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        projetoDAO.excluir(id);

        response.sendRedirect("projetos?action=list");
    }
}