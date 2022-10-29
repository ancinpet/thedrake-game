package trch.thedrake;

public abstract class Tile {
    protected TilePosition position;
    
    // Konstruktor, který očekává pozici dlaždice na hracím plánu
    protected Tile(TilePosition position) {
        this.position = position;
    }

    // Pozice dlaždice na hracím plánu
    public TilePosition position() {
        return position;        
    }

    // Je možné na dlaždici postavit zadanou jednotku?
    public abstract boolean acceptsTroop(Troop troop);

    // Stojí na dlaždici nějaká jednotka?
    public abstract boolean hasTroop();

    // Jednotka, která na dlaždici zrovna stojí. Pokud tam žádná není, vyhazuje UnsupportedOperationException
    public abstract Troop troop();
}
