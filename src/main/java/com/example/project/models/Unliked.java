package com.example.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "unliked")
public class Unliked {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long film_id;
    private Long user_id;

}
