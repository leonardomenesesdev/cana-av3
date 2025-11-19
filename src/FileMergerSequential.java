import java.io.*;
import java.util.*;

public class FileMergerSequential {

    //  função recebe uma lista de arquivos e uma string com o caminho em que o novo arquivo (oriundo das mesclagens) será gerado
    // a função retorna o custo total entre as combinações possíveis
    public static long mergeSequential(List<File> files, String outputDir) throws IOException {
        // copia para lista mutável, evitando alterar a lista original
        List<File> fileList = new ArrayList<>(files);
        // acumula o custo total de mesclagens (modelo de custo = soma dos tamanhos)
        long totalCost = 0;
        // controla nomes únicos para arquivos intermediários
        int step = 1;

        // continua até restar somente um arquivo (resultado final)
        while (fileList.size() > 1) {
            // pega os dois primeiros arquivos da lista
            File f1 = fileList.get(0);
            File f2 = fileList.get(1);
            // calcula o custo da mesclagem atual
            long cost = f1.length() + f2.length(); // custo = soma dos tamanhos
            // atualiza o custo total
            totalCost += cost;

            // cria o arquivo mesclado com nome único
            File merged = new File(outputDir, "merged_seq_" + step++ + ".txt");
            // realiza a mesclagem dos arquivos f1 e f2 no arquivo merged
            mergeFiles(f1, f2, merged);

            // remove os dois arquivos originais e adiciona o arquivo mesclado na lista
            fileList.remove(0);
            fileList.remove(0);
            fileList.add(0, merged);
        }
        // retorna o custo total acumulado
        return totalCost;
    }
    // função auxiliar para mesclar dois arquivos em um terceio arquivo final
    private static void mergeFiles(File f1, File f2, File merged) throws IOException {
        // usa try-with-resources para garantir o fechamento dos streams
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(merged));
             // abre os dois arquivos de entrada para leitura
             BufferedReader r1 = new BufferedReader(new FileReader(f1));
             BufferedReader r2 = new BufferedReader(new FileReader(f2))) {

            // lê linhas de ambos os arquivos e escreve no arquivo mesclado
            String line;
            // lê do primeiro arquivo (preservando a ordem)
            while ((line = r1.readLine()) != null) writer.write(line + "\n");
            // lê do segundo arquivo (usando o restante do conteúdo com concatenção à ordem)
            while ((line = r2.readLine()) != null) writer.write(line + "\n");
        }
    }
}
