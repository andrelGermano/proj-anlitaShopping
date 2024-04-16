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

        writer.println("<html>" + "<head>" + "<title>Produtos Disponíveis</title>" + "</head>" +
                "<body style='display: flex; flex-direction: column;  align-items: center; background-color: lightblue'>");
        if (browser != null) {
            writer.println(browser);
        }

        writer.println("<h1>Produtos Disponíveis</h1>");
        writer.println("<table border='1' style='background: white'>" +
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
                        "<td>" + "<a href='/addCarrinho?produtoId=" + t1.getId() + "&comando=add'>Adicionar</a>" + "</td>" + // Link para adicionar ao carrinho
                        "</tr>"
                        );
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
        writer.println("</table>" + "</br>" + "<button>"
                + "<a href='/verCarrinho' style='text-decoration: none; color: black; margin-top: 10px '>Ver carrinho</a>"
                + "</button>" + "</br>");
        writer.println("<button>" + "<a href='listaProdutos.html' style='text-decoration: none; color: black'>Voltar</a>" + "</button>");

        writer.println("</html>" +
                "</body>");

    }

}
