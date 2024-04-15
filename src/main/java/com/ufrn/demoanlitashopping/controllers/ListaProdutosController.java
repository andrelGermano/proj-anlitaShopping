package com.ufrn.demoanlitashopping.controllers;

import com.ufrn.demoanlitashopping.persistencia.ProdutoDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ListaProdutosController {

    @GetMapping("/listaProdutosCliente")
    public void listaProdutosCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ProdutoDAO dao = new ProdutoDAO();
        var writer = response.getWriter();

        String browser = request.getHeader("pipoca");

        var listarProdutos = dao.listaProdutos();

        response.setContentType("text/html");

        writer.println("<html>" +
                "<body>");
        if (browser != null) {
            writer.println(browser);
        }

        writer.println("<h1>Espaço do Cliente</h1>");
        writer.println("<table border='1'>" +
                "<tr>" +
                "<th>Nome</th>" +
                "<th>Descrição</th>" +
                "<th>Preço</th>" +
                "<th>Estoque</th>" +
                "<th>Carrinho</th>" +  // Adicionei uma coluna para o botão "Adicionar ao Carrinho"
                "</tr>");

        for (var t1 : listarProdutos){
            if(t1.getEstoque() != 0) {
                writer.println("<tr>" +
                        "<td>" + t1.getNome() + "</td>" +
                        "<td>" + t1.getDescricao() + "</td>" +
                        "<td>" + t1.getPreco() + "</td>" +
                        "<td>" + t1.getEstoque() + "</td>" +
                        "<td>" + "<a href='/addCarrinho?produtoId=" + t1.getId() + "&comando=add'>AdicionaraoCarrinho</a>" + "</td>" + // Link para adicionar ao carrinho
                        "</tr>" +
                        "<a href=/verCarrinho>Ver carrinho</a>");
            } else {
                writer.println("<tr>" +
                        "<td>" + t1.getNome() + "</td>" +
                        "<td>" + t1.getDescricao() + "</td>" +
                        "<td>" + t1.getPreco() + "</td>" +
                        "<td>" + t1.getEstoque() + "</td>" +
                        "<td>Sem Estoque</td>" +
                        "</tr>");
            }
        }
        writer.println("</table>");

        writer.println("</html>" +
                "</body>");

    }

}
