package factory;

import model.Produto;

// clara

public interface InterfaceProduto {
    Produto criarProduto(int id, String nome, float preco, int estoque);
}