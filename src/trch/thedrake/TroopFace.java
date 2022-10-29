package trch.thedrake;

public enum TroopFace {
    FRONT, BACK;
    
    public TroopFace flip() {
        return (this.equals(TroopFace.FRONT) ? TroopFace.BACK : TroopFace.FRONT);
    }
}
