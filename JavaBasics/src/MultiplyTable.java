class MultiplyTable {

    void print() {
        print(5);
    }

    void print(int t) {
        print(t, 1, 10);
    }

    void print(int t, int from, int to) {
        for (int i = from; i <= to; i++) {
            System.out.printf("%d X %d = %d", t, i, t * i).println();
        }
    }
}
