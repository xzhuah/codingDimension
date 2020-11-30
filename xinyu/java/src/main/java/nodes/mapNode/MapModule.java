package nodes.mapNode;

import com.google.inject.AbstractModule;
import nodes.mapNode.china.ChineseAreaInfoService;
import nodes.mapNode.china.db.ChineseLevel5AreaInfoDBService;
import nodes.mapNode.china.db.impls.ChineseLevel5AreaInfoDBServiceImpl;
import nodes.mapNode.china.impls.ChineseAreaInfoServiceImpl;

/**
 * Created by Xinyu Zhu on 2020/11/29, 19:47
 * nodes.mapNode in codingDimensionTemplate
 */
public class MapModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        bind(ChineseLevel5AreaInfoDBService.class).to(ChineseLevel5AreaInfoDBServiceImpl.class);
        bind(ChineseAreaInfoService.class).to(ChineseAreaInfoServiceImpl.class);
    }
}
