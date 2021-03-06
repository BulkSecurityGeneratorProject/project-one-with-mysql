package com.mycompany.myapp.service.dto;
import com.mycompany.myapp.domain.Board;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Board} entity.
 */
public class BoardDTO implements Serializable {

    private Long id;

    private String title;

    private String contents;

    private LocalDate createtime;

    private String imagelink;

    /*@Lob
    private byte[] image;

    private String imageContentType;*/

    public BoardDTO() {
        // Empty constructor needed for Jackson.
    }

    public BoardDTO(Long id, String title, String contents, LocalDate createtime, String imagelink) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createtime = createtime;
        this.imagelink = imagelink;
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

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    /*
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
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoardDTO boardDTO = (BoardDTO) o;
        if (boardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contents='" + getContents() + "'" +
            ", createtime='" + getCreatetime() + "'" +
            ", imagelink='" + getImagelink() + "'" +
            //", image='" + getImage() + "'" +
            "}";
    }
}
