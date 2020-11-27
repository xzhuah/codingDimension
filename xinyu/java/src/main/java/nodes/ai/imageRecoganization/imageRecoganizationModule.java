package nodes.ai.imageRecoganization;

import com.google.inject.AbstractModule;
import nodes.ai.imageRecoganization.orcrNode.ocrModule;

/**
 * Created by Xinyu Zhu on 2020/11/26, 21:02
 * nodes.ai.imageRecoganization in codingDimensionTemplate
 */
public class imageRecoganizationModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        install(new ocrModule());
    }
}