<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Equipes</title>
    
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
        <h2 class="text-center mb-4">Gerenciamento de Equipes</h2>

        <div class="mb-3 d-flex justify-content-between align-items-center">
            <a href="equipes?action=new" class="btn btn-success">
                <i class="fas fa-plus-circle"></i> Nova Equipe
            </a>
            <a href="index.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Voltar ao Dashboard
            </a>
        </div>

        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nome da Equipe</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="equipe" items="${requestScope.listaEquipes}">
    <tr>
        <td><c:out value="${equipe.id}" /></td>
        <td>
            <%-- O link agora aponta para o Servlet, com a nova ação 'detail' e o ID da equipe --%>
            <a href="equipes?action=detail&id=<c:out value='${equipe.id}' />">
                <c:out value="${equipe.nome}" />
            </a>
        </td>
        <td>
            <a href="equipes?action=edit&id=<c:out value='${equipe.id}' />" class="btn btn-warning btn-sm">Editar</a>
            <c:if test="${sessionScope.usuarioLogado.perfil eq 'ADMINISTRADOR'}">
                <a href="equipes?action=delete&id=<c:out value='${equipe.id}' />" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja remover esta equipe?');">Remover</a>
            </c:if>
        </td>
    </tr>
</c:forEach>
                <c:if test="${empty requestScope.listaEquipes}">
                    <tr>
                        <td colspan="3" class="text-center">Nenhuma equipe encontrada.</td>
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