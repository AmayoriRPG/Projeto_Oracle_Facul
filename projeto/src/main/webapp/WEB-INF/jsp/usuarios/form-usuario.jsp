<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Usuário</title>
    
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
                    <c:when test="${not empty requestScope.usuario}">
                        <h3>Editar Usuário</h3>
                    </c:when>
                    <c:otherwise>
                        <h3>Novo Usuário</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <form action="usuarios" method="POST">
                    
                    <c:if test="${not empty requestScope.usuario}">
                        <input type="hidden" name="id" value="${requestScope.usuario.id}" />
                    </c:if>

                    <div class="mb-3">
                        <label for="nomeCompleto" class="form-label">Nome Completo</label>
                        <input type="text" class="form-control" id="nomeCompleto" name="nomeCompleto" 
                               value="${requestScope.usuario.nomeCompleto}" required>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" 
                               value="${requestScope.usuario.email}" required>
                    </div>

                    <div class="mb-3">
                        <label for="login" class="form-label">Login</label>
                        <input type="text" class="form-control" id="login" name="login" 
                               value="${requestScope.usuario.login}" required>
                    </div>

                    <div class="mb-3">
                        <label for="senha" class="form-label">Senha</label>
                        <input type="password" class="form-control" id="senha" name="senha" 
                               placeholder="Deixe em branco para manter a senha atual na edição"
                               <c:if test="${empty requestScope.usuario}">required</c:if>>
                    </div>
                    
                    <div class="mb-3">
                        <label for="perfil" class="form-label">Perfil</label>
                        <select class="form-select" id="perfil" name="perfil" required>
                            <option value="">Selecione um perfil</option>
                            <option value="ADMINISTRADOR" 
                                <c:if test="${requestScope.usuario.perfil eq 'ADMINISTRADOR'}">selected</c:if>>ADMINISTRADOR</option>
                            <option value="GESTOR" 
                                <c:if test="${requestScope.usuario.perfil eq 'GESTOR'}">selected</c:if>>GESTOR</option>
                            <option value="MEMBRO" 
                                <c:if test="${requestScope.usuario.perfil eq 'MEMBRO'}">selected</c:if>>MEMBRO</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="cpf" class="form-label">CPF</label>
                        <input type="text" class="form-control" id="cpf" name="cpf" 
                               value="${requestScope.usuario.cpf}">
                    </div>
                    
                    <div class="mb-3">
                        <label for="cargo" class="form-label">Cargo</label>
                        <input type="text" class="form-control" id="cargo" name="cargo" 
                               value="${requestScope.usuario.cargo}">
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                        <a href="usuarios" class="btn btn-secondary me-md-2">Cancelar</a>
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