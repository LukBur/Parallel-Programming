class WatekRunnableCyclic implements Runnable {
    private Obraz obraz;
    private int threadId;
    private int n;
    private int m;
    private int num_threads;

    public WatekRunnableCyclic(int threadId, int n, int m, int num_threads, Obraz obraz) {
        this.threadId = threadId;
        this.n = n;
        this.m = m;
        this.num_threads = num_threads;
        this.obraz = obraz;
    }

    @Override
    public void run() {
        for (int col = threadId; col < m; col += num_threads) {
            for (char symbol = 33; symbol < 127; symbol++) {
                obraz.compute_column(symbol, col);
            }
        }
        obraz.print_histogram(2);
    }
}
