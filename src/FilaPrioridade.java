public class FilaPrioridade<T extends Comparable<T>>{

    private T[] heap;        // array que armazena a heap
    private int size;        // quantidade de elementos no heap

    public FilaPrioridade() {
        heap = (T[]) new Comparable[10]; // tamanho inicial fixo
        size = 0;
    }

    // adiciona um elemento na heap
    public void add(T value) {
        ensureCapacity();        // garante que tem espaço
        heap[size] = value;      // insere no final
        heapifyUp(size);         // ajusta a heap
        size++;
    }

    // retorna o menor elemento SEM remover
    public T peek() {
        if (size == 0) return null;
        return heap[0];
    }

    // remove e retorna o menor elemento
    public T poll() {
        if (size == 0) return null;

        T minValue = heap[0];      // menor valor
        heap[0] = heap[size - 1];  // move último para raiz
        size--;
        heapifyDown(0);            // ajusta estrutura

        return minValue;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // -------------------------------------------------------------
    // ------- MÉTODOS INTERNOS: EXPANSÃO DO ARRAY ------------------
    // -------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= heap.length) {
            T[] newHeap = (T[]) new Comparable[heap.length * 2];
            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    // -------------------------------------------------------------
    // ------- HEAPIFY-UP (subir elemento para manter min-heap) ----
    // -------------------------------------------------------------
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) >= 0) break;

            swap(index, parent);
            index = parent;
        }
    }

    // -------------------------------------------------------------
    // ------- HEAPIFY-DOWN (descer elemento para manter min-heap) --
    // -------------------------------------------------------------
    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == index) break;

            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // DEBUG
    public void printHeap() {
        System.out.print("[ ");
        for (int i = 0; i < size; i++) System.out.print(heap[i] + " ");
        System.out.println("]");
    }
}
