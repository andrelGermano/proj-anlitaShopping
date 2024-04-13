package com.ufrn.demoanlitashopping.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/cadastro.html")
public class FiltroAutenticacao implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        HttpSession session = request.getSession(false);

        if (session == null){
            response.sendRedirect("index.html?msg=Você precisa logar antes");
        } else {
            Boolean logado = (Boolean) session.getAttribute("logado");
            if (logado == null || !logado) {
                response.sendRedirect("index.html?msg=Você precisa logar antes");
                return; // Importante retornar para interromper o fluxo
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
