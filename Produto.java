package model;

//clara 

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idProduto;
    private String nome;
    private float precoUnitario;
    private int estoque;
    
    public Produto(int idProduto, String nome, float precoUnitario, int estoque) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.estoque = estoque;
    }

    //getters
    public int getIdProduto() { return idProduto; }
    public String getNome() { return nome; }
    public float getPrecoUnitario() { return precoUnitario; }
    public int getEstoque() { return estoque; }
    
    //set e tal
    public void setNome(String nome) { this.nome = nome; }
    public void setPrecoUnitario(float precoUnitario) {this.precoUnitario = precoUnitario; }

    //metodos e pa
    public void atualizarEstoque(int quantidade) {
        this.estoque += quantidade;
        System.out.println("Estoque de " + nome + " atualizado para " + this.estoque);
    }

    public void consultarDetalhes() {
        System.out.printf("ID: %d | Nome: %s | Preço: R$%.2f | Estoque: %d\n", 
            idProduto, nome, precoUnitario, estoque);
    }
}