import  java.util.Random;


class Obraz {

    private int size_n;
    private int size_m;
    private char[][] tab;
    private char[] tab_symb;
    private int[] histogram;
    private int[] histogram_parallel;
    private int start_znak;
    private final Object[] locks = new Object[94];

    public Obraz(int n, int m) {

        this.size_n = n;
        this.size_m = m;
        tab = new char[n][m];
        tab_symb = new char[94];
        histogram = new int[94];
        histogram_parallel = new int[94];
        start_znak = 0;

        final Random random = new Random();

        for (int i = 0; i < 94; i++) {
            locks[i] = new Object();
        }

        // for general case where symbols could be not just integers
        for(int k=0;k<94;k++) {
            tab_symb[k] = (char)(k+33); // substitute symbols
        }

        for(int i=0;i<n;i++) {
            for(int j=0;j<m;j++) {
                tab[i][j] = tab_symb[random.nextInt(94)];  // ascii 33-127
                //tab[i][j] = (char)(random.nextInt(94)+33);  // ascii 33-127
                System.out.print(tab[i][j]+" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");

        clear_histogram();
    }

    public void clear_histogram(){

        for(int i=0;i<94;i++) histogram[i]=0;

    }

    public synchronized void calculate_histogram(){

        for(int i=0;i<size_n;i++) {
            for(int j=0;j<size_m;j++) {

                // optymalna wersja obliczania histogramu, wykorzystujÄca fakt, Ĺźe symbole w tablicy
                // moĹźna przeksztaĹciÄ w indeksy tablicy histogramu
                // histogram[(int)tab[i][j]-33]++;

                // wersja bardziej ogĂłlna dla tablicy symboli nie utoĹźsamianych z indeksami
                // tylko dla tej wersji sensowne jest zrĂłwnoleglenie w dziedzinie zbioru znakĂłw ASCII
                for(int k=0;k<94;k++) {
                    if(tab[i][j] == tab_symb[k]) histogram[k]++;
                    //if(tab[i][j] == (char)(k+33)) histogram[k]++;
                }

            }
        }

    }

    public void calculate_histogram_part(char symbol) {
        int index = symbol - 33; // Map symbol to index in histogram
        for (int i = 0; i < size_n; i++) {
            for (int j = 0; j < size_m; j++) {
                if (tab[i][j] == symbol) histogram_parallel[index]++;
            }
        }
    }

    //public

    /*public synchronized void print_histogram_part(char symbol) {
        int index = symbol - 33; // Map symbol to index in histogram
        System.out.print(symbol + ": " + histogram_parallel[index] + " -> ");
        for (int j = 0; j < histogram_parallel[index]; j++) {
            System.out.print("=");
        }
        System.out.println();
    }*/

    public synchronized void print_histogram_part(char symbol) {
        int index = symbol - 33; // Map symbol to index in histogram
        System.out.print(symbol + ": " + histogram_parallel[index] + " -> ");
        for (int j = 0; j < histogram_parallel[index]; j++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public void compare_histograms() {
        boolean areEqual = true;

        for (int i = 0; i < 94; i++) {
            if (histogram[i] != histogram_parallel[i]) {
                areEqual = false;
                System.out.println("Mismatch at symbol: " + (char)(i + 33) +
                        " -> Sequential: " + histogram[i] +
                        ", Parallel: " + histogram_parallel[i]);
            }
        }
        System.out.println("Histogramy są identyczne!");
    }

    public void compute_row(char symbol, int row) {
        int index = symbol - 33;
        for (int i = 0; i < size_m; i++) {
            if (tab[row][i] == symbol) {
                synchronized (locks[index]) { // Synchronizuj tylko tę komórkę
                    histogram_parallel[index]++;
                }
            }
        }
    }

    public void compute_column(char symbol, int col){
        int index = symbol - 33;
        for(int i=0; i<size_n; i++){
            if(tab[i][col] == symbol) {
                synchronized (locks[index]){
                    histogram_parallel[index]++;
                }
            }
        }
    }

// uniwersalny wzorzec dla rĂłĹźnych wariantĂłw zrĂłwnoleglenia - moĹźna go modyfikowaÄ dla
// rĂłĹźnych wersji dekompozycji albo stosowaÄ tak jak jest zapisane poniĹźej zmieniajÄc tylko
// parametry wywoĹania w wÄtkach
//
//calculate_histogram_wzorzec(start_wiersz, end_wiersz, skok_wiersz,
//                            start_kol, end_kol, skok_kol,
//                            start_znak, end_znak, skok_znak){
//
//  for(int i=start_wiersz;i<end_wiersz;i+=skok_wiersz) {
//     for(int j=start_kol;j<end_kol;j+=skok_kol) {
//        for(int k=start_znak;k<end_znak;k+=skok_znak) {
//           if(tab[i][j] == tab_symb[k]) histogram[k]++;
//


    public void print_histogram(int hist) {
        if (hist == 1) { // Sequential histogram
            for (int i = 0; i < 94; i++) {
                System.out.print(tab_symb[i] + " " + histogram[i] + "\n");
            }
        } else { // Parallel histogram
            for (int i = 0; i < 94; i++) {
                System.out.print(tab_symb[i] + ": ");
                for (int j = 0; j < histogram_parallel[i]; j++) {
                    System.out.print("=");
                }
                System.out.println();
            }
        }
    }

}
