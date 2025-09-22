<%@ page language="java" contentType="text-html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes da Equipe: <c:out value="${equipe.nome}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <c:if test="${empty sessionScope.usuarioLogado}">
        <c:redirect url="../../login.jsp"/>
    </c:if>

    <jsp:include page="../comum/menu.jsp" />

    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Detalhes da Equipe: <c:out value="${equipe.nome}" /></h2>
            <a href="equipes" class="btn btn-secondary">Voltar para a lista</a>
        </div>

        <div class="card shadow-sm mb-4">
            <div class="card-header bg-dark text-white">
                <h5 class="mb-0">Projetos Atendidos</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty projetosDaEquipe}">
                        <ul class="list-group">
                            <c:forEach var="projeto" items="${projetosDaEquipe}">
                                <li class="list-group-item">
                                    <a href="projetos?action=detail&id=${projeto.id}">
                                        <c:out value="${projeto.nome}" />
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted mb-0">Esta equipe não está alocada em nenhum projeto no momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Membros Atuais</h5>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nome</th>
                                    <th>Ação</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="membro" items="${membros}">
                                    <tr>
                                        <td><c:out value="${membro.id}" /></td>
                                        <td><c:out value="${membro.nomeCompleto}" /></td>
                                        <td>
                                            <form action="equipes" method="POST" style="display:inline;">
                                                <input type="hidden" name="action" value="removeMembro" />
                                                <input type="hidden" name="equipeId" value="${equipe.id}" />
                                                <input type="hidden" name="usuarioId" value="${membro.id}" />
                                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Remover este membro?');">Remover</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty membros}">
                                    <tr>
                                        <td colspan="3" class="text-center">Nenhum membro nesta equipe.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0">Adicionar Membro</h5>
                    </div>
                    <div class="card-body">
                        <form action="equipes" method="POST">
                            <input type="hidden" name="action" value="addMembro" />
                            <input type="hidden" name="equipeId" value="${equipe.id}" />
                            
                            <div class="mb-3">
                                <label for="usuarioId" class="form-label">Selecionar Usuário</label>
                                <select class="form-select" id="usuarioId" name="usuarioId" required>
                                    <option value="">Selecione um usuário...</option>
                                    <c:forEach var="usuario" items="${todosUsuarios}">
                                        <option value="${usuario.id}">
                                            <c:out value="${usuario.nomeCompleto}"/> (<c:out value="${usuario.login}"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-success">Adicionar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

      <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>