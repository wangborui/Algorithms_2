/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partTwo_Assignment_2;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
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
    private double [][] color;

    public SeamCarver(Picture picture) // create a seam carver object based on the given picture
    {
        if (picture == null) {
            throw new java.lang.NullPointerException("picture is null");
        }
        H = picture.height();
        W = picture.width();
        p = picture;
        //     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     //* blue).
//        StdOut.println(Integer.toBinaryString(p.get(3, 5).getRGB()));
//        StdOut.println(Integer.toBinaryString(0xFF));
//        StdOut.println(Integer.toBinaryString(0xFF & p.get(3, 5).getRGB()));
//        StdOut.println(0xFF & p.get(3, 5).getRGB());
//        
//         StdOut.println(p.get(3, 5).getBlue());
//         StdOut.println(0xFF & (p.get(3, 5).getRGB() >> 8));
//        StdOut.println(p.get(3, 5).getGreen());
//        StdOut.println(0xFF & (p.get(3, 5).getRGB() >> 16));
//        StdOut.println(p.get(3, 5).getRed());
       // StdOut.println(p.get(3, 5).getRGB());
         
        energy = new double[H][W];
        color = new double[H][W];
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                energy[h][w] = energy(w, h);
                color[h][w] = p.get(w, h).getRGB();
            }
        }
    }

    public Picture picture() // current picture
    {
        Picture pic = new Picture(width(),height());
        for (int h = 0; h < height(); h++) {
            for (int w = 0; w < width(); w++) {
                 pic.set(w, h, new Color((int)color[h][w])); 
            }
        }
        return pic;
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
        if (x < 0 || x > width() - 1) {
            throw new java.lang.IndexOutOfBoundsException("x is out of range in energy()");
        }
        if (y < 0 || y > height() - 1) {
            throw new java.lang.IndexOutOfBoundsException("y is out of range in energy()");
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
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
        //transpose energy 2d array
        energy = transpose(energy);
        H = energy.length;
        W = energy[0].length;
        int []seam = findVerticalSeam();
        energy = transpose(energy);
        H = energy.length;
        W = energy[0].length;

        return seam;
    }
    private double [][] transpose(double [][] matrix){
        double [][] transposedEnergy = new double[width()][height()];
        
        for (int h = 0; h < transposedEnergy.length; h++) {
            for (int w = 0; w < transposedEnergy[0].length; w++) {
                //StdOut.println("Trans["+h+"]["+w+"] Matrix[" + w+"]["+h+"]" );
                transposedEnergy[h][w] = matrix[w][h];
            }
        }
 
        //StdOut.println("================================================");
        return transposedEnergy;
    }
    public int[] findVerticalSeam() // sequence of indices for vertical seam
    {    

        double[][] distance = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row != 0) {
                     distance[row][col] = Double.MAX_VALUE;
                 }  
            }
        }
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                 relax(row, col, distance, edgeTo);
            }
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
        //trace edges back to the top root
        for (int e = edgeTo[height][minIndex]; height >= 0; e = edgeTo[height--][e]) {
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
        if (height() <= 1) {
            throw new java.lang.IllegalArgumentException("height of the picture is too small");
        }
        if (seam.length != width()) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
        //transpose energy and color 
        energy = transpose(energy);
        color = transpose(color);
        H = energy.length;
        W = energy[0].length;
        //remove the seam
        removeVerticalSeam(seam);
        //transpose energy and color
        energy = transpose(energy);
        color = transpose(color);
        H = energy.length;
        W = energy[0].length;
        //reduce height after removal
        
    }

    public void removeVerticalSeam(int[] seam) // remove vertical seam from current picture
    {
        if (seam == null) {
            throw new java.lang.NullPointerException("seam is null");
        }
        if (width() <= 1) {
            throw new java.lang.IllegalArgumentException("width of the picture is too small");
        }
        if (seam.length != height()) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
        //double for loop going from top to bottom
        for(int h = 0; h < height(); h++){
            for(int s = seam[h] + 1; s < width(); s++){
                energy[h][s-1] = energy[h][s];
                color[h][s-1] = color[h][s];
            }
        }
        //reduce width after seam removal
        W--;
    }
    private void printEnergy(){
         for(int h = 0; h < height(); h++){
            for(int w = 0; w < width(); w++)
                StdOut.printf("%9.0f ",energy[h][w]);
            StdOut.println();
        }
         
        for(int h = 0; h < height(); h++){
            for(int w = 0; w < width(); w++)
                StdOut.printf("%9.0f ",color[h][w]);
            StdOut.println();
        }
    }
    public static void main(String[] args) {
        Picture picture = new Picture(
                new File("C:\\Users\\Borui Wang\\Desktop\\Borui Wang\\Coursera\\Algorithms Part 2\\"
                        + "Algorithms Part 2\\assignments\\ProgrammingAssignment2SeamCarvingHelpCenter\\"
                        + "seamCarving-testing\\seamCarving\\5x6.png"));

        SeamCarver sc = new SeamCarver(picture);
//        sc.findVerticalSeam();
//        //StdOut.println();
//        sc.printEnergy();
//        for(int a:sc.findVerticalSeam() )
//            StdOut.println(a);
//        sc.removeVerticalSeam(sc.findVerticalSeam());
//        sc.printEnergy();
 
        sc.findVerticalSeam();
        StdOut.println();
        sc.printEnergy();
        for(int a:sc.findHorizontalSeam())
            StdOut.println(a);
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        sc.printEnergy();
        StdOut.println("Remove second horizontal seam");
        StdOut.println();
        for (int a : sc.findHorizontalSeam()) {
            StdOut.println(a);
        }
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        sc.printEnergy();
        StdOut.println(sc.findHorizontalSeam());
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        sc.printEnergy();
        
        //StdOut.println( sc.energy(1, 2));
    }
}
