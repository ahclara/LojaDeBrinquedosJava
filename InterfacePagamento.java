package strategy;

//duda

public interface InterfacePagamento { //o molde
    boolean processarPagamento(float valor, String dadosPagamento);
}