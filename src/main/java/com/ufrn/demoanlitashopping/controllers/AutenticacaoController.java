package com.ufrn.demoanlitashopping.controllers;

import com.ufrn.demoanlitashopping.classes.Cliente;
import com.ufrn.demoanlitashopping.classes.Lojista;
import com.ufrn.demoanlitashopping.persistencia.ClienteDAO;
import com.ufrn.demoanlitashopping.persistencia.LojistaDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class AutenticacaoController {

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var email = request.getParameter("email");
        var senha = request.getParameter("senha");

        ClienteDAO cDAO = new ClienteDAO();
        LojistaDAO lDAO = new LojistaDAO();
        Cliente c = new Cliente(email, senha);
        Lojista l = new Lojista(email, senha);

        if(cDAO.procurar(c)){
            HttpSession session = request.getSession();
            session.setAttribute("logado", true);

            response.sendRedirect("listaProdutos.html");
        }
        else if (lDAO.procurar(l)) {
            HttpSession session = request.getSession();
            session.setAttribute("logado", true);

            response.sendRedirect("exibirProdutos.html");
        } else {
            response.sendRedirect("index.html?msg=Login falhou");
        }
    }


}