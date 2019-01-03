package cz.mendelu.vui2.agents;

import cz.mendelu.vui2.agents.greenfoot.AbstractAgent;

public class ReactionAgent extends AbstractAgent {

    public String orientation;
    public String direction;

    public ReactionAgent() {
        this.orientation = "N";
        this.direction = "N";
    }

    @Override
    public Action doAction(boolean cantMove, boolean dirty, boolean dock) {
        if (cantMove) {
            if (dirty) {
                return Action.CLEAN;
            } else {
                if (dock) {
                    return Action.TURN_OFF;
                } else {
                    if (this.direction == "N") {
                        if (this.orientation == "N") {
                            this.direction = "N2S";
                            this.orientation = "E";
                            return Action.TURN_RIGHT;
                        }
                        else if (this.orientation == "E"){
                            this.orientation = "N";
                            return Action.TURN_LEFT;
                        }
                    } else if (this.direction == "N2S") {
                        if (this.orientation == "E") {
                            this.direction = "S";
                            this.orientation = "S";
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "S"){
                        if (this.orientation == "S") {
                            this.direction = "S2N";
                            this.orientation = "E";
                            return Action.TURN_LEFT;
                        } else if (this.orientation == "E"){
                            this.direction = "S";
                            this.orientation = "S";
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "S2N"){
                        if (this.orientation == "E") {
                            this.direction = "N";
                            this.orientation = "N";
                            return Action.TURN_LEFT;
                        }
                    }
                }
            }
        } else {
            if (dirty) {
                return Action.CLEAN;
            } else {
                if (dock) {
                    return Action.TURN_OFF;
                } else {
                    if (this.direction == "N") {
                        if (this.orientation == "N") return Action.FORWARD;
                        else if (this.orientation == "E") {
                            this.orientation = "N";
                            return Action.TURN_LEFT;
                        }
                    } else if (this.direction == "S") {
                        if (this.orientation == "S") return Action.FORWARD;
                        else if (this.orientation == "E") {
                            this.orientation = "S";
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "N2S") {
                        if (this.orientation == "E"){
                            this.orientation = "E";
                            this.direction = "S";
                            return Action.FORWARD;
                        }
                    } else if (this.direction == "S2N") {
                        if (this.orientation == "E"){
                            this.orientation = "E";
                            this.direction = "N";
                            return Action.FORWARD;
                        }
                    }
                }
            }
        }
        return null;
    }
}

