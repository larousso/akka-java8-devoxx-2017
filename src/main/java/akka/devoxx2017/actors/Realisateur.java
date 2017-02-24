package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.devoxx2017.messages.Messages;

/**
 * Created by adelegue on 22/02/2017.
 */
public class Realisateur extends AbstractLoggingActor {

    private final ActorRef repondeurDeBillMurray;
    private final ActorRef replyTo;
    private final String scenario;


    public static Props props(ActorRef repondeur, ActorRef replyTo, String scenario) {
        return Props.create(Realisateur.class, () -> new Realisateur(repondeur, replyTo, scenario));
    }

    private Realisateur(ActorRef repondeurDeBillMurray, ActorRef replyTo, String scenario) {
        this.repondeurDeBillMurray = repondeurDeBillMurray;
        this.replyTo = replyTo;
        this.scenario = scenario;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Messages.ChercheUnActeur, m -> {
                    repondeurDeBillMurray.tell(Messages.MessageSurRepondeur(
                                "J'ai un role pour toi ",
                                Messages.Scenario(scenario),
                                self()
                            ), self());
                    //TODO Gérer le timeout

                })
                .match(Messages.JeSuisDAccord.class, m -> {
                    log().info("Il est d'accord !!!");
                    replyTo.tell(Messages.Film(Messages.Scenario(scenario), "Bill Murray"), self());
                    //TODO Gérer le timeout
                    context().stop(self());
                })
                .match(Messages.AllezVousFaire.class, m -> {
                    log().info("Il n'est pas d'accord, je prends Ben Affleck ...");
                    replyTo.tell(Messages.Film(Messages.Scenario(scenario), "Ben Affleck"), self());
                    //TODO Gérer le timeout
                    context().stop(self());
                })
                .matchEquals(ReceiveTimeout.getInstance(), m -> {
                    log().info("Il n'a pas répondu :(");
                    throw new MovieException("Il n'a pas répondu :(", Messages.Scenario(scenario));
                })
                .build();
    }

    @Override
    public void preStart() throws Exception {
        self().tell(Messages.ChercheUnActeur, self());
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        self().tell(Messages.ChercheUnActeur, self());
    }

    public static class MovieException extends RuntimeException {

        public final Messages.Scenario scenario;

        public MovieException(String message, Messages.Scenario scenario) {
            super(message);
            this.scenario = scenario;
        }
    }
}
