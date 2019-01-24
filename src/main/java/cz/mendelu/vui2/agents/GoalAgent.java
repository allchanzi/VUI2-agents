package cz.mendelu.vui2.agents;

import cz.mendelu.vui2.agents.greenfoot.AbstractAgent;

import java.util.*;

public class GoalAgent extends AbstractAgent {
    private Hashtable<String, String> dirToRight= new Hashtable<String, String>();
    private Hashtable<String, String> dirToLeft= new Hashtable<String, String>();
    private Hashtable<String, String> turn360= new Hashtable<String, String>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private String direction;
    private Hashtable<String, Integer> actualPosition= new Hashtable<String, Integer>();
    private Node actualNode;
    private Integer wayHomeLength;
    private Stack<String> wayHome = new Stack<String>();
    private Stack<Node> DFS = new Stack<Node>();
    private boolean homeMode;

    public GoalAgent(){
        this.dirToRight.put("N", "E");
        this.dirToRight.put("E", "S");
        this.dirToRight.put("S", "W");
        this.dirToRight.put("W", "N");
        this.dirToLeft.put("N", "W");
        this.dirToLeft.put("W", "S");
        this.dirToLeft.put("S", "E");
        this.dirToLeft.put("E", "N");
        this.turn360.put("N", "S");
        this.turn360.put("W", "E");
        this.turn360.put("S", "N");
        this.turn360.put("E", "W");
        this.direction = "N";
        this.actualPosition.put("x", 0);
        this.actualPosition.put("y", 0);
        this.wayHomeLength = 0;
        this.actualNode = new Node(0,0, this.wayHomeLength, null, null);
        this.actualNode.wayHome.push("H");
        this.nodes.add(this.actualNode);
        this.DFS.push(this.actualNode);
        this.homeMode = false;
        this.wayHome= (Stack<String>) this.actualNode.wayHome.clone();
    }

    @Override
    public Action doAction(boolean cantMove, boolean dirty, boolean dock) {
        if (dirty) {
            return Action.CLEAN;
        }
        if (this.wayHomeLength >= this.timeToSimulation + 5){
            this.homeMode = true;
        } else {
            this.timeToSimulation -= 1;
        }

        if (homeMode){
            System.out.println(this.wayHome.peek());
            if (this.wayHome.peek() == "H"){
                return Action.TURN_OFF;
            }
            if (this.direction == this.wayHome.peek()){
                this.wayHome.pop();
                return Action.FORWARD;
            } else {
                return this.choseTurn(this.wayHome.peek());
            }
        } else {

            if (!this.actualNode.canMove()) {
                if (this.DFS.size() == 1) {
                    return Action.TURN_OFF;
                }
                if (this.direction == this.actualNode.parentWay) {
                    this.actualNode = this.actualNode.parentNode;
                    this.actualPosition.put("x", this.actualNode.getX());
                    this.actualPosition.put("y", this.actualNode.getY());
                    this.wayHomeLength = this.actualNode.getHome();
                    this.wayHome = this.actualNode.wayHome;
                    this.DFS.pop();
                    return Action.FORWARD;
                } else {
                    return this.turnLeft();
                }
            } else {
                if (cantMove) {
                    return this.turnRight();
                } else {
                    return this.choseAction();
                }
            }
        }
    }


    public Action choseTurn(String alignDirection){
        if (this.dirToLeft.get(this.direction) == alignDirection){
            return this.turnLeft();
        } else if (this.dirToRight.get(this.direction) == alignDirection){
            return this.turnRight();
        } else {
            return this.turnRight();
        }
    }

    public Action choseAction(){
        if (this.direction == "N") {
            if (this.isExplored(this.actualPosition.get("x") + 1, this.actualPosition.get("y"))) {
                return this.turnRight();
            } else {
                this.actualPosition.put("x", this.actualPosition.get("x") + 1);
                return this.move();
            }

        } else if (this.direction == "E") {
            if (this.isExplored(this.actualPosition.get("x"), this.actualPosition.get("y") + 1)) {
                return this.turnRight();
            } else {
                this.actualPosition.put("y", this.actualPosition.get("y") + 1);
                return this.move();
            }

        } else if (this.direction == "S") {
            if (this.isExplored(this.actualPosition.get("x") - 1, this.actualPosition.get("y"))) {
                return this.turnRight();
            } else {
                this.actualPosition.put("x", this.actualPosition.get("x") - 1);
                return this.move();
            }
        } else if (this.direction == "W") {
            if (this.isExplored(this.actualPosition.get("x"), this.actualPosition.get("y") - 1)) {
                return this.turnRight();
            } else {
                this.actualPosition.put("y", this.actualPosition.get("y") - 1);
                return this.move();
            }
        } else {
            return null;
        }
    }

