package Entities;

import java.util.Date;

public class Post {
    private int postID;
    private Date date;
    private String content;
    private String threadTitle;
    private Integer isCommentOnPostID;
    private String folderName;

    public Post(Date date, String content, String threadTitle, Integer isCommentOnPostID) {
        this.date = date;
        this.content = content;
        this.threadTitle = threadTitle;
        this.isCommentOnPostID = isCommentOnPostID;
    }

    public Post(int postID, Date date, String content, String threadTitle, Integer isCommentOnPostID, String folderName) {
        this.postID = postID;
        this.content = content;
        this.threadTitle = threadTitle;
        this.isCommentOnPostID = isCommentOnPostID;
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
