package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ProjectOneWithMysqlApp;
import com.mycompany.myapp.domain.Posts;
import com.mycompany.myapp.repository.PostsRepository;
import com.mycompany.myapp.service.PostsService;
import com.mycompany.myapp.service.dto.PostsDTO;
import com.mycompany.myapp.service.mapper.PostsMapper;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PostsResource} REST controller.
 */
@SpringBootTest(classes = ProjectOneWithMysqlApp.class)
public class PostsResourceIT {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsMapper postsMapper;

    @Autowired
    private PostsService postsService;

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

    private MockMvc restPostsMockMvc;

    private Posts posts;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostsResource postsResource = new PostsResource(postsService);
        this.restPostsMockMvc = MockMvcBuilders.standaloneSetup(postsResource)
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
    public static Posts createEntity(EntityManager em) {
        Posts posts = new Posts();
        return posts;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posts createUpdatedEntity(EntityManager em) {
        Posts posts = new Posts();
        return posts;
    }

    @BeforeEach
    public void initTest() {
        posts = createEntity(em);
    }

    @Test
    @Transactional
    public void createPosts() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isCreated());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate + 1);
        Posts testPosts = postsList.get(postsList.size() - 1);
    }

    @Test
    @Transactional
    public void createPostsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts with an existing ID
        posts.setId(1L);
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList
        restPostsMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", posts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(posts.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPosts() throws Exception {
        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Update the posts
        Posts updatedPosts = postsRepository.findById(posts.getId()).get();
        // Disconnect from session so that the updates on updatedPosts are not directly saved in db
        em.detach(updatedPosts);
        PostsDTO postsDTO = postsMapper.toDto(updatedPosts);

        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isOk());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);
        Posts testPosts = postsList.get(postsList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingPosts() throws Exception {
        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeDelete = postsRepository.findAll().size();

        // Delete the posts
        restPostsMockMvc.perform(delete("/api/posts/{id}", posts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posts.class);
        Posts posts1 = new Posts();
        posts1.setId(1L);
        Posts posts2 = new Posts();
        posts2.setId(posts1.getId());
        assertThat(posts1).isEqualTo(posts2);
        posts2.setId(2L);
        assertThat(posts1).isNotEqualTo(posts2);
        posts1.setId(null);
        assertThat(posts1).isNotEqualTo(posts2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostsDTO.class);
        PostsDTO postsDTO1 = new PostsDTO();
        postsDTO1.setId(1L);
        PostsDTO postsDTO2 = new PostsDTO();
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
        postsDTO2.setId(postsDTO1.getId());
        assertThat(postsDTO1).isEqualTo(postsDTO2);
        postsDTO2.setId(2L);
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
        postsDTO1.setId(null);
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(postsMapper.fromId(null)).isNull();
    }
}
