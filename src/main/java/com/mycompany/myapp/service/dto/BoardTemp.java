package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public class BoardTemp {
    private Long id;

    private String title;

    private String contents;

    private LocalDate createtime;

    private byte[] image;

    private String imageContentType;

    public BoardTemp() {

    }

    public BoardTemp(Long id, String title, String contents, LocalDate createtime, byte[] image, String imageContentType) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createtime = createtime;
        this.image = image;
        this.imageContentType = imageContentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDate getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDate createtime) {
        this.createtime = createtime;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public String toString() {
        return "BoardTemp{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contents='" + getContents() + "'" +
            ", createtime='" + getCreatetime() + "'" +
            ", image='" + getImage() + "'" +
            ", imagetype='" + getImageContentType() + "'" +
            "}";
    }

}
