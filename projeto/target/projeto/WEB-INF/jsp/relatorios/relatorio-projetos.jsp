<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Relatório de Projetos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <c:if test="${empty sessionScope.usuarioLogado}">
        <c:redirect url="../../login.jsp" />
    </c:if>

    <jsp:include page="../comum/menu.jsp" />

    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Relatório de Status dos Projetos</h2>
            <a href="relatorios?action=exportExcel" class="btn btn-success">
                Exportar para Excel
            </a>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <div style="width: 75%; margin: auto;">
                    <canvas id="graficoStatusProjetos" data-dados='<c:out value="${dadosJson}" />'></canvas>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Pega o elemento canvas
        const canvas = document.getElementById('graficoStatusProjetos');
        
        // Lê a string JSON do atributo data-dados e a converte em um objeto JavaScript real
        const dadosDoJava = JSON.parse(canvas.dataset.dados || '{}');

        const labels = Object.keys(dadosDoJava);
        const data = Object.values(dadosDoJava);
        
        // Configuração do Gráfico
        const config = {
            type: 'pie', // Tipo do gráfico: 'pie' (pizza) ou 'doughnut' (rosquinha)
            data: {
                labels: labels,
                datasets: [{
                    label: 'Nº de Projetos',
                    data: data,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.7)',  // Vermelho
                        'rgba(54, 162, 235, 0.7)', // Azul
                        'rgba(255, 206, 86, 0.7)', // Amarelo
                        'rgba(75, 192, 192, 0.7)', // Verde
                        'rgba(153, 102, 255, 0.7)',// Roxo
                        'rgba(255, 159, 64, 0.7)'  // Laranja
                    ],
                    borderColor: 'rgba(255, 255, 255, 1)',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Distribuição de Projetos por Status'
                    }
                }
            },
        };

        // Renderiza o gráfico no canvas
        new Chart(canvas, config);
    </script>

      <jsp:include page="/WEB-INF/jsp/comum/footer.jsp" />
</body>
</html>