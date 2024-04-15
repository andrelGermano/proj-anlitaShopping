package com.ufrn.demoanlitashopping.controllers;

import com.ufrn.demoanlitashopping.classes.Carrinho;
import com.ufrn.demoanlitashopping.classes.Produto;
import com.ufrn.demoanlitashopping.persistencia.ProdutoDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class VerCarrinhoController {

    @GetMapping("/verCarrinho")
    public void verCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Cria uma instância de CarrinhoController
        CarrinhoController carrinhoController = new CarrinhoController();

        // Obtém o carrinho do cliente dos cookies usando o método de instância
        Map<Integer, Integer> carrinho = carrinhoController.getCarrinhoFromCookiesPublic(request);

        out.println("<html><body>");
        out.println("<h1>Carrinho de Compras</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>Nome</th><th>Preço</th><th>Quantidade</th><th>Remover</th></tr>");

        // Verifica se o carrinho não está vazio
        if (carrinho != null && !carrinho.isEmpty()) {
            // Itera sobre os itens do carrinho e os exibe na tabela
            for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                Produto produto = ProdutoDAO.getProdutoById(productId);

                // Exibir o produto na tabela
                out.println("<tr>");
                out.println("<td>" + produto.getNome() + "</td>");
                out.println("<td>" + produto.getPreco() + "</td>");
                out.println("<td>" + quantity + "</td>");
                out.println("<td><a href='/removerDoCarrinho?id=" + productId + "'>Remover</a></td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan='4'>Carrinho vazio</td></tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }
}
