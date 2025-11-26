package singleton;

//duda

import model.Cliente;

public class Usuario {
    private static Usuario instancia; //instância única 
    private Cliente clienteLogado; 
    private String papel;
    
    private Usuario() { //privado pra ter certeza que não vai ser instânciado por aí
        this.clienteLogado = null; 
        this.papel = null; //iniciar vazios
    }

    public static Usuario getInstancia() {
        if (instancia == null) {
            instancia = new Usuario();
        }
        return instancia; //metodo pra só instanciar se tiver mesmo null
    }
    
    public void fazerLogin(Cliente cliente, String papel) {
        if (cliente != null && papel != null && !papel.trim().isEmpty()) { //trim pra remover os espaços em branco. não podem estar vazios
            String papelFormatado = papel.toUpperCase(); //deixar no maiusculo
            if (!papelFormatado.equals("CLIENTE") && !papelFormatado.equals("VENDEDOR") && !papelFormatado.equals("GERENTE")) {
                System.out.println("Erro: Papel de acesso inválido."); 
                return;
            }
            
            //entrou
            this.clienteLogado = cliente;
            this.papel = papelFormatado; 
            System.out.println("Login realizado como " + this.papel + ": " + cliente.getNome());
        } else {
            System.out.println("Erro: Usuário ou papel inválido para login.");
        }
    }
    
    //saiu
    public void fazerLogout() {
        this.clienteLogado = null;
        this.papel = null;
        System.out.println("Logout realizado. Até mais!");
    }

    //getters
    public Cliente getClienteLogado() {return clienteLogado;}
    public String getPapel() {return papel;}
    
    //os logins
    public boolean isLogado() {return clienteLogado != null;}
    public boolean isGerente() {return "GERENTE".equals(papel);} 
    public boolean isVendedor() {return "VENDEDOR".equals(papel) || "GERENTE".equals(papel);} 
    public boolean isCliente() {return "CLIENTE".equals(papel) || "VENDEDOR".equals(papel) || "GERENTE".equals(papel);} // niveis de permissão
}