package nodes.exampleNode.impl;

import common.framework.NodeService;
import common.framework.ServiceParam;
import nodes.exampleNode.ExampleService;
import nodes.exampleNode.models.ExampleServiceParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static nodes.exampleNode.contants.Constant.EXAMPLE_MESSAGE;

/**
 * Created by Xinyu Zhu on 2020/11/5, 19:37
 * nodes.exampleNode.impl in codingDimensionTemplate
 */
public class ExampleServiceImplTest {
    ExampleService exampleService;
    NodeService nodeService;

    @Before
    public void setUp(){
        exampleService = new ExampleServiceImpl();
        nodeService = new ExampleServiceImpl();
    }

    @Test
    public void test() {
        String exampleString = exampleService.getExampleString();
        Assert.assertEquals(exampleString, EXAMPLE_MESSAGE);

        ServiceParam exampleServiceParam = new ExampleServiceParam(String.valueOf(System.currentTimeMillis()));
        nodeService.runService(exampleServiceParam);
    }
}