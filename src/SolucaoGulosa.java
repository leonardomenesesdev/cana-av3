import java.io.*;

public class SolucaoGulosa {

    // Estrutura para armazenar resultado final
    public static class GreedyResult {
        public long cost;
        public File finalFile;

        public GreedyResult(long cost, File finalFile) {
            this.cost = cost;
            this.finalFile = finalFile;
        }
    }

    // Classe wrapper para permitir comparação entre arquivos pelo tamanho
    // (necessário para que a MinHeap saiba qual é o menor arquivo)
    public static class FileWrapper implements Comparable<FileWrapper> {
        public File file;

        public FileWrapper(File f) {
            this.file = f;
        }

        @Override
        public int compareTo(FileWrapper o) {
            return Long.compare(this.file.length(), o.file.length());
        }
    }

    public static GreedyResult mergeGreedy(java.util.List<File> files, String outputDir) throws IOException {

        // Usa a Priority Queue manual (MinHeap) criada por você
        FilaPrioridade<FileWrapper> pq = new FilaPrioridade<>();

        // adiciona todos os arquivos à PQ manual
        for (File f : files) {
            pq.add(new FileWrapper(f));
        }

        long totalCost = 0;
        int step = 1;

        // enquanto houver pelo menos dois arquivos,
        // remove sempre os dois menores (estratégia gulosa)
        while (pq.size() > 1) {

            // remove os dois menores arquivos
            FileWrapper w1 = pq.poll();
            FileWrapper w2 = pq.poll();

            File f1 = w1.file;
            File f2 = w2.file;

            long cost = f1.length() + f2.length();
            totalCost += cost;

            // cria arquivo temporário resultante da fusão
            File merged = new File(outputDir, "merged_greedy_" + (step++) + ".txt");

            mergeFiles(f1, f2, merged);

            // adiciona o arquivo recém-fundido de volta na PQ manual
            pq.add(new FileWrapper(merged));
        }

        // o último arquivo restante é a fusão final
        File finalFile = pq.poll().file;

        return new GreedyResult(totalCost, finalFile);
    }

    private static void mergeFiles(File f1, File f2, File merged) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(merged));
             BufferedReader r1 = new BufferedReader(new FileReader(f1));
             BufferedReader r2 = new BufferedReader(new FileReader(f2))) {

            String line;

            while ((line = r1.readLine()) != null) writer.write(line + "\n");
            while ((line = r2.readLine()) != null) writer.write(line + "\n");
        }
    }
}
