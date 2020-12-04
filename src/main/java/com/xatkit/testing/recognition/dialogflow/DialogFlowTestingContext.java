package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.recognition.dialogflow.DialogFlowStateContext;
import com.xatkit.execution.State;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.IntentDefinition;

import java.util.Arrays;

/**
 * A DialogFlow context that can be configured to enable the matching of specific {@link IntentDefinition}s.
 * <p>
 * This class wraps an existing {@link DialogFlowStateContext}. See
 * {@link com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider#createContext(String)} to create
 * such context.
 * <p>
 * See {@link #enableIntents(IntentDefinition...)} to enable the matching of specific {@link IntentDefinition}s.
 * <p>
 * This class is specifically designed to test
 * {@link com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider#getIntent(String, StateContext)}
 * . It is not safe to rely on the bot execution logic when the context are manually updated (e.g. if you enable an
 * intent that is not supposed to be matched the bot's state machine won't know how to deal with it).
 * <p>
 * Example of test case involving this class:
 * <pre>
 * {@code
 * ExecutionModel model = ... // Get/create the bot model you want to test
 * XatkitBot bot = new XatkitBot(model, configuration);
 * bot.run();
 * IntentRecognitionProvider intentProvider = bot.getIntentRecognitionProvider();
 * DialogFlowTestingContext testingContext
 *      = new DialogFlowTestingContext((DialogFlowStateContext) intentProvider.createContext("MyContext"));
 * testingContext.enableIntent(myIntent);
 * RecognizedIntent recognizedIntent = intentProvider.getIntent("Hello", testingContext);
 * assertThat(recognizedIntent.getDefinition()).isEqualTo(myIntent);
 * }
 * </pre>
 */
public class DialogFlowTestingContext extends DialogFlowStateContext {

    /**
     * The fake {@link State} used to simulate enabled intents.
     * <p>
     * This {@link State} is returned by {@link #getState()} and replaces the default state stored in the context.
     * The fake state will be used by the DialogFlow connector to enable/disable intents, and can be configured
     * through {@link #enableIntents(IntentDefinition...)}.
     */
    private FakeState fakeState;

    /**
     * Creates a {@link DialogFlowTestingContext} from the provided {@code context}.
     *
     * @param context the base context
     */
    public DialogFlowTestingContext(DialogFlowStateContext context) {
        super(context.getSessionName());
        this.fakeState = new FakeState();
    }

    /**
     * Enables the provided {@code intents} for matching.
     * <p>
     * Calling this method ensures that the next calls to
     * {@link com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider#getIntent(String, StateContext)}
     * can match any of the provided {@code intents}. It's not possible to match other intents, and unmatched input
     * will be matched to {@link com.xatkit.core.recognition.IntentRecognitionProvider#DEFAULT_FALLBACK_INTENT}.
     *
     * @param intents the {@link IntentDefinition}s to enable
     */
    public void enableIntents(IntentDefinition... intents) {
        fakeState.setIntents(Arrays.asList(intents));
    }

    /**
     * Returns the fake {@link State} used to enable/disable {@link IntentDefinition}s.
     *
     * @return the fake {@link State} used to enable/disable {@link IntentDefinition}s
     */
    @Override
    public State getState() {
        return fakeState;
    }
}
