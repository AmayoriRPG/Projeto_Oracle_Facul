// Corrected EquipeServlet.java
package com.projeto_oracle.controller;

import java.io.IOException;
import java.util.List;

import com.projeto_oracle.dao.EquipeDAO;
import com.projeto_oracle.dao.EquipeMembroDAO;
import com.projeto_oracle.dao.ProjetoEquipeDAO;
import com.projeto_oracle.dao.UsuarioDAO;
import com.projeto_oracle.model.Equipe;
import com.projeto_oracle.model.Projeto;
import com.projeto_oracle.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/equipes")
public class EquipeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EquipeDAO equipeDAO;
    private ProjetoEquipeDAO projetoEquipeDAO;
    private EquipeMembroDAO equipeMembroDAO;
    private UsuarioDAO usuarioDAO;

    public void init() {
        equipeDAO = new EquipeDAO();
        projetoEquipeDAO = new ProjetoEquipeDAO();
        equipeMembroDAO = new EquipeMembroDAO();
        usuarioDAO = new UsuarioDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Permissões para CRUD de equipes (apenas ADMINISTRADOR e GERENTE)
        if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
            showAccessDenied(request, response, "Você não tem permissão para gerenciar equipes.");
            return;
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "delete":
                if ("ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                    deleteEquipe(request, response);
                } else {
                    showAccessDenied(request, response, "Você não tem permissão para excluir uma equipe.");
                }
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "detail":
                showDetalheEquipe(request, response);
                break;
            case "list":
            default:
                listEquipes(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Ações de POST também requerem permissão
        if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
            showAccessDenied(request, response, "Você não tem permissão para salvar equipes.");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("addMembro".equals(action)) {
                addMembro(request, response);
            } else if ("removeMembro".equals(action)) {
                removeMembro(request, response);
            } else {
                // Este método único lida com a lógica de salvar ou atualizar a equipe
                saveEquipe(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    // --- Métodos de Ação ---
    private void listEquipes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipe> listaEquipes = equipeDAO.listarTodos();
        request.setAttribute("listaEquipes", listaEquipes);
        request.getRequestDispatcher("WEB-INF/jsp/equipes/lista-equipes.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/equipes/form-equipe.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Equipe equipeExistente = equipeDAO.buscarPorId(id);
        request.setAttribute("equipe", equipeExistente);
        request.getRequestDispatcher("WEB-INF/jsp/equipes/form-equipe.jsp").forward(request, response);
    }

    private void deleteEquipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        equipeDAO.excluir(id);
        response.sendRedirect("equipes?action=list");
    }

    // Este é o método saveEquipe, que substitui 'insertEquipe' e 'updateEquipe'
    private void saveEquipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;
        String nome = request.getParameter("nome");

        Equipe equipe = new Equipe();
        equipe.setNome(nome);

        if (id == 0) {
            equipeDAO.cadastrar(equipe);
        } else {
            equipe.setId(id);
            equipeDAO.atualizar(equipe);
        }
        response.sendRedirect("equipes?action=list");
    }

    private void showAccessDenied(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void showDetalheEquipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Equipe equipe = equipeDAO.buscarPorId(id);

            if (equipe == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Equipe não encontrada.");
                return;
            }

            // Buscar a lista de membros da equipe (lógica que você já tem)
            List<Usuario> membros = equipeMembroDAO.listarMembros(id);

            // Obter a lista de todos os usuários disponíveis (lógica que você já tem)
            List<Usuario> todosUsuarios = usuarioDAO.listarTodos();

            // --- NOVA LÓGICA ADICIONADA ---
            // Buscar a lista de projetos em que esta equipe está alocada
            List<Projeto> projetosDaEquipe = projetoEquipeDAO.listarProjetosPorEquipe(id);
            // -----------------------------

            request.setAttribute("equipe", equipe);
            request.setAttribute("membros", membros);
            request.setAttribute("todosUsuarios", todosUsuarios);
            request.setAttribute("projetosDaEquipe", projetosDaEquipe); // NOVO ATRIBUTO ENVIADO PARA A JSP

            request.getRequestDispatcher("WEB-INF/jsp/equipes/detalhe-equipe.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addMembro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int equipeId = Integer.parseInt(request.getParameter("equipeId"));
        int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));

        equipeMembroDAO.adicionarMembro(equipeId, usuarioId);

        response.sendRedirect("equipes?action=detail&id=" + equipeId);
    }

    private void removeMembro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int equipeId = Integer.parseInt(request.getParameter("equipeId"));
        int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));

        equipeMembroDAO.removerMembro(equipeId, usuarioId);

        response.sendRedirect("equipes?action=detail&id=" + equipeId);
    }
}
