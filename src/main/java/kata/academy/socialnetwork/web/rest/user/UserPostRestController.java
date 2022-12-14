package kata.academy.socialnetwork.web.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.socialnetwork.model.api.Response;
import kata.academy.socialnetwork.model.converter.PostMapper;
import kata.academy.socialnetwork.model.dto.request.post.PostPersistRequestDto;
import kata.academy.socialnetwork.model.dto.request.post.PostUpdateRequestDto;
import kata.academy.socialnetwork.model.dto.response.post.PostResponseDto;
import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;
import kata.academy.socialnetwork.service.abst.dto.PostResponseDtoService;
import kata.academy.socialnetwork.service.abst.entity.PostService;
import kata.academy.socialnetwork.web.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Tag(name = "UserPostRestController", description = "Контроллер для работы с постами")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/posts")
public class UserPostRestController {

    private final PostService postService;
    private final PostResponseDtoService postResponseDtoService;

    @Operation(summary = "Эндпоинт для получения списка постов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список успешно выгружен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping
    public Response<Page<PostResponseDto>> getPostPage(Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);
        return Response.ok(posts.map(PostMapper::toDto));
    }

    @Operation(summary = "Эндпоинт для добавления нового поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост успешно создан"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PostMapping
    public Response<PostResponseDto> addPost(@RequestBody @Valid PostPersistRequestDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Response.ok(PostMapper.toDto(postService.save(PostMapper.toEntity(user, dto))));
    }

    @Operation(summary = "Эндпоинт для получения поста по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост успешно получен"),
            @ApiResponse(responseCode = "400", description = "Пост не найден или клиент допустил ошибки в запросе")
    })
    @GetMapping("/{postId}")
    public Response<PostResponseDto> getPostById(@PathVariable Long postId) {
        Optional<PostResponseDto> postResponseDtoOptional = postResponseDtoService.findById(postId);
        ApiValidationUtil.requireTrue(postResponseDtoOptional.isPresent(), "Post not found");
        return Response.ok(postResponseDtoOptional.get());
    }

    @Operation(summary = "Эндпоинт для удаления своего поста по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост успешно удален"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @DeleteMapping("/{postId}")
    public Response<Void> removePostById(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);
        ApiValidationUtil.requireTrue(postOptional.isPresent(), "Post not found");
        Post post = postOptional.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiValidationUtil.requireTrue(post.getUser().equals(user), "You are not the author of this post");
        postService.delete(post);
        return Response.ok();
    }

    @Operation(summary = "Эндпоинт для редактирования своего поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PutMapping
    public Response<PostResponseDto> updatePost(@RequestBody @Valid PostUpdateRequestDto dto) {
        Optional<Post> postOptional = postService.findById(dto.id());
        ApiValidationUtil.requireTrue(postOptional.isPresent(), "Post not found");
        Post post = postOptional.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiValidationUtil.requireTrue(post.getUser().equals(user), "You are not the author of this post");
        return Response.ok(PostMapper.toDto(postService.update(PostMapper.postUpdate(post, dto))));
    }

    @Operation(summary = "Эндпоинт для получения постов текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @GetMapping("/user")
    public Response<Page<PostResponseDto>> getPostByUser(Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Response.ok(postService.getPostPageByUser(user, pageable));
    }

    @Operation(summary = "Эндпоинт для получения постов по тегам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "400", description = "Клиент допустил ошибки в запросе")
    })
    @PostMapping("/tags")
    public Response<List<PostResponseDto>> getPostByTags(@RequestBody @Valid @NotEmpty List<String> tags) {
        return Response.ok(postService.getPostsByTags(tags));
    }
}
