package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.BoardService;
import com.mycompany.myapp.service.dto.BoardDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Board}.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private static final String ENTITY_NAME = "board";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardService boardService;

    public BoardResource(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * {@code POST  /board} : Create a new board.
     *
     * @param boardDTO the boardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardDTO, or with status {@code 400 (Bad Request)} if the board has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board")
    public ResponseEntity<BoardDTO> createBoard(@RequestBody BoardDTO boardDTO) throws URISyntaxException {
        log.debug("REST request to save Board : {}", boardDTO);
        if (boardDTO.getId() != null) {
            throw new BadRequestAlertException("A new board cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardDTO result = boardService.save(boardDTO);
        return ResponseEntity.created(new URI("/api/board/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board} : Updates an existing board.
     *
     * @param boardDTO the boardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardDTO,
     * or with status {@code 400 (Bad Request)} if the boardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board")
    public ResponseEntity<BoardDTO> updateBoard(@RequestBody BoardDTO boardDTO) throws URISyntaxException {
        log.debug("REST request to update Board : {}", boardDTO);
        if (boardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoardDTO result = boardService.save(boardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, boardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /board} : get all the board.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of board in body.
     */
    @GetMapping("/board")
    public ResponseEntity<List<BoardDTO>> getAllBoard(Pageable pageable) {
        log.debug("REST request to get a page of Board");
        Page<BoardDTO> page = boardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /board/:id} : get the "id" board.
     *
     * @param id the id of the boardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long id) {
        log.debug("REST request to get Board : {}", id);
        Optional<BoardDTO> boardDTO = boardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardDTO);
    }

    /**
     * {@code DELETE  /board/:id} : delete the "id" board.
     *
     * @param id the id of the boardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        log.debug("REST request to delete Board : {}", id);
        boardService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

