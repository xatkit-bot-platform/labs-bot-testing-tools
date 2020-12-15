package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.intent.IntentDefinition;
import lombok.Getter;

public class IntentMatch{
    @Getter
    private IntentDefinition expectedIntent;
    @Getter
    private IntentDefinition actualIntent;
    @Getter
    private float confidence;

    IntentMatch(IntentDefinition expectedIntent, IntentDefinition actualIntent, float confidence) {
        this.expectedIntent = expectedIntent;
        this.actualIntent = actualIntent;
        this.confidence = confidence;
    }
}
