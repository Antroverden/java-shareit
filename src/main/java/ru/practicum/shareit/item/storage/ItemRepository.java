package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text,
                                                                                                      String text2);

    List<Item> findItemsByOwner_IdOrderById(int ownerId);

    List<Item> findItemsByItemRequest_Id(int requestId);
}
