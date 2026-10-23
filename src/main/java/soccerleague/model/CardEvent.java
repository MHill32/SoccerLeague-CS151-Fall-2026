package main.java.soccerleague.model;

/**
 * A yellow or red card given during a match.
 * Pratham Patel
 */

import main.java.soccerleague.exceptions.InvalidMatchOperationException;

public class CardEvent extends MatchEvent {

    private String cardType;
    private String reason;
    private boolean secondYellow;

    public CardEvent(int minute, Player player, Team team, String cardType, String reason) {
        super(minute, player, team);
        if (cardType == null || !(cardType.trim().equalsIgnoreCase("YELLOW") || cardType.trim().equalsIgnoreCase("RED"))) {
            throw new IllegalArgumentException("Card type must be YELLOW or RED");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("A card needs a reason");
        }
        this.cardType = cardType.trim().toUpperCase();
        this.reason = reason.trim();
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

    public void validatePlayer(Match match) throws InvalidMatchOperationException {
        validateParticipant(match);
        if (isSentOff(match, getPlayer())) {
            throw new InvalidMatchOperationException(getPlayer().getName() + " was already sent off in this match");
        }
    }

    public int countYellowsInMatch(Match match) {
        int count = 0;
        for (MatchEvent event : match.getEvents()) {
            if (event instanceof CardEvent && event.getPlayer() == getPlayer()
                    && ((CardEvent) event).getCardType().equals("YELLOW")) {
                count++;
            }
        }
        return count;
    }

    public boolean isSendingOff() {
        return cardType.equals("RED") || secondYellow;
    }

        @Override
    public void applyEvent(Match match) throws InvalidMatchOperationException {
        validatePlayer(match);

        if (cardType.equals("YELLOW") && countYellowsInMatch(match) >= 1) {
            secondYellow = true;
        }
        getPlayer().receiveCard(cardType);
        getTeam().recordCard(cardType);
        if (secondYellow) {
            getPlayer().receiveCard("RED");
            getTeam().recordCard("RED");
        }
        setApplied(true);
    }

    @Override
    public String getEventType() {

        if (secondYellow) {
            return "SECOND YELLOW (RED CARD)";
        }
        return cardType + " CARD";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + reason;
    }
}