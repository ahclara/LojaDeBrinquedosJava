package principal;

import model.Cliente;
import model.Produto;
import model.Venda;
import model.ItemVenda;
import factory.FabricaBrinquedo;
import factory.InterfaceProduto;
import singleton.Usuario;
import strategy.InterfacePagamento;
import strategy.PagamentoPix;
import strategy.PagamentoDinheiro;
import strategy.PagamentoCartao;
import java.io.*; //salvar e carregar dados(manipular arquivos)
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors; //o lance dos streams(fluxos de dados) é que ele serve pra processar dados

public class SistemaLojaDeBrinquedos {
	private static final String ARQUIVO_DADOS = "C:\\Users\\dudac\\OneDrive\\LOJA DE BRINQUEDOS\\Loja_De_Brinquedos.txt";
	
	//listas
    private static List<Produto> produtos = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Venda> vendas = new ArrayList<>();
    
    private static InterfaceProduto fabrica = new FabricaBrinquedo();
    private static int proximoIdVenda = 1001;
    private static int proximoIdCliente = 1;
    private static int proximoIdProduto = 127; 

    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        carregarDados(); 
        
        System.out.println("···································");
        System.out.println("  Sistema para Loja de Brinquedos  ");
        System.out.println("···································");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> { //(o ambiente de execução do Java) (é um fechamento de emergência) (salvar dados se o sistema for desligado)
            System.out.println("\n Encerrando... Salvando dados...");
            salvarDados(); //tem que ter certeza que salvarDados() vai ser chamado antes que o programa morra
        }));

        while (true) {
            if (!Usuario.getInstancia().isLogado()) { //não tá logado?
                exibirMenuLogin(scanner);
            } else {
                if (Usuario.getInstancia().isGerente()) {
                    exibirMenuGerente(scanner);
                } else if (Usuario.getInstancia().isVendedor()) {
                    exibirMenuVendedor(scanner);
                } else { 
                    exibirMenuCliente(scanner);
                }
            }
        }
    }
    
    private static void salvarDados() {
    	//garantir que o fluxo (oos) vai ser fechado automaticamente
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) { //escrita em arquivo (escrita de objetos Java(serialização))
            oos.writeObject(produtos); //escreve o objeto produtos (que vai tá na lista de produtos) no arquivo.
            oos.writeObject(clientes);
            oos.writeObject(vendas);
            oos.writeInt(proximoIdVenda);
            oos.writeInt(proximoIdCliente);
            int maxIdProduto = produtos.stream()
                .mapToInt(Produto::getIdProduto) //mapeia cada Produto pro seu idProduto
                .max().orElse(127); //encontra id max. se a lista estiver vazia, usa o valor padrão
            oos.writeInt(maxIdProduto + 1); //pegar o id max e +1 pro próximo
            System.out.println("Dados salvos com sucesso.");
        } catch (IOException e) { //tratamento de exceção
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked") //avisos do compilador Java sobre o uso das conversões na hora de ler objetos serializados
    private static void carregarDados() {
    //complemento de salvarDados() pra ter certeza que o que foi feito não seja perdido
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists() && arquivo.length() > 0) { //condições
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {  ////lê bytes do arquivo/desserialização
            //inicia o fluxo de leitura dos objetos, fazendo que o fluxo (ois) seja fechado automaticamente
                produtos = (List<Produto>) ois.readObject(); //lê a lista de produtos
                clientes = (List<Cliente>) ois.readObject();
                vendas = (List<Venda>) ois.readObject();
                proximoIdVenda = ois.readInt();
                proximoIdCliente = ois.readInt();
                proximoIdProduto = ois.readInt();
                FabricaBrinquedo.setProximoId(proximoIdProduto); //atualiza o contador de id da fábrica
                System.out.println("Dados carregados com sucesso.");
            } catch (IOException | ClassNotFoundException e) { //erros de leitura/escrita ou se o java não encontrar a classe 
                System.err.println("Erro ao carregar dados. Iniciando com dados iniciais. Detalhe: " + e.getMessage());
                inicializarDadosIniciais();
            }
        } else {
            System.out.println("Arquivo de dados não encontrado ou vazio. Iniciando com dados iniciais.");
            inicializarDadosIniciais();
        }
    }
    
    private static void inicializarDadosIniciais() {
        produtos.add(fabrica.criarProduto(0, "Carrinho HottWheels", 15.50f, 50));
        produtos.add(fabrica.criarProduto(0, "Boneca Barbie", 79.90f, 25));
        produtos.add(fabrica.criarProduto(0, "Quebra-Cabeça 500 peças", 35.00f, 15));
        
        Cliente c1 = new Cliente(proximoIdCliente++, "Louis Gouveia", "111.111.111-11", "louis@gmail.com");
        Cliente c2 = new Cliente(proximoIdCliente++, "Ana Vitoria", "222.222.222-22", "ana@gamil.com");
        Cliente c3 = new Cliente(proximoIdCliente++, "Clara Cecília", "333.333.333-33", "clara@gmail.com");
        clientes.add(c1);
        clientes.add(c2);
        clientes.add(c3);
    }

    private static void exibirMenuLogin(Scanner scanner) {
        System.out.println("\n······ Acessar sistema ······");
        System.out.println("1. Fazer login");
        System.out.println("2. Fazer cadastro");
        System.out.println("3. Sair do Sistema");
        System.out.print("Escolha uma opção: ");

        int opcao = 0; // declarar e inicializar a variável
        try {
            opcao = Integer.parseInt(scanner.nextLine()); //converter string para int
        } catch (NumberFormatException e) { //se o usuário der uma de esperto
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
                System.out.println("Erro! Opção inválida.");
        }
    }
    
    private static void realizarLogin(Scanner scanner) {
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();
        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        
        Cliente clienteAutenticado = clientes.stream() //autentificar pra ele não dar uma de esperto
            .filter(c -> c.verificarCredenciais(email, cpf))
            .findFirst()
            .orElse(null);
            
        if (clienteAutenticado == null) {
            System.out.println("Falha no login: E-mail ou CPF incorretos. Tente novamente.");
            return;
        }
        
        //quem manda
        final String CPF_GERENTE = "111.111.111-11"; // Louis Gouveia
        final String CPF_VENDEDOR = "222.222.222-22"; // Ana Vitoria
       

        System.out.println("\nSelecione o tipo de acesso:");
        System.out.println("1. GERENTE");
        System.out.println("2. VENDEDOR");
        System.out.println("3. CLIENTE");
        System.out.print("Digite a opção: ");
        String opcaoPapel = scanner.nextLine();
        String papelEscolhido;
        
        switch (opcaoPapel) {
            case "1": 
                papelEscolhido = "GERENTE"; 
                
                //pra não tentar se passar de gerente
                if (!clienteAutenticado.getCpf().equals(CPF_GERENTE)) {
                    System.out.println("Acesso negado. Você não tem permissão de GERENTE.");
                    return;
                }
                break;
                
            case "2": 
                papelEscolhido = "VENDEDOR"; 
                
                //pra não tentar se passar de vendedor (gerente aqui mexe em tudo)
                boolean isGerenteOuVendedor = clienteAutenticado.getCpf().equals(CPF_VENDEDOR) || 
                                             clienteAutenticado.getCpf().equals(CPF_GERENTE);
                
                if (!isGerenteOuVendedor) {
                    System.out.println("Acesso negado. Você não tem permissão de VENDEDOR.");
                    return;
                }
                break;
                
            case "3": 
                papelEscolhido = "CLIENTE"; 
                break;
                
            default:
                System.out.println("Opção de papel inválida.");
                return;
        }
        
        //quando der tudo certo
        Usuario.getInstancia().fazerLogin(clienteAutenticado, papelEscolhido);
    }

    private static void fazerCadastro(Scanner scanner) {
        System.out.println("\n······ NOVO CADASTRO ······");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        if (clientes.stream().anyMatch(c -> c.getCpf().equals(cpf))) { //vê se é o mesmo cpf cadastrado ou não
             System.out.println("Erro de cadastro: CPF já existe.");
             return;
        }
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        Cliente novoCliente = new Cliente(proximoIdCliente++, nome, cpf, email);
        clientes.add(novoCliente);
        novoCliente.cadastrar(); 
        
        System.out.println("Cadastro concluído! Faça login como CLIENTE para continuar.");
    }
    private static void exibirMenuCliente(Scanner scanner) {
        Usuario sessao = Usuario.getInstancia();
        System.out.println("\n······ MENU DO CLIENTE ······");
        System.out.println("Usuário: " + sessao.getClienteLogado().getNome());
        System.out.println("1. Comprar produto"); 
        System.out.println("2. Consultar produto"); 
        System.out.println("3. Consultar minhas compras"); 
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

    private static void exibirMenuVendedor(Scanner scanner) {
        if (!Usuario.getInstancia().isVendedor() && !Usuario.getInstancia().isGerente()) { //se não tem as permissões
             System.out.println("Acesso negado.");
             return;
        }
        
        Usuario sessao = Usuario.getInstancia(); 
        String tipoUsuario = sessao.isGerente() ? "GERENTE" : "VENDEDOR";
        
        System.out.println("\n······ MENU DO VENDEDOR (" + tipoUsuario + ") ······");
        System.out.println("Usuário: " + sessao.getClienteLogado().getNome());
        System.out.println("1. Realizar venda"); 
        System.out.println("2. Consultar produto"); 
        System.out.println("3. Consultar vendas"); 
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
                realizarVendaParaCliente(scanner);
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
    
    private static void exibirMenuGerente(Scanner scanner) {
        if (!Usuario.getInstancia().isGerente()) {
             System.out.println("Acesso negado.");
             return;
        }
        
        System.out.println("\n······ MENU DO GERENTE ······");
        System.out.println("Usuário: " + Usuario.getInstancia().getClienteLogado().getNome());
        System.out.println("1. Gerenciar produto"); 
        System.out.println("2. Gerenciar usuario"); 
        System.out.println("3. Gerar relatórios de venda"); 
        System.out.println("4. Consultar todas vendas");
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
                menuGerenciarProduto(scanner);
                break;
            case 2:
                menuGerenciarCliente(scanner);
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

    private static void realizarVendaParaCliente(Scanner scanner) {
        if (clientes.isEmpty()) {
            System.out.println("Não há clientes cadastrados para realizar uma venda.");
            return;
        }
        
        System.out.println("\nLista de Clientes:");
        clientes.forEach(c -> System.out.printf("ID: %d | Nome: %s\n", c.getIdCliente(), c.getNome())); //loop pra imprimir o id e o nome de cada cliente

        System.out.print("Digite o ID do cliente: ");
        
        int idClienteLido; 
        try {
            idClienteLido = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de cliente inválido.");
            return;
        }

        final int idClienteFinal = idClienteLido; //usado dentro da expressão lambda do stream
        
        Cliente clienteVenda = clientes.stream()
            .filter(c -> c.getIdCliente() == idClienteFinal) 
            .findFirst() //retorna o primeiro (único (tem que ser)) resultado encontrado
            .orElse(null);

        if (clienteVenda == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        realizarNovaVenda(scanner, clienteVenda); //aí sim acontece a venda real com o cliente certo
    }
    
    private static void realizarNovaVenda(Scanner scanner, Cliente cliente) {
        if (produtos.isEmpty()) {
            System.out.println("Não é possível fazer a venda: Não há produtos cadastrados.");
            return;
        }

        Venda novaVenda = new Venda(proximoIdVenda++, cliente);
        String continuar = "S";

        System.out.println("\n······ INICIANDO VENDA #" + novaVenda.getIdPedido() + " para " + cliente.getNome() + " ······");
        
        while (continuar.equalsIgnoreCase("S")) {
            consultarProdutos(); 
            
            System.out.print("Digite o ID do produto para adicionar (0 para parar): ");
            int idProduto = 0;
            try {
                idProduto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID de produto inválido.");
                continue;
            }
            
            if (idProduto == 0) break;

            final int idProdutoFinal = idProduto; 
            Produto produtoSelecionado = produtos.stream()
                .filter(p -> p.getIdProduto() == idProdutoFinal) 
                .findFirst() 
                .orElse(null);

            if (produtoSelecionado == null) {
                System.out.println("Produto com ID " + idProduto + " não encontrado.");
                continue; //continua o processo de adição de itens
            }

            System.out.print("Quantidade desejada de " + produtoSelecionado.getNome() + ": ");
            int quantidade = 0;
             try {
                quantidade = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida.");
                continue;
            }

            if (quantidade > produtoSelecionado.getEstoque()) { //pediu mais do que a loja tem
                System.out.println("Quantidade indisponível! Estoque atual: " + produtoSelecionado.getEstoque());
            } else if (quantidade > 0) {
                novaVenda.adicionarItem(new ItemVenda(produtoSelecionado, quantidade));
            }

            System.out.print("Adicionar outro item? (S/N): ");
            continuar = scanner.nextLine();
        }

        if (novaVenda.getItens().isEmpty()) { //verifica se a lista de itens da venda atual está vazia
            System.out.println("Venda cancelada ou sem itens.");
            proximoIdVenda--; //se a venda não rolou, devolve o id para que a próxima venda use
            return;
        }

        System.out.printf("\nTOTAL DA VENDA: R$%.2f\n", novaVenda.calcularTotal());
        System.out.print("Deseja finalizar a venda e pagar? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            if (simularPagamento(novaVenda, scanner)) {
                vendas.add(novaVenda); 
            }
        } else {
            System.out.println("Venda não finalizada.");
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
        
        List<Venda> vendasFiltradas; //tem que ser filtrada pq depende do nivel de permissão
        Usuario sessao = Usuario.getInstancia();
        
        if (sessao.getPapel().equals("CLIENTE")) {
             final int idClienteLogado = sessao.getClienteLogado().getIdCliente(); //pegar o id do cliente logado e armazenar na variável final (pra usar no stream)
             vendasFiltradas = vendas.stream() 
                 .filter(v -> v.getCliente().getIdCliente() == idClienteLogado)
                 .collect(Collectors.toList()); //reúne as vendas filtradas em uma nova lista
        } else {
             vendasFiltradas = vendas; //vendedor e gerente podem ver todas
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
                v.getDataHora().toLocalTime().toString().substring(0, 5), 
                v.getValorTotal(), v.getStatus());
        }
        System.out.println("····································");
    }

    private static void menuGerenciarProduto(Scanner scanner) {
        if (!Usuario.getInstancia().isGerente()) {
             System.out.println("Acesso negado.");
             return;
        }
        
        System.out.println("\n······ GERENCIAR PRODUTOS ······");
        System.out.println("1. Adicionar novo brinquedo");
        System.out.println("2. Listar todos");
        System.out.println("3. Atualizar detalhes do produto");
        System.out.println("4. Atualizar estoque manual");
        System.out.println("5. Remover produto");
        System.out.println("6. Voltar ao Menu Gerente");
        System.out.print("Escolha uma opção: ");
        
        int opcao = 0;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }
        
        switch (opcao) {
            case 1:
                adicionarNovoBrinquedo(scanner);
                break;
            case 2:
                consultarProdutos();
                break;
            case 3:
                atualizarProdutoDetalhes(scanner);
                break;
            case 4:
                atualizarEstoqueManual(scanner);
                break;
            case 5:
                removerProduto(scanner);
                break;
            case 6:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }
    
    private static void adicionarNovoBrinquedo(Scanner scanner) {
    	System.out.println("\n······ ADICIONAR NOVO BRINQUEDO ······");
        System.out.print("Nome do brinquedo: ");
        String nome = scanner.nextLine();
        
        float preco = 0.0f;
        try {
            System.out.print("Preço unitário: R$");
            preco = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido. Usando 0.0f.");
        }


        int estoque = 0;
        try {
            System.out.print("Estoque inicial: ");
            estoque = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Estoque inválido. Usando 0.");
        }

        Produto novoBrinquedo = fabrica.criarProduto(0, nome, preco, estoque);
        produtos.add(novoBrinquedo);
        System.out.println("Brinquedo '" + novoBrinquedo.getNome() + "' cadastrado com sucesso!");
    }

    private static void atualizarProdutoDetalhes(Scanner scanner) {
        consultarProdutos();
        System.out.println("\n······ ATUALIZAR PRODUTO ······");
        System.out.print("Digite o ID do produto para atualizar: ");
        int idProduto = 0;
        try {
            idProduto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produto inválido.");
            return;
        }

        final int idProdutoFinal = idProduto; 
        Produto produtoSelecionado = produtos.stream()
            .filter(p -> p.getIdProduto() == idProdutoFinal)
            .findFirst()
            .orElse(null);

        if (produtoSelecionado == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        
        System.out.println("Produto atual: " + produtoSelecionado.getNome() + " | R$" + produtoSelecionado.getPrecoUnitario());
        
        System.out.print("Novo nome (deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            produtoSelecionado.setNome(novoNome);
        }

        System.out.print("Novo preço unitário (deixe em branco para manter): ");
        String novoPrecoStr = scanner.nextLine();
        if (!novoPrecoStr.trim().isEmpty()) {
            try {
                float novoPreco = Float.parseFloat(novoPrecoStr);
                produtoSelecionado.setPrecoUnitario(novoPreco);
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido. O preço não foi alterado.");
            }
        }
        
        System.out.println("Produto atualizado!");
        produtoSelecionado.consultarDetalhes();
    }
    
    //tipo assim, se acontecer uma perda, quebra, ou até repor uma venda, aí vai ser necessário usar esse
    private static void atualizarEstoqueManual(Scanner scanner) {
        consultarProdutos();
        System.out.println("\n······ ATUALIZAR ESTOQUE MANUAL ······");
        System.out.print("Digite o ID do produto para ajuste de estoque: ");
        int idProduto = 0;
        try {
            idProduto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produto inválido.");
            return;
        }
        
        final int idProdutoFinal = idProduto; 
        Produto produtoSelecionado = produtos.stream()
            .filter(p -> p.getIdProduto() == idProdutoFinal)
            .findFirst()
            .orElse(null);

        if (produtoSelecionado == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Ajuste de quantidade (+ para entrada, - para saída): ");
        int ajuste = 0;
        try {
            ajuste = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ajuste de quantidade inválido.");
            return;
        }
        
        if (produtoSelecionado.getEstoque() + ajuste < 0) { //se a soma do estoque atual com o ajuste for menor que zero
            System.out.println("ERRO: O ajuste resultaria em estoque negativo. Operação cancelada.");
            return;
        }
        
        produtoSelecionado.atualizarEstoque(ajuste); 
        System.out.println("Estoque de " + produtoSelecionado.getNome() + " ajustado.");
    }

    private static void removerProduto(Scanner scanner) {
        consultarProdutos();
        System.out.println("\n······ REMOVER PRODUTO ······");
        System.out.print("Digite o ID do produto para REMOVER: ");
        int idProduto = 0;
        try {
            idProduto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produto inválido.");
            return;
        }

        final int idProdutoFinal = idProduto; 
        Produto produtoParaRemover = produtos.stream()
            .filter(p -> p.getIdProduto() == idProdutoFinal)
            .findFirst()
            .orElse(null);

        if (produtoParaRemover == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        
        if (produtoParaRemover.getEstoque() > 0) { //não apagar se tiver produto ainda em estoque
            System.out.println("ERRO: Não é possível remover o produto. Estoque atual: " + produtoParaRemover.getEstoque() + ". Zere o estoque primeiro.");
            return;
        }

        if (produtos.remove(produtoParaRemover)) {
            System.out.println(produtoParaRemover.getNome() + " removido com sucesso!");
        } else {
             System.out.println("Erro ao remover produto.");
        }
    }
    
    private static void menuGerenciarCliente(Scanner scanner) {
        if (!Usuario.getInstancia().isGerente()) {
             System.out.println("Acesso negado.");
             return;
        }
        
        System.out.println("\n······ GERENCIAR USUÁRIOS ······");
        System.out.println("1. Listar usuários");
        System.out.println("2. Atualizar cadastro");
        System.out.println("3. Remover usuário");
        System.out.println("4. Voltar ao Menu Gerente");
        System.out.print("Escolha uma opção: ");
        
        int opcao = 0;
        try {
            opcao = Integer.parseInt(scanner.nextLine()); 
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }
        
        switch (opcao) {
            case 1:
                listarClientes();
                break;
            case 2:
                atualizarClienteCadastro(scanner);
                break;
            case 3:
                removerCliente(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }
    
    private static void listarClientes() {
        System.out.println("\n······ CLIENTES CADASTRADOS ······");
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        clientes.forEach(c -> System.out.printf("ID: %d | Nome: %s | CPF: %s | Email: %s\n", 
            c.getIdCliente(), c.getNome(), c.getCpf(), c.getemail()));
        System.out.println("····································");
    }
    
    private static void atualizarClienteCadastro(Scanner scanner) {
        listarClientes();
        System.out.println("\n······ ATUALIZAR CADASTRO DE CLIENTE ······");
        System.out.print("Digite o ID do cliente para atualizar: ");
        int idCliente = 0;
        try {
            idCliente = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de cliente inválido.");
            return;
        }

        final int idClienteFinal = idCliente; 
        Cliente clienteSelecionado = clientes.stream()
            .filter(c -> c.getIdCliente() == idClienteFinal)
            .findFirst()
            .orElse(null);

        if (clienteSelecionado == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        
        System.out.println("Cliente: " + clienteSelecionado.getNome() + " | Email: " + clienteSelecionado.getemail());
        
        System.out.print("Novo nome (deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            clienteSelecionado.setNome(novoNome);
        }

        System.out.print("Novo E-mail (deixe em branco para manter): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.trim().isEmpty()) {
            clienteSelecionado.setEmail(novoEmail);
        }
        
        clienteSelecionado.atualizarCadastro();
    }
    
    private static void removerCliente(Scanner scanner) {
        listarClientes();
        System.out.println("\n······ REMOVER CLIENTE ······");
        System.out.print("Digite o ID do cliente para REMOVER: ");
        int idCliente = 0;
        try {
            idCliente = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de cliente inválido.");
            return;
        }
        
        final int idClienteFinal = idCliente; 
        Cliente clienteParaRemover = clientes.stream()
            .filter(c -> c.getIdCliente() == idClienteFinal)
            .findFirst()
            .orElse(null);

        if (clienteParaRemover == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        
        //contar o número de vendas pendentes que possam impeder a remoção do cliente
        long vendasPendentes = vendas.stream()
            .filter(v -> v.getCliente().getIdCliente() == idClienteFinal && !v.getStatus().equals("CONCLUIDA!"))
            .count(); //quantos elementos sobraram após o filtro
            
        if (vendasPendentes > 0) {
            System.out.println("ERRO: Não é possível remover o cliente. Possui " + vendasPendentes + " vendas não concluídas.");
            return;
        }

        if (clientes.remove(clienteParaRemover)) {
            System.out.println("Cliente " + clienteParaRemover.getNome() + " removido com sucesso!");
        } else {
             System.out.println("Erro ao remover cliente.");
        }
    }

    private static void gerarRelatorios() {
        if (!Usuario.getInstancia().isGerente()) {
             System.out.println("Acesso negado.");
             return;
        }
        
        System.out.println("\n······ RELATÓRIO DE VENDAS ······");
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda concluída para gerar relatório.");
            return;
        }
        
        // retorna as vendas que estão como concluidas
        long totalVendas = vendas.stream().filter(v -> "CONCLUIDA!".equals(v.getStatus())).count(); 
        double valorTotalArrecadado = vendas.stream()
            .filter(v -> "CONCLUIDA!".equals(v.getStatus())) 
            .mapToDouble(Venda::getValorTotal) // mapeia para um stream de doubles
            .sum(); //soma tudo
            
        System.out.printf("Total de vendas concluídas: %d\n", totalVendas);
        System.out.printf("Valor total arrecadado: R$%.2f\n", valorTotalArrecadado);
        System.out.println("\nProdutos mais vendidos:");
        
        vendas.stream()
            .filter(v -> "CONCLUIDA!".equals(v.getStatus())) 
            .flatMap(v -> v.getItens().stream()) //vendas para itens da venda
            .collect(Collectors.groupingBy(item -> item.getProduto().getNome(),  //agrupa os ItemVendas, usando o nome do produto como a chave do mapa
                             Collectors.summingInt(ItemVenda::getQuantidade)))  //para cada produto vai ser somada a quantidade vendida
            .entrySet().stream() //ordenar
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) //do mais vendido para o menos vendido
            .forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue() + " unidades")); //imprime o nome do produto (e.getKey()) e a quantidade total (e.getValue())
            
        System.out.println("····································");
    }

   private static boolean simularPagamento(Venda venda, Scanner scanner) {
        System.out.println("\n--- PAGAMENTO ---");
        System.out.printf("Valor a pagar: R$%.2f\n", venda.getValorTotal());
        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1. Cartão de crédito");
        System.out.println("2. PIX");
        System.out.println("3. Dinheiro físico");
        System.out.print("Opção: ");
        String opcaoPagamento = scanner.nextLine();
        
        InterfacePagamento estrategia;
        String dadosPagamento = "";

        switch (opcaoPagamento) {
            case "1":
                estrategia = new PagamentoCartao();
                System.out.print("Digite o número do cartão: ");
                dadosPagamento = scanner.nextLine();
                break;
            case "2":
                estrategia = new PagamentoPix();
                System.out.print("Digite a chave PIX: ");
                dadosPagamento = scanner.nextLine();
                break;
            case "3":
                estrategia = new PagamentoDinheiro();
                System.out.print("Digite o valor recebido em dinheiro: R$");
                dadosPagamento = scanner.nextLine(); 
                break;
            default:
                System.out.println("Opção de pagamento inválida. Venda cancelada.");
                return false;
        }
        
        venda.setEstrategiaPagamento(estrategia);
        return venda.processarVenda(dadosPagamento);
    }
}