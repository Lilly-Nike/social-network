package kata.academy.socialnetwork.web.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.socialnetwork.model.api.Response;
import kata.academy.socialnetwork.model.converter.NotificationMapper;
import kata.academy.socialnetwork.model.dto.response.notification.NotificationResponseDto;
import kata.academy.socialnetwork.model.entity.Notification;
import kata.academy.socialnetwork.model.entity.User;
import kata.academy.socialnetwork.service.abst.entity.NotificationService;
import kata.academy.socialnetwork.web.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


@Tag(name = "UserNotificationRestController", description = "Контроллер для работы с уведомлениями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/notifications")
public class UserNotificationRestController {

    private final NotificationService notificationService;

    @Operation(summary = "Эндпоинт для получения списка уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список успешно выгружен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping
    public Response<Page<NotificationResponseDto>> getNotificationPage(Pageable pageable, @RequestParam(value = "isViewed", required = false) Boolean isViewed) {
        Page<Notification> notifications;
        if (isViewed != null) {
            notifications = notificationService.findAll(isViewed, pageable);
        } else {
            notifications = notificationService.findAll(pageable);
        }
        return Response.ok(notifications.map(NotificationMapper::toDto));
    }

    @Operation(summary = "Эндпоинт для отметки уведомлений как просмотренных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомления успешно отмечены как просмотренные"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PostMapping
    public Response<Void> makeNotificationViewed(@NotNull @RequestBody Long[] notificationIds) {
        ApiValidationUtil.requireTrue(notificationIds.length > 0, "Тело запроса не содержит ни одного ID");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Arrays.stream(notificationIds)
                .map(
                        id -> {
                            Optional<Notification> optionalNotification = notificationService.findById(id);
                            return optionalNotification.orElse(null);
                        }
                )
                .filter(Objects::nonNull)
                .filter(notification -> notification.getRecipient().equals(user))
                .forEach(notification -> {
                            notification.setIsViewed(true);
                            notificationService.update(notification);
                        }
                );
        return Response.ok();
    }
}
