package com.xatkit.testing.intentMatcher.matches;

import com.xatkit.intent.IntentDefinition;
import lombok.Getter;

public class StatelessIntentMatch {
    @Getter
    private IntentDefinition expectedIntent;
    @Getter
    private IntentDefinition actualIntent;
    @Getter
    private String matchingSentence;
    @Getter
    private float confidence;

    public StatelessIntentMatch(IntentDefinition expectedIntent, IntentDefinition actualIntent, String matchingSentence, float confidence) {
        this.expectedIntent = expectedIntent;
        this.actualIntent = actualIntent;
        this.matchingSentence = matchingSentence;
        this.confidence = confidence;
    }
}
