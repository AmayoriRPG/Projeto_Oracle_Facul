package com.projeto_oracle.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.projeto_oracle.dao.ProjetoDAO;
import com.projeto_oracle.model.Projeto;
import com.projeto_oracle.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProjetoDAO projetoDAO;

    public void init() {
        projetoDAO = new ProjetoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("exportExcel".equals(action)) {
                gerarRelatorioExcel(request, response);
            } else {
                mostrarPaginaRelatorio(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro de banco de dados ao gerar relatório", e);
        }
    }

    private void mostrarPaginaRelatorio(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Map<String, Integer> contagemStatus = projetoDAO.getContagemStatusProjetos();

        // --- LÓGICA ATUALIZADA ---
        // Converte manualmente o Mapa para uma String JSON
        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean primeiro = true;
        for (Map.Entry<String, Integer> entry : contagemStatus.entrySet()) {
            if (!primeiro) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            primeiro = false;
        }
        jsonBuilder.append("}");
        String dadosJson = jsonBuilder.toString();

        // Em vez de enviar o Map, enviamos a String JSON pronta
        request.setAttribute("dadosJson", dadosJson);
        // -------------------------

        request.getRequestDispatcher("WEB-INF/jsp/relatorios/relatorio-projetos.jsp").forward(request, response);
    }

    private void gerarRelatorioExcel(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        List<Projeto> listaProjetos = projetoDAO.listarTodos();

        // Cria um novo Workbook (arquivo Excel)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projetos");

        // Cria o cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Nome", "Descrição", "Data de Início", "Status", "ID Gerente"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Preenche as linhas com os dados
        int rowNum = 1;
        for (Projeto projeto : listaProjetos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(projeto.getId());
            row.createCell(1).setCellValue(projeto.getNome());
            row.createCell(2).setCellValue(projeto.getDescricao());
            // Formatação de data
            Cell dataInicioCell = row.createCell(3);
            if (projeto.getDataInicio() != null) {
                dataInicioCell.setCellValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(projeto.getDataInicio()));
            }
            row.createCell(4).setCellValue(projeto.getStatus());
            row.createCell(5).setCellValue(projeto.getGerenteId());
        }

        // Configura a resposta HTTP para download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_projetos.xlsx\"");

        // Escreve o workbook na saída da resposta
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
