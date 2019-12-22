package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class GameMode extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    // todo
    // Just for removing error on build
}
