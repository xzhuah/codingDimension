package nodes.stockinfoNode;

import com.google.inject.Key;
import nodes.NodeModule;
import nodes.stockinfoNode.impls.annota.ZeroDelay;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/11, 21:42
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class PriceAutoUpdaterTest {

    PriceAutoUpdater autoUpdater;

    @Test
    public void test() {
        autoUpdater = NodeModule.getGlobalInjector().getInstance(PriceAutoUpdater.class);

        autoUpdater = NodeModule.getGlobalInjector().getInstance(Key.get(PriceAutoUpdater.class, ZeroDelay.class));
    }
}