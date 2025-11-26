package model;

//duda

import java.io.Serializable; //usar o serializable pros objetos serem convertidos em uma sequência de bytes (salvar o arquivo bunitin)

public class ItemVenda implements Serializable {
    private static final long serialVersionUID = 1L; //identificador de versão pra compatibilidade
    private int quantidade;
    private float preco; 
    private Produto produto; 

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = produto.getPrecoUnitario(); 
    }

    public int getQuantidade() { return quantidade; }
    public float getPreco() { return preco; }
    public Produto getProduto() { return produto; }

    public float calcularSubtotal() {
        return this.quantidade * this.preco;
    }
}