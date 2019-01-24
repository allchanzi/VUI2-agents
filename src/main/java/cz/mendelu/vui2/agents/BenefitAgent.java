package cz.mendelu.vui2.agents;

import cz.mendelu.vui2.agents.greenfoot.AbstractAgent;

import java.util.Stack;


public class BenefitAgent extends AbstractAgent {


    private Stack<Node> mainDFStree = new Stack<Node>();

    public BenefitAgent(){

    }

    @Override
    public Action doAction(boolean cantMove, boolean dirty, boolean dock) {
       return null;
    }

    /**
     * Logika implementacie
     * 1. Pohyb ako goal agent
     * 2. Ak narazi na spinu spravi novy stack a prejde jednobodove okolie spiny
     * (implementovane pomocou dalsieho stacku, pre kazdu novu spinu vznikne novy node v stacku)
     * 3. Po preskumani okolia spiny sa vrati na povodnu cestu
     */

    public class Node{

        private Node parent;


        public Node(){

        }
    }

}
