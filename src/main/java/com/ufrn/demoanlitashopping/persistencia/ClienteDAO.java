package com.ufrn.demoanlitashopping.persistencia;

import com.ufrn.demoanlitashopping.classes.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ClienteDAO {
    private Conexao con;
    private String CAD = "INSERT INTO Cliente (nome, email, senha) VALUES (?, ?, ?)";
    private String PRO = "SELECT id FROM Cliente WHERE email=? AND senha=?";

    public ClienteDAO() {
        con = new Conexao("jdbc:postgresql://localhost:5432/anlitaShopping", "postgres", "17110521");
    }

    public void cadastrar(Cliente c) {
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(CAD);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getSenha());
            ps.execute();
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na inclusão: " + e.getMessage());
        }
    }

    public boolean procurar(Cliente c) {
        boolean achou = false;
        try{
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(PRO);
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getSenha());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                achou = true;
            }
            con.desconectar();
        }catch(Exception e){
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return achou;
    }

    public Integer selecionarId(Cliente c) {
        Integer id = null;
        try{
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(PRO);
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getSenha());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
            con.desconectar();
        }catch(Exception e){
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return id;
    }
}
