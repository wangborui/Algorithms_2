/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object. 
 * 
 ******************************************************************************/

package partTwo_Assignment_2;

 
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.io.File;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(new File("C:\\Users\\Borui Wang\\Desktop\\Borui Wang\\Coursera\\Algorithms Part 2\\Algorithms Part 2\\assignments\\ProgrammingAssignment2SeamCarvingHelpCenter\\seamCarving-testing\\seamCarving\\3x4.png"));
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
             StdOut.println();
        }
    }

}