package com.ufrn.demoanlitashopping.controllers;

import com.ufrn.demoanlitashopping.classes.Cliente;
import com.ufrn.demoanlitashopping.persistencia.ClienteDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class CadastroController {

    @RequestMapping(value = "/doCadastro", method = RequestMethod.POST)
    public void doCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException{
        var nome = request.getParameter("nome");
        var email = request.getParameter("email");
        var senha = request.getParameter("senha");

        ClienteDAO cDAO = new ClienteDAO();
        Cliente c = new Cliente(nome, email, senha);

        try{
            cDAO.cadastrar(c);
            response.sendRedirect("cadastro.html?msg=Cadastrado com sucesso!");
        }catch(Exception e){
            System.out.println(e.getMessage());
            response.sendRedirect("cadastro.html?msg=Erro ao cadastrar");
        }
    }
}