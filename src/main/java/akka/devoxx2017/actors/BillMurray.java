package akka.devoxx2017.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;
import akka.japi.pf.FI;
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
        return bonneHumeur();
    }

    private Receive bonneHumeur() {
        return receiveBuilder()
                .match(Messages.MessageSurRepondeur.class, m -> {
                    log().info("J'ai un nouveau message {}", m.message);
                    m.numeroTel.tell(Messages.JeSuisDAccord(m.scenario), self());
                    repondeur.tell(Messages.MessageSuivant, self());
                    getContext().become(provocateur(), true);
                })
                .matchEquals(Messages.PasDeMessage, pasDeMessage())
                .build();
    }


    private Receive provocateur() {
        return receiveBuilder()
                .match(Messages.MessageSurRepondeur.class, m -> {
                    log().info("J'ai un nouveau message {}", m.message);
                    m.numeroTel.tell(Messages.AllezVousFaire(m.scenario), self());
                    repondeur.tell(Messages.MessageSuivant, self());
                    getContext().become(grognon(), true);
                })
                .matchEquals(Messages.PasDeMessage, pasDeMessage())
                .build();
    }


    private Receive grognon() {
        return receiveBuilder()
                .match(Messages.MessageSurRepondeur.class, m -> {
                    log().info("J'ai un nouveau message {}", m.message);
                    repondeur.tell(Messages.MessageSuivant, self());
                    getContext().become(bonneHumeur(), true);
                })
                .matchEquals(Messages.PasDeMessage, pasDeMessage())
                .build();
    }

    private FI.UnitApply<Messages.PasDeMessage> pasDeMessage() {
        return m -> {
            context().system().scheduler().scheduleOnce(
                    FiniteDuration.create(1, TimeUnit.SECONDS),
                    repondeur,
                    Messages.MessageSuivant,
                    context().dispatcher(),
                    self()
            );
        };
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
