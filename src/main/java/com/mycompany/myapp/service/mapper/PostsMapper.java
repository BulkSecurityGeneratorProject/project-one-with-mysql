package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PostsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Posts} and its DTO {@link PostsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostsMapper extends EntityMapper<PostsDTO, Posts> {



    default Posts fromId(Long id) {
        if (id == null) {
            return null;
        }
        Posts posts = new Posts();
        posts.setId(id);
        return posts;
    }
}
