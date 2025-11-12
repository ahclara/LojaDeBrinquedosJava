package singleton;

import model.Cliente;

//duda

public class Usuario {
    //instância única 
    private static Usuario instancia;
    
    //cliente/usuário atualmente logado.
    private Cliente clienteLogado;
    //cliente, vendedor ou gerente
    private String papel;
    
    //construtor privado pra impedir novas instâncias
    private Usuario() {
        this.clienteLogado = null; //pra inicializar os dois como null
        this.papel = null;
    }
    //acesso global
    public static Usuario getInstancia() {
        if (instancia == null) {
            instancia = new Usuario();
        }
        return instancia;
    }
    
    public void fazerLogin(Cliente cliente, String papel) {
        if (cliente != null && papel != null && !papel.trim().isEmpty()) { //cliente não é nulo e o papel não é nulo (remover espaços)
            this.clienteLogado = cliente;
            this.papel = papel.toUpperCase(); //letras maiusculas
            System.out.println("Login realizado como " + this.papel + ": " + cliente.getNome());
        } else {
            System.out.println("Erro: Usuário inválido para login.");
        }
    }
    //limpar
    public void fazerLogout() {
        this.clienteLogado = null;
        this.papel = null;
        System.out.println("Logout realizado.");
    }
    //getters
    public Cliente getClienteLogado() {return clienteLogado;}
    public String getPapel() {return papel;}
    
    public boolean isLogado() {return clienteLogado != null;}

    public boolean isGerente() {return "GERENTE".equals(papel);}

    public boolean isVendedor() {return "VENDEDOR".equals(papel);}
    
    public boolean isCliente() {return "CLIENTE".equals(papel);}
}