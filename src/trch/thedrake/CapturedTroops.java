/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trch.thedrake;

import java.util.Collections;
import static java.util.Collections.singletonList;
import java.util.LinkedList;
import java.util.List;

public class CapturedTroops {

    //ATRIBUTY
    private List<TroopInfo> CapturedTroopsBLUE, CapturedTroopsORANGE;

    // Konstruktor vytvářející prázdné seznamy
    public CapturedTroops() {
        CapturedTroopsBLUE = new LinkedList<>();
        CapturedTroopsORANGE = new LinkedList<>();
    }
    
    public CapturedTroops(List<TroopInfo> blue, List<TroopInfo> orange) {
        CapturedTroopsBLUE = new LinkedList<>(blue);
        CapturedTroopsORANGE = new LinkedList<>(orange);
    }

// Vrací seznam zajatých jednotek pro daného hráče
    public List<TroopInfo> troops(PlayingSide side) {
        if (side.equals(PlayingSide.BLUE)) {
            return Collections.unmodifiableList(new LinkedList<>(CapturedTroopsBLUE)); // možno inak
        } else {
            return Collections.unmodifiableList(new LinkedList<>(CapturedTroopsORANGE));
        }
    }

// Přidává nově zajatou jednotku na začátek seznamu zajatých jednotek daného hráče.
    public CapturedTroops withTroop(PlayingSide side, TroopInfo info) {
        CapturedTroops tmp = new CapturedTroops(this.CapturedTroopsBLUE, this.CapturedTroopsORANGE);
        
        if (side.equals(PlayingSide.BLUE)) {
            tmp.CapturedTroopsBLUE.add(0, info);
            return tmp;
        } else {
            tmp.CapturedTroopsORANGE.add(0, info);
            return tmp;
        }

        //addFirst(E e)
    }
    

}
