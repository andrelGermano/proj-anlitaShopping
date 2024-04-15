package com.ufrn.demoanlitashopping.persistencia;

import com.ufrn.demoanlitashopping.classes.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProdutoDAO {
    private static Conexao con;
    private String CAD = "INSERT INTO Produto (nome_produto, descricao, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";
    private String REL ="SELECT * FROM Produto";
    private static String ID ="SELECT * FROM Produto WHERE id = ?";

    public ProdutoDAO() {
        con = new Conexao("jdbc:postgresql://localhost:5432/anlitaShopping", "postgres", "17110521");
    }

    public void cadastrar(Produto p) {
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(CAD);
            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setInt(3, p.getPreco());
            ps.setInt(4, p.getEstoque());
            ps.execute();
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na inclusão: " + e.getMessage());
        }
    }

    public ArrayList<Produto> listaProdutos() {
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        Produto p;
        try {
            con.conectar();
            Statement st = con.getConexao().createStatement();
            ResultSet rs = st.executeQuery(REL);
            while (rs.next()) {
                p = new Produto(rs.getInt("id"), rs.getInt("preco"), rs.getString("nome_produto"), rs.getString("descricao"), rs.getInt("quantidade_estoque"));
                produtos.add(p);
            }
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return produtos;
    }

    public static Produto getProdutoById(int produtoId) {

        Produto p = null;
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(ID);
            ps.setInt(1, produtoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                p = new Produto(rs.getInt("id"), rs.getInt("preco"), rs.getString("nome_produto"), rs.getString("descricao"), rs.getInt("quantidade_estoque"));

            }
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return p;
    }
}
