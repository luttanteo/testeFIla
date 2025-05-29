import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Evento {
    private int totalIngressos;
    private int ingressosIdosos;
    private int ingressosVIPs;
    private int ingressosGeral;

    private Queue<String> filaVIP = new LinkedList<>();
    private Queue<String> filaIdosos = new LinkedList<>();
    private Queue<String> filaGeral = new LinkedList<>();

    // Quantidade a ser chamada por ciclo
    private int atenderVIP;
    private int atenderIdoso;
    private int atenderGeral;

    public Evento(int totalIngressos, double percentIdosos, double percentVIP,
                  int atenderVIP, int atenderIdoso, int atenderGeral) {
        if (percentIdosos > (0.2 * totalIngressos) || percentVIP > (0.3 * totalIngressos) || (totalIngressos - (percentIdosos + percentVIP)) < (0.5 * totalIngressos)) {
            throw new IllegalArgumentException("Percentuais inválidos.");
        }

        this.totalIngressos = totalIngressos;
        this.ingressosIdosos = (int) (totalIngressos * percentIdosos);
        this.ingressosVIPs = (int) (totalIngressos * percentVIP);
        this.ingressosGeral = totalIngressos - (ingressosIdosos + ingressosVIPs);

        this.atenderVIP = atenderVIP;
        this.atenderIdoso = atenderIdoso;
        this.atenderGeral = atenderGeral;
    }

    public void adicionarCliente(String nome, String categoria) {
        switch (categoria.toLowerCase()) {
            case "vip":
                filaVIP.add(nome);
                break;
            case "idoso":
                filaIdosos.add(nome);
                break;
            case "geral":
                filaGeral.add(nome);
                break;
            default:
                System.out.println("Categoria inválida.");
        }
    }

    public void venderIngressos() {
        while (ingressosVIPs + ingressosIdosos + ingressosGeral > 0) {
            // Atender VIPs
            venderIngressosPorCategoria(filaVIP, "VIP", atenderVIP, ingressosVIPs);
            // Atender Idosos
            venderIngressosPorCategoria(filaIdosos, "Idoso", atenderIdoso, ingressosIdosos);
            // Atender Geral
            venderIngressosPorCategoria(filaGeral, "Geral", atenderGeral, ingressosGeral);

            // Se os ingressos acabaram, encerra
            if (ingressosVIPs <= 0 && ingressosIdosos <= 0 && ingressosGeral <= 0) {
                break;
            }
        }
        System.out.println("Venda finalizada.");
    }

    private void venderIngressosPorCategoria(Queue<String> fila, String categoria,
                                             int quantidade, int ingressosDisponiveis) {
        int vendidos = 0;
        while (vendidos < quantidade && ingressosDisponiveis > 0 && !fila.isEmpty()) {
            System.out.println("Ingresso vendido para " + categoria + ": " + fila.poll());
            vendidos++;
            // Atualizando o número de ingressos disponíveis para cada categoria
            if (categoria.equals("VIP")) {
                ingressosVIPs--;
            } else if (categoria.equals("Idoso")) {
                ingressosIdosos--;
            } else if (categoria.equals("Geral")) {
                ingressosGeral--;
            }
        }
    }

    public void statusFila() {
        System.out.println("Fila VIP: " + filaVIP);
        System.out.println("Fila Idosos: " + filaIdosos);
        System.out.println("Fila Geral: " + filaGeral);
    }
}

public class fila {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Receber as configurações do evento
        System.out.print("Informe o número total de ingressos: ");
        int totalIngressos = scanner.nextInt();

        double percentIdosos, percentVIP;

        // Percentual de ingressos para Idosos
        do {
            System.out.print("Informe o percentual de ingressos para Idosos (máximo 20%): ");
            percentIdosos = scanner.nextDouble();
        } while (percentIdosos > (0.2 * totalIngressos)); // Correção aqui para permitir até 20%

        // Percentual de ingressos para VIPs
        do {
            System.out.print("Informe o percentual de ingressos para VIPs (máximo 30%): ");
            percentVIP = scanner.nextDouble();
        } while (percentVIP > (0.3 * totalIngressos)); // Correção aqui para permitir até 30%

        // Percentual de ingressos para o público geral (será calculado automaticamente)
        double percentGeral = 1 - (percentIdosos + percentVIP);

        // Garantir que o percentual de Geral é no mínimo 50%
        if (percentGeral < 0.5) {
            percentGeral = 0.5;
            System.out.println("Ajustando o percentual de ingressos para Geral para 50%");
        }

        // Quantidade de clientes a serem atendidos por ciclo
        System.out.print("Informe quantos VIPs devem ser atendidos por ciclo: ");
        int atenderVIP = scanner.nextInt();
        System.out.print("Informe quantos Idosos devem ser atendidos por ciclo: ");
        int atenderIdoso = scanner.nextInt();
        System.out.print("Informe quantos clientes do público Geral devem ser atendidos por ciclo: ");
        int atenderGeral = scanner.nextInt();

        // Criando o evento
        Evento evento = new Evento(totalIngressos, percentIdosos, percentVIP,
                                   atenderVIP, atenderIdoso, atenderGeral);

        // Adicionar clientes nas filas
        System.out.println("Digite os clientes para adicionar às filas. Digite 'fim' para encerrar.");
        scanner.nextLine(); // Limpar o buffer do scanner
        while (true) {
            System.out.print("Digite o nome do cliente (ou 'fim' para encerrar): ");
            String nome = scanner.nextLine();
            if (nome.equalsIgnoreCase("fim")) {
                break;
            }
            System.out.print("Digite a categoria do cliente (VIP, Idoso, Geral): ");
            String categoria = scanner.nextLine();
            evento.adicionarCliente(nome, categoria);
        }

        // Realiza a venda de ingressos com a lógica cíclica
        evento.venderIngressos();

        // Exibe o status das filas após a venda
        evento.statusFila();

        scanner.close();
    }
}
