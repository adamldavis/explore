
package explore;

import java.io.IOException;
import static java.lang.System.out;
import static java.lang.System.in;
import static java.lang.System.err;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author adavis
 */
public class Explore {

    static final Scanner scan = new Scanner(in);
    
    static final Logger logger = Logger.getLogger(Explore.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        out.println("Hello, welcome to Explore version 0.0.1");
        out.println();
        Stream.generate(() -> "What is the name of your project?")
                .map(Explore::inputString)
                .forEach(Explore::createProject);
    }
    
    public static void createProject(String name) {
        try {
            Path p  = Paths.get(name);
            Files.createDirectory(p);
            System.exit(0);
        } catch (IOException ex) {
            out.println("Please try again");
        }
        
    }
    
    public static int inputInt(String str) {
        out.println(str);
        return scan.nextInt();
    }
    
    public static String inputString(String str) {
        out.println(str);
        return scan.next();        
    }
    
    public static final void err(String s) {
        err.println(s);
    }
    
    public static final void err(Exception e) {
        e.printStackTrace(err);
    }
    
}
