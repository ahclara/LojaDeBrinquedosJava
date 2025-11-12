package model;

//duda

public class Cliente {
    private int idCliente; //pk
    private String nome;
    private String cpf;
    private String email;

    //construtor
    public Cliente(int idCliente, String nome, String cpf, String email) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    //getters
    public int getIdCliente() { return idCliente; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf;}
    public String getemail() { return email;}

    //métodos
    public void cadastrar() {
        System.out.println("Cliente " + nome + " cadastrado com sucesso!");
    }

    public void atualizarCadastro() {
        System.out.println("Cadastro do cliente " + nome + " atualizado.");
    }
    
    public boolean verificarCredenciais(String email, String cpf) {
        return this.email.equalsIgnoreCase(email) && this.cpf.equals(cpf);
    }   //Compara ignorando se são maiúsculas/minúsculas e compara os cpfs
}