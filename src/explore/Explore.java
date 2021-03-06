/*
 * Copyright 2014 Adam L. Davis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package explore;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import static com.googlecode.totallylazy.Pair.pair;
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

    static final Map<Integer, Runnable> commands = Commands.intMap;

    public static final String VERSION = "0.0.22";
    public static final String HELLO_MSG = "Hello, welcome to Explore version "
            + VERSION;
    public static final String INITIAL_QUERY = "Enter 1 to create a project "
            + "and 2 for Fibonacci";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        out.println(HELLO_MSG);
        out.println();
        inputAndDo(() -> INITIAL_QUERY,
                Explore::inputInt, captureEx(Explore::handleInitialInput));
    }

    static void handleInitialInput(int i) {
        commands.getOrDefault(i,
                () -> {
                    throw new IllegalArgumentException("Unknown option:" + i);
                }
        ).run();
    }

    /**
     * Captures any Exception thrown and wraps it in Optional.
     */
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
        return fastfibTail(pair(1, 1L), n).invoke();
    }

    // fibonacci using "tail recursion"
    public static Tail<Long> fastfibTail(final Pair<Integer, Long> p, final int n) {
        return () -> {
            switch (n) {
                case 1:
                    return Tail.done(p.second());
                default:
                    return fastfibTail(pair(p.first() + 1,
                            fibMap.computeIfAbsent(p.first() + 1,
                                    x -> p.second() + fibMap.get(x - 2))), n - 1);
            }
        };
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
                .peek(ex -> {
                    if (ex.isPresent()) {
                        err(ex.get().getMessage());
                    }
                })
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
