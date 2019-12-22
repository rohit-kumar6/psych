package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Stats")
public class Stats extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private long gamePlayed;

    @Getter
    @Setter
    private Long numPsyched;

    @Getter
    @Setter
    private Long numPsychedBy;
}
