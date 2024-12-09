class WatekRunnableColumns implements Runnable {
    private Obraz obraz;
    private int start_col;
    private int end_col;
    private char[] symbols;

    public WatekRunnableColumns(int start_col, int end_col, Obraz obraz, char[] symbols) {
        this.start_col = start_col;
        this.end_col = end_col;
        this.obraz = obraz;
        this.symbols = symbols;
    }

    @Override
    public void run() {
        for (int col = start_col; col < end_col; col++) {
            for (char symbol : symbols) {
                obraz.compute_column(symbol, col);
            }
        }
    }
}
