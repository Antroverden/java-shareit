package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoWithName;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentMapper {

    UserService userService;
    ItemRepository itemRepository;

    public CommentDtoWithName toDto(Comment comment) {
        return new CommentDtoWithName(comment.getId(), comment.getText(), comment.getItem().getId(), comment.getAuthor().getName(), comment.getCreated());
    }

    public Comment toComment(CommentDto commentDto) {
        Item item = null;
        if (commentDto.getItemId() != null) {
            item = itemRepository.findById(commentDto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Айтема с айди " + commentDto.getItemId() + "не существует"));
        }
        User user = userService.getUserById(commentDto.getAuthorId());
        return new Comment(commentDto.getId(), commentDto.getText(), item, user, LocalDateTime.now());
    }
}
