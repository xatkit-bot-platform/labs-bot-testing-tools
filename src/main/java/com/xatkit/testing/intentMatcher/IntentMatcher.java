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
import lombok.Getter;
import sun.nio.ch.ThreadPool;


import java.util.*;
import java.util.concurrent.*;

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

    public List<IntentMatch> getMatchingIntentsWithAllEntities() throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        EntityMapper mapper = new ConcreteEntityReferenceMapper();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(State actualState: botModel.getStates()){
            for (IntentDefinition actualIntent :actualState.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = actualState.getAllAccessedIntents();
                intents.remove(actualIntent);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));

                List<Set<IntentParameter>> entitiesMapping = new ArrayList<>();
                for (ContextParameter param : actualIntent.getParameters()) {
                    Set<IntentParameter> params = new HashSet<>();
                    for (String entity : ((ConcreteEntityReferenceMapper) mapper).getAllMappingsFor(param.getEntity().getReferredEntity())) {
                        IntentParameter p = new IntentParameter(param.getTextFragments(), entity);
                        params.add(p);
                    }
                    entitiesMapping.add(params);
                }
                Set<Set<Object>> cartesian = CustomAlgorithm.cartesianProduct(entitiesMapping.toArray(new Set[0]));

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

    public List<IntentMatch> getMatchingIntentsWithAllEntitiesInThread() throws IntentRecognitionProviderException {
        List<IntentMatch> matchingIntents = new ArrayList<>();
        EntityMapper mapper = new ConcreteEntityReferenceMapper();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(State actualState: botModel.getStates()){
            for (IntentDefinition actualIntent :actualState.getAllAccessedIntents()){
                Collection<IntentDefinition> intents = actualState.getAllAccessedIntents();
                intents.remove(actualIntent);
                testingContext.enableIntents(intents.toArray(new IntentDefinition[0]));

                List<Set<IntentParameter>> entitiesMapping = new ArrayList<>();
                for (ContextParameter param : actualIntent.getParameters()) {
                    Set<IntentParameter> params = new HashSet<>();
                    for (String entity : ((ConcreteEntityReferenceMapper) mapper).getAllMappingsFor(param.getEntity().getReferredEntity())) {
                        IntentParameter p = new IntentParameter(param.getTextFragments(), entity);
                        params.add(p);
                    }
                    entitiesMapping.add(params);
                }
                Set<Set<Object>> cartesian = CustomAlgorithm.cartesianProduct(entitiesMapping.toArray(new Set[0]));

                Collection<Future<?>> futures = new LinkedList<Future<?>>();
                for (String sentence : actualIntent.getTrainingSentences()){
                    for (Set<Object> params : cartesian) {

                        futures.add(executor.submit(() -> {
                            String newSentence = sentence;
                            for (Object p : params){
                                for (String fragment : ((IntentParameter) p).getFragments()){
                                    newSentence = newSentence.replace(fragment, ((IntentParameter)p).getEntity());
                                }
                            }
                            RecognizedIntent recognizedIntent = null;
                            try {
                                recognizedIntent = intentRecognitionProvider.getIntent(newSentence, testingContext);
                                if(!recognizedIntent.getDefinition().equals(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT)){
                                    float confidence = recognizedIntent.getRecognitionConfidence();
                                    matchingIntents.add(new IntentMatch(actualIntent, (IntentDefinition) recognizedIntent.getDefinition(), actualState, newSentence, confidence));

                                }
                            } catch (IntentRecognitionProviderException e) {
                                e.printStackTrace();
                            }
                        }));
                        //executor.execute();
                    }

                }
                for (Future<?> future:futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
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
