package common.framework;

/**
 * Created by Xinyu Zhu on 2020/11/2, 22:43
 * common.framework in codingDimension
 *
 * This interface can be implemented optionally, only those service that need to be managed by framework in a unified
 * way need to implement this service, in the future, this interface will specify the detailed managed method
 */
public interface NodeService {

    default void runService(ServiceParam serviceParam) {
        // implementing this method is optional
    }

}
