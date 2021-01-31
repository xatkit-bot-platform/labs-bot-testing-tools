package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.testing.intentMatcher.IntentMatcher;
import com.xatkit.testing.intentMatcher.matches.IntentMatch;
import com.xatkit.testing.intentMatcher.matches.StatelessIntentMatch;
import com.xatkit.testing.recognition.dialogflow.model.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class MatchIntentExample {

    private static ExecutionModel botModel = new ChatBotCorpusBotModel();
    private static Thread botThread;
    private static IntentRecognitionProvider intentRecognitionProvider;

    /**
     * Starts the bot used to run the test cases.
     * <p>
     * Note that the bot is started once and is not reset between test cases. This shouldn't be an issue if the
     * test cases do not alter the bot and/or bot model. The bot can be accessed and queried through its
     * <a href="http://localhost:5000">web interface</a>.
     * <p>All
     * This method ensures that {@link #intentRecognitionProvider} is set when it returns.
     * <p>
     * <b>Note</b>: make sure you properly configured {@code /src/test/resources/greetings-bot.properties} with valid
     * DialogFlow credentials before you run this class.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws InterruptedException, ConfigurationException {
        Configurations configurations = new Configurations();
        Configuration botConfiguration = configurations.properties(MatchIntentExample.class.getClassLoader().getResource("greetings-bot.properties"));
        XatkitBot xatkitBot = new XatkitBot(botModel, botConfiguration);
        botThread = new Thread(xatkitBot);
        botThread.start();
        while (isNull(intentRecognitionProvider)) {
            Thread.sleep(1000);
            if (nonNull(xatkitBot.getXatkitServer()) && xatkitBot.getXatkitServer().isStarted()) {
                intentRecognitionProvider = xatkitBot.getIntentRecognitionProvider();
            }
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        botThread.interrupt();
    }

    @Test
    public void testMatchingIntents() throws IntentRecognitionProviderException {
        IntentMatcher intentMatcher = new IntentMatcher(botModel, intentRecognitionProvider);
        List<IntentMatch> matchingIntents = intentMatcher.getMatchingIntents();
        if(matchingIntents.size() == 0){
            System.out.println("NO matching intents");
        }
        else {
            for (IntentMatch im : matchingIntents) {
                System.out.println("Intent \"" + im.getExpectedIntent().getName()
                        + "\" was confused with intent \"" + im.getActualIntent().getName()
                        + "\" from state \"" + im.getFromState().getName()
                        + "\" with the sentence \"" + im.getMatchingSentence()
                        + "\" and a confidence of " + im.getConfidence());
            }
        }
    }

    @Test
    public void testMatchingIntentsWithAllEntities() throws IntentRecognitionProviderException {
        IntentMatcher intentMatcher = new IntentMatcher(botModel, intentRecognitionProvider);
        //List<IntentMatch> matchingIntents = intentMatcher.getMatchingIntentsWithAllEntities();
        List<IntentMatch> matchingIntents = intentMatcher.getMatchingIntentsWithAllEntitiesInThread();
        if(matchingIntents.size() == 0){
            System.out.println("NO matching intents");
        }
        else {
            for (IntentMatch im : matchingIntents) {
                System.out.println("Intent \"" + im.getExpectedIntent().getName()
                        + "\" was confused with intent \"" + im.getActualIntent().getName()
                        + "\" from state \"" + im.getFromState().getName()
                        + "\" with the sentence \"" + im.getMatchingSentence()
                        + "\" and a confidence of " + im.getConfidence());
            }
        }
    }

    @Test
    public void testStatelessMatchingIntents() throws IntentRecognitionProviderException {
        IntentMatcher intentMatcher = new IntentMatcher(botModel, intentRecognitionProvider);
        List<StatelessIntentMatch> matchingIntents = intentMatcher.getStatelessMatchingIntents();
        if(matchingIntents.size() == 0){
            System.out.println("NO matching intents");
        }
        else {
            for (StatelessIntentMatch im : matchingIntents) {
                System.out.println("Intent \"" + im.getExpectedIntent().getName()
                        + "\" was confused with intent \"" + im.getActualIntent().getName()
                        + "\" with the sentence \"" + im.getMatchingSentence()
                        + "\" and a confidence of " + im.getConfidence());
            }
        }
    }
}
