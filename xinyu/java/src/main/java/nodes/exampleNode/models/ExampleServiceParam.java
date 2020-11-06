package nodes.exampleNode.models;

import common.framework.ServiceParam;

/**
 * Created by Xinyu Zhu on 2020/11/2, 23:16
 * nodes.exampleNode.models in codingDimension
 */
public class ExampleServiceParam implements ServiceParam {
    private String message;

    public ExampleServiceParam(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
