package trch.thedrake;

public class TroopTile extends Tile {
    private Troop drake;

    public TroopTile(TilePosition position, Troop drake) {
        super(position);
        this.drake = drake;
    }

    @Override
    public boolean acceptsTroop(Troop troop) {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public Troop troop() {
        return drake;
    }
   
}
