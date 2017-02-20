package akka.devoxx2017;

import akka.actor.ActorSystem;
import scala.compat.java8.FutureConverters;

public class ApplicationMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        FutureConverters.toJava(system.terminate()).whenComplete((t, e) -> {
            System.out.println("Terminated");
        });
    }

}