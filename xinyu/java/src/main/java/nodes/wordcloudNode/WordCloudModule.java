package nodes.wordcloudNode;

import com.google.inject.AbstractModule;
import nodes.wordcloudNode.impls.WordCloudForWebpageImpl;
import nodes.wordcloudNode.impls.WordCloudGeneratorImpl;
import nodes.wordcloudNode.impls.WordFrequencyHelperImpl;

public class WordCloudModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WordFrequencyHelper.class).to(WordFrequencyHelperImpl.class);

        try {
            bind(WordCloudGenerator.class).toConstructor(WordCloudGeneratorImpl.class.getConstructor(WordFrequencyHelper.class));
        } catch (Exception e) {
            // Actually the same
            bind(WordCloudGenerator.class).to(WordCloudGeneratorImpl.class);
        }

        bind(WordCloudForWebpage.class).to(WordCloudForWebpageImpl.class);
    }
}