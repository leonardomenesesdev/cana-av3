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

            //realiza a fusão sequencial
            System.out.println("\n--- Testando Sequencial ---");
            //armazena o tempo inicial
            long startSeq = System.currentTimeMillis();
            //chama a função para a fusão
            //o arquivo final sempre será um último (ex: merged_seq_1.txt e merged_seq_2.txt, o resultante é o merged_seq_2.txt)
            long seqCost = FileMergerSequential.mergeSequential(files, outputDir);
            //armazena o tempo final
            long endSeq = System.currentTimeMillis();
            //calcula o tempo total de execução
            long timeSeq = endSeq - startSeq;
            //exibe o custo total da fusão em bytes
            System.out.println("Custo total: " + seqCost + " bytes");
            //exibe o tempo da fusão
            System.out.println("Tempo: " + timeSeq + " ms");

            //realiza a fusão por força bruta
             System.out.println("\n--- Testando Força Bruta ---");
             //armazena o tempo inicial
             long startBrute = System.currentTimeMillis();
             //chama a fusão e armazena o resultado na variável
             Result bruteResult = FileMergerBruteForce.mergeBruteForce(files, outputDir);
             //armazena o tempo final
             long endBrute = System.currentTimeMillis();
             //calcula o tempo total de execução
             long timeBrute = endBrute - startBrute;
             //verifica se encontrou uma fusão ótima, caso não tenha encontrado bruteResult será nulo
             if (bruteResult != null) {
             //exibe o custo da fusão ótima, em bytes
             System.out.println("Custo ótimo: " + bruteResult.cost + " bytes");
             //verifica se o arquivo resultante não é nulo e existe
             if (bruteResult.file != null && bruteResult.file.exists()) {
             //mostra onde o arquivo foi salvo
             System.out.println("Arquivo ótimo gerado em: " + bruteResult.file.getAbsolutePath());
             //cria um arquivo que conterá a fusão ótima e indicará pelo nome
             File finalFile = new File(outputDir, "merged_optimal.txt");
             //renomeia o arquivo da fusão ótima para o nome do arquivo criado acima
             bruteResult.file.renameTo(finalFile);
             }
             //mensagem pra caso o arquivo seja nulo
             }
             else {
             System.out.println("Resultado da força bruta é null.");
             }
             //mostra o tempo que levou para realizar a fusão por força bruta
             System.out.println("Tempo: " + timeBrute + " ms");
    }

    //função para carregar os arquivos da pasta 'archive', que contém arquivos tirados do dataset (20NewsGroups)
    //recebe o número de arquivos a ser carregados como parâmetro
    private static List<File> loadRealFiles(int n) {
        //mapeia o local da pasta
        File folder = new File("src/archive");
        //filtra por arquivos que terminem apenas com (.txt) e os lista em um vetor
        File[] allFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        //caso não tenha sido encontrado nenhum arquivo, faz essa verificação
        if (allFiles == null || allFiles.length == 0) {
            System.out.println("Nenhum arquivo encontrado na pasta src/archive");
            return Collections.emptyList();
        }
        //cria uma lista para armazenar os arquivos que serão selecionados
        List<File> selected = new ArrayList<>();

        // pegar apenas n arquivos
        for (int i = 0; i < n && i < allFiles.length; i++) {
            //adiciona o arquivo na lista
            selected.add(allFiles[i]);
        }
        //retorna a lista
        return selected;
    }

}
