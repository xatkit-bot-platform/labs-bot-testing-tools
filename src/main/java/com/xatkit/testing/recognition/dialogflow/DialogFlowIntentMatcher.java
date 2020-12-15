package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.EntityMapper;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.core.recognition.dialogflow.DialogFlowStateContext;
import com.xatkit.core.recognition.dialogflow.mapper.DialogFlowEntityReferenceMapper;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.execution.State;
import com.xatkit.intent.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DialogFlowIntentMatcher {

    private ExecutionModel botModel;
    private DialogFlowTestingContext testingContext;
    private IntentRecognitionProvider intentRecognitionProvider;

    public DialogFlowIntentMatcher(XatkitBot xatkitBot) throws IntentRecognitionProviderException {
        this.botModel = xatkitBot.getExecutionService().getModel();
        this.intentRecognitionProvider = xatkitBot.getIntentRecognitionProvider();
        this.testingContext = new DialogFlowTestingContext((DialogFlowStateContext) intentRecognitionProvider.createContext("DialogFlowIntentMatcherContext"));
    }

    public List<IntentMatch> getMatchingIntents() throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        EntityMapper mapper = new DialogFlowEntityReferenceMapper();
        for(State s: botModel.getStates()){
            for (IntentDefinition i :s.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = s.getAllAccessedIntents();
                intents.remove(i);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));
                for (String sentence : i.getTrainingSentences()){
                    for (ContextParameter param : i.getParameters()) {
                        EntityDefinition entity = param.getEntity().getReferredEntity();
                        for (String fragment : param.getTextFragments()) {
                            String concreteEntity = mapper.getMappingFor(entity);
                            sentence = sentence.replaceAll(fragment, concreteEntity);
                        }
                    }
                    RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(sentence, testingContext);
                    if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                        float confidence = recognizedIntent.getRecognitionConfidence();
                        matchingIntents.add(new IntentMatch(i, (IntentDefinition) recognizedIntent.getDefinition(), confidence));
                    }
                }
            }
        }
        return matchingIntents;
    }
}
