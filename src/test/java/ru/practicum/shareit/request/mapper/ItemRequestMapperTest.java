package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    // Test data
    private User requestor;
    private Item item;
    private ItemRequest itemRequest;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        requestor = new User(1, "Name", "email@rambler.ru");
        item = new Item(1, "ItemName", "ItemDescription", true, requestor, null);
        itemRequest = new ItemRequest(1, "Item request description", requestor, LocalDateTime.now());
        itemDto = new ItemDto(1, "ItemName", "ItemDescription", true, requestor.getId(), itemRequest.getId(),
                null, null, null);
    }

    @Test
    void testToDto() {
        // Mock behavior
        when(itemRepository.findItemsByItemRequest_Id(itemRequest.getId())).thenReturn(List.of(item));

        // Run method to test
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(itemRequest);

        // Verify results
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getRequestor().getId(), itemRequestDto.getRequestorId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void testToDtos() {
        // Mock behavior
        List<ItemRequest> itemRequestList = Collections.singletonList(itemRequest);
        when(itemRepository.findItemsByItemRequest_Id(itemRequest.getId())).thenReturn(Collections.singletonList(item));

        // Run method to test
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.toDtos(itemRequestList);

        // Verify results
        assertEquals(itemRequestList.size(), itemRequestDtos.size());
        assertEquals(itemRequestList.get(0).getId(), itemRequestDtos.get(0).getId());
    }

    @Test
    void testToItemRequest() {
        // Mock behavior
        ItemRequestDto itemRequestDto = new ItemRequestDto(itemRequest.getId(), requestor.getId(),
                itemRequest.getDescription(), itemRequest.getCreated(), Collections.singletonList(itemDto));
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);

        // Run method to test
        ItemRequest actualItemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        // Verify results
        assertEquals(itemRequestDto.getId(), actualItemRequest.getId());
        assertEquals(itemRequestDto.getRequestorId(), actualItemRequest.getRequestor().getId());
        assertEquals(itemRequestDto.getDescription(), actualItemRequest.getDescription());
    }
}
