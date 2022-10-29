package trch.thedrake;

import static java.lang.Math.abs;

public class Board {

    private final int dimension;
    private final Tile board[][];
    private  CapturedTroops troopsOut;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru se specefikovanými dlaždicemi.
    // Všechny ostatní dlažice se berou jako prázdné.
    public Board(int dimension, Tile... tiles) {
        this.dimension = dimension;
        board = new Tile[dimension][dimension];
        troopsOut = new CapturedTroops();

        for (int i = 0; i < tiles.length; ++i) {
            board[tiles[i].position.i][tiles[i].position.j] = tiles[i];
        }

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] == null) {
                    board[i][j] = new EmptyTile(new TilePosition(i, j));
                }
            }
        }
    }

    
    // Kostruktor s troopami
    
       public Board(int dimension, CapturedTroops captured ,Tile... tiles) {
        this.dimension = dimension;
        this.troopsOut = new CapturedTroops(captured.troops(PlayingSide.BLUE), captured.troops(PlayingSide.ORANGE));
        board = new Tile[dimension][dimension];

        for (int i = 0; i < tiles.length; ++i) {
            board[tiles[i].position.i][tiles[i].position.j] = tiles[i];
        }

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] == null) {
                    board[i][j] = new EmptyTile(new TilePosition(i, j));
                }
            }
        }
    }
    
    
    
    boolean legalPosition(TilePosition position) {
        return position.i >= 0 && position.i < dimension && position.j >= 0 && position.j < dimension;
    }

    // Rozměr hrací desky
    public int dimension() {
        return dimension;
    }

    // Vrací dlaždici na zvolené pozici. Pokud je pozice mimo desku, vyhazuje IllegalArgumentException
    public Tile tileAt(TilePosition position) {
        if (!legalPosition(position)) {
            throw new IllegalArgumentException("Board,tileAt: i:" + position.i + ",j: " + position.j + ", dim:" + dimension);
        }

        return board[position.i][position.j];
    }

    // Ověřuje, že pozice se nachází na hrací desce
    public boolean contains(TilePosition... positions) {
        for (int i = 0; i < positions.length; ++i) {
            if (!legalPosition(positions[i])) {
                return false;
            }
        }

        return true;
    }

    // Vytváří novou hrací desku s novými dlaždicemi z pole tiles. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(Tile... tiles) {
        Tile tmp[][] = new Tile[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                    tmp[i][j] = board[i][j];
            }
        }

        for (Tile tile : tiles) {
            tmp[tile.position.i][tile.position.j] = tile;
        }

        Tile tmpTwo[] = new Tile[dimension * dimension];

        for (int i = 0; i < dimension * dimension; ++i) {
            tmpTwo[i] = tmp[i / dimension][i % dimension];
        }

        return new Board(dimension, troopsOut, tmpTwo);
    }

    // která umožní vytvořit novou desku nejen s novými dlaždicemi ale i s novou zajatou jednotkou pro konkrétního hráče.
    public Board withCaptureAndTiles(TroopInfo info, PlayingSide side, Tile... tiles) {
       
        
        Tile tmp[][] = new Tile[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                    tmp[i][j] = board[i][j];
            }
        }

        for (Tile tile : tiles) {
            tmp[tile.position.i][tile.position.j] = tile;
        }

        Tile tmpTwo[] = new Tile[dimension * dimension];

        for (int i = 0; i < dimension * dimension; ++i) {
            tmpTwo[i] = tmp[i / dimension][i % dimension];
        }
        
       
        
        CapturedTroops troopsTMP = new CapturedTroops(troopsOut.troops(PlayingSide.BLUE), troopsOut.troops(PlayingSide.ORANGE));
        troopsTMP.withTroop(side, info);

        
        return new Board(dimension, troopsTMP ,tmpTwo);
    }

    // Vrací zajaté jednotky
    public CapturedTroops captured() {
        return troopsOut;
    }

// Stojí na pozici origin jednotka?
    public boolean canTakeFrom(TilePosition origin) {

        return contains(origin) && tileAt(origin).hasTroop();
    }

    /*
 * Lze na danou pozici postavit zadanou jednotku? Zde se řeší pouze
 * jednotky na hrací ploše, nikoliv zásobník, takže se v podstatě
 * pouze ptám, zda dlaždice na pozici target přijme danou jednotku.
     */
    public boolean canPlaceTo(Troop troop, TilePosition target) {
        return contains(target) && tileAt(target).acceptsTroop(troop);
    }

