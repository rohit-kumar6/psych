package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.psych.game.Constants;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="player_answers")
public class PlayerAnswer extends Auditable{
    @Getter
    @Setter
    @NotBlank
    @Column(length = Constants.MAX_ANSWER_LENGTH)
    private String answer;

    @Getter
    @Setter
    @JsonBackReference
    @NonNull
    private Round round;

    @Getter
    @Setter
    @NotNull
    private Player player;

    public PlayerAnswer() {}

    public PlayerAnswer(@NotBlank String answer, @NonNull Round round, Player player) {
        this.answer = answer;
        this.round = round;
        this.player = player;
    }
}
