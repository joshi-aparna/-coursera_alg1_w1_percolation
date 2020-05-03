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

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;
    private boolean[][] field;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF weightedQuickUnionUF2;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("invalid n");
        virtualTop = n * n;
        virtualBottom = (n * n) + 1;
        openSites = 0;
        this.n = n;
        field = new boolean[n][n];

        this.weightedQuickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
        this.weightedQuickUnionUF2 = new WeightedQuickUnionUF((n * n) + 1);
    }

    private void valid(int row, int col) {
        if (row < 0 || col < 0 || row > n - 1 || col > n - 1)
            throw new IllegalArgumentException("invalid row " + row + " or column " + col);
    }

    public boolean isOpen(int inRow, int inCol) {
        // this is required to support index starting at 1
        int row = inRow - 1;
        int col = inCol - 1;
        valid(row, col);
        return field[row][col];
    }

    public void open(int inRow, int inCol) {
        int row = inRow - 1;
        int col = inCol - 1;
        valid(row, col);
        field[row][col] = true;
        if (row > 0 && isOpen(inRow - 1, inCol)) {
            weightedQuickUnionUF.union(row * n + col, (row - 1) * n + col);
            weightedQuickUnionUF2.union(row * n + col, (row - 1) * n + col);
        }
        if (row < n - 1 && isOpen(inRow + 1, inCol)) {
            weightedQuickUnionUF.union(row * n + col, (row + 1) * n + col);
            weightedQuickUnionUF2.union(row * n + col, (row + 1) * n + col);
        }
        if (col > 0 && isOpen(inRow, inCol - 1)) {
            weightedQuickUnionUF.union(row * n + col, row * n + col - 1);
            weightedQuickUnionUF2.union(row * n + col, row * n + col - 1);
        }
        if (col < n - 1 && isOpen(inRow, inCol + 1)) {
            weightedQuickUnionUF.union(row * n + col, row * n + col + 1);
            weightedQuickUnionUF2.union(row * n + col, row * n + col + 1);
        }
        if (row == 0) {
            weightedQuickUnionUF.union(row * n + col, virtualTop);
            weightedQuickUnionUF2.union(row * n + col, virtualTop);
        }
        if (row == n - 1)
            weightedQuickUnionUF.union(row * n + col, virtualBottom);
        openSites++;
    }

    public boolean isFull(int inRow, int inCol) {
        int row = inRow - 1;
        int col = inCol - 1;
        valid(row, col);
        return weightedQuickUnionUF2.find(virtualTop) == weightedQuickUnionUF2.find(row * n + col);
    }

    public boolean percolates() {
        return weightedQuickUnionUF.find(virtualTop) == weightedQuickUnionUF.find(virtualBottom);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

}
