/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partTwo_Assignment_2;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.io.File;

/**
 *
 * @author Borui Wang
 */
public class SeamCarver {

    private int W;
    private int H;
    private final Picture p;
    private double[][] energy;

    public SeamCarver(Picture picture) // create a seam carver object based on the given picture
    {
        if (picture == null) {
            throw new java.lang.NullPointerException("picture is null");
        }
        H = picture.height();
        W = picture.width();
        p = picture;
        energy = new double[H][W];
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                energy[h][w] = energy(w, h);
            }
        }
    }

    public Picture picture() // current picture
    {
        return p;
    }

    public int width() // width of current picture
    {
        return W;
    }

    public int height() // height of current picture
    {
        return H;
    }

    public double energy(int x, int y) // energy of pixel at column x and row y
    {
        if (x < 0 || x > W - 1) {
            throw new java.lang.IndexOutOfBoundsException("x is out of range in energy()");
        }
        if (y < 0 || y > H - 1) {
            throw new java.lang.IndexOutOfBoundsException("y is out of range in energy()");
        }

        if (x == 0 || x == W - 1 || y == 0 || y == H - 1) {
            return 1000;
        }

        double xSq = getXSq(x, y);
        double ySq = getYSq(x, y);
        return Math.sqrt(xSq + ySq);
    }

    private double getYSq(int x, int y) {
        int rY = p.get(x, y - 1).getRed() - p.get(x, y + 1).getRed();
        int gY = p.get(x, y - 1).getGreen() - p.get(x, y + 1).getGreen();
        int bY = p.get(x, y - 1).getBlue() - p.get(x, y + 1).getBlue();
        return Math.pow(rY, 2) + Math.pow(gY, 2) + Math.pow(bY, 2);
    }

    private double getXSq(int x, int y) {
        int rX = p.get(x + 1, y).getRed() - p.get(x - 1, y).getRed();
        int gX = p.get(x + 1, y).getGreen() - p.get(x - 1, y).getGreen();
        int bX = p.get(x + 1, y).getBlue() - p.get(x - 1, y).getBlue();
        return Math.pow(rX, 2) + Math.pow(gX, 2) + Math.pow(bX, 2);
    }

    public int[] findHorizontalSeam() // sequence of indices for horizontal seam
    {
        return null;
    }

    public int[] findVerticalSeam() // sequence of indices for vertical seam
    {
        double[][] distance = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) {
                    distance[row][col] = 1000;
                    //sentinel as the root of shortest path
                } else {
                    distance[row][col] = Double.MAX_VALUE;
                }
            }
        }
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                //StdOut.printf("%9.0f ", energy[row][col]);
                relax(row, col, distance, edgeTo);
            }
            // StdOut.println();
        }

        return seam(distance, edgeTo);
    }

    private int[] seam(double[][] distance, int[][] edgeTo) {
        int[] seam = new int[height()];
        double minVal = Integer.MAX_VALUE;
        int minIndex = -1;
        //find the min value on the bottom row
        for (int i = 0; i < width(); i++) {
            if (distance[height() - 1][i] < minVal) {
                minVal = distance[height() - 1][i];
                minIndex = i;
            }
        }
        int height = height() - 1;

        for (int e = edgeTo[height][minIndex]; e != -1 && height >= 0; e = edgeTo[height--][e]) {
            seam[height] = e;
        }
        return seam;
    }

    private void relax(int row, int col, double[][] distance, int[][] edgeTo) {
        //relax three vertices below currenct vertex, belowLeft, below, and belowRight
        int start = -1, max = 2;
        if (col == 0) {
            start = 0;
        } else if (col == W - 1) {
            max = 1;
        }
        for (int i = start; i < max; i++) {           //StdOut.print(energy[row+1][col+i] + " ");
            if (distance[row + 1][col + i] > distance[row][col] + energy[row + 1][col + i]) {
                distance[row + 1][col + i] = distance[row][col] + energy[row + 1][col + i];
                edgeTo[row + 1][col + i] = col;
            }
        }

    }

    public void removeHorizontalSeam(int[] seam) // remove horizontal seam from current picture
    {
        if (seam == null) {
            throw new java.lang.NullPointerException("seam is null");
        }
        if (H <= 1) {
            throw new java.lang.IllegalArgumentException("height of the picture is too small");
        }
        if (seam.length != W) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
    }

    public void removeVerticalSeam(int[] seam) // remove vertical seam from current picture
    {
        if (seam == null) {
            throw new java.lang.NullPointerException("seam is null");
        }
        if (W <= 1) {
            throw new java.lang.IllegalArgumentException("width of the picture is too small");
        }
        if (seam.length != H) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
    }

    public static void main(String[] args) {
        Picture picture = new Picture(
                new File("C:\\Users\\Borui Wang\\Desktop\\Borui Wang\\Coursera\\Algorithms Part 2\\"
                        + "Algorithms Part 2\\assignments\\ProgrammingAssignment2SeamCarvingHelpCenter\\"
                        + "seamCarving-testing\\seamCarving\\diagonals.png"));

        SeamCarver sc = new SeamCarver(picture);
        sc.findVerticalSeam();
        for(int a:sc.findVerticalSeam() )
        StdOut.println(a);
        //StdOut.println( sc.energy(1, 2));
    }
}
