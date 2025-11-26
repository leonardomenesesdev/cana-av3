import java.io.*;
import java.util.*;

import org.knowm.xchart.*;     // ← XChart
import org.knowm.xchart.style.markers.SeriesMarkers;

public class Main {

    public static void main(String[] args) throws IOException {

        //carrega 3 arquivos
        List<File> files = loadRealFiles(3);

        //exibe os arquivos selecionados
        System.out.println("Arquivos selecionados:");
        for (File f : files) {
            System.out.println(" - " + f.getName() + " (" + f.length() + " bytes)");
        }

        // diretório de saída
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
        // 2) FUSÃO POR FORÇA BRUTA
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
                bruteResult.file.renameTo(finalFile);
            }
        }

        System.out.println("Tempo: " + timeBrute + " ms");



        // ============================================================
        // 3) FUSÃO GULOSA
        // ============================================================
        System.out.println("\n--- Testando Guloso (Heap Min) ---");

        long startGreedy = System.currentTimeMillis();
        SolucaoGulosa.GreedyResult greedyResult =
                SolucaoGulosa.mergeGreedy(new ArrayList<>(files), outputDir);
        long endGreedy = System.currentTimeMillis();
        long timeGreedy = endGreedy - startGreedy;

        System.out.println("Custo guloso: " + greedyResult.cost + " bytes");
        System.out.println("Tempo: " + timeGreedy + " ms");



        // ============================================================
        // 4) GERAR GRÁFICO COM XCHART
        // ============================================================
        gerarGraficoXChart(timeSeq, timeGreedy, timeBrute);

        System.out.println("\nGráfico criado em: output_grafico.png");
    }



    // ============================================================
    // FUNÇÃO PARA GERAR O GRÁFICO COM XCHART
    // ============================================================
    private static void gerarGraficoXChart(long seq, long greedy, long brute) throws IOException {

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Comparação de Tempo — 3 Arquivos Reais")
                .xAxisTitle("Algoritmo")
                .yAxisTitle("Tempo (ms)")
                .build();

        chart.addSeries("Tempo (ms)",
                Arrays.asList("Sequencial", "Guloso", "Força Bruta"),
                Arrays.asList(seq, greedy, brute)
        ).setMarker(SeriesMarkers.NONE);

        BitmapEncoder.saveBitmap(chart, "output_grafico", BitmapEncoder.BitmapFormat.PNG);
    }



    // ============================================================
    // CARREGA OS ARQUIVOS REAIS DO DATASET
    // ============================================================
    private static List<File> loadRealFiles(int n) {
        File folder = new File("src/archive");

        File[] allFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (allFiles == null || allFiles.length == 0) {
            System.out.println("Nenhum arquivo encontrado na pasta src/archive");
            return Collections.emptyList();
        }

        List<File> selected = new ArrayList<>();

        for (int i = 0; i < n && i < allFiles.length; i++)
            selected.add(allFiles[i]);

        return selected;
    }
}
