package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoWithName;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @Mock
    UserService userService;
    @Mock
    ItemService itemService;

    @InjectMocks
    CommentMapper commentMapper;

    @Test
    void toDto() {
        User user = new User(1, "Name", "email@rambler.ru");
        User user1 = new User(2, "Name", "email@rambler.ru");
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item(1, "itemName", "ItemDescription", true, user, itemRequest);
        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment(1, "comment", item, user1, now);
        CommentDtoWithName actual = commentMapper.toDto(comment);
        CommentDtoWithName commentDtoWithName = new CommentDtoWithName(1, "comment", 1, "Name",
                now);

        assertThat(actual).isEqualTo(commentDtoWithName);
    }

    @Test
    void toComment() {
        User user = new User(1, "Name", "email@rambler.ru");
        User user1 = new User(2, "Name", "email@rambler.ru");
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item(1, "itemName", "ItemDescription", true, user, itemRequest);
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment(1, "comment", item, user1, now);
        CommentDto commentDto = new CommentDto(1, "comment", 1, 2,
                now);

        when(itemService.getItemById(1)).thenReturn(item);
        when(userService.getUserById(2)).thenReturn(user1);

        Comment actual = commentMapper.toComment(commentDto);

        actual.setCreated(now);
        assertThat(actual).isEqualTo(comment);
    }
}