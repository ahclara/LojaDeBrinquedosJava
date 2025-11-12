package model;

//duda

public class ItemVenda {
    private int quantidade;
    private float preco; 
    private Produto produto; 

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = produto.getPrecoUnitario(); 
    }

    //getters
    public int getQuantidade() { return quantidade; }
    public float getPreco() { return preco; }
    public Produto getProduto() { return produto; }

    //método 
    public float calcularSubtotal() {
        return this.quantidade * this.preco;
    }
}