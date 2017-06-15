package akka.devoxx2017.actors;

import akka.actor.*;
import akka.devoxx2017.messages.Messages;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by 97306p on 15/06/2017.
 */
public class Producteur extends AbstractLoggingActor {

    private final ActorRef repondeurBillMurray;

    public final static Props props(ActorRef repondeurBillMurray) {
        return Props.create(Producteur.class, () -> new Producteur(repondeurBillMurray));
    }

    private Producteur(ActorRef repondeurBillMurray) {
        this.repondeurBillMurray = repondeurBillMurray;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(5, Duration.create(20, TimeUnit.SECONDS), e -> {
            if (e instanceof Realisateur.FilmException) {
                log().error("Arggg "+ e.getMessage());
                return SupervisorStrategy.restart();
            } else {
                return SupervisorStrategy.stop();
            }
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.FaitMoiUnFilm.class, msg -> {
                    ActorRef realisateur = context().actorOf(Realisateur.props(repondeurBillMurray, sender()));
                    context().watch(realisateur);
                })
                .match(Terminated.class, msg -> {
                    log().info("Realisateur terminé");
                })
                .build();
    }
}
