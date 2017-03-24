package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by adelegue on 22/02/2017.
 */
public class BillMurray extends AbstractLoggingActor {

    private final ActorRef repondeur;

    public static Props props(ActorRef repondeur) {
        return Props.create(BillMurray.class, () -> new BillMurray(repondeur));
    }

    private BillMurray(ActorRef repondeur) {
        this.repondeur = repondeur;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.MessageSurRepondeur.class, m -> {
                    log().info("J'ai un nouveau message {}", m.message);
                    m.numeroTel.tell(Messages.JeSuisDAccord(m.scenario), self());
                    repondeur.tell(Messages.MessageSuivant, self());
                })
                .matchEquals(Messages.PasDeMessage, m -> {
                    context().system().scheduler().scheduleOnce(
                            FiniteDuration.create(1, TimeUnit.SECONDS),
                            repondeur,
                            Messages.MessageSuivant,
                            context().dispatcher(),
                            self()
                    );
                })
                .build();
    }


    @Override
    public void preStart() throws Exception {
        context().system().scheduler().scheduleOnce(
                FiniteDuration.create(1, TimeUnit.SECONDS),
                repondeur,
                Messages.MessageSuivant,
                context().dispatcher(),
                self()
        );
    }
}
