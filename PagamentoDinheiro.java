package strategy;

//duda

import java.io.Serializable;

public class PagamentoDinheiro implements InterfacePagamento, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean processarPagamento(float valor, String valorRecebidoStr) {
        float valorRecebido = 0.0f; //inicializar, converter e essas paradas
        try { //tratar exceção
            valorRecebido = Float.parseFloat(valorRecebidoStr); //converter String pra float
        } catch (NumberFormatException e) { //se não vier um número
            System.out.println("Valor recebido em Dinheiro Físico inválido.");
            return false;
        }

        System.out.println("\n····· Estratégia: Pagamento em Dinheiro Físico ·····");
        if (valorRecebido >= valor) { 
            float troco = valorRecebido - valor;
            System.out.printf("Valor Recebido: R$%.2f | Troco: R$%.2f\n", valorRecebido, troco);
            System.out.println("Pagamento em Dinheiro Físico concluído! :)");
            return true;
        } else {
            System.out.printf("Valor Recebido (R$%.2f) é insuficiente. Pague R$%.2f, por favor.\n", valorRecebido, valor);
            return false;
        }
    }
}