package mobitechs.cityriders.model;

public class VideoItems {

    public String videoId;
    public String publishedDate;
    public String title;
    public String description;
    public String videoImage;


    public VideoItems() {
    }

    public VideoItems(String videoId, String publishedDate, String title, String description, String videoImage) {

        this.videoId = videoId;
        this.publishedDate = publishedDate;
        this.title = title;
        this.description = description;
        this.videoImage = videoImage;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
