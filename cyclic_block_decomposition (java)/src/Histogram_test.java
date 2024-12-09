import java.util.Scanner;
import static java.lang.Math.ceil;

class Histogram_test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Set image size: n (#rows), m (#columns)");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Obraz obraz_1 = new Obraz(n, m);

        // Sequential operations
        obraz_1.calculate_histogram();
        obraz_1.print_histogram(1); // Print sequential histogram

        // Parallel operation
        System.out.println("Set number of threads");
        int num_threads = scanner.nextInt();

        Thread[] threads = new Thread[num_threads];

        // Create threads to process columns cyclically
        for (int i = 0; i < num_threads; i++) {
            threads[i] = new Thread(new WatekRunnableCyclic(i, n, m, num_threads, obraz_1));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < num_threads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Compare histograms to check if the parallel version is correct

        obraz_1.print_histogram(0); // Print the final histogram (parallel)
        obraz_1.compare_histograms();
    }
}