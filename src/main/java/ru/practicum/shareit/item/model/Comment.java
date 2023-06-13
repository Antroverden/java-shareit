package ru.practicum.shareit.item.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Comment {
    @Id
    int id;
    String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    String author;
    @CreationTimestamp
    LocalDate created;
}
