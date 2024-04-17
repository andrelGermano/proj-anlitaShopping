package com.ufrn.demoanlitashopping.classes;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String senha;

    public Cliente(int id) {
        this.id = id;
    }

    public Cliente(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Cliente(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
