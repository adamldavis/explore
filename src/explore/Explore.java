package explore;

import java.io.IOException;
import static java.lang.System.out;
import static java.lang.System.in;
import static java.lang.System.err;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;
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
        inputAndDo(() -> new String[]{
            "What is the name of your project?",
            "What is your name?",
            "What is your email?"},
                Explore::inputStrings, Explore::createProject);
    }

    private static <S, T> void inputAndDo(Supplier<S> messagef,
            Function<S, T> inputf, Function<T, Optional<Exception>> dof) {
        Stream.generate(messagef)
                .map(inputf)
                .map(dof)
                .filter((ex) -> !ex.isPresent())
                .limit(1)
                .forEach(x -> out.println("Done"));
    }

    public static Optional<Exception> createProject(String[] array) {
        try {
            final String name = array[0];
            final String user = array[1];
            final String email = array[2];
            Path p = Paths.get(name);
            Files.createDirectory(p);
            Path readme = Paths.get(name, "README");
            Files.createFile(readme);
            Files.write(readme, Arrays.asList("Project: " + name,
                    "Author: " + user, "Email: " + email));
        } catch (IOException ex) {
            out.println("Please try again");
            return Optional.of(ex);
        }
        return Optional.empty();
    }

    public static int inputInt(String str) {
        out.println(str);
        return scan.nextInt();
    }

    public static String[] inputStrings(String[] str) {
        return Arrays.stream(str).map(Explore::inputString)
                .toArray((n) -> new String[n]);
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
