package principal;

import model.Cliente;
import model.Produto;
import model.Venda;
import model.ItemVenda;
import factory.FabricaBrinquedo;
import singleton.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SistemaLojaDeBrinquedos {
	//estático para ser armazenado cada um numa lista
    private static List<Produto> produtos = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Venda> vendas = new ArrayList<>();
    
    private static FabricaBrinquedo fabrica = new FabricaBrinquedo();
    private static int proximoIdVenda = 1001;
    private static int proximoIdCliente = 3;

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarDadosIniciais();
        
        System.out.println("···································");
        System.out.println("  Sistema para Loja de Brinquedos  ");
        System.out.println("···································");

        while (true) {
            if (!Usuario.getInstancia().isLogado()) {
                exibirMenuLogin(scanner);
            } else {
                if (Usuario.getInstancia().isGerente()) {
                    exibirMenuGerente(scanner);
                } else if (Usuario.getInstancia().isVendedor()) {
                    exibirMenuVendedor(scanner);
                } else { 
                    exibirMenuVendedor(scanner); 
                }
            }
        }
    }
    
    private static void inicializarDadosIniciais() {
    	 produtos.add(fabrica.criarProduto(0, "Carrinho HottWheels", 15.50f, 50));
         produtos.add(fabrica.criarProduto(0, "Boneca Barbie", 79.90f, 25));
         produtos.add(fabrica.criarProduto(0, "Quebra-Cabeça 500 peças", 35.00f, 15));
        
        //testes de cliente e de vendendor
        clientes.add(new Cliente(1, "Louis Gouveia", "111.111.111-11", "louis@gmail.com"));
        clientes.add(new Cliente(2, "Ana Vitoria", "222.222.222-22", "ana@gamil.com"));
        clientes.add(new Cliente(3, "Clara Cecília", "333.333.333-33", "clara@gmail.com"));
        proximoIdCliente = 4;
    }

    private static void exibirMenuLogin(Scanner scanner) {
        System.out.println("\n······ Acessar sistema ······");
        System.out.println("1. Fazer Login");
        System.out.println("2. Fazer Cadastro");
        System.out.println("3. Sair do Sistema");
        System.out.print("Escolha uma opção: ");

        int opcao = 0; //Inicializar
        try {
            opcao = Integer.parseInt(scanner.nextLine()); //converter String para um número inteiro
        } catch (NumberFormatException e) { //tenta converter um texto (quando não é um número válido)
            System.out.println("Opção inválida. Tente digitar um número.");
            return;
        }

        switch (opcao) {
            case 1:
                realizarLogin(scanner);
                break;
            case 2:
                fazerCadastro(scanner);
                break;
            case 3:
                System.out.println("Encerrando o Sistema. Até logo!! :)");
                System.exit(0);
                break;
            default:
                System.out.println("Erro! Tente novamente.");
        }
    }
    
    private static void fazerCadastro(Scanner scanner) {
        System.out.println("\n······ NOVO CADASTRO ······");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        Cliente novoCliente = new Cliente(proximoIdCliente++, nome, cpf, email);
        clientes.add(novoCliente);
        novoCliente.cadastrar(); 
        
        System.out.println("Cadastro concluído! Faça login como CLIENTE para continuar.");
    }

    private static void realizarLogin(Scanner scanner) {
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();
        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        
        System.out.println("\nSelecione o tipo de acesso:");
        System.out.println("1. GERENTE");
        System.out.println("2. VENDEDOR");
        System.out.println("3. CLIENTE");
        System.out.print("Digite a opção: ");
        String opcaoPapel = scanner.nextLine();
        String papelEscolhido;
        
        switch (opcaoPapel) {
            case "1": papelEscolhido = "GERENTE"; break;
            case "2": papelEscolhido = "VENDEDOR"; break;
            case "3": papelEscolhido = "CLIENTE"; break;
            default:
                System.out.println("Opção inválida.");
                return;
        }
        
        for (Cliente c : clientes) {
            if (c.verificarCredenciais(email, cpf)) {
                Usuario.getInstancia().fazerLogin(c, papelEscolhido); 
                return;
            }
        }
        System.out.println("Falha no login: E-mail ou CPF incorretos. Tente novamente.");
    }
    
    private static void exibirMenuVendedor(Scanner scanner) {
        Usuario sessao = Usuario.getInstancia();
        String tipoUsuario = sessao.isVendedor() ? "VENDEDOR" : "CLIENTE";
        
        System.out.println("\n······ MENU DE VENDAS (" + tipoUsuario + ") ······");
        System.out.println("Usuário: " + sessao.getClienteLogado().getNome());
        System.out.println("1. Realizar Venda/Comprar Produto"); 
        System.out.println("2. Consultar Produto"); 
        System.out.println("3. Consultar Venda"); 
        System.out.println("4. Fazer Logout");
        System.out.print("Escolha uma opção: ");

        int opcao = 0;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Digite um número.");
            return;
        }

        switch (opcao) {
            case 1:
                realizarNovaVenda(scanner, sessao.getClienteLogado());
                break;
            case 2:
                consultarProdutos();
                break;
            case 3:
                consultarVendas(scanner);
                break;
            case 4:
                sessao.fazerLogout();
                break;
            default:
                System.out.println("ERRO!");
        }
    }

    private static void consultarProdutos() {
        System.out.println("\n······ PRODUTOS EM ESTOQUE ······");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (Produto p : produtos) {
            p.consultarDetalhes();
        }
        System.out.println("····································");
    }

    private static void consultarVendas(Scanner scanner) {
        System.out.println("\n······ CONSULTAR VENDAS ······");
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }
        
        List<Venda> vendasFiltradas;
        Usuario sessao = Usuario.getInstancia();
        if (sessao.isCliente()) {
             vendasFiltradas = vendas.stream() //filtar
                .filter(v -> v.getCliente().getIdCliente() == sessao.getClienteLogado().getIdCliente()) //exibe as compras do cliente especifico
                .collect(Collectors.toList());
        } else {
             vendasFiltradas = vendas; 
        }
        
        if (vendasFiltradas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada para este usuário.");
            return;
        }
        
        System.out.println("ID | Cliente | Data/Hora | Total | Status");
        System.out.println("···|·········|···········|·······|·······");
        
        for(Venda v : vendasFiltradas) {
            System.out.printf("%d | %s | %s | R$%.2f | %s\n", 
                v.getIdPedido(), v.getCliente().getNome(), 
                v.getDataHora().toString().substring(11, 16), //O primeiro dígito da hora e o caractere após o minuto
                v.getValorTotal(), v.getStatus());
        }
        System.out.println("····································");
    }

    private static void realizarNovaVenda(Scanner scanner, Cliente cliente) {
        if (produtos.isEmpty()) {
            System.out.println("Não é possível fazer a venda: Não há produtos cadastrados.");
            return;
        }

        Venda novaVenda = new Venda(proximoIdVenda++, cliente);
        String continuar = "S";

        System.out.println("\n······ INICIANDO VENDA #" + novaVenda.getIdPedido() + " ······");
        
        while (continuar.equalsIgnoreCase("S")) {
            consultarProdutos(); 
            
            System.out.print("Digite o ID do produto para adicionar (0 para parar): ");
            int idProduto = Integer.parseInt(scanner.nextLine());
            if (idProduto == 0) break;

            Produto produtoSelecionado = produtos.stream() //para fazer operações no processamento de dados
                .filter(p -> p.getIdProduto() == idProduto) //condição de busca
                .findFirst() //encerra
                .orElse(null);

            if (produtoSelecionado == null) {
                System.out.println("Produto com ID " + idProduto + " não encontrado.");
                continue;
            }

            System.out.print("Quantidade desejada de " + produtoSelecionado.getNome() + ": ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade > produtoSelecionado.getEstoque()) {
                System.out.println("Quantidade indisponível! Estoque atual: " + produtoSelecionado.getEstoque());
            } else if (quantidade > 0) {
                novaVenda.adicionarItem(new ItemVenda(produtoSelecionado, quantidade));
            }

            System.out.print("Adicionar outro item? (S/N): ");
            continuar = scanner.nextLine();
        }

        if (novaVenda.getItens().isEmpty()) {
            System.out.println("Venda cancelada ou sem itens.");
            proximoIdVenda--; //"devolver"
            return;
        }

        System.out.printf("\nTOTAL DA VENDA: R$%.2f\n", novaVenda.calcularTotal());
        System.out.print("Deseja finalizar a venda e pagar? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            if (simularPagamento(novaVenda.getValorTotal(), scanner)) {
                novaVenda.finalizarVenda(); 
                vendas.add(novaVenda); 
            } else {
                System.out.println("Venda não finalizada devido a falha no pagamento.");
            }
        } else {
            System.out.println("Venda não finalizada.");
        }
    }
    
    private static void exibirMenuGerente(Scanner scanner) {
        System.out.println("\n······ MENU DO GERENTE ······");
        System.out.println("Usuário: " + Usuario.getInstancia().getClienteLogado().getNome());
        System.out.println("1. Gerenciar Produto"); 
        System.out.println("2. Atualizar Estoque"); 
        System.out.println("3. Gerar Relatórios de Venda"); 
        System.out.println("4. Consultar Venda");
        System.out.println("5. Fazer Logout");
        System.out.print("Escolha uma opção: ");

        int opcao = 0;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Digite um número.");
            return;
        }

        switch (opcao) {
            case 1:
                gerenciarProduto(scanner);
                break;
            case 2:
                atualizarEstoqueManual(scanner);
                break;
            case 3:
                gerarRelatorios();
                break;
            case 4:
                consultarVendas(scanner);
                break;
            case 5:
                Usuario.getInstancia().fazerLogout();
                break;
            default:
                System.out.println("ERRO!");
        }
    }
    
    private static void gerenciarProduto(Scanner scanner) {
        System.out.println("\n······ GERENCIAR PRODUTOS ······");
        System.out.println("1. Adicionar Novo Brinquedo");
        System.out.println("2. Consultar/Listar Todos");
        System.out.println("3. Voltar ao Menu Gerente");
        System.out.print("Escolha uma opção: ");
        
        int opcao = Integer.parseInt(scanner.nextLine());
        
        switch (opcao) {
            case 1:
                adicionarNovoBrinquedo(scanner);
                break;
            case 2:
                consultarProdutos();
                break;
            case 3:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }
    
    private static void adicionarNovoBrinquedo(Scanner scanner) {
        System.out.println("\n······ ADICIONAR NOVO BRINQUEDO ······");
        System.out.print("Nome do Brinquedo: ");
        String nome = scanner.nextLine();
        System.out.print("Preço Unitário: R$");
        float preco = 0.0f;
        try {
            preco = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido.");
        }

        System.out.print("Estoque Inicial: ");
        int estoque = 0;
        try {
            estoque = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Estoque inválido.");
        }

         Produto novoBrinquedo = fabrica.criarProduto(0, nome, preco, estoque);
        produtos.add(novoBrinquedo);
        System.out.println("Brinquedo '" + novoBrinquedo.getNome() + "' cadastrado com sucesso!");
    }

    private static void atualizarEstoqueManual(Scanner scanner) {
        consultarProdutos();
        System.out.println("\n······ ATUALIZAR ESTOQUE MANUAL ······");
        System.out.print("Digite o ID do produto para ajuste de estoque: ");
        int idProduto = Integer.parseInt(scanner.nextLine());

        Produto produtoSelecionado = produtos.stream()
            .filter(p -> p.getIdProduto() == idProduto)
            .findFirst()
            .orElse(null);

        if (produtoSelecionado == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Ajuste de quantidade (+ para entrada, - para saída): ");
        int ajuste = Integer.parseInt(scanner.nextLine());
        
        produtoSelecionado.atualizarEstoque(ajuste); 
        System.out.println("Estoque de " + produtoSelecionado.getNome() + " ajustado.");
    }
    
    private static void gerarRelatorios() {
        System.out.println("\n······ RELATÓRIO DE VENDAS ······");
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda concluída pra gerar relatório.");
            return;
        }
        
        long totalVendas = vendas.stream().filter(v -> "CONCLUIDA!".equals(v.getStatus())).count();
        double valorTotalArrecadado = vendas.stream()
            .filter(v -> "CONCLUIDA!".equals(v.getStatus()))
            .mapToDouble(Venda::getValorTotal) //usado para extrair um valor double de cada objeto
            .sum();
            
        System.out.printf("Total de vendas concluídas: %d\n", totalVendas);
        System.out.printf("Valor total arrecadado: R$%.2f\n", valorTotalArrecadado);
        System.out.println("\nProdutos mais vendidos (Simples):");
        vendas.stream()
            .flatMap(v -> v.getItens().stream()) //único Stream contendo todos os ItemVenda de todas as vendas
            .collect(Collectors.groupingBy(item -> item.getProduto().getNome(), //inicia a coleta dos itens. Map onde a chave é o nome do produto
                     Collectors.summingInt(ItemVenda::getQuantidade))) //Define o que deve ser somado para cada grupo
            .entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) //ordem decrescente
            .forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue() + " unidades")); //O e.getKey() é o nome do produto e e.getValue() é o total de unidades vendidas
            
        System.out.println("····································");
    }

    private static boolean simularPagamento(float valor, Scanner scanner) {
        System.out.println("\n--- PAGAMENTO ---");
        System.out.printf("Valor a pagar: R$%.2f\n", valor);
        System.out.print("Digite o número do cartão: ");
        String dadosCartao = scanner.nextLine();

        model.ServicoPagamento servicoPagamento = new model.ServicoPagamento(); 
        
        return servicoPagamento.processarPagamento(valor, dadosCartao);
    }
}