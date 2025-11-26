import java.io.*;
import java.util.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class MainGrafico {

    public static void main(String[] args) throws IOException {

        String outputDir = "output_grafico";
        new File(outputDir).mkdirs();

        File csv = new File("resultado_grafico.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(csv));

        writer.write("n,Sequencial,Guloso,ForcaBruta\n");

        // armazenar os dados em listas para gerar o gráfico depois
        List<Integer> eixoN = new ArrayList<>();
        List<Long> tempoSeq = new ArrayList<>();
        List<Long> tempoGreedy = new ArrayList<>();
        List<Long> tempoBrute = new ArrayList<>();

        // Executa de n = 1 até n = 6
        for (int n = 1; n <= 8; n++) {

            System.out.println("\n=============================");
            System.out.println("Executando para n = " + n);
            System.out.println("=============================");

            List<File> files = createTestFiles(n);

            // ============================
            // SEQUENCIAL
            // ============================
            long startSeq = System.currentTimeMillis();
            long costSeq = FileMergerSequential.mergeSequential(new ArrayList<>(files), outputDir);
            long endSeq = System.currentTimeMillis();
            long tSeq = endSeq - startSeq;

            // ============================
            // GULOSO
            // ============================
            long startGreedy = System.currentTimeMillis();
            SolucaoGulosa.GreedyResult greedyResult =
                    SolucaoGulosa.mergeGreedy(new ArrayList<>(files), outputDir);
            long endGreedy = System.currentTimeMillis();
            long tGreedy = endGreedy - startGreedy;

            // ============================
            // FORÇA BRUTA
            // ============================
            long tBrute;
            if (n <= 6) {
                long startBrute = System.currentTimeMillis();
                Result brute = FileMergerBruteForce.mergeBruteForce(new ArrayList<>(files), outputDir);
                long endBrute = System.currentTimeMillis();
                tBrute = endBrute - startBrute;
            } else {
                tBrute = -1; // não executado
            }

            // escreve linha no CSV
            writer.write(n + "," + tSeq + "," + tGreedy + "," + tBrute + "\n");

            // salva nos arrays para gerar o gráfico
            eixoN.add(n);
            tempoSeq.add(tSeq);
            tempoGreedy.add(tGreedy);
            tempoBrute.add(tBrute);

            cleanupFiles(files);
        }

        writer.close();
        System.out.println("\nCSV gerado: " + csv.getAbsolutePath());

        // ============================================================
        // GERAR GRÁFICO
        // ============================================================

        XYChart chart = new XYChartBuilder()
                .width(900)
                .height(600)
                .title("Comparação de Tempo — Sequencial vs Guloso vs Força Bruta")
                .xAxisTitle("n (qtde de arquivos)")
                .yAxisTitle("tempo (ms)")
                .build();

        // Série Sequencial
        XYSeries s1 = chart.addSeries("Sequencial", eixoN, tempoSeq);
        s1.setMarker(SeriesMarkers.CIRCLE);

        // Série Guloso
        XYSeries s2 = chart.addSeries("Guloso", eixoN, tempoGreedy);
        s2.setMarker(SeriesMarkers.DIAMOND);

        // Série Força Bruta
        XYSeries s3 = chart.addSeries("Força Bruta", eixoN, tempoBrute);
        s3.setMarker(SeriesMarkers.SQUARE);

        // Exportar PNG
        BitmapEncoder.saveBitmap(chart, "grafico_comparativo", BitmapEncoder.BitmapFormat.PNG);

        System.out.println("\nGráfico gerado como: grafico_comparativo.png");
    }

    // cria n arquivos reais
    private static List<File> createTestFiles(int n) throws IOException {
        List<File> files = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            File f = new File("file" + i + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                for (int j = 0; j < i * 10; j++) {
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
