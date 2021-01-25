package com.xatkit.testing.recognition.dialogflow.model;

import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import lombok.val;

import static com.xatkit.dsl.DSL.*;

public class WebApplicationsCorpusBotModel extends ExecutionModelImpl {


    public WebApplicationsCorpusBotModel(){
        ReactPlatform reactPlatform = new ReactPlatform();
        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val printHello = state("PrintHello");

        val WebService = mapping("WebService")
                .entry().value("YouTube")
                ;
        val OperatingSystem = mapping("OperatingSystem")
                .entry().value("Android")
                ;
        val Browser = mapping("Browser")
                .entry().value("Chrome")
                ;
        val FindAlternative = intent("FindAlternative")
                .trainingSentence("Q&A Platform similar to WebService model?")
                .trainingSentence("What are the alternatives to WebService/Meetup for Event Planning")
                .trainingSentence("Is there a free and privacy-aware alternative to WebService?")
                .trainingSentence("WebService (registration bypasser) alternative that doesn't suck?")
                .trainingSentence("Alternative to WebService")
                .trainingSentence("WebService alternatives for Canada")
                .trainingSentence("Alternatives to WebService")
                .trainingSentence("Any alternatives to WebService?")
                .trainingSentence("Are there any good WebService or WebService alternatives?")
                .trainingSentence("Are there any good WebService alternatives with general availability outside the US?")
                .trainingSentence("WebService search engine alternatives")
                .trainingSentence("Any alternatives to WebService for your domain?")
                .trainingSentence("WebService alternatives?")
                .trainingSentence("Photo Sharing Site (alternative to WebService) supporting WebService sign in?")
                .trainingSentence("Any good alternative to WebService?")
                .trainingSentence("What alternatives to WebService Product Search exist?")
                .trainingSentence("Alternatives to discontinued WebService Appointment Slots")
                .trainingSentence("Is there a HTML alternative to WebService?")
                .trainingSentence("Alternatives for WebService (with OperatingSystem synchronizing)")
                .trainingSentence("Is there an online alternative to WebService")
                .trainingSentence("Alternatives to WebService with more features?")
                .trainingSentence("WebService alternative")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .parameter("OperatingSystem").fromFragment("OperatingSystem").entity(OperatingSystem)
                .getIntentDefinition();
        val DeleteAccount = intent("DeleteAccount")
                .trainingSentence("How do I delete my WebService account?")
                .trainingSentence("How can I delete my WebService account?")
                .trainingSentence("How can I remove myself from a WebService organization?")
                .trainingSentence("How to disable/delete a WebService account?")
                .trainingSentence("How to delete my WebService account?")
                .trainingSentence("How can I permanently delete my WebService account?")
                .trainingSentence("How do I delete my WebService profile?")
                .trainingSentence("How to change my WebService password or delete the account?")
                .trainingSentence("How to permanently delete a WebService ID")
                .trainingSentence("How to delete a WebService account")
                .trainingSentence("Deleting an account(website) from WebService?")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        val ExportData = intent("ExportData")
                .trainingSentence("How do I transfer my photos from WebService to WebService?")
                .trainingSentence("How can I backup my WebService hosted blog?")
                .trainingSentence("How can I export track.scrobble data from WebService?")
                .trainingSentence("Is it possible to export my data from WebService to back it up?")
                .trainingSentence("Archive/export all the blog entries from a RSS feed in WebService")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        val SyncAccounts = intent("SyncAccounts")
                .trainingSentence("How can I sync my WebService with WebService?")
                .trainingSentence("How do I sync WebService with my WebService?")
                .trainingSentence("How do I sync my WebService workout schedule with WebService?")
                .trainingSentence("Sync not export WebService events with WebService calendar")
                .trainingSentence("WebService not syncing calendars that have been added by URL with Windows 10 Calendar")
                .trainingSentence("How do I sync WebService WebService & WebService?")
                .trainingSentence("Can I Share/Sync My WebService Web Albums Automatically to My WebService Account?")
                .trainingSentence("WebService Contact Synchronization on the iPhone 3G")
                .trainingSentence("WebService Bookmarks and Browser Bookmark Sync -- Different?")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .parameter("Browser").fromFragment("Browser").entity(Browser)
                .getIntentDefinition();
        val None = intent("None")
                .trainingSentence("Change subject line in new WebService compose window")
                .trainingSentence("How can I get WebService search to show only matches within 5 miles of me?")
                .trainingSentence("Good wireframing apps?")
                .trainingSentence("Is there a catch to WebService's Premier account type?")
                .trainingSentence("Email WebService Form daily?")
                .trainingSentence("Embedding stop time in a WebService video link")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        val ChangePassword = intent("ChangePassword")
                .trainingSentence("How to retrieve a stolen WebService account's password?")
                .trainingSentence("WebService user set up through WebService Apps can't change their password")
                .trainingSentence("How do I change my password on WebService?")
                .trainingSentence("WebService recover password while logged in Iphone")
                .trainingSentence("Why can't I change my password and login with WebService?")
                .trainingSentence("How to retrieve forgotten WebService Admin password")
                .trainingSentence("Users can't reset their passwords anymore on WebService Apps?")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        val FilterSpam = intent("FilterSpam")
                .trainingSentence("Does moving a mail to the IMAP spam folder trains WebService's spam filter?")
                .trainingSentence("What are alternatives to WebService for spam filtering?")
                .trainingSentence("Does marking an email as spam in WebService affect the filter for everyone?")
                .trainingSentence("How can I prevent/lessen event spam on WebService?")
                .trainingSentence("WebService's filter for SPAM folder and/or keywords in multiple fields")
                .trainingSentence("Get rid of Russian junk from my WebService")
                .trainingSentence("WebService and WebService spam")
                .trainingSentence("Mark large numbers of messages as \"not spam\"")
                .trainingSentence("Does using WebService's \"Never send it to Spam\" filter mean I can't train their spam filter?")
                .trainingSentence("Correctly Identifying Spam Messages")
                .trainingSentence("Totally Blocking Spam To WebService Account")
                .trainingSentence("Stopping spam emails in WebService by pattern")
                .trainingSentence("When I move spam to my inbox it goes back into the spam again")
                .trainingSentence("How to remove spam filter for WebService Apps email")
                .trainingSentence("Do emails auto-forwarded from one WebService account to another stop if marked as spam?")
                .trainingSentence("How to send messages to spam in WebService filter?")
                .trainingSentence("Discarding spam mail faster in WebService")
                .trainingSentence("How do I disable the spam filter in WebService?")
                .trainingSentence("How to check your WebService spam label for good emails")
                .trainingSentence("How can I auto-delete some spam from WebService?")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        val DownloadVideo = intent("DownloadVideo")
                .trainingSentence("How do I download a WebService video?")
                .parameter("WebService").fromFragment("WebService").entity(WebService)
                .getIntentDefinition();
        awaitingInput.next()
                .when(intentIs(FindAlternative)).moveTo(printHello)
                .when(intentIs(DeleteAccount)).moveTo(printHello)
                .when(intentIs(ExportData)).moveTo(printHello)
                .when(intentIs(SyncAccounts)).moveTo(printHello)
                .when(intentIs(None)).moveTo(printHello)
                .when(intentIs(ChangePassword)).moveTo(printHello)
                .when(intentIs(FilterSpam)).moveTo(printHello)
                .when(intentIs(DownloadVideo)).moveTo(printHello)
        ;
        printHello.body(context -> {
            reactPlatform.reply(context, "Hey!");
        }).next().moveTo(awaitingInput);init.next().when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);
        val defaultFallback = fallbackState().body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));
        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedProviders().add(reactPlatform.getReactEventProvider());
        this.getUsedProviders().add(reactPlatform.getReactIntentProvider());
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }
}
