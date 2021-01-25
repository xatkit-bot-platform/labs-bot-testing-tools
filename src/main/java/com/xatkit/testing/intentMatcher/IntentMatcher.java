package com.xatkit.testing.intentMatcher;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.EntityMapper;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.execution.State;
import com.xatkit.intent.ContextParameter;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.intent.RecognizedIntent;
import com.xatkit.stubs.TestingStateContext;
import com.xatkit.stubs.TestingStateContextFactory;
import com.xatkit.testing.intentMatcher.matches.IntentMatch;
import com.xatkit.testing.intentMatcher.matches.StatelessIntentMatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class IntentMatcher {

    private ExecutionModel botModel;
    private TestingStateContext testingContext;
    private IntentRecognitionProvider intentRecognitionProvider;
    private final static String TESTING_CONTEXT_NAME = "IntentMatcherContext";

    public IntentMatcher(XatkitBot xatkitBot) throws IntentRecognitionProviderException {
        this.botModel = xatkitBot.getExecutionService().getModel();
        this.intentRecognitionProvider = xatkitBot.getIntentRecognitionProvider();
        this.testingContext = TestingStateContextFactory.wrap(intentRecognitionProvider.createContext(TESTING_CONTEXT_NAME));
    }

    public IntentMatcher(ExecutionModel botModel, IntentRecognitionProvider intentRecognitionProvider) throws IntentRecognitionProviderException {
        this.botModel = botModel;
        this.intentRecognitionProvider = intentRecognitionProvider;
        this.testingContext = TestingStateContextFactory.wrap(intentRecognitionProvider.createContext(TESTING_CONTEXT_NAME));
    }

    public List<IntentMatch> getMatchingIntents() throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        EntityMapper mapper = new ConcreteEntityReferenceMapper();
        for(State actualState: botModel.getStates()){
            for (IntentDefinition actualIntent :actualState.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = actualState.getAllAccessedIntents();
                intents.remove(actualIntent);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));
                for (String sentence : actualIntent.getTrainingSentences()){
                    for (ContextParameter param : actualIntent.getParameters()) {
                        String concreteEntity = mapper.getMappingFor(param.getEntity().getReferredEntity());
                        for (String fragment : param.getTextFragments()) {
                            sentence = sentence.replaceAll(fragment, concreteEntity);
                        }
                    }
                    RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(sentence, testingContext);
                    if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                        float confidence = recognizedIntent.getRecognitionConfidence();
                        matchingIntents.add(new IntentMatch(actualIntent, (IntentDefinition) recognizedIntent.getDefinition(), actualState, sentence, confidence));
                    }
                }
            }
        }
        return matchingIntents;
    }

    public List<StatelessIntentMatch> getStatelessMatchingIntents() throws IntentRecognitionProviderException {
        List<StatelessIntentMatch> matchingIntents = new ArrayList<>();
        EntityMapper mapper = new ConcreteEntityReferenceMapper();
        HashSet<IntentDefinition> intentDefinitions = new HashSet<>();
        for(State s: botModel.getStates()) {
            intentDefinitions.addAll(s.getAllAccessedIntents());
        }
        for (IntentDefinition i : intentDefinitions){
            Collection<IntentDefinition> intents = (Collection<IntentDefinition>) intentDefinitions.clone();
            intents.remove(i);
            testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));
            for (String sentence : i.getTrainingSentences()){
                for (ContextParameter param : i.getParameters()) {
                    String concreteEntity = mapper.getMappingFor(param.getEntity().getReferredEntity());
                    for (String fragment : param.getTextFragments()) {
                        sentence = sentence.replaceAll(fragment, concreteEntity);
                    }
                }
                RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(sentence, testingContext);
                if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                    float confidence = recognizedIntent.getRecognitionConfidence();
                    matchingIntents.add(new StatelessIntentMatch(i, (IntentDefinition) recognizedIntent.getDefinition(), sentence, confidence));
                }
            }
        }
        return matchingIntents;
    }
}
