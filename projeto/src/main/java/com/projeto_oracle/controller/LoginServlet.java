package com.projeto_oracle.controller;

import java.io.IOException;

import com.projeto_oracle.dao.UsuarioDAO;
import com.projeto_oracle.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet que controla o fluxo de autenticação (login). Mapeado para a URL
 * '/login'.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Instância do DAO para acessar o banco de dados
    private UsuarioDAO usuarioDAO;

    // O método init() é chamado pelo container uma vez quando o servlet é criado.
    // É um bom lugar para inicializar objetos como o DAO.
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Lida com requisições GET. Geralmente, quando o usuário acessa a URL
     * '/login' diretamente. Apenas exibe a página de login.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Apenas encaminha para a página JSP de login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Lida com requisições POST, que é o método usado pelo nosso formulário de
     * login.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // 1. Obter os parâmetros do formulário
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");

            // 2. Chamar o DAO para validar as credenciais
            Usuario usuario = usuarioDAO.validarLogin(login, senha);

            // 3. Verificar o resultado da validação
            if (usuario != null) {
                // SUCESSO: O usuário é válido

                // Criar uma nova sessão HTTP para o usuário
                HttpSession session = request.getSession();

                // Armazenar o objeto 'usuario' na sessão para uso futuro (ex: exibir nome no topo da página)
                session.setAttribute("usuarioLogado", usuario);

                // Redirecionar o usuário para a página principal do sistema (dashboard)
                // Usamos sendRedirect para mudar a URL no navegador do usuário.
                response.sendRedirect("index.jsp");

                

            } else {
                // FALHA: Credenciais inválidas

                // Adicionar uma mensagem de erro na requisição
                request.setAttribute("errorMessage", "Usuário ou senha inválidos. Tente novamente.");

                // Encaminhar de volta para a página de login para exibir o erro
                // Usamos forward para manter a mesma requisição e exibir a mensagem de erro.
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

    

        } catch (Exception e) {
            // Tratamento de erro genérico
            e.printStackTrace(); // Logar o erro no console do servidor
            request.setAttribute("errorMessage", "Ocorreu um erro inesperado: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
