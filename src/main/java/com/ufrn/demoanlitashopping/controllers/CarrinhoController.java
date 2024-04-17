package com.ufrn.demoanlitashopping.controllers;

import com.ufrn.demoanlitashopping.classes.Produto;
import com.ufrn.demoanlitashopping.persistencia.ProdutoDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CarrinhoController {

    @GetMapping("/addCarrinho")
    public void addCarrinho(@RequestParam int produtoId, @RequestParam String comando, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true); // Obtém a sessão do usuário ou cria uma nova se não existir

        Produto p = ProdutoDAO.getProdutoById(produtoId);

        // Obtém o ID do cliente da sessão
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        // Verifica se o carrinho já existe na sessão do usuário
        Map<Integer, Integer> carrinho = (Map<Integer, Integer>) session.getAttribute("carrinho_" + clienteId);
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        // Adiciona ou remove o produto do carrinho
        if ("add".equals(comando)) {
            p.diminuiEstoque();
            carrinho.put(produtoId, carrinho.getOrDefault(produtoId, 0) + 1);
        } else if ("remove".equals(comando)) {
            p.incrementaEstoque();
            if (carrinho.containsKey(produtoId)) {
                int quantidade = carrinho.get(produtoId);
                if (quantidade > 1) {
                    carrinho.put(produtoId, quantidade - 1);
                } else {
                    carrinho.remove(produtoId);
                }
            }
        }

        // Armazena o carrinho atualizado na sessão do usuário usando o ID do cliente como chave
        session.setAttribute("carrinho_" + clienteId, carrinho);

        // Salva o carrinho nos cookies
        salvarCarrinhoNosCookies(clienteId, carrinho, response);

        // Redireciona de volta à página de lista de produtos
        response.sendRedirect("listaProdutos.html");
    }

    private void salvarCarrinhoNosCookies(Integer clienteId, Map<Integer, Integer> carrinho, HttpServletResponse response) {
        StringBuilder carrinhoString = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            carrinhoString.append(entry.getKey()).append(":").append(entry.getValue()).append("_");
        }
        if (carrinhoString.length() > 0) {
            carrinhoString.deleteCharAt(carrinhoString.length() - 1);
        }
        Cookie carrinhoCookie = new Cookie("carrinho_" + clienteId, carrinhoString.toString());
        carrinhoCookie.setMaxAge(48 * 60 * 60); // O cookie vai expirar em 48 horas
        response.addCookie(carrinhoCookie);
    }

    public Map<Integer, Integer> getCarrinhoFromCookies(Integer clienteId, HttpServletRequest request) {
        Map<Integer, Integer> carrinho = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("carrinho_" + clienteId).equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    if (!cookieValue.isEmpty()) {
                        String[] itens = cookieValue.split("_");
                        for (String item : itens) {
                            String[] produtoInfo = item.split(":");
                            if (produtoInfo.length >= 2) {
                                try {
                                    int produtoId = Integer.parseInt(produtoInfo[0]);
                                    int quantidade = Integer.parseInt(produtoInfo[1]);
                                    carrinho.put(produtoId, quantidade);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        return carrinho;
    }

    @GetMapping("/finalizarCompra")
    public void finalizarCompra( HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        CarrinhoController carrinhoController = new CarrinhoController();

        // Obtém o carrinho do cliente dos cookies
        Map<Integer, Integer> carrinho = carrinhoController.getCarrinhoFromCookies(clienteId, request);

        // Itera sobre os itens do carrinho e decrementa o estoque selecionado de cada produto
        ProdutoDAO p = new ProdutoDAO();
        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Produto produto = ProdutoDAO.getProdutoById(productId);
            p.decrementarEstoque(quantity, productId);
        }

        // Apaga o carrinho da sessão pelo id do cliente
        session.removeAttribute("carrinho_" + clienteId);

        // Apaga o cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("carrinho_" + clienteId).equals(cookie.getName())) {
                    cookie.setMaxAge(0); // Define o tempo de vida do cookie como zero para removê-lo
                    cookie.setPath("/"); // Define o mesmo caminho do cookie que deseja excluir
                    response.addCookie(cookie); // Adiciona o cookie à resposta para que seja enviado ao navegador e remova o cookie existente
                    break;
                }
            }
        }
        // Redireciona para a página de lista de produtos do cliente
        response.sendRedirect("/listaProdutosCliente");
    }
}
