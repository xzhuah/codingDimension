package nodes.ai.imageRecoganization.orcrNode.impls;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nodes.ai.imageRecoganization.orcrNode.OCRService;
import nodes.ai.imageRecoganization.orcrNode.constants.OCRConstant;

import java.io.File;

/**
 * Created by Xinyu Zhu on 2020/11/26, 21:08
 * nodes.ai.imageRecoganization.orcrNode.impls in codingDimensionTemplate
 */
public class OCRServiceImpl implements OCRService {

    private final Tesseract tesseract;

    public OCRServiceImpl() {
        tesseract = new Tesseract();
        tesseract.setDatapath(OCRConstant.TESSERACT_PATH);
    }
    @Override
    public String extractTextFromImage(String imageFile) {
        try {
            return tesseract.doOCR(new File(imageFile));
        } catch (TesseractException e) {
            e.printStackTrace();
            return "";
        }
    }
}
