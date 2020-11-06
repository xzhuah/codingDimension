package common.io.file;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Xinyu Zhu on 7/2/2020, 10:30 PM
 * common.io.file in AllInOne
 */
public class PlaintextClient {
    public static String readFile(String filename) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = in.readLine()) != null) {
                builder.append(str + System.lineSeparator());
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<String> readFileLines(String filename) {
        String content = readFile(filename);
        if (null == content) {
            return null;
        }
        return readFileLines(filename, System.lineSeparator());
    }

    // automatically remove prefix and suffix spliter
    public static List<String> readFileLines(String filename, String spliter) {
        String content = readFile(filename);

        // remove prefix spliter
        while (content.startsWith(spliter)) {
            content = content.replaceFirst(spliter, "");
        }
        // remove prefix suffix spliter
        while (content.endsWith(spliter)) {
            content = content.substring(0, content.length() - spliter.length());
        }
        if (null == content) {
            return null;
        }

        List<String> result = Arrays.asList(content.split(spliter));

        return result;
    }

    public static void write(String filename, String content) {
        write(filename, content, false);
    }

    private static void write(String filename, String content, boolean append) {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, append), "UTF-8"))) {
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String filename, String content) {
        write(filename, content, true);
    }
}
