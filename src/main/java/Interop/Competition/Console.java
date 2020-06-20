package Interop.Competition;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Function;

/**
 * @author Tomasz Darmetko
 */
public class Console {

    public static void disableErrOutput() {
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
    }

    public static void runSilent(Runnable runnable) {
        PrintStream consoleOutput = setSystemOutSink();
        runnable.run();
        System.setOut(consoleOutput);
    }

    public static <T, R> R runSilent(Function<T, R> function, T input) {
        PrintStream consoleOutput = setSystemOutSink();
        R r =function.apply(input);
        System.setOut(consoleOutput);
        return r;
    }

    private static PrintStream setSystemOutSink() {
        PrintStream ps = new PrintStream(new ByteArrayOutputStream());
        PrintStream consoleOutput = System.out;
        System.setOut(ps);
        System.out.flush();
        return consoleOutput;
    }

}
