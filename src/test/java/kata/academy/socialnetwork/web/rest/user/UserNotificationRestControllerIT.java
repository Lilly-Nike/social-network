package kata.academy.socialnetwork.web.rest.user;


import kata.academy.socialnetwork.SpringSimpleContextTest;
import kata.academy.socialnetwork.model.entity.Notification;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserNotificationRestControllerIT extends SpringSimpleContextTest {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulTrueValueTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulTrueValueTest/AfterTest.sql")
    public void getNotificationPage_SuccessfulTrueValueTest() throws Exception {
        String token = getToken("user", "user");
        mockMvc.perform(get("/api/v1/user/notifications?isViewed=true")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageable.paged", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].text", Is.is("notification text of true")));


    }


    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulFalseValueTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulFalseValueTest/AfterTest.sql")
    @Test
    public void getNotificationPage_SuccessfulFalseValueTest() throws Exception {
        String token = getToken("user", "user");
        mockMvc.perform(get("/api/v1/user/notifications?isViewed=false")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageable.paged", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].text", Is.is("notification text of false")));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulEmptyValueTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/getNotificationPage_SuccessfulEmptyValueTest/AfterTest.sql")
    @Test
    public void getNotificationPage_SuccessfulEmptyValueTest() throws Exception {
        String token = getToken("user", "user");
        mockMvc.perform(get("/api/v1/user/notifications?page=0&size=1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].text", Is.is("notification text of true")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageable.paged", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages", Is.is(2)));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_PositiveTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_PositiveTest/AfterTest.sql")
    @Test
    public void makeNotificationViewed_PositiveTest() throws Exception {
        String token = getToken("user", "user");
        Long[] notificationsId = {101L};
        mockMvc.perform(post("/api/v1/user/notifications")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(notificationsId))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", IsNull.nullValue()));
        Notification notification = (Notification) entityManager.createNativeQuery("select * from notifications where id = 101", Notification.class).getSingleResult();
        Assertions.assertTrue(notification.getIsViewed());
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_NullNotificationsIdTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_PositiveTest/AfterTest.sql")
    @Test
    public void makeNotificationViewed_NullNotificationsIdTest() throws Exception {
        String token = getToken("user", "user");
        Long[] notificationsId = null;
        mockMvc.perform(post("/api/v1/user/notifications")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(notificationsId))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("В запросе не указано тело")));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_EmptyNotificationsIdTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_EmptyNotificationsIdTest/AfterTest.sql")
    @Test
    public void makeNotificationViewed_EmptyNotificationsIdTest() throws Exception {
        String token = getToken("user", "user");
        Long[] notificationsId = {};
        mockMvc.perform(post("/api/v1/user/notifications")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(notificationsId))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("Тело запроса не содержит ни одного ID")));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_NotificationNotFoundTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_NotificationNotFoundTest/AfterTest.sql")
    @Test
    public void makeNotificationViewed_NotificationNotFoundTest() throws Exception {
        String token = getToken("user", "user");
        Long[] notificationsId = {102L};
        mockMvc.perform(post("/api/v1/user/notifications")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(notificationsId))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", IsNull.nullValue()));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_OtherUserTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/user/UserNotificationRestController/makeNotificationViewed_OtherUserTest/AfterTest.sql")
    @Test
    public void makeNotificationViewed_OtherUserTest() throws Exception {
        String token = getToken("user", "user");
        Long[] notificationsId = {101L};
        mockMvc.perform(post("/api/v1/user/notifications")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(notificationsId))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", IsNull.nullValue()));
        Notification notification = (Notification) entityManager.createNativeQuery("select * from notifications where id = 101", Notification.class).getSingleResult();
        Assertions.assertFalse(notification.getIsViewed());
    }
}
