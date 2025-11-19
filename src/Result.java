import java.io.File;

//Classe auxiliar usada para armazenar tanto o custo total quanto o arquivo ótimo final
class Result {
    long cost;   //menor custo obtido
    File file;   //arquivo final resultante da melhor sequência de fusão

    public Result(long cost, File file) {
        this.cost = cost;
        this.file = file;
    }
}