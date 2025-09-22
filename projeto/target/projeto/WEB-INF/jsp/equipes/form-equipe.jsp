<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Equipe</title>
    
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
        <div class="card shadow-sm">
            <div class="card-header text-center bg-dark text-white">
                <c:choose>
                    <c:when test="${not empty requestScope.equipe}">
                        <h3>Editar Equipe</h3>
                    </c:when>
                    <c:otherwise>
                        <h3>Nova Equipe</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <form action="equipes" method="POST">
                    
                    <%-- Campo escondido para o ID. Usado apenas na edição. --%>
                    <c:if test="${not empty requestScope.equipe}">
                        <input type="hidden" name="id" value="${requestScope.equipe.id}" />
                    </c:if>

                    <div class="mb-3">
                        <label for="nome" class="form-label">Nome da Equipe</label>
                        <input type="text" class="form-control" id="nome" name="nome" 
                               value="${requestScope.equipe.nome}" required>
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                        <a href="equipes" class="btn btn-secondary me-md-2">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Salvar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
            crossorigin="anonymous"></script>
           <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>