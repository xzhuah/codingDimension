package nodes.ai.imageRecoganization.orcrNode;

import com.google.inject.AbstractModule;
import nodes.ai.imageRecoganization.orcrNode.impls.OCRServiceImpl;

/**
 * Created by Xinyu Zhu on 2020/11/26, 21:03
 * nodes.ai.imageRecoganization.orcNode in codingDimensionTemplate
 */
public class ocrModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();

        bind(OCRService.class).to(OCRServiceImpl.class);
    }
}