    public Action move(){
        this.wayHomeLength = this.wayHomeLength + 1;
        this.actualNode.popDirection(this.direction);
        this.actualNode = new Node(this.actualPosition.get("x"),
                this.actualPosition.get("y"),
                this.wayHomeLength, this.turn360.get(this.direction), this.actualNode);
        this.actualNode.popDirection(this.turn360.get(this.direction));
        popExploredNeighbors(this.actualNode);
        System.out.println("Get home->" + getDirectionHome(this.actualNode));
        switch (getDirectionHome(this.actualNode)){
            case "S" : {
                this.actualNode.wayHome =
                        (Stack<String>) getNeighbor(this.actualNode.getX() -1, this.actualNode.getY())
                                .wayHome
                                .clone();
                wayHomeCalc("S");
                break;
            }
            case "N" : {
                this.actualNode.wayHome =
                        (Stack<String>) getNeighbor(this.actualNode.getX() +1, this.actualNode.getY())
                                .wayHome
                                .clone();
                wayHomeCalc("N");
                break;
            }
            case "E" : {
                this.actualNode.wayHome =
                        (Stack<String>) getNeighbor(this.actualNode.getX(), this.actualNode.getY() +1 )
                                .wayHome
                                .clone();
                wayHomeCalc("E");
                break;
            }
            case "W" : {
                this.actualNode.wayHome =
                        (Stack<String>) getNeighbor(this.actualNode.getX(), this.actualNode.getY() - 1)
                                .wayHome
                                .clone();
                wayHomeCalc("W");
                break;
            }
            default: break;
        }
        this.nodes.add(this.actualNode);
        this.wayHome = (Stack<String>) this.actualNode.wayHome.clone();
        this.DFS.push(this.actualNode);
        return Action.FORWARD;
    }

    private Action turnRight(){
        this.actualNode.popDirection(this.direction);
        this.direction = this.getRightDirection(this.direction);
        return Action.TURN_RIGHT;
    }

    private Action turnLeft(){
        this.actualNode.popDirection(this.direction);
        this.direction = this.getLeftDirection(this.direction);
        return Action.TURN_LEFT;
    }

    private void wayHomeCalc(String direction){
        if (this.actualNode.wayHome.empty() || this.actualNode.wayHome.peek() == "H"){
            this.actualNode.wayHome.push(direction);
        } else {
            if (this.actualNode.wayHome.peek() == direction) {
                this.actualNode.wayHome.push(direction);
            } else if (this.dirToLeft.get(direction) == this.actualNode.wayHome.peek() ||
                    this.dirToRight.get(direction) == this.actualNode.wayHome.peek()) {
                this.actualNode.wayHome.push(direction);
                this.wayHomeLength += 1;
                this.actualNode.home += 1;
            } else if (this.turn360.get(direction) == this.actualNode.wayHome.peek()) {
                this.actualNode.wayHome.push(direction);
                this.wayHomeLength += 2;
                this.actualNode.home += 2;
            }
        }
    }

    private String getLeftDirection(String actualDirection){
        return this.dirToLeft.get(actualDirection);
    }

    private String getRightDirection(String actualDirection){
        return this.dirToRight.get(actualDirection);
    }

    private void popExploredNeighbors(Node node){
        Node sNeighbor = this.getNeighbor(node.getX() -1 , node.getY());
        Node nNeighbor = this.getNeighbor(node.getX() + 1 , node.getY());
        Node wNeighbor = this.getNeighbor(node.getX() , node.getY() -1 );
        Node eNeighbor = this.getNeighbor(node.getX() , node.getY() + 1);

        if (sNeighbor != null){
            node.popDirection("S");
            sNeighbor.popDirection("N");
        }
        if (nNeighbor != null){
            node.popDirection("N");
            nNeighbor.popDirection("S");
        }
        if (wNeighbor != null){
            node.popDirection("W");
            wNeighbor.popDirection("E");
        }
        if (eNeighbor != null){
            node.popDirection("E");
            eNeighbor.popDirection("W");
        }
    }

    private boolean isExplored(Integer x, Integer y){
        for (Node node: this.nodes) {
            if(node.getX() == x && node.getY() == y){
                return true;
            }
        }
        return false;
    }

    private Node getNeighbor(Integer x, Integer y){
        for (Node node: this.nodes) {
            if(node.getX() == x && node.getY() == y){
                return node;
            }
        }
        return null;
    }

    private String getDirectionHome(Node node){
        ArrayList<Node> candidates = new ArrayList<Node>(){};
        candidates.add(0, this.getNeighbor(node.getX() -1 , node.getY())); //S
        candidates.add(1, this.getNeighbor(node.getX() + 1 , node.getY())); //N
        candidates.add(2, this.getNeighbor(node.getX() , node.getY() + 1 )); //E
        candidates.add(3, this.getNeighbor(node.getX() , node.getY() - 1)); //W
        System.out.println(candidates);
        Integer shortest = Integer.MAX_VALUE;
        String direction = "S";
        for (int i = 0; i < 4; i++){
            if (candidates.get(i) != null){
                System.out.println(i + " -- " + candidates.get(i).getHome());
            }
            if (candidates.get(i) != null && candidates.get(i).getHome() < shortest){
                shortest = candidates.get(i).getHome();
                switch (i){
                    case 1: direction = "N"; break;
                    case 2: direction = "E"; break;
                    case 3: direction = "W"; break;
                }
            }
        }
        return direction;
    }

    public class Node {
        private ArrayList<String> directions = new ArrayList<>();
        public Stack<String> wayHome= new Stack<String>();
        private Integer x;
        private Integer y;
        private Integer home;
        public String parentWay;
        public Node parentNode;

        public Integer getHome() {
            return home;
        }

        public Integer getX() {
            return x;
        }

        public Integer getY() {
            return y;
        }

        public boolean canMove(){
            if (this.directions.isEmpty()){
                return false;
            } else {
                return true;
            }
        }

        public void popDirection(String direction){
            this.directions.remove(direction);
        }

        public Node(Integer x, Integer y, Integer home, String parentWay, Node parentNode){
            this.directions.add("N");
            this.directions.add("W");
            this.directions.add("S");
            this.directions.add("E");
            this.home = home;
            this.parentWay = parentWay;
            this.parentNode = parentNode;
            this.x = x;
            this.y = y;

        }
    }
}
