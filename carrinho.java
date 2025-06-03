
import java.util.*;

class Product {
    String id;
    String name;
    double price;

    Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotalPrice() {
        return product.price * quantity;
    }
}

class DiscountCalculator {
    public static double calcularDesconto(double subtotal) {
        if (subtotal > 200) {
            return subtotal * 0.10;
        }
        return 0;
    }
}

class CartService {
    private List<CartItem> cart = new ArrayList<>();

    public void adicionarProduto(Product produto, int quantidade) {
        for (CartItem item : cart) {
            if (item.product.id.equals(produto.id)) {
                item.quantity += quantidade;
                return;
            }
        }
        cart.add(new CartItem(produto, quantidade));
    }

    public void removerProduto(String idProduto) {
        cart.removeIf(item -> item.product.id.equals(idProduto));
    }

    public void exibirCarrinho() {
        System.out.println("\n---------- CARRINHO ----------");
        if (cart.isEmpty()) {
            System.out.println("Carrinho vazio.");
            System.out.println("-----------------------------\n");
            return;
        }

        double subtotal = 0;
        for (CartItem item : cart) {
            System.out.printf("%s x%d = R$%.2f\n", item.product.name, item.quantity, item.getTotalPrice());
            subtotal += item.getTotalPrice();
        }

        double desconto = DiscountCalculator.calcularDesconto(subtotal);
        double total = subtotal - desconto;

        System.out.println("-----------------------------");
        System.out.printf("Subtotal: R$%.2f\n", subtotal);
        System.out.printf("Desconto: R$%.2f\n", desconto);
        System.out.printf("Total:    R$%.2f\n", total);
        System.out.println("-----------------------------\n");
    }

    public double calcularTotal() {
        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.getTotalPrice();
        }
        double desconto = DiscountCalculator.calcularDesconto(subtotal);
        return subtotal - desconto;
    }

    public void limparCarrinho() {
        cart.clear();
    }

    public boolean carrinhoVazio() {
        return cart.isEmpty();
    }
}

class ContaBancaria {
    private double saldo;

    public void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("⚠️ Valor de depósito inválido.");
            return;
        }
        saldo += valor;
        System.out.printf("✅ Depósito de R$%.2f realizado com sucesso!\n", valor);
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("⚠️ Valor de saque inválido.");
        } else if (valor > saldo) {
            System.out.println("❌ Saldo insuficiente para saque.");
        } else {
            saldo -= valor;
            System.out.printf("✅ Saque de R$%.2f realizado com sucesso!\n", valor);
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean pagar(double valor) {
        if (valor > saldo) {
            return false;
        }
        saldo -= valor;
        return true;
    }

    public void exibirSaldo() {
        System.out.printf("💰 Saldo atual: R$%.2f\n", saldo);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CartService carrinho = new CartService();
        ContaBancaria conta = new ContaBancaria();

        Product p1 = new Product("1", "Monitor", 150);
        Product p2 = new Product("2", "Teclado", 100);
        Product p3 = new Product("3", "Mouse", 50);

        int opcao;
        do {
            System.out.println("\n========== MENU ==========");
            System.out.println("1 - Adicionar produto");
            System.out.println("2 - Remover produto");
            System.out.println("3 - Ver carrinho");
            System.out.println("4 - Finalizar compra");
            System.out.println("5 - Consultar saldo");
            System.out.println("6 - Depositar");
            System.out.println("7 - Sacar");
            System.out.println("0 - Sair");
            System.out.println("==========================");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            System.out.println();

            switch (opcao) {
                case 1:
                    System.out.println("---------- PRODUTOS ----------");
                    System.out.println("1 - Monitor  (R$150)");
                    System.out.println("2 - Teclado  (R$100)");
                    System.out.println("3 - Mouse    (R$50)");
                    System.out.println("------------------------------");
                    System.out.print("Escolha o produto (1-3): ");
                    int escolha = scanner.nextInt();

                    System.out.print("Quantidade: ");
                    int qtd = scanner.nextInt();
                    System.out.println("------------------------------");

                    if (escolha == 1) carrinho.adicionarProduto(p1, qtd);
                    else if (escolha == 2) carrinho.adicionarProduto(p2, qtd);
                    else if (escolha == 3) carrinho.adicionarProduto(p3, qtd);
                    else {
                        System.out.println("❌ Produto inválido.");
                        break;
                    }

                    System.out.println("✅ Produto adicionado ao carrinho!\n");
                    break;

                case 2:
                    System.out.print("Digite o ID do produto para remover: ");
                    String idRemover = scanner.next();
                    carrinho.removerProduto(idRemover);
                    System.out.println("✅ Produto removido (se existia no carrinho).\n");
                    break;

                case 3:
                    carrinho.exibirCarrinho();
                    break;

                case 4:
                    System.out.println("---------- FINALIZAÇÃO ----------");
                    if (carrinho.carrinhoVazio()) {
                        System.out.println("⚠️ O carrinho está vazio.");
                        break;
                    }
                    double totalCompra = carrinho.calcularTotal();
                    System.out.printf("Total da compra: R$%.2f\n", totalCompra);

                    if (conta.pagar(totalCompra)) {
                        carrinho.limparCarrinho();
                        System.out.println("✅ Compra finalizada com sucesso!");
                        conta.exibirSaldo();
                    } else {
                        System.out.println("❌ Saldo insuficiente. Deposite mais dinheiro na conta.");
                    }
                    System.out.println("---------------------------------\n");
                    break;

                case 5:
                    conta.exibirSaldo();
                    break;

                case 6:
                    System.out.print("Digite o valor para depósito: R$");
                    double valorDeposito = scanner.nextDouble();
                    conta.depositar(valorDeposito);
                    break;

                case 7:
                    System.out.print("Digite o valor para saque: R$");
                    double valorSaque = scanner.nextDouble();
                    conta.sacar(valorSaque);
                    break;

                case 0:
                    System.out.println("👋 Saindo... Obrigado por usar o sistema!");
                    break;

                default:
                    System.out.println("⚠️ Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}
