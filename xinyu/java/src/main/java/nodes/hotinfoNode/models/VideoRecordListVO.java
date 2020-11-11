package nodes.hotinfoNode.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:14 PM
 */
public class VideoRecordListVO {
    private List<VideoRecordVO> videoRecords;

    public VideoRecordListVO(List<VideoRecordVO> videoRecords) {

        this.videoRecords = new ArrayList<>(videoRecords);
    }

    public List<VideoRecordVO> getVideoRecords() {
        return videoRecords;
    }

    @Override
    public String toString() {
        return "VideoRecordListVO{" +
                "videoRecords=" + videoRecords +
                '}';
    }
}
