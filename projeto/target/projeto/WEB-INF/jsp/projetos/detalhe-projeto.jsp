<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes do Projeto - <c:out value="${projeto.nome}" /></title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <c:if test="${empty sessionScope.usuarioLogado}">
        <c:redirect url="../../login.jsp"/>
    </c:if>

    <div class="container mt-5">
        <div class="card shadow-sm">
            <div class="card-header bg-dark text-white">
                <h2>Detalhes do Projeto</h2>
            </div>
            <div class="card-body">
                <h4 class="card-title"><c:out value="${projeto.nome}" /></h4>
                <p class="card-text text-muted"><c:out value="${projeto.descricao}" /></p>
                <hr>
                <dl class="row">
                    <dt class="col-sm-3">Gerente Responsável</dt>
                    <dd class="col-sm-9"><c:out value="${gerente.nomeCompleto}" /></dd>

                    <dt class="col-sm-3">Status</dt>
                    <dd class="col-sm-9"><span class="badge bg-info text-dark"><c:out value="${projeto.status}" /></span></dd>

                    <dt class="col-sm-3">Data de Início</dt>
                    <dd class="col-sm-9"><fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy"/></dd>

                    <dt class="col-sm-3">Previsão de Término</dt>
                    <dd class="col-sm-9"><fmt:formatDate value="${projeto.dataTerminoPrevista}" pattern="dd/MM/yyyy"/></dd>
                </dl>
                
                <hr class="my-4">

                <div class="row">
                    <div class="col-md-6">
                        <h4>Equipes no Projeto</h4>
                        <c:choose>
                            <c:when test="${not empty equipesNoProjeto}">
                                <ul class="list-group">
                                    <c:forEach var="equipe" items="${equipesNoProjeto}">
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            <c:out value="${equipe.nome}" />
                                            <a href="projetos?action=desalocarEquipe&projetoId=${projeto.id}&equipeId=${equipe.id}"
                                               class="btn btn-outline-danger btn-sm"
                                               onclick="return confirm('Tem certeza que deseja desalocar esta equipe?');">
                                               Desalocar
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Nenhuma equipe alocada neste projeto ainda.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="col-md-6">
                        <h4>Alocar Nova Equipe</h4>
                        <c:choose>
                            <c:when test="${not empty equipesDisponiveis}">
                                <form action="projetos" method="POST" class="p-3 border rounded">
                                    <input type="hidden" name="action" value="alocarEquipe">
                                    <input type="hidden" name="projetoId" value="${projeto.id}">
                                    
                                    <div class="mb-3">
                                        <label for="equipeId" class="form-label">Selecione uma equipe:</label>
                                        <select name="equipeId" id="equipeId" class="form-select" required>
                                            <option value="">-- Equipes Disponíveis --</option>
                                            <c:forEach var="equipe" items="${equipesDisponiveis}">
                                                <option value="${equipe.id}"><c:out value="${equipe.nome}" /></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <button type="submit" class="btn btn-primary w-100">Alocar Equipe</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Nenhuma equipe disponível para alocação.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="card-footer text-end">
                <a href="projetos?action=list" class="btn btn-secondary">Voltar para a Lista de Projetos</a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
      <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>