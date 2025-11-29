import java.io.*;
import java.util.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class MainGrafico {

    public static void main(String[] args) throws IOException {
        String outputDir = "output_empirico";
        new File(outputDir).mkdirs();

        List<Integer> eixoN = new ArrayList<>();
        List<Long> tempoSeq = new ArrayList<>();
        List<Long> tempoGreedy = new ArrayList<>();
        List<Long> tempoBrute = new ArrayList<>();

        // Executa de n = 1 até n = 6
        for (int n = 1; n <= 6; n++) {

            System.out.println("\n=============================");
            System.out.println("Executando para n = " + n);
            System.out.println("=============================");

            // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            // CRIA A PASTA DE RESULTADOS PARA ESTE n
            // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            String pastaN = outputDir + "/n_" + n;
            new File(pastaN).mkdirs();
            // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

            List<File> files = createTestFiles(n);

            // ============================
            // SEQUENCIAL
            // ============================
            long startSeq = System.currentTimeMillis();
            long costSeq = FileMergerSequential.mergeSequential(new ArrayList<>(files), pastaN);
            long endSeq = System.currentTimeMillis();
            long tSeq = endSeq - startSeq;

            System.out.println("\nAlgoritmo Sequencial:");
            System.out.println(" Custo total: " + costSeq + " bytes");
            System.out.println(" Tempo: " + tSeq + " ms");

            // ============================
            // FORÇA BRUTA
            // ============================
            long tBrute;
            if (n <= 7) {
                long startBrute = System.currentTimeMillis();
                Result brute = FileMergerBruteForce.mergeBruteForce(new ArrayList<>(files), pastaN);
                long endBrute = System.currentTimeMillis();
                tBrute = endBrute - startBrute;

                System.out.println("\nAlgoritmo Força Bruta:");
                System.out.println(" Custo total: " + brute.cost + " bytes");
                System.out.println(" Tempo: " + tBrute + " ms");
                System.out.println(" Resultado força bruta: " + brute.file.getAbsolutePath());


            } else {
                tBrute = -1;
            }


            // ============================
            // GULOSO
            // ============================
            long startGreedy = System.currentTimeMillis();
            SolucaoGulosa.GreedyResult greedyResult =
                    SolucaoGulosa.mergeGreedy(new ArrayList<>(files), pastaN);
            long endGreedy = System.currentTimeMillis();
            long tGreedy = endGreedy - startGreedy;

            System.out.println("\nAlgoritmo Guloso:");
            System.out.println(" Custo total: " + greedyResult.cost + " bytes");
            System.out.println(" Tempo: " + tGreedy + " ms");
            System.out.println(" Resultado guloso: " + greedyResult.finalFile.getAbsolutePath());



            eixoN.add(n);
            tempoSeq.add(tSeq);
            tempoGreedy.add(tGreedy);
            tempoBrute.add(tBrute);
        }


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

        XYChart chart2 = new XYChartBuilder()
                .width(900)
                .height(600)
                .title("Comparação de Tempo — Sequencial vs Guloso")
                .xAxisTitle("n (qtde de arquivos)")
                .yAxisTitle("tempo (ms)")
                .build();

        XYChart chart3 = new XYChartBuilder()
                .width(900)
                .height(600)
                .title("Comparação de Tempo — Sequencial vs Força Bruta")
                .xAxisTitle("n (qtde de arquivos)")
                .yAxisTitle("tempo (ms)")
                .build();

        // Série Sequencial
        XYSeries s1 = chart3.addSeries("Seq", eixoN, tempoSeq);
        s1.setMarker(SeriesMarkers.CIRCLE);
        XYSeries s2 = chart3.addSeries("Bruta", eixoN, tempoBrute);
        s2.setMarker(SeriesMarkers.SQUARE);
        BitmapEncoder.saveBitmap(chart3, "grafico_forcabruta_vs_sequencial", BitmapEncoder.BitmapFormat.PNG);

        XYSeries s3 = chart.addSeries("Sequencial", eixoN, tempoSeq);
        s3.setMarker(SeriesMarkers.CIRCLE);

        // Série Guloso
        XYSeries s4 = chart.addSeries("Guloso", eixoN, tempoGreedy);
        s4.setMarker(SeriesMarkers.DIAMOND);

        // Série Força Bruta
        XYSeries s5 = chart.addSeries("Força Bruta", eixoN, tempoBrute);
        s5.setMarker(SeriesMarkers.SQUARE);

        // Exportar PNG
        BitmapEncoder.saveBitmap(chart, "grafico_comparativo", BitmapEncoder.BitmapFormat.PNG);

        System.out.println("\nGráfico gerado como: grafico_comparativo.png");

        XYSeries s6 = chart2.addSeries("Seq", eixoN, tempoSeq);
        s6.setMarker(SeriesMarkers.CIRCLE);
        XYSeries s7 = chart2.addSeries("Algoritmo Guloso", eixoN, tempoGreedy);
        s7.setMarker(SeriesMarkers.SQUARE);
        BitmapEncoder.saveBitmap(chart2, "grafico_sem_bruteforce", BitmapEncoder.BitmapFormat.PNG);

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