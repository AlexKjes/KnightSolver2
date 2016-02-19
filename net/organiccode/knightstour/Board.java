package net.organiccode.knightstour;

/**
 * @author Alexander Kjeserud
 *
 * Creates a chessboard and fills it with tiles.
 * Then maps up all tiles adjacent to eachother.
 *
 */
public class Board {


    public final Tile[] board;
    private int[] dimensions;

    /**
     *
     * @return board dimensions
     */
    public int[] getDimensions(){
        return dimensions;
    }

    public Board(int x, int y){

         dimensions = new int[]{x,y};
         board = new Tile[x*y];
         for (int i = 0; i < board.length;i++){
             board[i] = new Tile(new int[]{i%x,i/x});
         }

        for (Tile tile:board){
            for(int[] move:Knight.moves){
                int[] next_move = {tile.tile_pos[0]+move[0],tile.tile_pos[1]+move[1]};
                if(0<=next_move[0] && next_move[0] < x &&
                        0<=next_move[1] && next_move[1] < y){
                    tile.addAdjacentTile(board[(next_move[0]+(next_move[1]*x))]);
                }
            }
        }
    }

    public void printBoardAsWeightedGraph(){
        for (int i = 0; i< board.length;i++){
            System.out.print(board[i].getNumberOfAdjacentTiles());
            if ((i+1)% dimensions[0]==0){
                System.out.print("\n");
            }
        }
    }
}
