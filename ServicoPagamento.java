package model;

//clara

//ambiente de pagamento externo
public class ServicoPagamento {
    private String urlApi = "https://api.pagamentos.com/v1/";
    private String tokenAutenticacao = "anaduda0202";

    //simulação de metodos da api sabe?
    public boolean processarPagamento(float valor, String dadosCartao) {
        System.out.println("\nProcessando pagamento de R$" + valor + "...");
        if (dadosCartao != null && dadosCartao.length() > 5) {
            System.out.println("   - Pagamento realizado com sucesso!");
            return true;
        }
        System.out.println("   - Falha no processamento do pagamento.");
        return false;
    }
    
    //quando não dá certo, estorno, devolver
    public boolean estornarPagamento(String idTransacao) {
        System.out.println("Estornando transação " + idTransacao + "...");
        return true;
    }
}