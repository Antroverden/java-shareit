package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testToDtoWithLastAndNextBooking() {
        User owner = new User(1, "John Doe", "john@example.com");
        Item item = new Item(1, "Item", "Description", true, owner, null);

        when(commentRepository.findAllByItem_Id(item.getId())).thenReturn(Collections.emptyList());
        when(bookingRepository.findFirstByStartBeforeAndItem_IdAndStatusInOrderByEndDesc(any(), any(), any())).thenReturn(null);
        when(bookingRepository.findFirstByStartAfterAndItem_IdAndStatusInOrderByStartAsc(any(), any(), any())).thenReturn(null);

        ItemDto itemDto = itemMapper.toDtoWithLastAndNextBooking(item);

        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getNextBooking());
        assertEquals(0, itemDto.getComments().size());
    }

    @Test
    void testToItem() {
        User owner = new User(1, "John Doe", "john@example.com");
        ItemDto itemDto = new ItemDto(1, "Item", "Description", true, owner.getId(), null, null, null, Collections.emptyList());

        when(userService.getUserById(itemDto.getOwnerId())).thenReturn(owner);

        Item item = itemMapper.toItem(itemDto);

        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getOwnerId(), item.getOwner().getId());
        assertNull(item.getItemRequest());
    }

    @Test
    public void testToDto() {
        User user = new User(1, "John Doe", "john@example.com");
        Item item = new Item(1, "Item 1", "Item 1 description", true, user, null);
        Comment comment = new Comment(1, "Comment 1", item, user,
                LocalDateTime.now());
        when(commentRepository.findAllByItem_Id(1)).thenReturn(List.of(comment));

        ItemDto itemDto = itemMapper.toDto(item);

        assertEquals(1, itemDto.getId());
        assertEquals("Item 1", itemDto.getName());
        assertEquals("Item 1 description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
    }

    @Test
    public void testToDto_listOfItems() {
        User user = new User(1, "John Doe", "john@example.com");
        Item item = new Item(1, "Item 1", "Item 1 description", true, user, null);
        List<Item> items = Collections.singletonList(item);

        when(commentRepository.findAllByItem_Id(1)).thenReturn(Collections.emptyList());

        List<ItemDto> itemDtos = itemMapper.toDto(items);

        assertEquals(1, itemDtos.get(0).getId());
        assertEquals("Item 1", itemDtos.get(0).getName());
        assertEquals("Item 1 description", itemDtos.get(0).getDescription());
        assertEquals(true, itemDtos.get(0).getAvailable());
        assertEquals(0, itemDtos.get(0).getComments().size());
    }
}
