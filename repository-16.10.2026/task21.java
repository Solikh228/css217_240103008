interface CsvExporter {
    String exportCsv();
}

class ArrayDataStore {
    public String[] fetchWords() {
        return new String[]{"hello", "world", "java"};
    }
}

class ArrayToCsvAdapter implements CsvExporter {
    ArrayDataStore store;

    public ArrayToCsvAdapter(ArrayDataStore store) {
        this.store = store;
    }

    public String exportCsv() {
        String[] words = store.fetchWords();
        
        return String.join(",", words);
    }
}