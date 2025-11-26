package model;

//duda

import java.io.Serializable;  //usar o serializable pros obejetos serem convertidos em uma sequência de bytes (salvar o arquivo bunitin)

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L; //identificador de versão para compatibilidade
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

    //gettes
    public int getIdCliente() { return idCliente; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf;}
    public String getemail() { return email;}
    
    //setters
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }

    //metodos 
    public void cadastrar() {
        System.out.println("Cliente " + nome + " cadastrado com sucesso!");
    }

    public void atualizarCadastro() {
        System.out.println("Cadastro do cliente " + nome + " atualizado.");
    }
    
    public boolean verificarCredenciais(String email, String cpf) {
        return this.email.equalsIgnoreCase(email) && this.cpf.equals(cpf); //condições da iqualdade
    }
}