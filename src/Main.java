import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        //carrega 3 arquivos reais da pasta archive (dataset: 20NewsGroups)
        List<File> files = loadRealFiles(3);

        //exibe os arquivos que foram selecionados
        System.out.println("Arquivos selecionados:");
        for (File f : files) {
            System.out.println(" - " + f.getName() + " (" + f.length() + " bytes)");
        }

        // diretório para salvar fusões
        String outputDir = "output/";
        new File(outputDir).mkdirs();


        // ============================================================
        // 1) FUSÃO SEQUENCIAL
        // ============================================================
        System.out.println("\n--- Testando Sequencial ---");

        long startSeq = System.currentTimeMillis();
        long seqCost = FileMergerSequential.mergeSequential(files, outputDir);
        long endSeq = System.currentTimeMillis();
        long timeSeq = endSeq - startSeq;

        System.out.println("Custo total: " + seqCost + " bytes");
        System.out.println("Tempo: " + timeSeq + " ms");






        // ============================================================
        // 3) FUSÃO POR FORÇA BRUTA
        // ============================================================
        System.out.println("\n--- Testando Força Bruta ---");

        long startBrute = System.currentTimeMillis();
        Result bruteResult = FileMergerBruteForce.mergeBruteForce(files, outputDir);
        long endBrute = System.currentTimeMillis();
        long timeBrute = endBrute - startBrute;

        if (bruteResult != null) {
            System.out.println("Custo ótimo: " + bruteResult.cost + " bytes");

            if (bruteResult.file != null && bruteResult.file.exists()) {

                File finalFile = new File(outputDir, "merged_optimal.txt");
                System.out.println("Arquivo ótimo gerado em: " + finalFile.getAbsolutePath());

                bruteResult.file.renameTo(finalFile);
            }
        } else {
            System.out.println("Resultado da força bruta é null.");
        }

        System.out.println("Tempo: " + timeBrute + " ms");

        //otimização por algoritmo guloso
        System.out.println("\n--- Testando Guloso (Heap Min) ---");

        long startGreedy = System.currentTimeMillis();
        SolucaoGulosa.GreedyResult greedyResult =
                SolucaoGulosa.mergeGreedy(new ArrayList<>(files), outputDir);
        long endGreedy = System.currentTimeMillis();
        long timeGreedy = endGreedy - startGreedy;

        System.out.println("Custo guloso: " + greedyResult.cost + " bytes");
        System.out.println("Tempo: " + timeGreedy + " ms");

        if (greedyResult.finalFile != null && greedyResult.finalFile.exists()) {
            System.out.println("Arquivo final (guloso): " + greedyResult.finalFile.getAbsolutePath());
        }

    }



    //função para carregar os arquivos da pasta 'archive', que contém arquivos tirados do dataset (20NewsGroups)
    //recebe o número de arquivos a ser carregados como parâmetro
    private static List<File> loadRealFiles(int n) {
        File folder = new File("src/archive");

        File[] allFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (allFiles == null || allFiles.length == 0) {
            System.out.println("Nenhum arquivo encontrado na pasta src/archive");
            return Collections.emptyList();
        }

        List<File> selected = new ArrayList<>();

        // pegar apenas n arquivos
        for (int i = 0; i < n && i < allFiles.length; i++) {
            selected.add(allFiles[i]);
        }

        return selected;
    }
}