package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.BooksService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.BooksDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Books}.
 */
@RestController
@RequestMapping("/api")
public class BooksResource {

    private final Logger log = LoggerFactory.getLogger(BooksResource.class);

    private static final String ENTITY_NAME = "books";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksService booksService;

    public BooksResource(BooksService booksService) {
        this.booksService = booksService;
    }

    /**
     * {@code POST  /books} : Create a new books.
     *
     * @param booksDTO the booksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksDTO, or with status {@code 400 (Bad Request)} if the books has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books")
    public ResponseEntity<BooksDTO> createBooks(@RequestBody BooksDTO booksDTO) throws URISyntaxException {
        log.debug("REST request to save Books : {}", booksDTO);
        if (booksDTO.getId() != null) {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksDTO result = booksService.save(booksDTO);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books} : Updates an existing books.
     *
     * @param booksDTO the booksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksDTO,
     * or with status {@code 400 (Bad Request)} if the booksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books")
    public ResponseEntity<BooksDTO> updateBooks(@RequestBody BooksDTO booksDTO) throws URISyntaxException {
        log.debug("REST request to update Books : {}", booksDTO);
        if (booksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BooksDTO result = booksService.save(booksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /books} : get all the books.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @GetMapping("/books")
    public ResponseEntity<List<BooksDTO>> getAllBooks(Pageable pageable) {
        log.debug("REST request to get a page of Books");
        Page<BooksDTO> page = booksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books/:id} : get the "id" books.
     *
     * @param id the id of the booksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BooksDTO> getBooks(@PathVariable Long id) {
        log.debug("REST request to get Books : {}", id);
        Optional<BooksDTO> booksDTO = booksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksDTO);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" books.
     *
     * @param id the id of the booksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBooks(@PathVariable Long id) {
        log.debug("REST request to delete Books : {}", id);
        booksService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
