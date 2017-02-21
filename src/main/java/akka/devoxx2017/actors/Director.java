package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;
import javaslang.control.Option;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by adelegue on 20/02/2017.
 */
public class Director extends AbstractLoggingActor {

    private final ActorRef replyTo;
    private final ActorRef scenarist;
    private final ActorRef billMurrayAnswerPhone;
    private Option<Cancellable> timeout = Option.none();
    private String scenario;

    public static FuckYou FuckYou = new FuckYou();

    public static IAmIn IAmIn = new IAmIn();

    public final static Props props(ActorRef replyTo, ActorRef scenarist, ActorRef billMurrayAnswerPhone) {
        return Props.create(Director.class, () -> new Director(replyTo, scenarist, billMurrayAnswerPhone));
    }

    private Director(ActorRef replyTo, ActorRef scenarist, ActorRef billMurrayAnswerPhone) {
        this.scenarist = scenarist;
        this.replyTo = replyTo;
        this.billMurrayAnswerPhone = billMurrayAnswerPhone;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Scenario.class, msg -> {
                    this.scenario = msg.scenario;

                    billMurrayAnswerPhone.tell(
                            AnswerPhone.LeaveAMessage(Messages.PhoneMessage("Hey I have a new Scenario for you : "+scenario, self())),
                            self()
                    );

                    timeout = Option.of(context().system().scheduler().scheduleOnce(
                            FiniteDuration.create(3, TimeUnit.SECONDS),
                            self(),
                            "To late",
                            context().dispatcher(),
                            self()
                    ));
                })
                .match(IAmIn.class, msg -> {
                    log().info("He's OK !!!");
                    timeout.forEach(Cancellable::cancel);
                    Messages.AMovie movie = Messages.AMovie(scenario, "Bill Murray");
                    context().parent().tell(movie, self());
                    replyTo.tell(movie, self());
                    context().stop(self());
                })
                .match(FuckYou.class, msg -> {
                    log().info("He's not Ok !!!");
                    throw new MovieException("He told me fuck you", scenario);
                })
                .match(String.class, m -> m.startsWith("To late"), msg -> {
                    log().info("He never reply for " + scenario);
                    Messages.AMovie movie = Messages.AMovie(scenario, "Ben Affleck");
                    context().parent().tell(movie, self());
                    replyTo.tell(movie, self());
                    context().stop(self());
                })
                .build();
    }

    @Override
    public void preStart() throws Exception {
        scenarist.tell(Scenarist.CreateScenario, self());
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log().info("Retrying !!! ");
        scenarist.tell(Scenarist.CreateScenario, self());
    }

    @Override
    public void postStop() throws Exception {
        timeout.forEach(Cancellable::cancel);
    }

    public static class MovieException extends RuntimeException {

        public final String scenario;
        
        public MovieException(String message, String scenario) {
            super(message);
            this.scenario = scenario;
        }
    }

    public static Scenario Scenario(String scenario) {
        return new Scenario(scenario);
    }

    public static class Scenario {

        public final String scenario;

        public Scenario(String scenario) {
            this.scenario = scenario;
        }

    }

    public static class FuckYou {}

    public static class IAmIn {}
}
