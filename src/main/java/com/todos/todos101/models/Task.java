package com.todos.todos101.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotNull(message = "name is mandatory")
    private String name;

    @NotNull(message = "desc is mandatory")
    @Column(name = "description")
    private String desc;

    @NotNull(message = "completed is mandatory")
    private Boolean completed;
}
