package kata.academy.socialnetwork.web.scheduler;

import kata.academy.socialnetwork.model.entity.Notification;
import kata.academy.socialnetwork.service.abst.entity.NotificationService;
import kata.academy.socialnetwork.service.abst.entity.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final NotificationService notificationService;
    private final UserService userService;

    @Scheduled(cron = "0 30 0 * * ?")
    public void happyBirthday() {
        userService.findAll().stream()
                .filter(user -> {
                            LocalDate birthdate = user.getUserInfo().getBirthdate();
                            LocalDate now = LocalDate.now();
                            return birthdate.getMonth().equals(now.getMonth()) &&
                                    birthdate.getDayOfMonth() == now.getDayOfMonth();
                        }
                )
                .forEach(
                        user -> notificationService.save(
                                new Notification(
                                        null,
                                        user,
                                        String.format("С Днем Рождения %s %s!", user.getUserInfo().getFirstName(), user.getUserInfo().getLastName()),
                                        LocalDateTime.now(),
                                        false
                                )
                        )
                );
    }
}
