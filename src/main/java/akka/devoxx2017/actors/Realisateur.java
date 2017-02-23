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
    private final String scenario;


    public static Props props(ActorRef repondeur, String scenario) {
        return Props.create(Realisateur.class, () -> new Realisateur(repondeur, scenario));
    }

    private Realisateur(ActorRef repondeurDeBillMurray, String scenario) {
        this.repondeurDeBillMurray = repondeurDeBillMurray;
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
                    context().parent().tell(Messages.Film(m.scenario, "Bill Murray"), self());
                    context().stop(self());
                })
                .match(Messages.AllezVousFaire.class, m -> {
                    log().info("Il n'est pas d'accord, je prend Ben Affleck ...");
                    context().parent().tell(Messages.Film(m.scenario, "Ben Affleck"), self());
                    context().stop(self());
                })
                .matchEquals(ReceiveTimeout.getInstance(), m -> {

                })
                .build();
    }

    @Override
    public void preStart() throws Exception {
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
