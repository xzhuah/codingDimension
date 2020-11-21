package nodes.pianoNode;

import common.utils.CmdTools;

import static nodes.pianoNode.constants.PianoConstant.PIANO_PATH;
import static nodes.pianoNode.constants.PianoConstant.PYTHON_PATH;

/**
 * Created by Xinyu Zhu on 2020/11/20, 22:52
 * nodes.pianoNode in codingDimensionTemplate
 * <p>
 * refer: https://explodingart.com/jmusic/jmtutorial/t1.html
 */
public class PianoLauncher {


    public static void main(String[] args) {
        launchPythonPiano();
    }

    public static void launchPythonPiano() {
        CmdTools.runCommandInBackground(PYTHON_PATH, PIANO_PATH);
    }
}
