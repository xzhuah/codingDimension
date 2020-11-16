package nodes.datascienceNode.stockInfo.utils;

import java.util.List;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/15, 18:28
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class ReportPrinter {
    public static void printReport(final List<String> columnLabels,  final List<String> rowLabels, final List<List<String>> values, final String cornerLabel, final boolean showLineNumber) {
        checkStatus(rowLabels.size() == values.size(), "row labels number does not match values number");
        checkStatus(values.size() == 0 || values.get(0).size() == columnLabels.size(), "column number is incorrect");

        System.out.println(cornerLabel + "\t" + String.join("\t", columnLabels));
        if (showLineNumber) {
            int rowIndex = 1;
            for (int i = 0; i < rowLabels.size(); i++) {
                System.out.println(rowIndex + ":" + rowLabels.get(i) + "\t" + String.join("\t", values.get(i)));
                rowIndex += 1;
            }
        } else {
            for (int i = 0; i < rowLabels.size(); i++) {
                System.out.println(rowLabels.get(i) + "\t" + String.join("\t", values.get(i)));
            }
        }
    }
}
