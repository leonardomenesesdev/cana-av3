import java.io.*;
import java.util.*;
public class MainEmpirica {
    public static void main(String[] args) throws IOException {
        System.out.println("Comparando algoritmos de fusão real de arquivos:\n");

        // -----------------------------------------------------------------
        // TESTE 1: N PEQUENO (Viável para Força Bruta)
        // -----------------------------------------------------------------
        // Vamos usar uma lista de tamanhos fixos para garantir que
        // a força bruta e o sequencial sejam comparados nos mesmos dados.
        List<Integer> sizesSmall = Arrays.asList(50, 10, 30, 20, 40); // n=5
        runTest("Teste N=5 (Fixo)", sizesSmall, true);

        // -----------------------------------------------------------------
        // TESTE 2: N MÉDIO (Limite da Força Bruta)
        // -----------------------------------------------------------------
        // Com n=8, a força bruta já será BEM lenta.
        List<Integer> sizesMedium = Arrays.asList(10, 60, 20, 80, 30, 50, 40, 70); // n=8
        runTest("Teste N=8 (Fixo)", sizesMedium, true);

        // -----------------------------------------------------------------
        // TESTE 3: N GRANDE (Inviável para Força Bruta)
        // -----------------------------------------------------------------
        // Aqui, n=50. A força bruta travaria por muito tempo.
        List<Integer> sizesLarge = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            sizesLarge.add(rand.nextInt(1000) + 10); // Tamanhos de 10 a 1010 linhas
        }
        runTest("Teste N=50 (Randômico)", sizesLarge, true); // false = não rodar força bruta
    }

    /**
     * Executa um cenário de teste completo.
     * @param testName Nome do teste
     * @param lineCounts Lista de quantas linhas cada arquivo de teste deve ter
     * @param runBruteForce Se true, executa o algoritmo de força bruta (lento)
     */
    private static void runTest(String testName, List<Integer> lineCounts, boolean runBruteForce) throws IOException {
        System.out.println("=================================================");
        System.out.println("Iniciando: " + testName);
        System.out.println("=================================================");

        String outputDir = "output_" + testName.replaceAll("[^a-zA-Z0-9]", "_");
        new File(outputDir).mkdirs();

        List<File> files = null;
        try {
            // 1. Criar arquivos de teste
            files = createTestFiles(lineCounts);
            System.out.println("Arquivos criados (linhas): " + lineCounts);

            // 2. Rodar Sequencial
            long startSeq = System.currentTimeMillis();
            long costSeq = FileMergerSequential.mergeSequential(new ArrayList<>(files), outputDir);
            long endSeq = System.currentTimeMillis();
            long timeSeq = endSeq - startSeq;

            System.out.println("\nAlgoritmo Sequencial:");
            System.out.println(" Custo total: " + costSeq + " bytes");
            System.out.println(" Tempo: " + timeSeq + " ms");

            // 3. Rodar Força Bruta (se permitido)
            if (runBruteForce) {
                long startBrute = System.currentTimeMillis();
                Result costBrute = FileMergerBruteForce.mergeBruteForce(new ArrayList<>(files), outputDir);
                long endBrute = System.currentTimeMillis();
                long timeBrute = endBrute - startBrute;

                System.out.println("\nAlgoritmo Força Bruta (Otimo):");
                System.out.println(" Custo total: " + costBrute.cost + " bytes");
                if (costBrute.file != null && costBrute.file.exists()) {
                    System.out.println("Arquivo ótimo gerado em: " + costBrute.file.getAbsolutePath());
                    // opcional: renomear para nome fixo
                    File finalFile = new File(outputDir, "merged_optimal.txt");
                    costBrute.file.renameTo(finalFile); // } else {
                }
                System.out.println(" Tempo: " + timeBrute + " ms");

                if (costBrute.cost < costSeq) {
                    System.out.println(" (Força Bruta foi " + (costSeq - costBrute.cost) + " bytes mais eficiente)");
                } else {
                    System.out.println(" (Custos iguais)");
                }

            } else {
                System.out.println("\nAlgoritmo Força Bruta: [PULADO (N muito grande)]");
            }

            System.out.println("\nArquivos de fusao salvos em: " + new File(outputDir).getAbsolutePath());

        } finally {
            // 4. Limpar arquivos
            if (files != null) {
                cleanupFiles(files);
            }
        }
        System.out.println("\n\n");
    }

    /**
     * Cria arquivos de teste com base em uma lista de contagem de linhas.
     */
    private static List<File> createTestFiles(List<Integer> lineCounts) throws IOException {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < lineCounts.size(); i++) {
            File f = new File("test_file_" + i + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                int numLines = lineCounts.get(i);
                for (int j = 0; j < numLines; j++) {
                    // Escreve conteúdo para dar tamanho ao arquivo
                    writer.write("Linha " + j + " do arquivo " + i + "\n");
                }
            }
            files.add(f);
        }
        return files;
    }

    /**
     * Deleta os arquivos de teste originais.
     */
    private static void cleanupFiles(List<File> files) {
        for (File f : files) {
            f.delete();
        }
    }
}
