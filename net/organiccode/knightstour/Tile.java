package net.organiccode.knightstour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Alexander Kjeserud
 *
 * Represents a tile on the chessboard
 * Contains an array of adjacant tiles
 * And a reqursive comparator
 *
 */
public class Tile implements Comparable{

    public static int sortDepth = 0;

    private ArrayList<Tile> adjacent_tiles = new ArrayList<>();
    private boolean used = false;
    public final int[] tile_pos;
    public int turn_used = 0;
    private int usableAdjacent = 0;


    /**
     *
     * @param tile_pos, this tiles position on the chessboard
     */
    public Tile(int[] tile_pos){
        this.tile_pos = tile_pos;
    }


    public int getNumberOfAdjacentTiles(){return adjacent_tiles.size();}
    public void sortAdjacentTiles(){Collections.sort(adjacent_tiles);}

    /**
     * Add an adjacant tile to adjacent list
     * @param tile, a tile adjacent to this
     */
    public void addAdjacentTile(Tile tile){
        if(!adjacent_tiles.contains(tile)){
            adjacent_tiles.add(tile);
            usableAdjacent++;
        }
    }

    /**
     * Set this tile to used
     * @param turn, the turn this tile is used
     */
    public void use(int turn){
        used = true;
        turn_used = turn;
        for (Tile tiles :adjacent_tiles){
            tiles.usableAdjacent--;
        }
    }

    /**
     * set this tile unused
     */
    public void unUse(){
        used = false;
        turn_used=0;
        for (Tile tiles :adjacent_tiles){
            tiles.usableAdjacent++;
        }
    }

    /**
     * Se if the tile is used or not
     * @return used, state
     */
    public boolean isUsed(){
        return used;
    }


    /**
     *
     * @return list of adjacent unused tiles
     */
    public Tile[] getAdjacentTiles(){
        int i = 0;
        Tile[] ret = new Tile[adjacent_tiles.size()];
        for (Tile tile:adjacent_tiles){
            if (!tile.isUsed()){
                ret[i] = tile;
                i++;
            }

        }
        return Arrays.copyOf(ret, i);
    }

    /**
     *
     * @param i, index
     * @return return tile at position i
     */
    public Tile getNextAdjacent(int i){
        return adjacent_tiles.get(i);
    }

    /**
     * Compares to tiles on number of adjacent tiles recursively in ascending order
     *
     *
     * @param this_next_tile, tile sequence 1 to compare
     * @param other_next_tile tile sequence 2 to compare
     * @param n, recursion depth
     * @return -1, 1, 0
     */
    private int recCompare(Tile this_next_tile, Tile other_next_tile, int n){
        if(this_next_tile.usableAdjacent< other_next_tile.usableAdjacent){
            return -1;
        }else if (this_next_tile.usableAdjacent == other_next_tile.usableAdjacent){
            Tile this_next = this_next_tile.getNextAdjacent(0);
            Tile other_next = other_next_tile.getNextAdjacent(0);
            if (n<sortDepth && this_next != null && other_next != null){
                return recCompare(this_next, other_next, n+1);
            } else if (this_next.usableAdjacent != 0 && other_next.usableAdjacent != 0) {
                return 0;
            }else if (this_next.usableAdjacent != 0){
                    return -1;
            } else {
                    return 1;
                }
            }
            return 1;
        }



    @Override
    public int compareTo(Object o) {
       return recCompare(this, (Tile)o, 0);
    }

    @Override
    public String toString() {
        return "("+tile_pos[0]+", "+tile_pos[1]+")";
    }
}
