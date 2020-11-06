package nodes.hotinfoNode.models;

/**
 * Created by Xinyu Zhu on 6/30/2020, 5:36 AM
 */
public class VideoRecordVO {
    private final String title;
    private final String description;
    private final String palyCount;
    private final String commentCount;
    private final String likeCount;
    private final String author;
    private final String videoUrl;
    private final String imgUrl;
    private final Integer rank;
    private final Integer score;

    public VideoRecordVO(String title, String description, String palyCount, String commentCount, String likeCount, String author, String videoUrl, String imgUrl, Integer rank, Integer score) {
        this.title = title;
        this.description = description;
        this.palyCount = palyCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.author = author;
        this.videoUrl = videoUrl;
        this.imgUrl = imgUrl;
        this.rank = rank;
        this.score = score;
    }

    @Override
    public String toString() {
        return "VideoRecordVO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", palyCount='" + palyCount + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", likeCount='" + likeCount + '\'' +
                ", author='" + author + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public String getImgUrl() {
        return imgUrl;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public String getPalyCount() {
        return palyCount;
    }


    public String getCommentCount() {
        return commentCount;
    }


    public String getLikeCount() {
        return likeCount;
    }


    public Integer getRank() {
        return rank;
    }


    public Integer getScore() {
        return score;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * create Builder method
     **/
    public static Builder custom() {
        return new Builder();
    }


    public static class Builder {
        private String title;
        private String description;
        private String palyCount;
        private String commentCount;
        private String likeCount;
        private String author;
        private String imgUrl;
        private String videoUrl;
        private Integer rank;
        private Integer score;

        public Builder() {
            title = "";
            description = "";
            palyCount = "";
            commentCount = "";
            likeCount = "";
            author = "";
            videoUrl = "";
            imgUrl = "";
            rank = 0;
            score = 0;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPalyCount(String palyCount) {
            this.palyCount = palyCount;
            return this;
        }

        public Builder setCommentCount(String commentCount) {
            this.commentCount = commentCount;
            return this;
        }

        public Builder setLikeCount(String likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder setRank(Integer rank) {
            this.rank = rank;
            return this;
        }

        public Builder setScore(Integer score) {
            this.score = score;
            return this;
        }

        public VideoRecordVO build() {
            return new VideoRecordVO(title, description, palyCount, commentCount, likeCount, author, videoUrl, imgUrl, rank, score);
        }
    }
}
