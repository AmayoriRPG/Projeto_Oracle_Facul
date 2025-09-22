package com.projeto_oracle.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.projeto_oracle.dao.UsuarioDAO;
import com.projeto_oracle.model.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        try {
            switch (action) {
                case "list":
                    listUsuarios(request, response);
                    break;
                case "new":
                    if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                        showAccessDenied(request, response, "Você não tem permissão para adicionar usuários.");
                        return;
                    }
                    showNewForm(request, response);
                    break;
                case "edit":
                    if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                        showAccessDenied(request, response, "Você não tem permissão para editar usuários.");
                        return;
                    }
                    showEditForm(request, response);
                    break;
                case "delete":
                    if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                        showAccessDenied(request, response, "Você não tem permissão para excluir usuários.");
                        return;
                    }
                    deleteUsuario(request, response);
                    break;
                default:
                    listUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;

        try {
            if (id == 0) { // Criação de novo usuário
                if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                    showAccessDenied(request, response, "Você não tem permissão para adicionar usuários.");
                    return;
                }
            } else { // Edição de usuário existente
                Usuario usuarioSendoEditado = usuarioDAO.buscarPorId(id);

                // Nova verificação de segurança: GERENTE não pode editar ADMINISTRADOR
                if ("GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil()) && "ADMINISTRADOR".equalsIgnoreCase(usuarioSendoEditado.getPerfil())) {
                    // Chame o método showAccessDenied aqui
                    showAccessDenied(request, response, "Um GERENTE não pode editar um usuário administrador.");
                    return;
                }

                // Verificação de permissão geral para edição
                if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogado.getPerfil()) && !"GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil())) {
                    showAccessDenied(request, response, "Você não tem permissão para editar usuários.");
                    return;
                }
            }

            saveUsuario(request, response);

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // --- MÉTODOS DE AÇÃO ---
    private void showAccessDenied(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/usuarios/lista-usuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void listUsuarios(HttpServletRequest request, HttpServletResponse response)throws SQLException, IOException, ServletException {
        List<Usuario> listaUsuarios = usuarioDAO.listarTodos();
        request.setAttribute("listaUsuarios", listaUsuarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/usuarios/lista-usuarios.jsp");
        dispatcher.forward(request, response);
    }

   private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException, SQLException {

    request.getRequestDispatcher("WEB-INF/jsp/usuarios/form-usuario.jsp").forward(request, response);
}

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuarioExistente = usuarioDAO.buscarPorId(id);

        // Verificação antes de exibir o formulário
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if ("GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil()) && "ADMINISTRADOR".equalsIgnoreCase(usuarioExistente.getPerfil())) {
            showAccessDenied(request, response, "Você não pode editar um usuário administrador.");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/usuarios/form-usuario.jsp");
        request.setAttribute("usuario", usuarioExistente);
        dispatcher.forward(request, response);
    }

    private void deleteUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Verificação de perfil para a ação de DELETE
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuarioSendoDeletado = usuarioDAO.buscarPorId(id);

        if ("GERENTE".equalsIgnoreCase(usuarioLogado.getPerfil()) && "ADMINISTRADOR".equalsIgnoreCase(usuarioSendoDeletado.getPerfil())) {
            showAccessDenied(request, response, "Você não pode excluir um usuário administrador.");
            return;
        }

        usuarioDAO.excluir(id);
        response.sendRedirect("usuarios?action=list");
    }

    private void saveUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String idParam = request.getParameter("id");
        int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;

        String nomeCompleto = request.getParameter("nomeCompleto");
        String email = request.getParameter("email");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String perfil = request.getParameter("perfil");
        String cpf = request.getParameter("cpf");
        String cargo = request.getParameter("cargo");

        Usuario usuario = new Usuario(id, nomeCompleto, cpf, email, cargo, login, senha, perfil);

        if (id == 0) {
            usuarioDAO.cadastrar(usuario);
        } else {
            usuarioDAO.atualizar(usuario);
        }

        response.sendRedirect("usuarios?action=list");
    }
}
