package nodes.exampleNode.impl;

import common.framework.NodeService;
import common.framework.ServiceParam;
import nodes.exampleNode.ExampleService;
import nodes.exampleNode.models.ExampleServiceParam;

import static nodes.exampleNode.contants.Constant.EXAMPLE_MESSAGE;

/**
 * Created by Xinyu Zhu on 2020/11/2, 23:00
 * nodes.exampleNode.impl in codingDimension
 */
public class ExampleServiceImpl implements ExampleService {

    public void runService(ServiceParam serviceParam) {
        ExampleServiceParam exampleServiceParam = (ExampleServiceParam) serviceParam;
        System.out.println(exampleServiceParam.getMessage() + ":" + getExampleString());
    }

    public String getExampleString() {
        return EXAMPLE_MESSAGE;
    }

    public static void main(String[] args) {
        // Used by user
        ExampleService exampleService = new ExampleServiceImpl();
        System.out.println(exampleService.getExampleString());

        // Used by System
        NodeService nodeService = new ExampleServiceImpl();
        ServiceParam exampleServiceParam = new ExampleServiceParam(String.valueOf(System.currentTimeMillis()));
        nodeService.runService(exampleServiceParam);

    }

}
