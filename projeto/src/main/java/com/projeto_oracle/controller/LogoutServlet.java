package com.projeto_oracle.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet que controla a finalização da sessão (logout). Mapeado para a URL
 * '/logout'.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * O método init() não é necessário para este servlet, mas é mantido para
     * seguir o mesmo molde do LoginServlet.
     */
    public void init() {
        // Nenhuma inicialização é necessária aqui para o logout.
    }

    /**
     * Lida com requisições GET, que é o método usado para finalizar a sessão.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Obtém a sessão atual. Usar 'false' evita que uma nova seja criada se
        // o usuário já tiver saído.
        HttpSession session = request.getSession(false);

        // Se a sessão existir, a invalida para remover todos os dados de usuário
        if (session != null) {
            session.invalidate();
        }

        // Redireciona o usuário para a página de login após o logout
        response.sendRedirect("login.jsp");
    }

    /**
     * Lida com requisições POST. Não é utilizado para o logout, mas é mantido
     * para seguir o mesmo molde.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}