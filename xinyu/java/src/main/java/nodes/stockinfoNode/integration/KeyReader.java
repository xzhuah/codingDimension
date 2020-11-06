package nodes.stockinfoNode.integration;

import common.io.file.PlaintextClient;

/**
 * Created by Xinyu Zhu on 2020/11/5, 22:46
 * nodes.stockinfoNode.integration in codingDimensionTemplate
 */
public class KeyReader {
    private static final String keyFile = "stockinfoKey.pass";
    public static String getKey() {
        return PlaintextClient.readFile(keyFile).trim();
    }
}