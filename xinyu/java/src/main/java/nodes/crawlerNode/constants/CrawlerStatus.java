package nodes.crawlerNode.constants;

/**
 * Created by Xinyu Zhu on 2020/11/7, 0:51
 * nodes.crawlerNode.constants in codingDimensionTemplate
 * Only used for those advanced crawler which has more than one status during its lifetime
 * Stateless crawler does not need this
 */
public enum CrawlerStatus {
    // Crawler can accept new job
    ACCEPTING_JOB,

    // Crawler is doing its job but can accept new job and can return Future object
    RUNNING_JOB_ASYNC,

    // Crawler is doing its job but can't accept new job/ or return Future object
    RUNNING_JOB_SYNC,

    // Crawler is finished, can return result but can't accept new job
    FINISHED,

    // Crawler is killed, can't accept or return any result
    KILLED;

}
