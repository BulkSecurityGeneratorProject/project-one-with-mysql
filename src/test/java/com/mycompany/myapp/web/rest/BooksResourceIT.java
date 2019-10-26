package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ProjectOneWithMysqlApp;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.BooksService;
import com.mycompany.myapp.service.dto.BooksDTO;
import com.mycompany.myapp.service.mapper.BooksMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BooksResource} REST controller.
 */
@SpringBootTest(classes = ProjectOneWithMysqlApp.class)
public class BooksResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATETIME = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private BooksMapper booksMapper;

    @Autowired
    private BooksService booksService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBooksMockMvc;

    private Books books;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BooksResource booksResource = new BooksResource(booksService);
        this.restBooksMockMvc = MockMvcBuilders.standaloneSetup(booksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Books createEntity(EntityManager em) {
        Books books = new Books()
            .title(DEFAULT_TITLE)
            .contents(DEFAULT_CONTENTS)
            .createtime(DEFAULT_CREATETIME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return books;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Books createUpdatedEntity(EntityManager em) {
        Books books = new Books()
            .title(UPDATED_TITLE)
            .contents(UPDATED_CONTENTS)
            .createtime(UPDATED_CREATETIME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return books;
    }

    @BeforeEach
    public void initTest() {
        books = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooks() throws Exception {
        int databaseSizeBeforeCreate = booksRepository.findAll().size();

        // Create the Books
        BooksDTO booksDTO = booksMapper.toDto(books);
        restBooksMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
            .andExpect(status().isCreated());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeCreate + 1);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBooks.getContents()).isEqualTo(DEFAULT_CONTENTS);
        assertThat(testBooks.getCreatetime()).isEqualTo(DEFAULT_CREATETIME);
        assertThat(testBooks.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBooks.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createBooksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = booksRepository.findAll().size();

        // Create the Books with an existing ID
        books.setId(1L);
        BooksDTO booksDTO = booksMapper.toDto(books);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList
        restBooksMockMvc.perform(get("/api/books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(books.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS.toString())))
            .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get the books
        restBooksMockMvc.perform(get("/api/books/{id}", books.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(books.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS.toString()))
            .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingBooks() throws Exception {
        // Get the books
        restBooksMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Update the books
        Books updatedBooks = booksRepository.findById(books.getId()).get();
        // Disconnect from session so that the updates on updatedBooks are not directly saved in db
        em.detach(updatedBooks);
        updatedBooks
            .title(UPDATED_TITLE)
            .contents(UPDATED_CONTENTS)
            .createtime(UPDATED_CREATETIME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        BooksDTO booksDTO = booksMapper.toDto(updatedBooks);

        restBooksMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
            .andExpect(status().isOk());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBooks.getContents()).isEqualTo(UPDATED_CONTENTS);
        assertThat(testBooks.getCreatetime()).isEqualTo(UPDATED_CREATETIME);
        assertThat(testBooks.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBooks.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Create the Books
        BooksDTO booksDTO = booksMapper.toDto(books);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeDelete = booksRepository.findAll().size();

        // Delete the books
        restBooksMockMvc.perform(delete("/api/books/{id}", books.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Books.class);
        Books books1 = new Books();
        books1.setId(1L);
        Books books2 = new Books();
        books2.setId(books1.getId());
        assertThat(books1).isEqualTo(books2);
        books2.setId(2L);
        assertThat(books1).isNotEqualTo(books2);
        books1.setId(null);
        assertThat(books1).isNotEqualTo(books2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksDTO.class);
        BooksDTO booksDTO1 = new BooksDTO();
        booksDTO1.setId(1L);
        BooksDTO booksDTO2 = new BooksDTO();
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
        booksDTO2.setId(booksDTO1.getId());
        assertThat(booksDTO1).isEqualTo(booksDTO2);
        booksDTO2.setId(2L);
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
        booksDTO1.setId(null);
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(booksMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(booksMapper.fromId(null)).isNull();
    }
}
