package kata.academy.socialnetwork.web.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.socialnetwork.model.api.Response;
import kata.academy.socialnetwork.model.entity.Comment;
import kata.academy.socialnetwork.model.entity.CommentLike;
import kata.academy.socialnetwork.model.entity.User;
import kata.academy.socialnetwork.service.abst.entity.CommentLikeService;
import kata.academy.socialnetwork.service.abst.entity.CommentService;
import kata.academy.socialnetwork.web.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.Optional;

@Tag(name = "UserCommentLikeRestController", description = "Контроллер для работы с лайками комментов")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/comment-likes")
public class UserCommentLikeRestController {

    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @Operation(summary = "Эндпоинт для получения количества лайков коммента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Количество лайков успешно получено"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping("/{commentId}/count")
    public Response<Integer> getCommentLikeCount(@PathVariable @Positive Long commentId, @RequestParam("positive") Boolean positive) {
        ApiValidationUtil.requireTrue(commentService.existsById(commentId), "Comment not found");
        return Response.ok(commentLikeService.countCommentLikesByIdAndPositive(commentId, positive));
    }

    @Operation(summary = "эндпоинт для добавления лайка или дизлайка комменту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лайк или дизлайк добавлен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PostMapping("/{commentId}")
    public Response<Void> addCommentLike(@PathVariable @Positive Long commentId, @RequestParam("positive") Boolean positive) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Comment> commentOptional = commentService.findById(commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), "Comment not found");
        ApiValidationUtil.requireFalse(commentLikeService.existsByCommentIdAndUserId(commentId, user.getId()), "Comment like already exist");
        commentLikeService.save(new CommentLike(null, positive, commentOptional.get(), user));
        return Response.ok();
    }

    @Operation(summary = "эндпоинт для удаления лайка или дизлайка комменту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лайк или дизлайк удален"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @DeleteMapping("/{commentId}")
    public Response<Void> deleteCommentLike(@PathVariable @Positive Long commentId) {
        ApiValidationUtil.requireTrue(commentService.existsById(commentId), "Comment not found");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CommentLike> commentLikeOptional = commentLikeService.findByCommentIdAndUserId(commentId, user.getId());
        ApiValidationUtil.requireTrue(commentLikeOptional.isPresent(), "Comment like does not exist");
        commentLikeService.delete(commentLikeOptional.get());
        return Response.ok();
    }

    @Operation(summary = "эндпоинт для обновления лайка или дизлайка комменту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лайк или дизлайк обновлен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PutMapping("/{commentId}")
    public Response<Void> updateCommentLike(@PathVariable @Positive Long commentId, @RequestParam("positive") Boolean positive) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CommentLike> commentLikeOptional = commentLikeService.findByCommentIdAndUserId(commentId, user.getId());
        ApiValidationUtil.requireTrue(commentLikeOptional.isPresent(), "Comment like does not exist");
        CommentLike commentLike = commentLikeOptional.get();
        commentLike.setPositive(positive);
        commentLikeService.update(commentLike);
        return Response.ok();
    }
}
