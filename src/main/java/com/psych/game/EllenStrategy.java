package com.psych.game;

import com.psych.game.model.EllenAnswer;
import com.psych.game.model.Round;

// interface of ellen because their can be lots of strategy
public interface EllenStrategy {
    EllenAnswer getAnswer(Round round);
}
