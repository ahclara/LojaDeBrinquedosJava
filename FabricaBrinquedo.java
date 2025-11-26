package factory;

import model.Produto;
import java.io.Serializable;

//clara

//factory qui pipipi
public class FabricaBrinquedo implements InterfaceProduto, Serializable {
	private static final long serialVersionUID = 1L;
    private static int proximoId = 127; // Contador pro id dos brinquedos
    
    @Override
    public Produto criarProduto(int id, String nome, float preco, int estoque) {
        Produto novoProduto = new Produto(proximoId++, nome, preco, estoque); // incrementar, colocar o ++ uma linha antes
        System.out.println(" Novo brinquedo: " + novoProduto.getNome());
        return novoProduto;
        //todo o lance da criação do produto
        
    }
        public static void setProximoId(int nextId) {
        	if (nextId > proximoId) {
        		proximoId = nextId;
        		
        	}
     }
}