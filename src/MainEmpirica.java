import java.io.*;
import java.util.*;

public class MainEmpirica {

    public static void main(String[] args) throws IOException {

        System.out.println("Comparando algoritmos de fusão real de arquivos:\n");

        //teste pra n=1
        List<Integer> sizeOne = Arrays.asList(50);
        runTest("Teste N=1 (Fixo)", sizeOne, true);

        //teste pra n=5
        List<Integer> sizesSmall = Arrays.asList(50, 10, 30, 20, 40); // n=5
        runTest("Teste N=5 (Fixo)", sizesSmall, true);

        //teste pra n=8 (começa a ser inviável pra força bruta)
        List<Integer> sizesMedium = Arrays.asList(10, 60, 20, 80, 30, 50, 40, 70); // n=8
        runTest("Teste N=8 (Fixo)", sizesMedium, false);
        //teste n=50 (impossível de ser executado com força bruta)
        List<Integer> sizesLarge = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            sizesLarge.add(rand.nextInt(1000) + 10);
        }

        runTest("Teste N=50 (Randômico)", sizesLarge, false);
    }

    private static void runTest(String testName, List<Integer> lineCounts, boolean runBruteForce) throws IOException {

        System.out.println("=================================================");
        System.out.println("Iniciando: " + testName);
        System.out.println("=================================================");

        String outputDir = "output_" + testName.replaceAll("[^a-zA-Z0-9]", "_");
        new File(outputDir).mkdirs();

        List<File> files = null;

        try {
            // 1. Criar arquivos
            files = createTestFiles(lineCounts);
            System.out.println("Arquivos criados (linhas): " + lineCounts);

            // ============================================================
            // SEQUENCIAL
            // ============================================================
            long startSeq = System.currentTimeMillis();
            long costSeq = FileMergerSequential.mergeSequential(new ArrayList<>(files), outputDir);
            long endSeq = System.currentTimeMillis();

            long timeSeq = endSeq - startSeq;

            System.out.println("\nAlgoritmo Sequencial:");
            System.out.println(" Custo total: " + costSeq + " bytes");
            System.out.println(" Tempo: " + timeSeq + " ms");

            // ============================================================
            // GULOSO (sempre rápido)
            // ============================================================
            long startGreedy = System.currentTimeMillis();
            SolucaoGulosa.GreedyResult greedy = SolucaoGulosa.mergeGreedy(new ArrayList<>(files), outputDir);
            long endGreedy = System.currentTimeMillis();

            long timeGreedy = endGreedy - startGreedy;

            System.out.println("\nAlgoritmo Guloso (Min-Heap):");
            System.out.println(" Custo total: " + greedy.cost + " bytes");
            System.out.println(" Tempo: " + timeGreedy + " ms");

            if (greedy.finalFile != null && greedy.finalFile.exists()) {
                System.out.println(" Arquivo final (guloso): " + greedy.finalFile.getAbsolutePath());
            }

            if (greedy.cost == costSeq) {
                System.out.println(" (Guloso e Sequencial produziram o mesmo custo)");
            }

            // ============================================================
            // FORÇA BRUTA (se permitido)
            // ============================================================
            if (runBruteForce) {

                long startBrute = System.currentTimeMillis();
                Result brute = FileMergerBruteForce.mergeBruteForce(new ArrayList<>(files), outputDir);
                long endBrute = System.currentTimeMillis();

                long timeBrute = endBrute - startBrute;

                System.out.println("\nAlgoritmo Força Bruta (Ótimo):");
                System.out.println(" Custo total: " + brute.cost + " bytes");
                System.out.println(" Tempo: " + timeBrute + " ms");

                if (brute.file != null && brute.file.exists()) {
                    System.out.println(" Arquivo ótimo final: " + brute.file.getAbsolutePath());
                }

                System.out.println(" Comparação força bruta x guloso: ");
                if (brute.cost == greedy.cost) {
                    System.out.println(" → Guloso encontrou o custo ótimo!");
                } else {
                    System.out.println(" → Guloso não encontrou o custo ótimo.");
                }

            } else {
                System.out.println("\nAlgoritmo Força Bruta: [PULADO — N muito grande]");
            }

            System.out.println("\nArquivos de fusão salvos em: " + new File(outputDir).getAbsolutePath());

        } finally {
            if (files != null) cleanupFiles(files);
        }

        System.out.println("\n\n");
    }

    private static List<File> createTestFiles(List<Integer> lineCounts) throws IOException {
        List<File> files = new ArrayList<>();

        for (int i = 0; i < lineCounts.size(); i++) {
            File f = new File("test_file_" + i + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                int lines = lineCounts.get(i);

                for (int j = 0; j < lines; j++) {
                    writer.write("Linha " + j + " do arquivo " + i + "\n");
                }
            }

            files.add(f);
        }

        return files;
    }

    private static void cleanupFiles(List<File> files) {
        for (File f : files) f.delete();
    }
}
