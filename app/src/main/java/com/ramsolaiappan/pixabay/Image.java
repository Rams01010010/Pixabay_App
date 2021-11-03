package com.ramsolaiappan.pixabay;

public class Image {
    private String id;
    private String pageUrl;
    private String type;
    private String tags;
    private String imagePreviewUrl;
    private String imageUrl;
    private int views;
    private int downloads;
    private int likes;
    private int comments;
    private String userId;
    private String user;
    private String userImageUrl;
    private boolean liked = false;

    public Image(String id, String pageUrl, String type, String tags, String imagePreviewUrl, String imageUrl, int views, int downloads, int likes, int comments, String userId, String user, String userImageUrl,boolean liked) {
        this.id = id;
        this.pageUrl = pageUrl;
        this.type = type;
        this.tags = tags;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageUrl = imageUrl;
        this.views = views;
        this.downloads = downloads;
        this.likes = likes;
        this.comments = comments;
        this.userId = userId;
        this.user = user;
        this.userImageUrl = userImageUrl;
        this.liked = liked;
    }

    public String getId() {
        return id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getType() {
        return type;
    }

    public String getTags() {
        return tags;
    }

    public String getImagePreviewUrl() {
        return imagePreviewUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getViews() {
        return views;
    }

    public int getDownloads() {
        return downloads;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public String getUserId() {
        return userId;
    }

    public String getUser() {
        return user;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
