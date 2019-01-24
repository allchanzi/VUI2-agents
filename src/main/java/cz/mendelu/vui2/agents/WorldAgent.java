package cz.mendelu.vui2.agents;

import cz.mendelu.vui2.agents.greenfoot.AbstractAgent;

import java.util.HashMap;
import java.util.Stack;

public class WorldAgent extends AbstractAgent {

    public String direction;
    public String orientation;
    public Stack<Action> rechargeBacktrack = new Stack();
    public Boolean recharge;
    public Integer turnHome;
    public String lastDirectionMoved;
    public boolean switchOrientation;
    public Integer wholeTurn;
    public HashMap<String, String> flips = new HashMap<String, String>(){{
       put("N", "S");
       put("S", "N");
       put("W", "E");
       put("E", "W");
    }};


    public WorldAgent() {
        this.orientation = "N";
        this.direction = "N";
        this.recharge = false;
        this.turnHome = 2;
        this.lastDirectionMoved = "";
        this.wholeTurn = 0;
        this.switchOrientation = false;
    }

    @Override
    public Action doAction(boolean cantMove, boolean dirty, boolean dock) {
        if (this.timeToSimulation < 501) {
            this.recharge = true;
        } else {
            this.timeToSimulation -= 1;
        }

        if (!this.recharge){
            if (cantMove) {
                if (dirty) {
                    return Action.CLEAN;
                } else {
                    if (dock) this.rechargeBacktrack.push(Action.TURN_OFF);
                    if (this.switchOrientation){
                        return this.turnHorizontal();
                    } else {
                        return this.turnVertical();
                    }
                }
            } else {
                if (dirty) {
                    return Action.CLEAN;
                } else {
                    if (dock) this.rechargeBacktrack.push(Action.TURN_OFF);
                    if (this.switchOrientation){
                        return this.moveHorizontal();
                    } else {
                        return this.moveVertical();
                    }
                }
            }


        } else {
            if (this.turnHome > 0){
                this.turnHome -= 1;
                return Action.TURN_LEFT;
            } else return (Action) this.rechargeBacktrack.pop();
        }

//        return null;
    }

    public Action turnVertical(){
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
        return null;
    }

    public Action moveVertical(){
        if (this.direction == "N") {
            if (this.orientation == "N") {
                if (this.checkLoop("N")){
                    this.direction = "E";
                    this.orientation = "E";
                    this.rechargeBacktrack.push(Action.TURN_LEFT);
                    return Action.TURN_RIGHT;
                } else {
                    this.lastDirectionMoved = "N";
                    this.rechargeBacktrack.push(Action.FORWARD);
                    return Action.FORWARD;
                }
            } else if (this.orientation == "E") {
                this.orientation = "N";
                this.rechargeBacktrack.push(Action.TURN_RIGHT);
                return Action.TURN_LEFT;
            }
        } else if (this.direction == "S") {
            if (this.orientation == "S") {
                if (this.checkLoop("S")){
                    this.direction = "E";
                    this.orientation = "E";
                    this.rechargeBacktrack.push(Action.TURN_RIGHT);
                    return Action.TURN_LEFT;
                } else {
                    this.lastDirectionMoved = "S";
                    this.rechargeBacktrack.push(Action.FORWARD);
                    return Action.FORWARD;
                }
            } else if (this.orientation == "E") {
                this.orientation = "S";
                this.rechargeBacktrack.push(Action.TURN_LEFT);
                return Action.TURN_RIGHT;
            }
        } else if (this.direction == "N2S") {
            if (this.orientation == "E"){
                this.orientation = "E";
                this.direction = "S";
                this.wholeTurn = 0;
                this.rechargeBacktrack.push(Action.FORWARD);
                return Action.FORWARD;
            }
        } else if (this.direction == "S2N") {
            if (this.orientation == "E"){
                this.orientation = "E";
                this.direction = "N";
                this.wholeTurn = 0;
                this.rechargeBacktrack.push(Action.FORWARD);
                return Action.FORWARD;
            }
        } else {
            return null;
        }

        return null;
    }

    public Action moveHorizontal(){
        if (this.direction == "W"){
            if (this.orientation == "W") {
                if (this.checkLoop("W")){
                    this.direction = "S";
                    this.orientation = "S";
                    this.rechargeBacktrack.push(Action.TURN_RIGHT);
                    return Action.TURN_LEFT;
                } else {
                    this.lastDirectionMoved = "W";
                    this.rechargeBacktrack.push(Action.FORWARD);
                    return Action.FORWARD;
                }
            } else if (this.orientation == "N"){
                this.orientation = "W";
                this.rechargeBacktrack.push(Action.TURN_RIGHT);
                return Action.TURN_LEFT;
            }
        } else if (this.direction == "E"){
            if (this.orientation == "E") {
                if (this.checkLoop("E")){
                    this.direction = "N";
                    this.orientation = "N" ;
                    this.rechargeBacktrack.push(Action.TURN_RIGHT);
                    return Action.TURN_LEFT;
                } else {
                    this.lastDirectionMoved = "E";
                    this.rechargeBacktrack.push(Action.FORWARD);
                    return Action.FORWARD;
                }
            } else if (this.orientation == "N") {
                this.orientation = "E";
                this.rechargeBacktrack.push(Action.TURN_LEFT);
                return Action.TURN_RIGHT;
            }
        } else if (this.direction == "W2E") {
            if (this.orientation == "N"){
                this.orientation = "N";
                this.direction = "E";
                this.wholeTurn = 0;
                this.rechargeBacktrack.push(Action.FORWARD);
                return Action.FORWARD;
            }
        } else if (this.direction == "E2W") {
            if (this.orientation == "N"){
                this.orientation = "N";
                this.direction = "W";
                this.wholeTurn = 0;
                this.rechargeBacktrack.push(Action.FORWARD);
                return Action.FORWARD;
            }
        }
        return null;
    }

    public Action turnHorizontal(){
        if (this.direction == "W") {
            if (this.orientation == "W") {
                this.direction = "W2E";
                this.orientation = "N";
                this.rechargeBacktrack.push(Action.TURN_LEFT);
                return Action.TURN_RIGHT;
            }
            else if (this.orientation == "N"){
                this.orientation = "W";
                this.rechargeBacktrack.push(Action.TURN_RIGHT);
                return Action.TURN_LEFT;
            }
        } else if (this.direction == "W2E") {
            if (this.orientation == "N") {
                this.direction = "E";
                this.orientation = "E";
                this.rechargeBacktrack.push(Action.TURN_LEFT);
                return Action.TURN_RIGHT;
            }
        } else if (this.direction == "E"){
            if (this.orientation == "E") {
                this.direction = "E2W";
                this.orientation = "N";
                this.rechargeBacktrack.push(Action.TURN_RIGHT);
                return Action.TURN_LEFT;
            } else if (this.orientation == "N"){
                this.direction = "E";
                this.orientation = "E";
                this.rechargeBacktrack.push(Action.TURN_LEFT);
                return Action.TURN_RIGHT;
            }
        } else if (this.direction == "E2W"){
            if (this.orientation == "N") {
                this.direction = "W";
                this.orientation = "W";
                this.rechargeBacktrack.push(Action.TURN_RIGHT);
                return Action.TURN_LEFT;
            }
        }
        return null;
    }

    public boolean checkLoop(String movedDirection){
        if (this.lastDirectionMoved == this.flips.get(movedDirection)) {
            this.wholeTurn += 1;
        }
        if (wholeTurn == 3){
            this.switchOrientation = (this.switchOrientation) ? false : true;
            this.wholeTurn = 0;
            return true;
        } else {
            return false;
        }
    }

}
