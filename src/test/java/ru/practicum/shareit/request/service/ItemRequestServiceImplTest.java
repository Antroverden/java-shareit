package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    UserService userService;

    User requestor;
    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        requestor = new User(1, "John Doe", "john.doe@mail.com");
        itemRequest = new ItemRequest(1, "Request description", requestor, null);
    }

    @Test
    void testAddItemRequest() {
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.addItemRequest(itemRequest);

        assertEquals(itemRequest, result);
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).save(itemRequest);
    }

    @Test
    void testGetItemRequestById() {
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequest result = itemRequestService.getItemRequestById(itemRequest.getId(), requestor.getId());

        assertEquals(itemRequest, result);
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).findById(itemRequest.getId());
    }

    @Test
    void testGetItemRequestById_NotFoundException() {
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getItemRequestById(itemRequest.getId(), requestor.getId())
        );
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).findById(itemRequest.getId());
    }

    @Test
    void testGetItemRequestsForUser() {
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.findAllByRequestor_Id(requestor.getId()))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequest> results = itemRequestService.getItemRequestsForUser(requestor.getId());

        assertEquals(1, results.size());
        assertEquals(itemRequest, results.get(0));
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).findAllByRequestor_Id(requestor.getId());
    }

    @Test
    void testGetItemRequests() {
        Integer from = 0;
        Integer size = 10;

        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.findAllByRequestor_IdNot(eq(requestor.getId()), any()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));

        List<ItemRequest> results = itemRequestService.getItemRequests(requestor.getId(), from, size);

        assertEquals(1, results.size());
        assertEquals(itemRequest, results.get(0));
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).findAllByRequestor_IdNot(eq(requestor.getId()), any());
    }

    @Test
    void testGetItemRequests_NullParams() {
        when(userService.getUserById(requestor.getId())).thenReturn(requestor);
        when(itemRequestRepository.findAllByRequestor_Id(requestor.getId()))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequest> results = itemRequestService.getItemRequests(requestor.getId(), null, null);

        assertEquals(1, results.size());
        assertEquals(itemRequest, results.get(0));
        verify(userService, times(1)).getUserById(requestor.getId());
        verify(itemRequestRepository, times(1)).findAllByRequestor_Id(requestor.getId());
    }
}