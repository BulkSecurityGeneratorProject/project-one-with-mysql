package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Books.
 */
@Entity
@Table(name = "books")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "createtime")
    private LocalDate createtime;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Books title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public Books contents(String contents) {
        this.contents = contents;
        return this;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDate getCreatetime() {
        return createtime;
    }

    public Books createtime(LocalDate createtime) {
        this.createtime = createtime;
        return this;
    }

    public void setCreatetime(LocalDate createtime) {
        this.createtime = createtime;
    }

    public byte[] getImage() {
        return image;
    }

    public Books image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Books imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Books)) {
            return false;
        }
        return id != null && id.equals(((Books) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Books{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contents='" + getContents() + "'" +
            ", createtime='" + getCreatetime() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
