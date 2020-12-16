package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.testing.recognition.dialogflow.model.DummiestBotModel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import static org.assertj.core.api.Assertions.assertThat;

public class DummiestBotMatchIntentTest {

    private static ExecutionModel botModel = new DummiestBotModel();

    private static XatkitBot xatkitBot;

    private static Thread botThread;

    private static DialogFlowIntentRecognitionProvider dialogFlowProvider;

    /**
     * Starts the bot used to run the test cases.
     * <p>
     * Note that the bot is started once and is not reset between test cases. This shouldn't be an issue if the
     * test cases do not alter the bot and/or bot model. The bot can be accessed and queried through its
     * <a href="http://localhost:5000">web interface</a>.
     * <p>All
     * This method ensures that {@link #dialogFlowProvider} is set when it returns.
     * <p>
     * <b>Note</b>: make sure you properly configured {@code /src/test/resources/greetings-bot.properties} with valid
     * DialogFlow credentials before you run this class.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws InterruptedException, ConfigurationException {
        Configurations configurations = new Configurations();
        Configuration botConfiguration = configurations.properties(DummiestBotMatchIntentTest.class.getClassLoader().getResource("greetings-bot.properties"));

        xatkitBot = new XatkitBot(botModel, botConfiguration);
        botThread = new Thread(xatkitBot);
        botThread.start();
        while (isNull(dialogFlowProvider)) {
            Thread.sleep(1000);
            if (nonNull(xatkitBot.getXatkitServer()) && xatkitBot.getXatkitServer().isStarted()) {
                /*
                 * The Xatkit server is started. At this point the DialogFlow connector should be fully initialized.
                 * (And yes, this is a ugly fix)
                 */
                dialogFlowProvider = (DialogFlowIntentRecognitionProvider) xatkitBot.getIntentRecognitionProvider();
            }
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        /*
         * Stop the bot, this may take a few seconds.
         */

        botThread.interrupt();
    }


    @Test
    public void testMatchingIntents() throws IntentRecognitionProviderException {
        DialogFlowIntentMatcher dialogFlowIntentMatcher = new DialogFlowIntentMatcher(xatkitBot);
        List<IntentMatch> matchingIntents = dialogFlowIntentMatcher.getMatchingIntents();
        assertThat(matchingIntents.size()).isEqualTo(2);
    }
}
