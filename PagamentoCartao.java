package strategy;

//duda

import model.ServicoPagamento;
import java.io.Serializable; //para os objetos serem salvos em arquivos e restaurados depois

public class PagamentoCartao implements InterfacePagamento, Serializable {
    private static final long serialVersionUID = 1L; //quando for carregar um objeto salvo (desserialização), a versão da classe vai ser compatível com a versão salva no arquivo
    private ServicoPagamento servicoPagamento; 

    public PagamentoCartao() {
        this.servicoPagamento = new ServicoPagamento(); 
    }

    @Override 
    public boolean processarPagamento(float valor, String dadosCartao) {
        if (dadosCartao == null || dadosCartao.length() < 5) { //condições
            System.out.println("Pagamento com Cartão inválido.");
            return false;
        }
        System.out.println("\n····· Estratégia: Pagamento com Cartão ·····");
        return servicoPagamento.processarPagamento(valor, dadosCartao);
    }
}