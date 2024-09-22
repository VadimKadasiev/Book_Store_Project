package book.store.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.dto.BookDto;
import book.store.dto.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-books-to-table.sql"));
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/remove-all-data-from-tables.sql"));
        }
    }

    @Test
    @DisplayName("Creates new book")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/books/add-categories-to-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_validRequestDto_OK() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Title");
        createBookRequestDto.setAuthor("Author");
        createBookRequestDto.setPrice(BigDecimal.valueOf(1.20));
        createBookRequestDto.setIsbn("ISBN");
        createBookRequestDto.setCategoryIds(Set.of(1L));
        createBookRequestDto.setDescription("Description");
        createBookRequestDto.setCoverImage("Cover Image");

        BookDto expected = new BookDto();
        expected.setTitle(createBookRequestDto.getTitle());
        expected.setAuthor(createBookRequestDto.getAuthor());
        expected.setPrice(createBookRequestDto.getPrice());
        expected.setIsbn(createBookRequestDto.getIsbn());
        expected.setDescription(createBookRequestDto.getDescription());
        expected.setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @DisplayName("Returns all books from DB")
    @WithMockUser(username = "user", roles = "USER")
    void findAll_GivenBooksCatalog_ReturnsAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("Title 1").setAuthor("Author 1")
                .setIsbn("A01").setPrice(BigDecimal.valueOf(1.11)).setCategoryIds(List.of()));
        expected.add(new BookDto().setId(2L).setTitle("Title 2").setAuthor("Author 2")
                .setIsbn("A02").setPrice(BigDecimal.valueOf(2.22)).setCategoryIds(List.of()));
        expected.add(new BookDto().setId(3L).setTitle("Title 3").setAuthor("Author 3")
                .setIsbn("A03").setPrice(BigDecimal.valueOf(3.33)).setCategoryIds(List.of()));
        expected.add(new BookDto().setId(4L).setTitle("Title 4").setAuthor("Author 4")
                .setIsbn("A04").setPrice(BigDecimal.valueOf(4.44)).setCategoryIds(List.of()));

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto[].class);
        Assertions.assertEquals(4, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Returns book with existing Id")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_WithExistingId_ReturnsBook() throws Exception {
        long bookId = 1L;
        BookDto expected = new BookDto().setId(1L).setTitle("Title 1").setAuthor("Author 1")
                .setIsbn("A01").setPrice(BigDecimal.valueOf(1.11)).setCategoryIds(List.of());

        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throws exception after requesting a book with nonExisting Id")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_WithNonExistingId_ThrowsException() throws Exception {
        long bookId = 100L;
        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String exceptionMessage = Objects.requireNonNull(result
                .getResolvedException()).getMessage();
        Assertions.assertEquals(exceptionMessage, "Can't find book by ID " + bookId);
    }
}
