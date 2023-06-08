package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ItemDao {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private int id = 0;

    @Override
    public Item addItem(Item item) {
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItemById(int id) {
        return items.get(id);
    }

    @Override
    public void deleteItem(int id) {
        items.remove(id);
    }
}
