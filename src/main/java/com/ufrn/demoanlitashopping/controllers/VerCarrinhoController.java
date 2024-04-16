    package com.ufrn.demoanlitashopping.controllers;

    import com.ufrn.demoanlitashopping.classes.Carrinho;
    import com.ufrn.demoanlitashopping.classes.Produto;
    import com.ufrn.demoanlitashopping.persistencia.ProdutoDAO;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;

    import java.io.IOException;
    import java.io.PrintWriter;
    import java.util.Map;

    @Controller
    public class VerCarrinhoController {

        private final HttpServletRequest httpServletRequest;

        public VerCarrinhoController(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
        }

        @GetMapping("/verCarrinho")
        public void verCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
            int total = 0;

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();

            HttpSession session = request.getSession();
            Integer clienteId = (Integer) session.getAttribute("clienteId");


            // Cria uma instância de CarrinhoController
            CarrinhoController carrinhoController = new CarrinhoController();

            // Obtém o carrinho do cliente dos cookies usando o método de instância
            Map<Integer, Integer> carrinho = carrinhoController.getCarrinhoFromCookies(clienteId, request);
            System.out.println("Carrinho do cliente: " + carrinho);

            writer.println("<html>" + "<head>"+ "<title>Carrinho</title>"
                    +"</head>" + "<body style='display: flex; flex-direction: column;  align-items: center; background-color: lightblue'>"
                    + "<h1>Carrinho de Compras</h1>"
                    + "<table border='1' style='background: white'>" +
                    "<tr><th>Nome</th>" +
                    "<th>Preço</th>" +
                    "<th>Quantidade</th>" +
                    "<th>Remover</th></tr>"
            );


            // Verifica se o carrinho não está vazio
            if (carrinho != null && !carrinho.isEmpty()) {
                // Itera sobre os itens do carrinho e os exibe na tabela

                for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();


                    Produto produto = ProdutoDAO.getProdutoById(productId);


                    // Exibir o produto na tabela
                    writer.println("<tr>");
                    writer.println("<td>" + produto.getNome() + "</td>");
                    writer.println("<td>" + produto.getPreco() + "</td>");
                    writer.println("<td>" + quantity + "</td>");
                    writer.println("<td>" + "<a href='/addCarrinho?produtoId=" + produto.getId() + "&comando=remove'>Remover</a>");
                    writer.println("</tr>");
                    total = total + (quantity * produto.getPreco());

                }
                System.out.println("total: " + total);
            } else {
                response.sendRedirect("/listaProdutosCliente");
            }

            writer.println("</table>" + "<br>" + "<table border='1' style='background-color: white; margin-right: 160px; margin-top: -20px'>" + "<tr>"
                    + "<th style='width: 85px'>Total</th>" + "<td>" + total + "</td>" + "</tr>" + "</table>" + "<button style='margin-top: -27; margin-left: 140px; height: 28px; width: 150px' >" +
                    "<a href='/finalizarCompra' style='text-decoration: none; color: black'>Finalizar compra</a>" + "</button>" + "</br>" + "<button>"
                    + "<a href='/listaProdutosCliente' style='text-decoration: none; color: black '>Voltar</a>" + "</button>"

            );
            writer.println("</body></html>");
        }

    }
