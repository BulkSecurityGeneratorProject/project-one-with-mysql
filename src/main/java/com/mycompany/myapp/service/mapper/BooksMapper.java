package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BooksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Books} and its DTO {@link BooksDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BooksMapper extends EntityMapper<BooksDTO, Books> {



    default Books fromId(Long id) {
        if (id == null) {
            return null;
        }
        Books books = new Books();
        books.setId(id);
        return books;
    }
}
