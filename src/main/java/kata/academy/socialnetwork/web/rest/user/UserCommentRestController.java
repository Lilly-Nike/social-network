package kata.academy.socialnetwork.web.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.socialnetwork.model.api.Response;
import kata.academy.socialnetwork.model.converter.CommentMapper;
import kata.academy.socialnetwork.model.dto.request.comment.CommentPersistRequestDto;
import kata.academy.socialnetwork.model.dto.request.comment.CommentUpdateRequestDto;
import kata.academy.socialnetwork.model.dto.response.comment.CommentResponseDto;
import kata.academy.socialnetwork.model.entity.Comment;
import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;
import kata.academy.socialnetwork.service.abst.entity.CommentService;
import kata.academy.socialnetwork.service.abst.entity.PostService;
import kata.academy.socialnetwork.web.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Tag(name = "UserCommentRestController", description = "Контроллер для работы c комментариями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/comments")
public class UserCommentRestController {

    private final CommentService commentService;
    private final PostService postService;

    @Operation(summary = "Эндпоинт для получения коментариев по postId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Коментарии поста успешно получены"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping("/post/{postId}")
    public Response<Page<CommentResponseDto>> getCommentPage(@PathVariable @Positive Long postId, Pageable pageable) {
        ApiValidationUtil.requireTrue(postService.existsById(postId), "Post not found");
        return Response.ok(commentService.getCommentPageByPostId(postId, pageable));
    }

    @Operation(summary = "Эндпоинт для получения коментария по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно получен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping("/{commentId}")
    public Response<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        Optional<Comment> commentOptional = commentService.findById(commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), "Comment not found by id: " + commentId);
        return Response.ok(CommentMapper.toDto(commentOptional.get()));
    }

    @Operation(summary = "Эндпоинт для добавления нового комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно создан"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PostMapping
    public Response<CommentResponseDto> addComment(@RequestBody @Valid CommentPersistRequestDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Post> postOptional = postService.findById(dto.postId());
        ApiValidationUtil.requireTrue(postOptional.isPresent(), "Post not found by id: " + dto.postId());
        return Response.ok(CommentMapper.toDto(
                commentService.save(CommentMapper.toEntity(user, postOptional.get(), dto))
        ));
    }

    @Operation(summary = "Эндпоинт для редактирования своего комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PutMapping
    public Response<CommentResponseDto> updateComment(@RequestBody @Valid CommentUpdateRequestDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Comment> commentOptional = commentService.findById(dto.id());
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), "Comment not found by id: " + dto.id());
        Comment comment = commentOptional.get();
        ApiValidationUtil.requireTrue(comment.getUser().equals(user), "You are not the author of this comment");
        return Response.ok(CommentMapper.toDto(commentService.update(CommentMapper.commentUpdate(comment, dto))));
    }

    @Operation(summary = "Эндпоинт для удаления своего комментария по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @DeleteMapping("/{commentId}")
    public Response<Void> removeCommentById(@PathVariable Long commentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Comment> commentOptional = commentService.findById(commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), "Comment not found by id: " + commentId);
        Comment comment = commentOptional.get();
        ApiValidationUtil.requireTrue(comment.getUser().equals(user), "You are not the author of this comment");
        commentService.delete(comment);
        return Response.ok();
    }
}
