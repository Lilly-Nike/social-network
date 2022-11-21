package kata.academy.socialnetwork.web.rest.user;

import kata.academy.socialnetwork.SpringSimpleContextTest;
import kata.academy.socialnetwork.model.dto.request.comment.CommentPersistRequestDto;
import kata.academy.socialnetwork.model.dto.request.comment.CommentUpdateRequestDto;
import kata.academy.socialnetwork.model.entity.Comment;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserCommentRestControllerIT extends SpringSimpleContextTest {
    @Value("${jwt.header}")
    private String header;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_PostNotFound/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_PostNotFound/AfterTest.sql")
    public void getCommentPage_PostNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user/comments/post/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, getToken("test@gmail.com", "pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", Is.is(false)))
                .andExpect(jsonPath("$.error", Is.is("Post not found")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_PostExist/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_PostExist/AfterTest.sql")
    public void getCommentPage_PostExist() throws Exception {
        mockMvc.perform(get("/api/v1/user/comments/post/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, getToken("test@gmail.com", "pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", Is.is(true)))
                .andExpect(jsonPath("$.data", IsNull.notNullValue()));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_CommentsNotFound/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_CommentsNotFound/AfterTest.sql")
    public void getCommentPage_CommentsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user/comments/post/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, getToken("test@gmail.com", "pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", Is.is(true)))
                .andExpect(jsonPath("$.data.content", empty()));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_CommentsExist/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentPage_CommentsExist/AfterTest.sql")
    public void getCommentPage_CommentsExist() throws Exception {
        mockMvc.perform(get("/api/v1/user/comments/post/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, getToken("test@gmail.com", "pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", Is.is(true)))
                .andExpect(jsonPath("$.data.content[0].text", Is.is("my test comment")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentById_SuccessfulTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentById_SuccessfulTest/AfterTest.sql")
    public void getCommentById_SuccessfulTest() throws Exception {
        String token = getToken("test", "test");
        mockMvc.perform(get("/api/v1/user/comments/101")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", IsNull.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(101)));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentById_CommentNotFoundTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/getCommentById_CommentNotFoundTest/AfterTest.sql")
    public void getCommentById_CommentNotFoundTest() throws Exception {
        String token = getToken("test", "test");
        mockMvc.perform(get("/api/v1/user/comments/102")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("Comment not found by id: 102")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/addComment_SuccessfulTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/addComment_SuccessfulTest/AfterTest.sql")
    public void addComment_SuccessfulTest() throws Exception {
        CommentPersistRequestDto dto = new CommentPersistRequestDto(
                101L,
                "my test comment 2"
        );

        String token = getToken("test", "test");
        mockMvc.perform(post("/api/v1/user/comments")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/addComment_IsEmptyTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/addComment_IsEmptyTest/AfterTest.sql")
    public void addComment_IsEmptyTest(String value) throws Exception {
        CommentPersistRequestDto dto = new CommentPersistRequestDto(
                101L,
                value
        );

        String token = getToken("test", "test");
        mockMvc.perform(post("/api/v1/user/comments")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.containsString("В теле запроса допущены ошибки в следующих полях:")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/updateComment_SuccessfulTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/updateComment_SuccessfulTest/AfterTest.sql")
    public void updateComment_SuccessfulTest() throws Exception {
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto(101L, "new my test comment");

        String token = getToken("test", "test");
        mockMvc.perform(put("/api/v1/user/comments")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())));

        Comment comment = entityManager.find(Comment.class, 101L);

        assertEquals(dto.id(), comment.getId());
        assertEquals(dto.text(), comment.getText());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/updateComment_IsEmptyTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/updateComment_IsEmptyTest/AfterTest.sql")
    public void updateComment_IsEmptyTest(String value) throws Exception {
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto(
                101L,
                value);

        String token = getToken("test", "test");
        mockMvc.perform(put("/api/v1/user/comments")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.containsString("В теле запроса допущены ошибки в следующих полях:")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/removeCommentById_SuccessfulTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/removeCommentById_SuccessfulTest/AfterTest.sql")
    public void removeCommentById_SuccessfulTest() throws Exception {
        String token = getToken("test", "test");
        mockMvc.perform(delete("/api/v1/user/comments/101")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", IsNull.nullValue()));
        assertNull(entityManager.find(Comment.class, 101L));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/removeCommentById_CommentNotFoundTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserCommentRestController/removeCommentById_CommentNotFoundTest/AfterTest.sql")
    public void removeCommentById_CommentNotFoundTest() throws Exception {
        String token = getToken("test", "test");
        mockMvc.perform(delete("/api/v1/user/comments/102")
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("Comment not found by id: 102")));
    }
}
