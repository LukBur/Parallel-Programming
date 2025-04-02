class WatekRunnable implements Runnable {
    private int start;
    private int end;
    private Obraz obraz;
    //private int leap;
    private int threadId;

    public WatekRunnable(int threadId, int start, int end, Obraz obraz) {
        this.threadId = threadId;
        this.start = start;
        this.end = end;
        //this.leap = leap;
        this.obraz = obraz;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            char symbol = (char) i;
            obraz.calculate_histogram_part(symbol);
            //obraz.print_histogram_part(symbol);
            //System.out.println("Thread ID: " + threadId + ", Symbol: " + symbol);
        }
    }
}
