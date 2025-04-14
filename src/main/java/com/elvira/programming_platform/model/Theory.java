package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theory")
@Data
public class Theory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String content;
}
