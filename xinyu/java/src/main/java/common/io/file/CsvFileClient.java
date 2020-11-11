package common.io.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Xinyu Zhu on 7/3/2020, 2:45 AM
 * common.io.file in AllInOne
 * Csv file has a more static structure
 * first line must be header
 * column must be splited by ,
 * line must be splited by system default linesplitor
 */
public class CsvFileClient {
    private final String SPLITOR = ",";
    private List<String> header;
    private final String filename;

    public CsvFileClient(String filename) {
        this.filename = filename;

    }

    public List<JsonObject> readAsJson() throws IOException {
        List<JsonObject> result = new ArrayList<>();
        List<String> contents = PlaintextClient.readFileLines(filename);
        if (null == contents || contents.isEmpty()) {
            return result;
        } else {
            if (null == this.header) {
                this.header = Arrays.asList(contents.get(0).split(SPLITOR));
            }
            for (int i = 1; i < contents.size(); i++) {
                JsonObject object = new JsonObject();
                String[] line = contents.get(i).split(SPLITOR);
                for (int j = 0; j < header.size(); j++) {
                    if (j >= line.length) {
                        break;
                    }
                    object.addProperty(header.get(j), line[j]);
                }
                result.add(object);
            }

        }
        return result;
    }

    public void writeWithJson(List<JsonObject> content) {
        if (null == content || content.isEmpty()) {
            return;
        }
        if (null == header) {
            header = parseKeys(content.get(0));
        }
        String builder = getHeader() + System.lineSeparator() +
                convertToString(content);
        PlaintextClient.write(filename, builder);
    }

    public void appendWithJson(List<JsonObject> content) throws IOException {
        if (null == content || content.isEmpty()) {
            return;
        }
        if (null == header) {
            header = parseKeys(content.get(0));
        }

        StringBuilder builder = new StringBuilder();

        String oldContent = PlaintextClient.readFile(filename);
        if (oldContent.length() == 0) {
            builder.append(getHeader()).append(System.lineSeparator());
        }

        builder.append(convertToString(content));

        PlaintextClient.appendFile(filename, builder.toString());
    }

    private List<String> parseKeys(JsonObject jsonObject) {
        List<String> allKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            allKeys.add(entry.getKey());
        }
        return allKeys;
    }

    private String getHeader() {
        if (null == header) {
            return "";
        }
        return String.join(SPLITOR, header);
    }

    // if not set, will be automatically identify as read or write content
    public void setHeader(List<String> header) {
        this.header = new ArrayList<>(header);
    }

    private String convertToString(List<JsonObject> content) {

        StringBuilder builder = new StringBuilder();
        for (JsonObject jsonObject : content) {
            for (int i = 0; i < header.size(); i++) {
                builder.append(jsonObject.get(header.get(i)).getAsString());
                if (i != header.size() - 1) {
                    builder.append(SPLITOR);
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

}
