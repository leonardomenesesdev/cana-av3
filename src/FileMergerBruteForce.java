import java.io.*;
import java.util.*;


public class FileMergerBruteForce {

    //função recebe uma lista de arquivos e uma string com o caminho em que o novo arquivo (oriundo das mesclagens) será gerado
    //retorna um objeto Result contendo tanto o custo mínimo quanto o arquivo ótimo final
    public static Result mergeBruteForce(List<File> files, String outputDir) throws IOException {

        //Se a lista tiver apenas um arquivo ou for vazia, o custo é 0, pois não é necessária uma mesclagem - Tratamento de Caso Extremo.
        //retorna também o próprio arquivo, pois ele já é o resultado da fusão
        if (files == null || files.size() <= 1) {
            return new Result(0, files.get(0));
        }

        //garante que o diretório de saída existe antes de tentar escrever os arquivos temporários
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        //inicializa a variável minCost com o maior valor possível, assim, quando o código encontrar um valor menor, ela poderá ser
        //redefinida sem problemas.
        long minCost = Long.MAX_VALUE;

        //variável para armazenar o arquivo resultante com fusão ótima
        File bestFile = null;

        //instancia dois for's para percorrer todas as combinações possíveis entre os arquivos
        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {

                //primeiro arquivo
                File f1 = files.get(i);
                //segundo arquivo
                File f2 = files.get(j);

                //tratamento de caso extremo: caso algum arquivo não exista ou esteja inacessível, a fusão não ocorre
                if (!f1.exists() || !f2.exists() || !f1.canRead() || !f2.canRead()) {
                    continue; //pula a combinação inválida
                }

                //o custo é representado pelo tamanho dos arquivos em questão (consumo em bytes)
                long cost = f1.length() + f2.length();

                //cria arquivos temporários únicos para cada chamada, evitando sobrescrita incorreta.
                File merged = File.createTempFile("merged_brute_", ".txt", dir);

                //chama a função mergeFiles para consumar a fusão
                mergeFiles(f1, f2, merged);

                //cria uma nova lista, com base na que é passada no parâmetro, para substituir os 2 arquivos pais pelo arquivo resultante
                List<File> newList = new ArrayList<>(files);

                //remove os arquivos pais da lista
                newList.remove(j);
                newList.remove(i);

                //adiciona o arquivo resultante na lista, consumando a substituição para uma próxima iteração
                newList.add(merged);

                //faz uma chamada recursiva para calcular e armazenar o custo da fusão-teste, a recursão também retorna
                // o arquivo ótimo dessa sequência
                Result recResult = mergeBruteForce(newList, outputDir);
                long totalCost = cost + recResult.cost;

                //se o custo resultante da chamada recursiva for menor que o minCost (originalmente armazenado com um valor muito grande)
                //o minCost assume o valor do custo encontrado, que é menor
                if (totalCost < minCost) {

                    //se já havia um arquivo ótimo anterior, ele é removido
                    if (bestFile != null && bestFile.exists()) {
                        bestFile.delete();
                    }

                    minCost = totalCost;

                    //o arquivo ótimo é o arquivo retornado pela recursão
                    bestFile = recResult.file;

                } else {
                    //se a fusão temporária não pertence ao caminho ótimo, ela é apagada.
                    merged.delete();

                    //o arquivo ótimo da recursão também não pertence à melhor sequência, então ele é deletado
                    recResult.file.delete();
                }
            }
        }

        //retorna tanto o menor custo quanto o arquivo ótimo final
        return new Result(minCost, bestFile);
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
