//implementação de uma fila de prioridade usando uma min-heap
public class FilaPrioridade<T extends Comparable<T>>{

    //array genérico para armazenar os elementos
    private T[] heap;        //array que armazena a heap
    private int size;        // quantidade de elementos no heap

    // construtor inicial da fila de prioridade
    public FilaPrioridade() {
        heap = (T[]) new Comparable[10]; // tamanho inicial fixo
        size = 0;                        // nenhum elemento inicialmente
    }

    // adiciona um elemento na heap
    public void add(T value) {
        ensureCapacity();        // garante que tem espaço
        heap[size] = value;      // insere no final
        heapifyUp(size);         // ajusta a heap
        size++;
    }

    // retorna o menor elemento sem remover
    public T peek() {
        if (size == 0) return null;
        return heap[0];
    }

    // remove e retorna o menor elemento
    public T poll() {
        if (size == 0) return null;

        T minValue = heap[0];       // menor valor
        heap[0] = heap[size - 1];   // move último para raiz
        size--;                     // reduz tamanho
        heapifyDown(0);       // ajusta estrutura

        return minValue;            // retorna o menor valor
    }

    // verifica se a fila está vazia
    public boolean isEmpty() {
        return size == 0;
    }

    // retorna o tamanho atual da fila
    public int size() {
        return size;
    }



    // garante que há espaço suficiente no array
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {

        // se o array estiver cheio, dobra o tamanho
        if (size >= heap.length) {

            // cria novo array com o dobro do tamanho
            T[] newHeap = (T[]) new Comparable[heap.length * 2];

            // copia elementos para o novo array
            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }

            // atualiza a referência para o novo array
            heap = newHeap;
        }
    }


    // ajusta a heap subindo o elemento no índice dado
    private void heapifyUp(int index) {

        // enquanto não chegar na raiz
        while (index > 0) {

            // calcula o índice do pai
            int parent = (index - 1) / 2;

            // se o elemento atual for maior ou igual ao pai, está ok
            if (heap[index].compareTo(heap[parent]) >= 0) break;

            // senão, troca com o pai
            swap(index, parent);

            // move o índice para o pai
            index = parent;
        }
    }


    // ajusta a heap descendo o elemento no índice dado
    private void heapifyDown(int index) {

        // enquanto houver filhos
        while (true) {

            // calcula os índices dos filhos
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            // assume que o menor é o atual
            int smallest = index;

            // verifica se o filho esquerdo é menor
            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {

                // atualiza o menor
                smallest = left;
            }

            // verifica se o filho direito é menor
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {

                // atualiza o menor
                smallest = right;
            }

            // se o menor é o atual, está ok
            if (smallest == index) break;

            // senão, troca com o menor filho
            swap(index, smallest);

            // move o índice para o menor filho
            index = smallest;
        }
    }

    // troca os elementos nos índices i e j
    private void swap(int i, int j) {

        // troca os elementos
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // DEBUG
    // imprime o conteúdo da heap
    public void printHeap() {
        System.out.print("[ ");
        for (int i = 0; i < size; i++) System.out.print(heap[i] + " ");
        System.out.println("]");
    }
}
