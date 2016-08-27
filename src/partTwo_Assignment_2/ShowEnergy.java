/******************************************************************************
 *  Compilation:  javac ShowEnergy.java
 *  Execution:    java ShowEnergy input.png
 *  Dependencies: SeamCarver.java SCUtility.java
 *                
 *
 *  Read image from file specified as command line argument. Show original
 *  image (only useful if image is large enough).
 *
 ******************************************************************************/
package partTwo_Assignment_2;

/**
 *
 * @author Borui Wang
 */
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.io.File;

public class ShowEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(new File("C:\\Users\\Borui Wang\\Desktop\\Borui Wang\\Coursera\\Algorithms Part 2\\Algorithms Part 2\\assignments\\ProgrammingAssignment2SeamCarvingHelpCenter\\seamCarving-testing\\seamCarving\\chameleon.png"));
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}