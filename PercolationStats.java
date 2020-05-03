/* *****************************************************************************
 *  Name:    Alan Turing
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description:  Prints 'Hello, World' to the terminal window.
 *                By tradition, this is everyone's first program.
 *                Prof. Brian Kernighan initiated this tradition in 1974.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private final int n;
    private final int trials;
    private double[] threshold;
    private final double CONSTANT = 1.96;

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        percolationStats.run();
        StdOut.println("mean = " + percolationStats.mean());
        StdOut.println("stddev = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ","
                               + percolationStats.confidenceHi() + "]");
    }

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.threshold = new double[trials];
    }

    private void run() {
        for (int i = 0; i < trials; i++) {
            int j = 0;
            Percolation percolation = new Percolation(n);
            while (j < n * n) {
                int randomRow = StdRandom.uniform(n);
                int randomColumn = StdRandom.uniform(n);
                if (!percolation.isOpen(randomRow, randomColumn)) {
                    percolation.open(randomRow, randomColumn);
                    if (percolation.percolates()) {
                        threshold[i] = (double) j / (n * n);
                        break;
                    }
                    j++;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials <= 1)
            return Double.NaN;
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONSTANT * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONSTANT * stddev() / Math.sqrt(trials));
    }

}
