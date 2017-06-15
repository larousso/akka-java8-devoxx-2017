package akka.devoxx2017.actors;

import akka.actor.*;
import akka.devoxx2017.messages.Messages;
import akka.devoxx2017.utils.LucBessonScenarioGenerator;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by 97306p on 15/06/2017.
 */
public class Realisateur extends AbstractLoggingActor {

    private final ActorRef repondeurBillMurray;
    private final ActorRef replyTo;

    private Messages.Scenario scenario;

    public final static Props props(ActorRef repondeurBillMurray, ActorRef replyTo) {
        return Props.create(Realisateur.class, () -> new Realisateur(repondeurBillMurray, replyTo));
    }

    private Realisateur(ActorRef repondeurBillMurray, ActorRef replyTo) {
        this.repondeurBillMurray = repondeurBillMurray;
        this.replyTo = replyTo;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.JeSuisDAccord.class, msg -> {
                    replyTo.tell(Messages.Film(scenario, "Bill Murray"), self());
                    context().setReceiveTimeout(Duration.Undefined());
                })
                .match(Messages.AllezVousFaire.class, msg -> {
                    replyTo.tell(Messages.Film(scenario, "Ben Affleck"), self());
                    context().setReceiveTimeout(Duration.Undefined());
                })
                .match(ReceiveTimeout.class, msg -> {
                    context().setReceiveTimeout(Duration.Undefined());
                    throw new FilmException("Il ne m'a pas répondu !!!", scenario);
                })
                .build();
    }

    @Override
    public void preStart() throws Exception {
        init();
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        init();
        log().info("Restarted");
    }

    private void init() {
        log().info("Envoi d'un message sur répondeur");
        scenario =  Messages.Scenario(LucBessonScenarioGenerator.nextScenario());
        repondeurBillMurray.tell(Messages.NouveauMessageSurRepondeur(
                "J'ai un scénario pour toi",
                Messages.Scenario(LucBessonScenarioGenerator.nextScenario())
        ), self());
        context().setReceiveTimeout(Duration.create(2, TimeUnit.SECONDS));
    }

    public static class FilmException extends RuntimeException {
        public final Messages.Scenario scenario;

        public FilmException(String message, Messages.Scenario scenario) {
            super(message);
            this.scenario = scenario;
        }

        public Messages.Scenario scenario() {
            return scenario;
        }
    }
}
