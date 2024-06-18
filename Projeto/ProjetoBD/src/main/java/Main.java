import DAO.*;
import Model.Biblioteca;
import Model.Cliente;
import Model.Dono;
import Model.Livro;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();
        DonoDAO donoDAO = new DonoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        LivroDAO livroDAO = new LivroDAO();
        BibliotecaHasLivroDAO bibliotecaHasLivroDAO = new BibliotecaHasLivroDAO();

        //vendo quantidades de bibliotecas que existem
        int t = bibliotecaDAO.quantidadeBibliotecas();

        //criando dois donos
        Dono d1 = new Dono("Lucas","01234567890");
        Dono d2 = new Dono("Ana Clara","12345678910");

        //criando duas bibliotecas
        Biblioteca b1;
        Biblioteca b2;

        //vendo numero de bibliotecas, como nao pode inserir, sempre tera duas
        //se nao tiver eh porque deu drop no database
        //ai ira cria-las
        if(t == 0) {

            //colocando o dono no banco de dados
            donoDAO.insertDono(d1);
            donoDAO.insertDono(d2);

            //criando as duas bibliotecas, com nome, cidade e qual dono eh de cada
            b1 = new Biblioteca("Livros&CIA", "Paraisopolis", d1);
            b2 = new Biblioteca("CIA&Livros", "Ouros", d1);

            //criando 4 livros
            Livro l1 = new Livro("Harry Potter");
            Livro l2 = new Livro("Percy Jackson");
            Livro l3 = new Livro("O Pequeno Principe");
            Livro l4 = new Livro("Alienista");

            //colocando os livros em uma biblioteca
            bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b1, l1);
            bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b1, l2);
            bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b2, l3);
            bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b2, l4);
        }
        //se as bibliotecas ja estiverem sido criadas
        else{
            //resgatando nome da biblioteca
            b1 = bibliotecaDAO.getBibliotecaByName("Livros&Cia");
            b2 = bibliotecaDAO.getBibliotecaByName("CIA&Livros");
        }

        //variavel do loop
        boolean comp = true;
        //criando um cliente
        Cliente cliente = new Cliente();

        //loop verificacao cliente
        while(comp) {

            //menu de verificacao do cliente
            System.out.println("Bem vindo Cliente");
            System.out.println("Cliente novo?");
            System.out.println("1 - sim");
            System.out.println("2 - nao");
            int check = sc.nextInt();
            sc.nextLine();
            //se for cliente novo
            if (check == 1) {
                System.out.print("Qual seu nome? ");
                String nomeCLiente = sc.nextLine();
                cliente = new Cliente(nomeCLiente);
                System.out.println("Sua matricula sera: "+cliente.getMatricula());
                comp = false;
            }
            //se ja for cliente
            else if (check == 2) {
                clienteDAO.selectCliente();
                System.out.println("Qual a sua matricula?");
                int matri = sc.nextInt();
                if(clienteDAO.busca(matri) == null){
                    System.out.println("Matricula invalida");
                }
                else{
                    cliente = new Cliente(clienteDAO.busca(matri));
                    System.out.println("Bem vindo de volta "+cliente.getNome());
                    System.out.println("Matricula: "+cliente.getMatricula());
                    comp = false;
                }
            }
            //se for um numero invalido
            else{
                System.out.println("Numero invalido");
            }
        }

        boolean flag = true;
        int opcao;
        int atualizaNome;
        int deleta;
        int escolheLivro;
        int devolver;

        //menu
        while(flag){
            System.out.println("Menu");
            System.out.println("1 - Inserir livro");
            System.out.println("2 - Alugar livro");
            System.out.println("3 - Atualizar nome");
            System.out.println("4 - Deletar");
            System.out.println("5 - Mostra informacoes");
            System.out.println("6 - Devolver");
            System.out.println("7 - Sair");
            opcao = sc.nextInt();
            sc.nextLine();
            //vendo qual a opcao que o cliente quis
            switch (opcao){
                //se quiser inserir um livro
                case 1:
                    //nome do livro
                    System.out.print("Nome do Livro: ");
                    String nomeLivro = sc.nextLine();
                    //criando o livro
                    Livro livro = new Livro(nomeLivro);
                    //vendo qual biblioteca faz parte
                    System.out.println("A qual biblioteca ele faz parte? (b1 ou b2)");
                    String qb = sc.nextLine();
                    //adicionando o livro a biblioteca
                    if(qb.equals("b1")) {
                        bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b1, livro);
                    } else if (qb.equals("b2")){
                        bibliotecaHasLivroDAO.insertBibliotecaHasLivro(b2, livro);
                    } else{
                        System.out.println("Numero invalido");
                    }
                    break;
                    //se quiser alugar um livro
                case 2:
                    //vendo qual biblioteca o livro faz parte
                    System.out.println("Biblioteca: b1 ou b2?");
                    String numbiblioteca = sc.nextLine();
                    if(numbiblioteca.equals("b1")){
                        //mostrando os livros da biblioteca 1
                        bibliotecaDAO.selecionada(1);
                        System.out.println("Qual o numero do livro deseja pegar?");
                        escolheLivro = sc.nextInt();
                        //alugando o livro
                        livroDAO.aluga(cliente.getMatricula(),escolheLivro);
                    } else if (numbiblioteca.equals("b2")) {
                        //mostrando os livros da biblioteca 2
                        bibliotecaDAO.selecionada(2);
                        System.out.println("Qual o numero do livro deseja pegar?");
                        escolheLivro = sc.nextInt();
                        //alugando o livro
                        livroDAO.aluga(cliente.getMatricula(),escolheLivro);
                    }else{
                        System.out.println("Invalido");
                    }
                    break;
                    //se quiser atualizar o nome
                case 3:
                    System.out.println("Do que deseja atualizar o nome?");
                    System.out.println("1 - Cliente");
                    System.out.println("2 - Biblioteca");
                    System.out.println("3 - Livro");
                    atualizaNome = sc.nextInt();
                    sc.nextLine();
                    //vendo qual opcao o cliente quis
                    switch (atualizaNome) {
                        //mudar nome do cliente
                        case 1:
                            System.out.print("Deseja atualizar para que nome? ");
                            String novoCliente = sc.nextLine();
                            try {
                                clienteDAO.updateClienteNome(cliente.getMatricula(), novoCliente);
                            } catch (Exception e) {
                                System.out.println("Erro!!!!!");
                            }
                            break;
                            //mudar nome da biblioteca
                        case 2:
                            System.out.print("De que biblioteca deseja atualizar o nome? (Numero)");
                            bibliotecaDAO.selectBiblioteca();
                            int qualBiblioteca = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Deseja atualizar para que nome? ");
                            String novaBiblioteca = sc.nextLine();
                            try {
                                bibliotecaDAO.updateBibliotecaNome(qualBiblioteca, novaBiblioteca);
                            } catch (Exception e) {
                                System.out.println("Erro!!!!!");
                            }
                            break;
                            //mudar nome do livro
                        case 3:
                            System.out.println("Numero do livro que deseja atualizar o nome");
                            livroDAO.selectLivro();
                            int qualLivro = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Deseja atualizar para que nome? ");
                            String novoLivro = sc.nextLine();
                            try {
                                livroDAO.updateLivroNome(qualLivro, novoLivro);
                            } catch (Exception e) {
                                System.out.println("Erro!!!!!");
                            }
                            break;
                        default:
                            System.out.println("Numero invalido");
                    }
                    break;
                    //se quiser deletar
                case 4:
                    System.out.println("O que deseja deletar?");
                    System.out.println("1 - Cliente");
                    System.out.println("2 - Livro");
                    deleta = sc.nextInt();
                    sc.nextLine();
                    //vendo o que o cliente quer
                    switch (deleta) {
                        //caso queira deleter um cliente
                        case 1:
                            System.out.println("Deseja deletar que Cliente? (Numero)");
                            //mostrando os clientes
                            clienteDAO.selectCliente();
                            int deletarCliente = sc.nextInt();
                            try {
                                //tentando deletar
                                boolean c = clienteDAO.deleteCliente(deletarCliente);
                                if(c){
                                    flag = false;
                                }
                            } catch (Exception e){
                                System.out.println("Erro!!!!!");
                            }
                            break;
                            //caso queira deletar um livro
                        case 2:
                            System.out.println("Deseja deletar que livro? (Numero)");
                            //mostrando os livros
                            livroDAO.selectLivro();
                            int deletarLivro = sc.nextInt();
                            try {
                                //tentando deletar o livro
                                livroDAO.deleteLivro(deletarLivro);
                            } catch (Exception e){
                                System.out.println("Erro!!!!!");
                            }
                            break;
                        default:
                            System.out.println("Numero invalido");
                    }
                    break;
                    //se quiser ver as informacoes
                case 5:
                    System.out.println("Deseja ver qual tabela?");
                    System.out.println("1 - Dono");
                    System.out.println("2 - Biblioteca");
                    System.out.println("3 - Livro");
                    System.out.println("4 - Cliente");
                    int tabela = sc.nextInt();
                    //vendo qual tabela o cliente quer ver
                    switch (tabela){
                        //caso for a de dono
                        case 1:
                            System.out.println("Tabela de donos");
                            //mostrando a tabela de donos
                            donoDAO.selectDono();
                            break;
                            //caso for a de bibliotecas
                        case 2:
                            System.out.println("Tabela de bibliotecas");
                            //mostrando a tabela de bibliotecas
                            bibliotecaDAO.selectBiblioteca();
                            break;
                            //caso for a de livros
                        case 3:
                            System.out.println("Tabela de livros");
                            //mostrando a tabela de livros
                            livroDAO.selectLivro();
                            break;
                            //caso for a de clientes
                        case 4:
                            System.out.println("Tabela de clientes");
                            //mostrando a tabela de clientes
                            clienteDAO.selectCliente();
                            break;
                        default:
                            System.out.println("Numero invalido");
                    }
                break;
                    //se quiser devolver o livro
                case 6:
                    System.out.println("Qual livro deseja devolver?");
                    livroDAO.livrosAlugados(cliente.getMatricula());
                    devolver = sc.nextInt();
                    //devolvendo
                    livroDAO.devolver(cliente.getMatricula(), devolver);
                    break;
                    //se quiser sair
                case 7:
                    flag = false;
                    break;
                default:
                    System.out.println("Numero invalido");
            }
        }
        //fechando o scanner
        sc.close();
    }
}