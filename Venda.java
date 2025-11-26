package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import strategy.InterfacePagamento;

//clara

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idPedido; 
    private LocalDateTime dataHora;
    private float valorTotal;
    private String status;
    private Cliente cliente; 
    private List<ItemVenda> itens; 
    
    private transient InterfacePagamento estrategiaPagamento;   //meio que pede pra ignorar a estrategia no salvamento em arquivo, pq ja nao e necessario
    
    public Venda(int idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.status = "PENDENTE";
        this.valorTotal = 0.0f;
    }
    
    public void setEstrategiaPagamento(InterfacePagamento estrategiaPagamento) {
        this.estrategiaPagamento = estrategiaPagamento;
    }
    
    public int getIdPedido() { return idPedido; }
    public LocalDateTime getDataHora() { return dataHora; }
    public float getValorTotal() { return valorTotal; }
    public String getStatus() { return status; }
    public Cliente getCliente() { return cliente; }
    public List<ItemVenda> getItens() { return itens; }

    
    //caso o cliente queira adicionar
    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        this.valorTotal = calcularTotal(); 
        System.out.printf("    - Adicionado: %s (x%d) | Subtotal: R$%.2f\n", 
            item.getProduto().getNome(), item.getQuantidade(), item.calcularSubtotal());
    }

    public float calcularTotal() {
        float total = 0.0f;
        for (ItemVenda item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public boolean processarVenda(String dadosPagamento) {
        if (this.valorTotal <= 0) {
            System.out.println("Venda inválida: Total zero.");
            return false;
        }
        
        if (estrategiaPagamento == null) {
            System.out.println("Erro: Estratégia de pagamento não definida.");
            return false;
        }

        boolean sucessoPagamento = this.estrategiaPagamento.processarPagamento(this.valorTotal, dadosPagamento);

        if (sucessoPagamento) {
            return finalizarVenda();
        } else {
            System.out.println("Venda não finalizada devido a falha no pagamento.");
            return false;
        }
    }

    public boolean finalizarVenda() {
        if (this.status.equals("PENDENTE")) {
            for (ItemVenda item : itens) {
                if (item.getQuantidade() > item.getProduto().getEstoque()) {
                    System.out.println("ERRO FATAL: Estoque insuficiente após pagamento. Contate o administrador.");
                    return false;
                }
                item.getProduto().atualizarEstoque(-item.getQuantidade());
            }
            
            this.status = "CONCLUIDA!";
            System.out.printf("\nVenda %d finalizada! Valor total: R$%.2f. Status: %s\n", 
                idPedido, valorTotal, status);
            return true;
        }
        System.out.println("Erro ao finalizar venda " + idPedido + ". Status: " + this.status);
        return false;
    }
}