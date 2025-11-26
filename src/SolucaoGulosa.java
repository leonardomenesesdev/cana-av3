import java.io.*;

public class SolucaoGulosa {

    //estrutura para armazenar o resultado final (custo e arquivo)
    public static class GreedyResult {
        public long cost;
        public File finalFile;

        public GreedyResult(long cost, File finalFile) {
            this.cost = cost;
            this.finalFile = finalFile;
        }
    }

    //classe wrapper para permitir comparação entre arquivos pelo tamanho, ajuda pra que a minheap saiba qual é o menor arquivo
    public static class FileWrapper implements Comparable<FileWrapper> {
        //atributo 'file'
        public File file;

        public FileWrapper(File f) {
            this.file = f;
        }
        //comparador do arquivo, compara o objeto file com o objeto 'o'
        @Override
        public int compareTo(FileWrapper o) {
            return Long.compare(this.file.length(), o.file.length());
        }
    }

    //funcao para realizar a solução gulosa
    public static GreedyResult mergeGreedy(java.util.List<File> files, String outputDir) throws IOException {
        // lista nula ou vazia → não há fusão
        if (files == null || files.isEmpty()) {
            return new GreedyResult(0, null);
        }

        // lista com apenas 1 arquivo → custo 0
        if (files.size() == 1) {
            return new GreedyResult(0, files.get(0));
        }
        // usa uma fila de prioridade criada manualmente, nomeada de pq
        FilaPrioridade<FileWrapper> pq = new FilaPrioridade<>();

        // adiciona todos os arquivos na fila
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
    //função usada para realizar, de fato, a fusão dos arquivos
    //recebe os arquivos pais (f1 e f2) e o arquivo que deverá receber o conteúdo que tem nos arquivos pais.
    private static void mergeFiles(File f1, File f2, File merged) throws IOException {

        //Usa a biblioteca BufferedWriter para 'escrever' o conteúdo dos arquivos pais
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(merged));

             //lê o conteúdo do arquivo 1
             BufferedReader r1 = new BufferedReader(new FileReader(f1));

             //lê o conteúdo do arquivo 2
             BufferedReader r2 = new BufferedReader(new FileReader(f2))) {

            //cria uma variável linha para armazenar o valor de cada linha dos arquivos
            String line;

            //while para percorrer as linhas do arquivo 1, sempre trocando o valor da variável line e escrevendo, no arquivo resultante,
            //o conteúdo salvo em "line"
            while ((line = r1.readLine()) != null) writer.write(line + "\n");

            //while para percorrer as linhas do arquivo 2, sempre trocando o valor da variável line e escrevendo, no arquivo resultante,
            //o conteúdo salvo em "line"
            while ((line = r2.readLine()) != null) writer.write(line + "\n");
        }
    }
}
