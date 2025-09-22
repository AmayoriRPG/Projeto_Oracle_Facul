<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Usuários</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
</head>
<body>

    <c:if test="${empty sessionScope.usuarioLogado}">
        <c:redirect url="../../login.jsp"/>
    </c:if>

    <jsp:include page="../comum/menu.jsp" />

    <div class="container mt-5">
        <h2 class="text-center mb-4">Gerenciamento de Usuários</h2>

        <div class="mb-3 d-flex justify-content-between align-items-center">
            <c:if test="${sessionScope.usuarioLogado.perfil eq 'ADMINISTRADOR'}">
                <a href="usuarios?action=new" class="btn btn-success">
                    <i class="fas fa-plus-circle"></i> Novo Usuário
                </a>
            </c:if>
        </div>

        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nome Completo</th>
                    <th>Email</th>
                    <th>Login</th>
                    <th>Perfil</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="usuario" items="${requestScope.listaUsuarios}">
                    <tr>
                        <td><c:out value="${usuario.id}" /></td>
                        <td><c:out value="${usuario.nomeCompleto}" /></td>
                        <td><c:out value="${usuario.email}" /></td>
                        <td><c:out value="${usuario.login}" /></td>
                        <td><c:out value="${usuario.perfil}" /></td>
                        <td>
                            <c:if test="${(sessionScope.usuarioLogado.perfil eq 'ADMINISTRADOR') or (sessionScope.usuarioLogado.perfil eq 'GERENTE' and usuario.perfil ne 'ADMINISTRADOR')}">
                                <a href="usuarios?action=edit&id=<c:out value='${usuario.id}' />" class="btn btn-warning btn-sm">Editar</a>
                            </c:if>
                            <c:if test="${sessionScope.usuarioLogado.perfil eq 'ADMINISTRADOR'}">
                                <a href="usuarios?action=delete&id=<c:out value='${usuario.id}' />" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja remover este usuário?');">Remover</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requestScope.listaUsuarios}">
                    <tr>
                        <td colspan="6" class="text-center">Nenhum usuário encontrado.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
            crossorigin="anonymous"></script>

          <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>