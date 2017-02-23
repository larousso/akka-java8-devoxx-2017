package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.devoxx2017.messages.Messages;
import akka.devoxx2017.utils.LucBessonScenarioGenerator;

/**
 * Created by adelegue on 23/02/2017.
 */
public class Producteur extends AbstractLoggingActor {

    private final ActorRef repondeurDeBillMurray;

    public static Props props(ActorRef repondeurDeBillMurray) {
        return Props.create(Producteur.class, () -> new Producteur(repondeurDeBillMurray));
    }

    private Producteur(ActorRef repondeurDeBillMurray) {
        this.repondeurDeBillMurray = repondeurDeBillMurray;
    }

//    @Override
//    public SupervisorStrategy supervisorStrategy() {
//        return new OneForOneStrategy(1, FiniteDuration.Zero(), e -> {
//            if(e instanceof Realisateur.MovieException) {
//                return SupervisorStrategy.restart();
//            } else {
//                return SupervisorStrategy.stop();
//            }
//        });
//    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Messages.FaitMoiUnFilm, m -> {
                    String scenario = LucBessonScenarioGenerator.nextScenario();
                    ActorRef realisateur = context().actorOf(Realisateur.props(repondeurDeBillMurray, sender(), scenario));
                    //TODO surveiller le realisateur
                })
                .match(Terminated.class, m -> {
                    log().info("Acteur terminé : {}", m.getActor().path());
                })
                .build();
    }
}
