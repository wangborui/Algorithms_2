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
    private boolean isTransposed;

    public SeamCarver(Picture picture) // create a seam carver object based on the given picture
    {
        if (picture == null) {
            throw new java.lang.NullPointerException("picture is null");
        }
        H = picture.height();
        W = picture.width();
        p = picture;
        isTransposed = false;

        energy = new double[H][W];
        color = new double[H][W];
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                color[h][w] = p.get(w, h).getRGB();
            }
        }
         for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {             
                energy[h][w] = energy(w, h);
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
        //reverse x and y
        double xSq = getXSq(y, x);
        double ySq = getYSq(y, x);
        return Math.sqrt(xSq + ySq);
    }

    private double getYSq(int h, int w) {
 
         int rY = (0xFF & ((int) color[h-1][w] >> 16 )) - (0xFF & ((int) color[h+1][w] >> 16 ));
         int gY = (0xFF & ((int) color[h-1][w] >> 8 )) - (0xFF & ((int) color[h+1][w] >> 8 ));
         int bY = (0xFF & ((int) color[h-1][w] )) - (0xFF & ((int) color[h+1][w] ));
 
        return Math.pow(rY, 2) + Math.pow(gY, 2) + Math.pow(bY, 2);
    }

    private double getXSq(int h, int w) {
 
        int rX = (0xFF & ((int) color[h][w + 1]  >> 16 )) - (0xFF & ((int) color [h][w-1] >> 16 ));
        int gX = (0xFF & ((int) color[h][w + 1]  >> 8 )) - (0xFF & ((int) color [h][w-1] >> 8 ));
        int bX = (0xFF & ((int) color[h][w + 1]  )) - (0xFF & ((int) color [h][w-1]  ));
 
        return Math.pow(rX, 2) + Math.pow(gX, 2) + Math.pow(bX, 2);
    }

    public int[] findHorizontalSeam() // sequence of indices for horizontal seam
    {
        //transpose energy 2d array
        if (!isTransposed) {
            transpose();
            isTransposed = !isTransposed;
        }
        return findSeam();
    }
    private void transpose(){
        int transWidth = height();
        int transHeight = width();
        double [][] transposedEnergy = new double[transHeight][transWidth];
        double [][] transposedColor = new double[transHeight][transWidth];

        for (int h = 0; h < transHeight; h++) {
            for (int w = 0; w < transWidth; w++) {
                 transposedEnergy[h][w] = energy[w][h];
                 transposedColor[h][w] = color[w][h];
            }
        }
        energy = transposedEnergy;
        color = transposedColor;
        H = transHeight;
        W = transWidth;
     }
    public int[] findVerticalSeam() // sequence of indices for vertical seam
    {    
        if (isTransposed) {
            transpose();
            isTransposed = !isTransposed;
        }

        return findSeam();
    }
    private int[] findSeam(){
        if(width() == 1){
            int [] seam = new int[height()];
            return seam;
        }
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
        //print all distances 
//        StdOut.println("distance");
//        for(double[]a:distance){
//            for(double b: a){
//                StdOut.printf("%9.0f ",b);
//            }
//            StdOut.println();
//        }
//        StdOut.println();
        //find the min value on the bottom row
        for (int i = 0; i < width(); i++) {
            if (distance[height() - 1][i] < minVal) {
                minVal = distance[height() - 1][i];
                minIndex = i;
            }
        }
        
//         StdOut.println("edgeTo");
//        for(int[]a:edgeTo){
//            for(int b: a){
//                StdOut.printf("%9d ",b);
//            }
//            StdOut.println();
//        }
//        StdOut.println();
        int height = height() - 1;
        //trace edges back to the top root
        for (int e = minIndex; height >= 0; e = edgeTo[height--][e]) {
            seam[height] = e;
        }
//          for(double b: seam){
//                StdOut.printf("%9.0f ",b);
//         }
//           StdOut.println();
        return seam;
    }

    private void relax(int row, int col, double[][] distance, int[][] edgeTo) {
        //relax three vertices below currenct vertex, belowLeft, below, and belowRight
        int start = -1, max = 2;
        if (col == 0) {
            start = 0;
        } else if (col == width() - 1) {
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
         //transpose energy and color
        if(!isTransposed){
            transpose();
            isTransposed = !isTransposed;
        }
        if (seam == null) {
            throw new java.lang.NullPointerException("seam is null");
        }
        if (width() <= 1) {
            throw new java.lang.IllegalArgumentException("height of the picture is too small");
        }
        if (seam.length != height()) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
       
        //remove the seam
        removeSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) // remove vertical seam from current picture
    {
        if(isTransposed){
            transpose();
            isTransposed = !isTransposed;
        }
        if (seam == null) {
            throw new java.lang.NullPointerException("seam is null");
        }
        if (width() <= 1) {
            throw new java.lang.IllegalArgumentException("width of the picture is too small");
        }
        if (seam.length != height()) {
            throw new java.lang.IllegalArgumentException("seam is invalid");
        }
        
         
        removeSeam(seam);
 
    }
    private void removeSeam(int[] seam){
        //remove seam vertically
        for(int h = 0; h < height(); h++){
            for(int s = seam[h] + 1; s < width(); s++){
                energy[h][s-1] = energy[h][s];
                color[h][s-1] = color[h][s];
            }
        }
        //reduce width after seam removal
        W--;
        //recalculate energy for pixels after seam removal
        for (int h = 0; h < height(); h++) {
            for (int s = seam[h]; s < width(); s++) {
                energy[h][s] = energy(s,h);
            }
        }
    }
    private void printEnergy(){
         for(int h = 0; h < height(); h++){
            for(int w = 0; w < width(); w++)
                StdOut.printf("%9.0f ",energy[h][w]);
            StdOut.println();
        }
         StdOut.println("============================");
//         
//        for(int h = 0; h < height(); h++){
//            for(int w = 0; w < width(); w++)
//                StdOut.printf("%9.0f ",color[h][w]);
//            StdOut.println();
//        }
    }
    public static void main(String[] args) {
        Picture picture = new Picture(
                new File("C:\\Users\\Borui Wang\\Desktop\\Borui Wang\\Coursera\\Algorithms Part 2\\"
                        + "Algorithms Part 2\\assignments\\ProgrammingAssignment2SeamCarvingHelpCenter\\"
                        + "seamCarving-testing\\seamCarving\\3x4.png"));

        SeamCarver sc = new SeamCarver(picture);
        //remove all vertical seams one at a time
//         int width =  sc.width();
//        for(int w = 0; w < width; w++){
//            StdOut.println(w);
//            sc.removeVerticalSeam(sc.findVerticalSeam());
//            sc.printEnergy();
//        }

        int height =  sc.height();
        for(int w = 0; w < height; w++){
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
            sc.printEnergy();
        }
    }
}
