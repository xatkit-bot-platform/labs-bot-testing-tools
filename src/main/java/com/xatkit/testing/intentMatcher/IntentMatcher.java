package com.xatkit.testing.intentMatcher;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.EntityMapper;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.execution.State;
import com.xatkit.intent.*;
import com.xatkit.stubs.TestingStateContext;
import com.xatkit.stubs.TestingStateContextFactory;
import com.xatkit.testing.intentMatcher.matches.IntentMatch;
import com.xatkit.testing.intentMatcher.matches.StatelessIntentMatch;

import java.util.*;

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

    private Set<Set<Object>> getCartesian(List<Set<Object>> entitiesMapping){
        Set<Set<Object>> cartesian;
        if (entitiesMapping.size() > 1)
            cartesian = CustomAlgorithm.cartesianProduct(entitiesMapping.toArray(new Set[0]));
        else if ((entitiesMapping.size() == 1))
            cartesian = new HashSet<>(entitiesMapping);
        else {
            Set<Object> params = new HashSet<>();
            params.add(new IntentParameter(Collections.singletonList(""), ""));
            entitiesMapping.add(params);
            cartesian = new HashSet<>(entitiesMapping);
        }
        return cartesian;
    }

    public List<IntentMatch> getMatchingIntentsWithAllEntities() throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        ConcreteEntityReferenceMapper mapper = new ConcreteEntityReferenceMapper();

        for(State actualState: botModel.getStates()){
            for (IntentDefinition actualIntent :actualState.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = actualState.getAllAccessedIntents();
                intents.remove(actualIntent);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));

                List<Set<Object>> entitiesMapping = new ArrayList<>();
                for (ContextParameter param : actualIntent.getParameters()) {
                    Set<Object> params = new HashSet<>();
                    for (String entity : mapper.getAllMappingsFor(param.getEntity().getReferredEntity())) {
                        IntentParameter p = new IntentParameter(param.getTextFragments(), entity);
                        params.add(p);
                    }
                    entitiesMapping.add(params);
                }
                Set<Set<Object>> cartesian = getCartesian(entitiesMapping);
                long count = 1;
                long totalElements = cartesian.size() * actualIntent.getTrainingSentences().size();
                for (String sentence : actualIntent.getTrainingSentences()){
                    for (Set<Object> params : cartesian) {
                        String newSentence = sentence;
                        for (Object p : params){
                            for (String fragment : ((IntentParameter) p).getFragments()){
                                newSentence = newSentence.replace(fragment, ((IntentParameter)p).getEntity());
                            }
                        }
                        RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(newSentence, testingContext);
                        System.out.println("Processed " + count + " out of " + totalElements);
                        count++;
                        if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                            float confidence = recognizedIntent.getRecognitionConfidence();
                            matchingIntents.add(new IntentMatch(actualIntent, (IntentDefinition) recognizedIntent.getDefinition(), actualState, newSentence, confidence));

                        }
                    }

                }
            }
        }
        return matchingIntents;
    }

    public List<IntentMatch> getMatchingIntentsWithSeveralEntities(float percentage) throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        ConcreteEntityReferenceMapper mapper = new ConcreteEntityReferenceMapper();

        for(State actualState: botModel.getStates()){
            for (IntentDefinition actualIntent :actualState.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = actualState.getAllAccessedIntents();
                intents.remove(actualIntent);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));

                List<Set<Object>> entitiesMapping = new ArrayList<>();
                for (ContextParameter param : actualIntent.getParameters()) {
                    Set<Object> params = new HashSet<>();
                    for (String entity : mapper.getSeveralMappingsFor(param.getEntity().getReferredEntity(), percentage)) {
                        IntentParameter p = new IntentParameter(param.getTextFragments(), entity);
                        params.add(p);
                    }
                    entitiesMapping.add(params);
                }
                Set<Set<Object>> cartesian = getCartesian(entitiesMapping);
                for (String sentence : actualIntent.getTrainingSentences()){
                    for (Set<Object> params : cartesian) {
                        String newSentence = sentence;
                        for (Object p : params){
                            for (String fragment : ((IntentParameter) p).getFragments()){
                                newSentence = newSentence.replace(fragment, ((IntentParameter)p).getEntity());
                            }
                        }
                        RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(newSentence, testingContext);
                        if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                            float confidence = recognizedIntent.getRecognitionConfidence();
                            matchingIntents.add(new IntentMatch(actualIntent, (IntentDefinition) recognizedIntent.getDefinition(), actualState, newSentence, confidence));

                        }
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
