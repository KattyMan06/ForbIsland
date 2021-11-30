package Logic;

import Cards.Card;
import Cards.TreasureCard;
import Decks.FloodDeck;
import Decks.TreasureDeck;
import Graphics.GameBoardGraphic;
import Water.WaterMeter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class GameState {
    public static String[] collectedTreasures;
    private BufferedImage boardTemplate;
    public static WaterMeter waterMeter;
    public static TreasureDeck treasureDeck;
    public static FloodDeck floodDeck;
    public static ArrayList<Pawn> pawnLoc;
    public static ArrayList<Tile> tileLoc;
    public static int turn;
    private static int actionCount;
    public final static String[] TILENAMES =
            {"Whispering Garden","Watchtower","Twilight Hollow","Tidal Palace"
                    ,"Temple of the Sun","Temple of the Moon","Silver Gate","Phantom Rock","Observatory","Misty Marsh"
                    ,"Lost Lagoon","Iron Gate","Howling Garden","Gold Gate","Fools' Landing", "Dunes of Deception",
                    "Crimson Forest", "Coral Palace", "Copper Gate", "Cliffs of Abandon", "Cave of Shadows", "" +
                    "Cave of Embers", "Bronze Gate", "Breakers Bridge"};
    private static HashMap<Point, Tile> dupl;
    private static int[] loc;

    public GameState(int numPlayers, String diff){
        collectedTreasures = new String[4];
        pawnLoc = new ArrayList<>();
        tileLoc = new ArrayList<>();
        treasureDeck = new TreasureDeck();
        floodDeck = new FloodDeck();
        turn = 0;
        actionCount = 0;
        dupl = GameBoardGraphic.localTileLoc;
        //loc = new int[0];

        if(diff.equals("Novice")){waterMeter = new WaterMeter(2.0);}
        else if(diff.equals("Normal")){waterMeter = new WaterMeter(2.25);}
        else if(diff.equals("Elite")){waterMeter = new WaterMeter(2.5);}
        else{waterMeter = new WaterMeter(3.0);}

        shuffleTiles();
        setRoles(numPlayers);

        new GameBoardGraphic();
    }

    private static void setRoles(int np){
        String[] temp = {"Engineer", "Diver", "Explorer", "Navigator", "Pilot", "Messenger"};
        ArrayList<String> roles = new ArrayList(Arrays.asList(temp));

        for(int i=0; i<np; i++) {
            int pos = (int)(Math.random()*roles.size())+0;
            Pawn p = new Pawn(roles.remove(pos), i+1);
            pawnLoc.add(p);
        }
    }

    public static void shuffleTiles() {
        for(int i = 0; i < 24; i++) {
            tileLoc.add(new Tile(TILENAMES[i])); //TO BE CHANGED
        }
        Collections.shuffle(tileLoc);
        for(int i=0; i<6; i++){
            String name = floodDeck.getCard().getCardName();
            for(int ii=0; ii<tileLoc.size(); ii++){
                if(tileLoc.get(ii).getName().equals(name)){
                    Tile t = tileLoc.get(ii);
                    t.floodTile();
                    tileLoc.set(ii, t);
                }
            }
        }
    }

    public double riseWaterLevel() {
        waterMeter.watersRise();
        return waterMeter.getWaterLevel();
    }

    public static void setOriginalPlayerLocation() {
        for(Pawn p: pawnLoc){
            if(p.getRole().equals("Diver")){for(Tile t: tileLoc){if(t.getName().equals("Iron Gate")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
            if(p.getRole().equals("Explorer")){for(Tile t: tileLoc){if(t.getName().equals("Copper Gate")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
            if(p.getRole().equals("Messenger")){for(Tile t: tileLoc){if(t.getName().equals("Silver Gate")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
            if(p.getRole().equals("Navigator")){for(Tile t: tileLoc){if(t.getName().equals("Gold Gate")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
            if(p.getRole().equals("Pilot")){for(Tile t: tileLoc){if(t.getName().contains("Fool")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
            if(p.getRole().equals("Engineer")){for(Tile t: tileLoc){if(t.getName().equals("Bronze Gate")){p.setLocation(t.getLocation().get(0), t.getLocation().get(1));}}}
        }
    }

    public static boolean checkMove(Tile t, Pawn p) {
        if(t==null || t.isSunk()==true)
            return false;
        ArrayList<Integer> pawnL = p.getLocation();
        ArrayList<Integer> tileL = t.getLocation();
        if(pawnL.get(0)==tileL.get(0)){if(pawnL.get(1)+1==tileL.get(1) || pawnL.get(1)-1==tileL.get(1)){return true;}}
        if(pawnL.get(1)==tileL.get(1)){if(pawnL.get(0)+1==tileL.get(0) || pawnL.get(0)-1==tileL.get(0)){return true;}}
//        else if(p.getRole().equals("Explorer")&&
//                ((t.getLocation().get(0)==p.getLocation().get(0)+1&&t.getLocation().get(1)==p.getLocation().get(1)+1)||
//                        (t.getLocation().get(0)==p.getLocation().get(0)-1&&t.getLocation().get(1)==p.getLocation().get(1)+1)||
//                        (t.getLocation().get(0)==p.getLocation().get(0)+1&&t.getLocation().get(1)==p.getLocation().get(1)-1)||
//                        (t.getLocation().get(0)==p.getLocation().get(0)-1&&t.getLocation().get(1)==p.getLocation().get(1)-1)))
//            return true;
//        else if(p.getRole().equals("Pilot"))
//            return true;
//        else if((t.getLocation().get(0)==p.getLocation().get(0)+1||t.getLocation().get(0)==p.getLocation().get(0)-1)||
//                (t.getLocation().get(1)==p.getLocation().get(1)+1||t.getLocation().get(1)==p.getLocation().get(1)-1))
//            return true;
//        else
//            return false;
        return false;
    }
    public boolean movePawn(Tile t, Pawn p) {
        if(checkMove(t, p)) {
            p.setLocation(t.getLocation().get(0), t.getLocation().get(1));
            return true;
        }
        return false;
    }

    public boolean shore(Tile t) {
        if(t.isSunk()==false&&t.isFlooded()==true) {
            t.shoreUp();
            return true;
        }
        return true;
    }

//    public boolean checkLose(){
//        if(dupl.get(findLoc("Fool's Landing", loc)).isSunk()){
//            return true;
//        }
//        else if(dupl.get(findLoc("Temple of the Moon", loc)).isSunk() &&
//        dupl.get(findLoc("Temple of the Sun", loc)).isSunk()){
//            return true;
//        }
//        else if(dupl.get(findLoc("Tidal Palace", loc)).isSunk() &&
//                dupl.get(findLoc("Coral Palace", loc)).isSunk()){
//            return true;
//        }
//        else if(dupl.get(findLoc("Whispering Garden", loc)).isSunk() &&
//                dupl.get(findLoc("Howling Garden", loc)).isSunk()){
//            return true;
//        }
//        else if(dupl.get(findLoc("Cave of Embers", loc)).isSunk() &&
//                dupl.get(findLoc("Cave of Shadows", loc)).isSunk()){
//            return true;
//        }
//        return false;
//    }
//
//    public int[] findLoc(String tileName, int[] location){
//        for (Map.Entry<int[], Tile> entry : dupl.entrySet()) {
//            if (entry.equals(tileName)) {
//                loc = entry.getKey();
//                return loc;
//            }
//        }
//        return loc;
//    }

    public static HashMap<Point, ArrayList<Integer>> pawnLocs(){
        HashMap<Point, ArrayList<Integer>> total = new HashMap<>();

        for(int i=0; i< pawnLoc.size();i++){
            Point p = new Point(pawnLoc.get(i).getLocation().get(0), pawnLoc.get(i).getLocation().get(1));
            if(total.containsKey(p)){
                total.get(p).add(i);
            }
            else{
                total.put(p, new ArrayList<Integer>());
                total.get(p).add(i);
            }
        }
        return total;
    }

    public static ArrayList<int[]> coords(){
        Pawn p = pawnLoc.get(turn);
        ArrayList<int[]> ret = new ArrayList<>();
        Tile temp = GameBoardGraphic.localTileLoc.get(new Point(p.getLocation().get(0), p.getLocation().get(1)+1));
        if(temp!= null && checkMove(temp, p)){
            ret.add(new int[]{p.getLocation().get(0), p.getLocation().get(1) + 1});
        }
        temp = GameBoardGraphic.localTileLoc.get(new Point(p.getLocation().get(0), p.getLocation().get(1)-1));
        if(temp!=null && checkMove(temp, p)){
            ret.add(new int[]{p.getLocation().get(0), p.getLocation().get(1)-1});
        }
        temp = GameBoardGraphic.localTileLoc.get(new Point(p.getLocation().get(0)+1, p.getLocation().get(1)));
        if(temp!=null && checkMove(temp, p)){
            ret.add(new int[]{p.getLocation().get(0)+1, p.getLocation().get(1)});
        }
        temp = GameBoardGraphic.localTileLoc.get(new Point(p.getLocation().get(0)-1, p.getLocation().get(1)-1));
        if(temp!=null && checkMove(temp, p)){
            ret.add(new int[]{p.getLocation().get(0)-1, p.getLocation().get(1)});
        }
        return ret;

    }

    public static boolean checkTrade(Pawn b) {
        Pawn temp = GameState.pawnLoc.get(turn);

        ArrayList<Integer> aLoc = temp.getLocation();
        ArrayList<Integer> bLoc = b.getLocation();

        if(aLoc.get(0)==bLoc.get(0)&&aLoc.get(1)==bLoc.get(1))
            return true;
        else if(aLoc.get(0)==bLoc.get(0)+1&&aLoc.get(1)==bLoc.get(1))
            return true;
        else if(aLoc.get(0)==bLoc.get(0)-1&&aLoc.get(1)==bLoc.get(1))
            return true;
        else if(aLoc.get(0)==bLoc.get(0)&&aLoc.get(1)==bLoc.get(1)+1)
            return true;
        else if(aLoc.get(0)==bLoc.get(0)&&aLoc.get(1)==bLoc.get(1)-1)
            return true;
        else
            return false;
    }

    public static void trade(Pawn b, Card c) {
        if(checkTrade(b)) {
            GameState.pawnLoc.get(turn).removeCard(c);
            b.addCard(c);
        }
    }

}
