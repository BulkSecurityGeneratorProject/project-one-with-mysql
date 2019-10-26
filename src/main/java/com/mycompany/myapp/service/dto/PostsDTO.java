package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Posts} entity.
 */
public class PostsDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostsDTO postsDTO = (PostsDTO) o;
        if (postsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostsDTO{" +
            "id=" + getId() +
            "}";
    }
}
