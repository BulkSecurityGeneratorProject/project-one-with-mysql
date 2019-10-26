package com.mycompany.myapp.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Books} entity.
 */
public class BooksDTO implements Serializable {

    private Long id;

    private String title;

    private String contents;

    private LocalDate createtime;

    @Lob
    private byte[] image;

    private String imageContentType;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BooksDTO booksDTO = (BooksDTO) o;
        if (booksDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), booksDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BooksDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contents='" + getContents() + "'" +
            ", createtime='" + getCreatetime() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