// Může zadaná jednotka zajmout na pozici target soupeřovu jednotku?
    public boolean canCaptureOn(Troop troop, TilePosition target) {
        return contains(target) && !tileAt(target).troop().face().equals(troop.face());
    }

    ;



/*
 * Stojí na políčku origin jednotka, která může udělat krok na pozici target
 * bez toho, aby tam zajala soupeřovu jednotku?
 */
public boolean canStepOnly(TilePosition origin, TilePosition target) {
        return contains(target) && contains(origin) && tileAt(target).acceptsTroop(tileAt(origin).troop());
    }

    /*
 * Stojí na políčku origin jednotka, která může zůstat na pozici origin
 * a zajmout soupeřovu jednotku na pozici target?
     */
    public boolean canCaptureOnly(TilePosition origin, TilePosition target) {
        return contains(target) && contains(origin) && !tileAt(target).troop().face().equals(tileAt(origin).troop().face());
    }

    /*
 * Stojí na pozici origin jednotka, která může udělat krok na pozici target
 * a zajmout tam soupeřovu jednotku?
     */
    public boolean canStepAndCapture(TilePosition origin, TilePosition target) {
        return contains(target) && contains(origin) && isRoadEmpty(origin, target) && !tileAt(target).troop().face().equals(tileAt(origin).troop().face());
    }

    /*
 * Nová hrací deska, ve které jednotka na pozici origin se přesunula
 * na pozici target bez toho, aby zajala soupeřovu jednotku.
     */
    public Board stepOnly(TilePosition origin, TilePosition target) {

        Troop attacker = tileAt(origin).troop();
        
        return withTiles(new EmptyTile(origin), new TroopTile(target, attacker.flipped()) );

    }

    /*
 * Nová hrací deska, ve které jednotka na pozici origin se přesunula
 * na pozici target, kde zajala soupeřovu jednotku.
     */
    public Board stepAndCapture(TilePosition origin, TilePosition target) {
        Troop attacker = tileAt(origin).troop();
        Troop targetTroop = tileAt(target).troop();

        return withCaptureAndTiles(
                targetTroop.info(),
                targetTroop.side(),
                new EmptyTile(origin),
                new TroopTile(target, attacker.flipped()));

    }

    /*
 * Nová hrací deska, ve které jednotka zůstává stát na pozici origin
 * a zajme soupeřovu jednotku na pozici target.
     */
    public Board captureOnly(TilePosition origin, TilePosition target) {
        Troop attacker = tileAt(origin).troop();
        Troop targetTroop = tileAt(target).troop();

        return withCaptureAndTiles(
                targetTroop.info(),
                targetTroop.side(),
                new EmptyTile(target));
    }

    
    
    
    
    public boolean isRoadEmpty(TilePosition origin, TilePosition target) {

        // VPRAVO a LAVO
        if (origin.i == target.i) {
            if (origin.j < target.j) {
                for (int i = 1; i < abs(origin.j - target.j); ++i) {
                    if (board[origin.i][origin.j + i] != null) {
                        return false;
                    }
                }

            } else {
                for (int i = 1; i < abs(origin.i - target.i); ++i) {
                    if (board[origin.i][origin.j - i] != null) {
                        return false;
                    }
                }
            }
        }

        // HORE A DOLE
        if (origin.j == target.j) {
            if (origin.i < target.i) {
                for (int i = 1; i < abs(origin.i - target.i); ++i) {
                    if (board[origin.i + i][origin.j] != null) {
                        return false;
                    }
                }

            } else {
                for (int i = 1; i < abs(origin.i - target.i); ++i) {
                    if (board[origin.i - i][origin.j] != null) {
                        return false;
                    }
                }
            }
        }

        // DIAGONALNE
        if ((origin.j != target.j) && (origin.i != target.i)) {
            for (int i = 0, j = 0;
                    abs(i) + 1 < abs(origin.i - target.i) && abs(j) + 1 < abs(origin.j - target.j);) {

                if ((origin.i - target.i) > 0) {
                    ++i;
                } else {
                    --i;
                }

                if ((origin.j - target.j) > 0) {
                    ++j;
                } else {
                    --j;
                }

                if (board[target.i + i][target.j + j] != null) {
                    return false;
                }

            }

        }

        return true;
    }

}
// git try
