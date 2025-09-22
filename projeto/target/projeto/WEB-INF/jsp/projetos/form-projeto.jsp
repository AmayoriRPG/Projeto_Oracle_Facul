<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Projeto</title>
    
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
                    <c:when test="${not empty requestScope.projeto}">
                        <h3>Editar Projeto</h3>
                    </c:when>
                    <c:otherwise>
                        <h3>Novo Projeto</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <form action="projetos" method="POST">
                    
                    <c:if test="${not empty requestScope.projeto}">
                        <input type="hidden" name="id" value="${requestScope.projeto.id}" />
                    </c:if>

                    <div class="mb-3">
                        <label for="nome" class="form-label">Nome do Projeto</label>
                        <input type="text" class="form-control" id="nome" name="nome" 
                               value="${requestScope.projeto.nome}" required>
                    </div>

                    <div class="mb-3">
                        <label for="descricao" class="form-label">Descrição</label>
                        <textarea class="form-control" id="descricao" name="descricao" rows="3">${requestScope.projeto.descricao}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="dataInicio" class="form-label">Data de Início</label>
                        <input type="date" class="form-control" id="dataInicio" name="dataInicio" 
                               value="<fmt:formatDate value="${requestScope.projeto.dataInicio}" pattern="yyyy-MM-dd"/>" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="dataTerminoPrevista" class="form-label">Previsão de Término</label>
                        <input type="date" class="form-control" id="dataTerminoPrevista" name="dataTerminoPrevista" 
                               value="<fmt:formatDate value="${requestScope.projeto.dataTerminoPrevista}" pattern="yyyy-MM-dd"/>">
                    </div>
                    
                    <div class="mb-3">
                        <label for="status" class="form-label">Status</label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="PLANEJADO" <c:if test="${requestScope.projeto.status eq 'PLANEJADO'}">selected</c:if>>Planejado</option>
                            <option value="EM_ANDAMENTO" <c:if test="${requestScope.projeto.status eq 'EM_ANDAMENTO'}">selected</c:if>>Em Andamento</option>
                            <option value="CONCLUIDO" <c:if test="${requestScope.projeto.status eq 'CONCLUIDO'}">selected</c:if>>Concluído</option>
                            <option value="CANCELADO" <c:if test="${requestScope.projeto.status eq 'CANCELADO'}">selected</c:if>>Cancelado</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="gerenteId" class="form-label">Gerente</label>
                        <select class="form-select" id="gerenteId" name="gerenteId" required>
                            <option value="">Selecione um gerente</option>
                            <c:forEach var="gerente" items="${requestScope.gerentes}">
                                <option value="<c:out value="${gerente.id}"/>" 
                                    <c:if test="${requestScope.projeto.gerenteId eq gerente.id}">selected</c:if>>
                                    <c:out value="${gerente.nomeCompleto}"/> (<c:out value="${gerente.perfil}"/>)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                        <a href="projetos" class="btn btn-secondary me-md-2">Cancelar</a>
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