package main.java.soccerleague.model;

public class CardEvent extends MatchEvent {

    private String cardType;
    private String reason;
    private boolean secondYellow;

    public CardEvent(int minute, Player player, Team team, String cardType, String reason) {
        super(minute, player, team);
        this.cardType = cardType;
        this.reason = reason;
        this.secondYellow = false;
    }

    public String getCardType() {
        return cardType;
    }

    public String getReason() {
        return reason;
    }

    public boolean isSecondYellow() {
        return secondYellow;
    }

    public boolean validatePlayer(Match match) {
        return true;
    }

    public boolean isSendingOff() {
        return cardType.equals("RED") || secondYellow;
    }

    public void reverseCard() {
    }

    @Override
    public void applyEvent(Match match) {
    }

    @Override
    public String getEventType() {
        return cardType + " CARD";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + reason;
    }
}
