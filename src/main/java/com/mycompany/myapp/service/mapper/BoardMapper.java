package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BoardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Board} and its DTO {@link BoardDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoardMapper extends EntityMapper<BoardDTO, Board> {



    default Board fromId(Long id) {
        if (id == null) {
            return null;
        }
        Board board = new Board();
        board.setId(id);
        return board;
    }
}
