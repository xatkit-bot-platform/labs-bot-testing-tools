package com.xatkit.testing.recognition.dialogflow.model;

import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import lombok.val;

import static com.xatkit.dsl.DSL.*;

public class DummyBotModel extends ExecutionModelImpl {

    public DummyBotModel() {

        /*
         * Define the intents our bot will react to.
         */
        val iAm = intent("IAm")
                .trainingSentence("I'm NAME")
                .trainingSentence("I am NAME")
                .parameter("name")
                .fromFragment("NAME")
                .entity(
                        any()
                )
                .getIntentDefinition();

        val youAre = intent("YouAre")
                .trainingSentence("You're NAME")
                .trainingSentence("You are NAME")
                .parameter("name")
                .fromFragment("NAME")
                .entity(
                        any()
                )
                .getIntentDefinition();

        /*
         * Instantiate the platform we will use in the bot definition.
         */
        /*
         * Similarly, instantiate the intent/event providers we want to use.
         */
        ReactPlatform reactPlatform = new ReactPlatform();
        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();


        /*
         * Create the states we want to use in our bot.
         */
        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val handleIAm = state("HandleWelcome");
        val handleYouAre = state("HandleWhatsUp");

        /*
         * Specify the content of the bot states (i.e. the behavior of the bot).
         */
        init
                .next()
                /*
                 * We check that the received event matches the ClientReady event defined in the
                 * ReactEventProvider. The list of events defined in a provider is available in the provider's
                 * wiki page.
                 */
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);

        awaitingInput
                .next()
                /*
                 * The Xatkit DSL offers dedicated predicates (intentIs(IntentDefinition) and eventIs
                 * (EventDefinition) to check received intents/events.
                 * <p>
                 * You can also check a condition over the underlying bot state using the following syntax:
                 * <pre>
                 * {@code
                 * .when(context -> [condition manipulating the context]).moveTo(state);
                 * }
                 * </pre>
                 */
                .when(intentIs(iAm)).moveTo(handleIAm)
                .when(intentIs(youAre)).moveTo(handleYouAre);

        handleIAm
                .body(context -> reactPlatform.reply(context, "Yes, you are"))
                .next()
                /*
                 * A transition that is automatically navigated: in this case once we have answered the user we
                 * want to go back in a state where we wait for the next intent.
                 */
                .moveTo(awaitingInput);

        handleYouAre
                .body(context -> reactPlatform.reply(context, "Yes, I am"))
                .next()
                .moveTo(awaitingInput);

        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));

        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedProviders().add(reactEventProvider);
        this.getUsedProviders().add(reactIntentProvider);
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }
}
