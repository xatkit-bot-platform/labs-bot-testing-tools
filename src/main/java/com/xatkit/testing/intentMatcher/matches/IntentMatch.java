package com.xatkit.testing.intentMatcher.matches;

import com.xatkit.execution.State;
import com.xatkit.intent.IntentDefinition;
import lombok.Getter;

public class IntentMatch{
    @Getter
    private IntentDefinition expectedIntent;
    @Getter
    private IntentDefinition actualIntent;
    @Getter
    private State fromState;
    @Getter
    private String matchingSentence;
    @Getter
    private float confidence;

    public IntentMatch(IntentDefinition expectedIntent, IntentDefinition actualIntent, State fromState, String matchingSentence, float confidence) {
        this.expectedIntent = expectedIntent;
        this.actualIntent = actualIntent;
        this.fromState = fromState;
        this.matchingSentence = matchingSentence;
        this.confidence = confidence;
    }
}
