package nodes.hotinfoNode;

import nodes.NodeModule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/11, 21:12
 * nodes.hotinfoNode in codingDimensionTemplate
 */
public class BilibiliInfoManagerTest {
    private final BilibiliInfoManager bilibiliInfoManager = NodeModule.getGlobalInjector().getInstance(BilibiliInfoManager.class);

    @Test
    public void collectLatestHotRanking() {
        long start = System.currentTimeMillis();
        bilibiliInfoManager.collectLatestHotRanking();
        System.out.println("Used " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        bilibiliInfoManager.exit();
    }
}