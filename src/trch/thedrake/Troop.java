package trch.thedrake;

public class Troop {
    private final TroopInfo info;
    private final PlayingSide side;
    private final TroopFace face;
    
    public Troop(TroopInfo info, PlayingSide side, TroopFace face) {
        this.info = info;
        this.side = side;
        this.face = face;
    }
    
    public TroopInfo info() {
        return info;
    }
        
    public PlayingSide side() {
        return side;
    }

    public TroopFace face() {
        return face;
    }

    public Offset2D pivot() {
        return info.pivot(face);
    }

    public Troop flipped() {
        return new Troop(info, side, face.flip());
    }    
}
