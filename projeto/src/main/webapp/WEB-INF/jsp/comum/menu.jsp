<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.jsp">Gestão de Projetos</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="projetos?action=list">Projetos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="usuarios?action=list">Usuários</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="equipes?action=list">Equipes</a>
                </li>
                <li class="nav-item">
    <a class="nav-link" href="${pageContext.request.contextPath}/relatorios">Relatórios</a>
</li>
            </ul>
            <div class="d-flex">
                <span class="navbar-text text-white me-3">
                    Bem-vindo, <c:out value="${sessionScope.usuarioLogado.nomeCompleto}"/>!
                </span>
                <a href="logout" class="btn btn-outline-danger">Sair</a>
            </div>
        </div>
    </div>
</nav>