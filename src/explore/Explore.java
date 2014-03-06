package explore;

import com.googlecode.totallylazy.Pair;
import java.io.IOException;
import static java.lang.System.out;
import static java.lang.System.in;
import static java.lang.System.err;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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
        inputAndDo(() -> "Enter 1 to create a project and 2 for Fibonacci",
                Explore::inputInt, captureEx(Explore::handleInitialInput));
    }

    public static <T> Function<T, Optional<Exception>> captureEx(Consumer<T> f) {
        return t -> {
            try {
                f.accept(t);
                return Optional.empty();
            } catch (Exception e) {
                return Optional.of(e);
            }
        };
    }

    public static void handleInitialInput(int i) {
        if (i == 1) {
            handleCreateProject();
        } else if (i == 2) {
            handleFibonacci();
        } else {
            throw new IllegalArgumentException("Unknown option:" + i);
        }
    }

    public static void handleFibonacci() {
        inputAndDo(() -> "Please enter a positive natural number (< 93).",
                Explore::inputInt, captureEx(n
                        -> out.println(fastfib(n))));
    }

    static final Map<Integer, Long> fibMap = new ConcurrentHashMap<>();

    public static long fastfib(final int n) {
        fibMap.put(0, 0L);
        fibMap.put(1, 1L);
        fibMap.put(2, 1L);
        // Pair of index, fib-value
        return Stream
                .iterate(Pair.pair(1, 1L),
                        p -> Pair.pair(p.first() + 1,
                                fibMap.computeIfAbsent(p.first() + 1,
                                        z -> p.second() + fibMap.get(p.first() - 1))))
                .peek(out::println)
                .skip(n - 1)
                .findFirst()
                .get().second();
    }

    public static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    public static void handleCreateProject() {
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
