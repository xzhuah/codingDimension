package nodes.ai;

import com.google.inject.AbstractModule;
import nodes.ai.imageRecoganization.imageRecoganizationModule;

/**
 * Created by Xinyu Zhu on 2020/11/26, 21:01
 * nodes.ai in codingDimensionTemplate
 */
public class AiModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        install(new imageRecoganizationModule());
    }
}
