package cz.mendelu.vui2.agents;

import cz.mendelu.vui2.agents.greenfoot.AbstractAgent;

import java.util.Stack;

public class WorldAgent extends AbstractAgent {

    public String direction;
    public String orientation;
    public Stack<Action> rechargeBacktrack = new Stack();
    public Boolean recharge;
    public Integer turnHome;

    

    public WorldAgent() {
        this.orientation = "N";
        this.direction = "N";
        this.recharge = false;
        this.turnHome = 2;
    }

    @Override
    public Action doAction(boolean cantMove, boolean dirty, boolean dock) {
        if (this.timeToSimulation < 501) {
            this.recharge = true;
        } else {
            this.timeToSimulation -= 1;
        }
        System.out.println(this.timeToSimulation);
        if (!this.recharge){
            if (cantMove) {
                if (dirty) {
                    return Action.CLEAN;
                } else {
                    if (dock) this.rechargeBacktrack.push(Action.TURN_OFF);
                    if (this.direction == "N") {
                        if (this.orientation == "N") {
                            this.direction = "N2S";
                            this.orientation = "E";
                            this.rechargeBacktrack.push(Action.TURN_LEFT);
                            return Action.TURN_RIGHT;
                        }
                        else if (this.orientation == "E"){
                            this.orientation = "N";
                            this.rechargeBacktrack.push(Action.TURN_RIGHT);
                            return Action.TURN_LEFT;
                        }
                    } else if (this.direction == "N2S") {
                        if (this.orientation == "E") {
                            this.direction = "S";
                            this.orientation = "S";
                            this.rechargeBacktrack.push(Action.TURN_LEFT);
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "S"){
                        if (this.orientation == "S") {
                            this.direction = "S2N";
                            this.orientation = "E";
                            this.rechargeBacktrack.push(Action.TURN_RIGHT);
                            return Action.TURN_LEFT;
                        } else if (this.orientation == "E"){
                            this.direction = "S";
                            this.orientation = "S";
                            this.rechargeBacktrack.push(Action.TURN_LEFT);
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "S2N"){
                        if (this.orientation == "E") {
                            this.direction = "N";
                            this.orientation = "N";
                            this.rechargeBacktrack.push(Action.TURN_RIGHT);
                            return Action.TURN_LEFT;
                        }
                    }

                }
            } else {
                if (dirty) {
                    return Action.CLEAN;
                } else {
                    if (dock) this.rechargeBacktrack.push(Action.TURN_OFF);
                    if (this.direction == "N") {
                        if (this.orientation == "N") {
                            this.rechargeBacktrack.push(Action.FORWARD);
                            return Action.FORWARD;
                        } else if (this.orientation == "E") {
                            this.orientation = "N";
                            this.rechargeBacktrack.push(Action.TURN_RIGHT);
                            return Action.TURN_LEFT;
                        }
                    } else if (this.direction == "S") {
                        if (this.orientation == "S") {
                            this.rechargeBacktrack.push(Action.FORWARD);
                            return Action.FORWARD;
                        } else if (this.orientation == "E") {
                            this.orientation = "S";
                            this.rechargeBacktrack.push(Action.TURN_LEFT);
                            return Action.TURN_RIGHT;
                        }
                    } else if (this.direction == "N2S") {
                        if (this.orientation == "E"){
                            this.orientation = "E";
                            this.direction = "S";
                            this.rechargeBacktrack.push(Action.FORWARD);
                            return Action.FORWARD;
                        }
                    } else if (this.direction == "S2N") {
                        if (this.orientation == "E"){
                            this.orientation = "E";
                            this.direction = "N";
                            this.rechargeBacktrack.push(Action.FORWARD);
                            return Action.FORWARD;
                        }
                    }

                }
            }
        } else {
            if (this.turnHome > 0){
                this.turnHome -= 1;
                return Action.TURN_LEFT;
            } else return (Action) this.rechargeBacktrack.pop();
        }

        return null;
    }
}
