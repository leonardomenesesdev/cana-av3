import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        // Diretório para salvar os merges
        String outputDir = "output";
        new File(outputDir).mkdirs();

        // Criar arquivos de teste
        List<File> files = createTestFiles();

        System.out.println("Comparando algoritmos de fusão real de arquivos:\n");

        // Sequencial
        long startSeq = System.currentTimeMillis();
        long costSeq = FileMergerSequential.mergeSequential(files, outputDir);
        long endSeq = System.currentTimeMillis();
        long timeSeq = endSeq - startSeq;

        // Brute Force
        long startBrute = System.currentTimeMillis();
        long costBrute = FileMergerBruteForce.mergeBruteForce(files, outputDir);
        long endBrute = System.currentTimeMillis();
        long timeBrute = endBrute - startBrute;

        System.out.println("Algoritmo Sequencial:");
        System.out.println(" → Custo total: " + costSeq + " bytes");
        System.out.println(" → Tempo: " + timeSeq + " ms");
        System.out.println();
        System.out.println("Algoritmo Força Bruta:");
        System.out.println(" → Custo total: " + costBrute + " bytes");
        System.out.println(" → Tempo: " + timeBrute + " ms");

        System.out.println("\nArquivos fundidos foram salvos em: " + new File(outputDir).getAbsolutePath());
    }

    private static List<File> createTestFiles() throws IOException {
        List<File> files = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
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
}
