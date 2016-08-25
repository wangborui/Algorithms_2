/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partTwo_Assignment_2;

import edu.princeton.cs.algs4.Picture;

/**
 *
 * @author Borui Wang
 */
public class SeamCarver {
    private int W;
    private int H;
   public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
   {
       if(picture == null) throw new java.lang.NullPointerException("picture is null");
       H = picture.height();
       W = picture.width();
   }
   public Picture picture()                          // current picture
   {
       return null;
   }
   public     int width()                            // width of current picture
   {
       return -1;
   }
   public     int height()                           // height of current picture
   {
       return -1;
   }
   public  double energy(int x, int y)               // energy of pixel at column x and row y
   {
       if(x < 0 || x > W - 1) throw new java.lang.IndexOutOfBoundsException("x is out of range in energy()");
       if(y < 0 || y > H - 1) throw new java.lang.IndexOutOfBoundsException("y is out of range in energy()");
       return -1;
   }
   public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
   {
       return null;
   }
   public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
   {
       return null;
   }
   public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
   {
       if(seam == null) throw new java.lang.NullPointerException("seam is null");
       if(H <= 1) throw new java.lang.IllegalArgumentException("height of the picture is too small");
   }
   public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
   {
       if(seam == null) throw new java.lang.NullPointerException("seam is null");
        if(W <= 1) throw new java.lang.IllegalArgumentException("width of the picture is too small");
   }
}