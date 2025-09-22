<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Projetos</title>
    
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
        <h2 class="text-center mb-4">Gerenciamento de Projetos</h2>

        <div class="mb-3 d-flex justify-content-between align-items-center">
            <a href="projetos?action=new" class="btn btn-success">
                <i class="fas fa-plus-circle"></i> Novo Projeto
            </a>
            <a href="index.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Voltar ao Dashboard
            </a>
        </div>

        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nome do Projeto</th>
                    <th>Gerente</th>
                    <th>Data de Início</th>
                    <th>Previsão de Término</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="projeto" items="${requestScope.listaProjetos}">
                    <tr>
                        <td><c:out value="${projeto.id}" /></td>
                        <td>
                            <a href="projetos?action=detail&id=<c:out value='${projeto.id}' />">
                                <c:out value="${projeto.nome}" />
                            </a>
                        </td>
                        <td><c:out value="${projeto.gerenteId}" /></td>
                        <td><fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${projeto.dataTerminoPrevista}" pattern="dd/MM/yyyy"/></td>
                        <td><c:out value="${projeto.status}" /></td>
                        <td>
                            <a href="projetos?action=edit&id=<c:out value='${projeto.id}' />" class="btn btn-warning btn-sm">Editar</a>
                            <c:if test="${sessionScope.usuarioLogado.perfil eq 'ADMINISTRADOR'}">
                                <a href="projetos?action=delete&id=<c:out value='${projeto.id}' />" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja remover este projeto?');">Remover</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requestScope.listaProjetos}">
                    <tr>
                        <td colspan="7" class="text-center">Nenhum projeto encontrado.</td>
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