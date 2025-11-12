package factory;

import model.Produto;

//clara

//factory qui pipipi
public class FabricaBrinquedo implements InterfaceProduto {
    private static int proximoId = 127; // Contador pro id dos brinquedos
    
    @Override
    public Produto criarProduto(int id, String nome, float preco, int estoque) {
        Produto novoProduto = new Produto(proximoId++, nome, preco, estoque);
        System.out.println(" Novo brinquedo: " + novoProduto.getNome());
        return novoProduto;
        //todo o lance da criação do produto
    }
}