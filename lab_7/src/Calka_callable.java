import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Calka_callable implements Callable<Double>{
//public class Calka_callable{
    
    private double dx;
    private double xp;
    private double xk;
    private int N;
    
    public Calka_callable(double xp, double xk, double dx) {
	this.xp = xp;
	this.xk = xk;
	this.N = (int) Math.ceil((xk-xp)/dx);
	this.dx = (xk-xp)/N;
	System.out.println("Creating an instance of Calka_callable");
	System.out.println("xp = " + xp + ", xk = " + xk + ", N = " + N);
	System.out.println("dx requested = " + dx + ", dx final = " + this.dx);
	
    }
    
    private double getFunction(double x) {
		return Math.sin(x);
    }
    
    public double compute_integral() {
	double calka = 0;
	int i;
	for(i=0; i<N; i++){
	    double x1 = xp+i*dx;
	    double x2 = x1+dx;
	    calka += ((getFunction(x1) + getFunction(x2))/2.)*dx;
	}
	System.out.println("Calka czastkowa: " + calka);
		return calka;
    }

	public static void main(String[] args){

		double xp = 0;
		double xk = Math.PI;
		double dx = 0.01;
		int numTasks = 10;
		int numThreads = 4;

		double intervalWidth = (xk - xp) / numTasks;

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<Future<Double>> results = new ArrayList<>();

		for (int i=0; i<numTasks; i++){
			double subXp = xp + i * intervalWidth;
			double subXk = subXp + intervalWidth;
			Calka_callable task = new Calka_callable(subXp, subXk, dx);
			results.add(executor.submit(task));
		}

		double totalIntegral = 0;
		try {
			for(Future<Double> result : results){
				totalIntegral += result.get();
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		executor.shutdown();

		System.out.println("Total integral: " + totalIntegral);

	}

	public Double call(){
		return compute_integral();
	}

        
}
