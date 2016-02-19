package net.organiccode.knightstour;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        int x = 1;
        int y = 1;
        int[] start_pos = {0,0};
        int output = 0;

        System.out.println("This is a algorithm assignment to find a knights tour through a chessboard.\n" +
                "It is tested for up to 512x512 tiles, though this requires an increase in stack size");
        // input block
        try {
            BufferedReader input;
            // x coordinate
            System.out.print("Board width: ");
            input = new BufferedReader(new InputStreamReader(System.in));
            x = new Integer(input.readLine());
            // y coordinate
            System.out.print("Board height: ");
            input = new BufferedReader(new InputStreamReader(System.in));
            y = new Integer(input.readLine());
            // Start position
            System.out.print("Start position(e.g. 0,0): ");
            input = new BufferedReader(new InputStreamReader(System.in));
            String[] start_pos_s = input.readLine().split(",");
            for (int i =0; i< 2;i++){
                start_pos[i] = new Integer(start_pos_s[i]);
            }
            // Depth on next move sort
            System.out.print("SortDepth: ");
            input = new BufferedReader(new InputStreamReader(System.in));
            Tile.sortDepth = new Integer(input.readLine());
            // output option
            System.out.print("Output options: \n No extra output: \t\t\t\t\t0\n Show board: \t\t\t\t\t\t1\n" +
                    " Show move sequence: \t\t\t\t2\n Show board as a weighted graph.\n " +
                    "This was just a debug feature,\n but added it just for lolz: \t\t3 \n Option: ");
            input = new BufferedReader(new InputStreamReader(System.in));
            output = new Integer(input.readLine());
        } catch (Exception e){
            System.out.print("ooops, something went wrong!");
            System.exit(1);
        }

        Board board = new Board(x, y);
        long start_time = System.currentTimeMillis();

        // thread timeout
        if (output!=3) {
            Solver solver;
            outer:while(true){
                long restart_time = System.currentTimeMillis();
                solver = new Solver(new Board(x,y), start_pos);
                while(true) {

                    if (!solver.isAlive()) {
                        // solution found, break to print.
                        break outer;
                    } else if(System.currentTimeMillis()-restart_time>3000){
                        // solver timeout, kill thread, increase sortDepth, and try again
                        solver.stop();
                        Tile.sortDepth+=5;
                        break;
                    }
                }
            }

            System.out.println("---------------------------------------\nPath found!!");
            System.out.println("Time passed: "+(System.currentTimeMillis()-start_time)+"ms");
            if (output ==1 ) {
                solver.printBoard();
            } else if (output == 2){
                solver.printMoveSequence();
            }
        } else {
            board.printBoardAsWeightedGraph();
        }
    }

    /**
     * The solver class takes a board and a start position,
     * and starts solving the problem in a new thread not to block main.
     *
     */
    public static class Solver extends Thread {
        Board board;
        Tile[] backtrack;
        int[] start_pos;

        /**
         * Assigns instance variables and starts the solving thread
         *
         * @param board, board to use
         * @param start_pos, start position on board
         */
        public Solver(Board board, int[] start_pos) {
            this.board = board;
            this.start_pos = start_pos;
            backtrack = new Tile[board.board.length];
            this.setDaemon(true);
            start();
        }

        @Override
        public void run(){
            solve(1, board.board[start_pos[0] + start_pos[1] * board.getDimensions()[0]]);
        }

        /**
         * Print the board with each tile represented as their turn numbers
         */
        public void printBoard(){
            for (int i = 0; i < board.board.length; i++) {
                System.out.print(board.board[i].turn_used + "\t");
                if ((i + 1) % board.getDimensions()[0] == 0) {
                    System.out.print("\n");
                }
            }
        }

        /**
         * Print each move in sequence with "turn: (tile coordinates) "
         */
        public void printMoveSequence(){
            for (int i = 0;i<board.board.length;i++){
                System.out.print(i + ": "+backtrack[i]);
                if ((i+1)%10==0 || i == backtrack.length-1){
                    System.out.print("\n");
                } else {
                    System.out.print(", ");
                }
            }
        }

        /**
         * Solves the problem recursively ends if turn == tile amount,
         * Terminates if a path is found or there are no moves left to make.
         *
         * @param turn, turn number
         * @param current_tile, tile tu use this turn
         * @return true if path is found, else false
         */
        private boolean solve(int turn, Tile current_tile){
            current_tile.use(turn);
            if(turn == board.board.length){
                backtrack[turn-1] = current_tile;
                return true;
            }
            current_tile.sortAdjacentTiles();
            for (Tile next_tile: current_tile.getAdjacentTiles()){
                if (solve(turn + 1, next_tile)) {
                    backtrack[turn-1] = current_tile;
                    return true;
                }
            }
            current_tile.unUse();
            return false;
        }
    }
}