import java.util.Arrays;

interface SortStrategy {
    void sort(int[] data);
}

class BubbleSortStrategy implements SortStrategy {
    public void sort(int[] data) {
        if (data == null) return;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }
}

class SelectionSortStrategy implements SortStrategy {
    public void sort(int[] data) {
        
    }
}

class InsertionSortStrategy implements SortStrategy {
    public void sort(int[] data) {
        
    }
}

class DataProcessor {
    private SortStrategy strategy;

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void processData(int[] arr) {
        if (strategy != null) {
            strategy.sort(arr);
            System.out.println(Arrays.toString(arr));
        }
    }
}