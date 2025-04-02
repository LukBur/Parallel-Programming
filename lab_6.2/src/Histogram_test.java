import java.util.Scanner;

import static java.lang.Math.ceil;


class Histogram_test {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Set image size: n(#rows), m(#kolumns)");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Obraz obraz_1 = new Obraz(n, m);

        //operacje sekwencyjne
        obraz_1.calculate_histogram();
        obraz_1.print_histogram();

        System.out.println("Set number of threads");
        int num_threads = scanner.nextInt();
        int leap;

        //WatekRunnable[] NewThr = new WatekRunnable[num_threads];
        //Thread[] NewThr = new Thread(new WatekRunnable());
        Thread NewThr[] = new Thread[num_threads];

        int totalSymbols = 94;
        int blockSize = (int) ceil((double) totalSymbols / num_threads);

        //default
        /*
        for (int i = 0; i < num_threads; i++) {
            char symbol = (char) (33 + (i % 94));
            (NewThr[i] = new WatekRunnable(symbol,obraz_1)).start();
        }
        */

        //blokowo
        long startParallel = System.nanoTime();
        for(int i=0; i<num_threads; i++){
            int moj_start = 33 + i * blockSize;
            int moj_koniec = Math.min(33 + (i + 1) * blockSize, 127);

            WatekRunnable runnable = new WatekRunnable(i, moj_start, moj_koniec, obraz_1);
            NewThr[i] = new Thread(runnable);
            NewThr[i].start();
        }

        //cyklicznie
        /*System.out.println("Podaj wielkosc skoku: ");
        leap = scanner.nextInt();

        for(int i=0; i<n; i+=leap){
            for(int j=0; j<num_threads; j++){
                WatekRunnable runnable = new WatekRunnable(i, )
            }
        }*/

        for (int i = num_threads - 1; i >= 0; i--) {
            try {
                NewThr[i].join();
            } catch (InterruptedException e) {}
        }
        long endParallel = System.nanoTime();
        long timeParallel = endParallel - startParallel;

        System.out.println("Czas wykonania (równolegle): " + timeParallel + " ns");

        obraz_1.compare_histograms();
    }

}