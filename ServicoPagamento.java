package model;

//clara

import java.io.Serializable; //salvar em arquivo em byte

public class ServicoPagamento implements Serializable {
    private static final long serialVersionUID = 1L; //identificador de versão para compatibilidade
    private String urlApi = "https://api.pagamentos.com/v1/";
    private String tokenAutenticacao = "anaduda0202";

    //metodos só
    public boolean processarPagamento(float valor, String dadosPagamento) {
        System.out.println("\nProcessando pagamento de R$" + valor + "...");
        if (dadosPagamento != null && !dadosPagamento.trim().isEmpty()) {
            System.out.println("   - Pagamento realizado com sucesso!");
            return true;
        }
        System.out.println("   - Falha no processamento do pagamento.");
        return false;
    }
    
    public boolean estornarPagamento(String idTransacao) {
        System.out.println("Estornando transação " + idTransacao + "...");
        return true;
    }
}