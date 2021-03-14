package Entities;

import java.time.LocalDate;
import java.util.Date;

public class Post {
    private LocalDate date;
    private String content;
    private String threadTitle;
    private int isCommentOnPostID;

    public Post(LocalDate date, String content, String threadTitle, Integer isCommentOnPostID) {
        this.date = date;
        this.content = content;
        this.threadTitle = threadTitle;
        this.isCommentOnPostID = isCommentOnPostID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public Integer getIsCommentOnPostID() {
        return isCommentOnPostID;
    }

    public void setIsCommentOnPostID(Integer isCommentOnPostID) {
        this.isCommentOnPostID = isCommentOnPostID;
    }
}
