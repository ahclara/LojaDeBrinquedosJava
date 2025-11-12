package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//clara

public class Venda {
    private int idPedido; //chave primaria
    private LocalDateTime dataHora;
    private float valorTotal;
    private String status;
    private Cliente cliente; 
    private List<ItemVenda> itens; 

    public Venda(int idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.status = "PENDENTE";
        this.valorTotal = 0.0f;
    }
    
    //getters
    public int getIdPedido() { return idPedido; }
    public LocalDateTime getDataHora() { return dataHora; }
    public float getValorTotal() { return valorTotal; }
    public String getStatus() { return status; }
    public Cliente getCliente() { return cliente; }
    public List<ItemVenda> getItens() { return itens; }

    
    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        this.valorTotal = calcularTotal(); 
        System.out.printf("   - Adicionado: %s (x%d) | Subtotal: R$%.2f\n", 
            item.getProduto().getNome(), item.getQuantidade(), item.calcularSubtotal());
    }

    public float calcularTotal() {
        float total = 0.0f;
        for (ItemVenda item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public boolean finalizarVenda() {
        if (this.valorTotal > 0 && this.status.equals("PENDENTE")) {
            //para atualizar a quantidade no estoque
            for (ItemVenda item : itens) {
                item.getProduto().atualizarEstoque(-item.getQuantidade());
            }
            
            //mudar o status da compra
            this.status = "CONCLUIDA!";
            System.out.printf("\nVenda %d finalizada! Valor total: R$%.2f. Status: %s\n", 
                idPedido, valorTotal, status);
            return true;
        }
        System.out.println("Erro ao finalizar venda " + idPedido + ". Tente novamente.");
        return false;
    }
}