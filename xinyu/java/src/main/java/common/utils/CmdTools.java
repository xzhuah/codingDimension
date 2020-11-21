package common.utils;

import java.io.*;
import java.lang.ProcessBuilder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/20, 22:09
 * common.utils in codingDimensionTemplate
 *
 * Provide a way to execute cmd command from Java, can be used to create process
 * Refer to https://www.baeldung.com/java-lang-processbuilder-api
 */
public class CmdTools {
    public static void main(String[] args) throws IOException {

        runCommandInBackground("java", "--version");
        // runMongoBIConnector();
    }

    public static List<String> runCommand(String... command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process process = processBuilder.start();
        return readOutputFromConsole(process.getInputStream());
    }

    public static void runCommandInBackground(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);

        new Thread(() -> {
            Process process;
            try {
                process = processBuilder.start();
                printOutputFromConsole(process.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private static List<String> readOutputFromConsole(InputStream inputStream) {
        List<String> result = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String str;
            while ((str = in.readLine()) != null) {
                result.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void printOutputFromConsole(InputStream inputStream) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Some commanly used command
    public static void runMongoBIConnector() {
        runCommandInBackground("mongosqld");
    }
}
