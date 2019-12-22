package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ellen_answers")
public class EllenAnswer extends Auditable {

    // is there will be id or only q_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

//    @Id
//    @Getter
//    @Setter
//    @NotBlank
//    private Long q_id;

    @Getter
    @Setter
    @NotBlank
    private String answer;

    @Getter
    @Setter
    //is voteCount can be Blank
    @NotBlank
    private int voteCount;
}
