package nodes.pianoNode;

import common.utils.CmdTools;

import java.io.IOException;

import static nodes.pianoNode.constants.PianoConstant.PIANO_PATH;
import static nodes.pianoNode.constants.PianoConstant.PYTHON_PATH;

/**
 * Created by Xinyu Zhu on 2020/11/20, 22:52
 * nodes.pianoNode in codingDimensionTemplate
 *
 * refer: https://explodingart.com/jmusic/jmtutorial/t1.html
 */
public class PianoLauncher {
    public static void main(String[] args) {
        launchPythonPiano();
    }

    public static void launchPythonPiano() {
        try {
            CmdTools.runCommandInBackground(PYTHON_PATH, PIANO_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
