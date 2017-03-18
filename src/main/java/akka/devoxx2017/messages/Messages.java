package akka.devoxx2017.messages;

import akka.actor.ActorRef;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by adelegue on 21/02/2017.
 */
public interface Messages {

    ChercheUnActeur ChercheUnActeur = new ChercheUnActeur();

    class ChercheUnActeur {}



    static JeSuisDAccord JeSuisDAccord(Scenario scenario) {
      return new JeSuisDAccord(scenario);
    }

    class JeSuisDAccord {
        public final Scenario scenario;

        public JeSuisDAccord(Scenario scenario) {
            this.scenario = scenario;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("scenario", scenario)
                    .toString();
        }
    }


    static AllezVousFaire AllezVousFaire(Scenario scenario) {
      return new AllezVousFaire(scenario);
    }

    class AllezVousFaire {
        public final Scenario scenario;

        public AllezVousFaire(Scenario scenario) {
            this.scenario = scenario;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("scenario", scenario)
                    .toString();
        }
    }



    FaitMoiUnFilm FaitMoiUnFilm = new FaitMoiUnFilm();

    class FaitMoiUnFilm {}



    FaitMoiUnScenar FaitMoiUnScenar = new FaitMoiUnScenar();

    class FaitMoiUnScenar {}



    MessageSuivant MessageSuivant = new MessageSuivant();

    class MessageSuivant {}



    PasDeMessage PasDeMessage = new PasDeMessage();

    class PasDeMessage {}



    static MessageSurRepondeur MessageSurRepondeur(String message, Scenario scenario, ActorRef numeroTel) {
        return new MessageSurRepondeur(message, scenario, numeroTel);
    }

    class MessageSurRepondeur {

        public final String message;

        public final Scenario scenario;

        public final ActorRef numeroTel;

        public MessageSurRepondeur(String message, Scenario scenario, ActorRef numeroTel) {
            this.message = message;
            this.scenario = scenario;
            this.numeroTel = numeroTel;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("message", message)
                    .append("scenario", scenario)
                    .append("numeroTel", numeroTel)
                    .toString();
        }
    }

    static NouveauMessageSurRepondeur NouveauMessageSurRepondeur(String message, Scenario scenario) {
        return new NouveauMessageSurRepondeur(message, scenario);
    }

    class NouveauMessageSurRepondeur {

        public final String message;

        public final Scenario scenario;

        public NouveauMessageSurRepondeur(String message, Scenario scenario) {
            this.message = message;
            this.scenario = scenario;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("message", message)
                    .append("scenario", scenario)
                    .toString();
        }
    }




    static Scenario Scenario(String scenario) {
        return new Scenario(scenario);
    }

    class Scenario {

        public final String scenario;
        public Scenario(String scenario) {
            this.scenario = scenario;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("scenario", scenario)
                    .toString();
        }
    }



    static Film Film(Scenario scenario, String acteur) {
        return new Film(scenario, acteur);
    }

    class Film {

        public final Scenario scenario;
        public final String acteur;

        public Film(Scenario scenario, String acteur) {
            this.scenario = scenario;
            this.acteur = acteur;
        }

        public String json() {
            return "{" +
                    "   \\”acteur\\”: \\”"+acteur+"\\”, " +
                    "   \\”scenario\\”: \\”"+scenario.scenario+"\\” " +
                    "}";
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("scenario", scenario)
                    .append("acteur", acteur)
                    .toString();
        }
    }

}
