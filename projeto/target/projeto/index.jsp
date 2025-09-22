<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema de Gestão</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
          
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
        }
        .dashboard-card {
            width: 100%;
        }
    </style>
</head>
<body>

    <c:if test="${empty sessionScope.usuarioLogado}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <jsp:include page="WEB-INF/jsp/comum/menu.jsp" />
    
    <div class="container mt-5">
        <div class="card dashboard-card shadow-sm">
            <div class="card-header text-center">
                <h2>Dashboard Principal</h2>
            </div>
            <div class="card-body text-center">
                <p class="card-text">
                    Use o menu acima para gerenciar os projetos, usuários e equipes do sistema.
                </p>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
            crossorigin="anonymous"></script>

            <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>