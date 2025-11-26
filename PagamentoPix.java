package strategy;

//duda

import java.io.Serializable;

public class PagamentoPix implements InterfacePagamento, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean processarPagamento(float valor, String chavePix) {
        if (chavePix == null || chavePix.length() < 5) {
            System.out.println("Pagamento PIX inválido.");
            return false;
        }
        
        System.out.println("\n····· Estratégia: Pagamento com PIX ·····");
        System.out.printf("Chave PIX: %s | Valor: R$%.2f\n", chavePix, valor);
        System.out.println("QR CODE gerado. Aguardando a confirmação da transação...");
        return true; 
    }
}