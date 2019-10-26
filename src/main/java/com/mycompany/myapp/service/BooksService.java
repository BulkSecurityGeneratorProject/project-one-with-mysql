package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.dto.BooksDTO;
import com.mycompany.myapp.service.mapper.BooksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Books}.
 */
@Service
@Transactional
public class BooksService {

    private final Logger log = LoggerFactory.getLogger(BooksService.class);

    private final BooksRepository booksRepository;

    private final BooksMapper booksMapper;

    public BooksService(BooksRepository booksRepository, BooksMapper booksMapper) {
        this.booksRepository = booksRepository;
        this.booksMapper = booksMapper;
    }

    /**
     * Save a books.
     *
     * @param booksDTO the entity to save.
     * @return the persisted entity.
     */
    public BooksDTO save(BooksDTO booksDTO) {
        log.debug("Request to save Books : {}", booksDTO);
        Books books = booksMapper.toEntity(booksDTO);
        books = booksRepository.save(books);
        return booksMapper.toDto(books);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return booksRepository.findAll(pageable)
            .map(booksMapper::toDto);
    }


    /**
     * Get one books by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksDTO> findOne(Long id) {
        log.debug("Request to get Books : {}", id);
        return booksRepository.findById(id)
            .map(booksMapper::toDto);
    }

    /**
     * Delete the books by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Books : {}", id);
        booksRepository.deleteById(id);
    }
}
