package model;

//clara

public class Produto {
    private int idProduto;
    private String nome;
    private float precoUnitario;
    private int estoque;
    
    //construtor 
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

    //métodos
    public void atualizarEstoque(int quantidade) {
        this.estoque += quantidade;
        System.out.println("Estoque de " + nome + " atualizado para " + this.estoque);
    }

    public void consultarDetalhes() {
        System.out.printf("ID: %d | Nome: %s | Preço: R$%.2f | Estoque: %d\n", 
            idProduto, nome, precoUnitario, estoque);
    }
}