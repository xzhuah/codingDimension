package nodes.ai.imageRecoganization.orcrNode;

import nodes.NodeModule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Xinyu Zhu on 2020/11/26, 21:12
 * nodes.ai.imageRecoganization.orcrNode in codingDimensionTemplate
 */
public class OCRServiceTest {

    OCRService orcService = NodeModule.getGlobalInjector().getInstance(OCRService.class);

    @Test
    public void extractTextFromImage() {
        String result = orcService.extractTextFromImage("D:/1.png");
        System.out.println(result);

    }
}