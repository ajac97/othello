import java.awt.*;
import java.util.ArrayList;

public class OthelloModel {
    final int SIZE;
    Spot[][] board;
    private ArrayList<Point> noPiecesToFlip = new ArrayList<>();
    public boolean otherPlayerHasNoMove = false;

    Spot.Colors player = Spot.Colors.WHITE;

    static class Spot {
        boolean hasNeighbors;

        enum Colors {
            EMPTY, WHITE, BLACK;

            public String toIcon() {
                if (this == Colors.WHITE) return "\u25CB";
                if (this == Colors.BLACK) return "\u2B24";
                return "";
            }
        }

        Colors color;

        public Spot() {
            hasNeighbors = false;
            color = Colors.EMPTY;
        }
    }

    public OthelloModel() {
        this.SIZE = 8;
        board = new Spot[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Spot();
            }
        }
        board[3][3].color = Spot.Colors.WHITE;
        board[3][4].color = Spot.Colors.BLACK;
        board[4][3].color = Spot.Colors.BLACK;
        board[4][4].color = Spot.Colors.WHITE;
        setNeighbors(3, 3);
        setNeighbors(4, 3);
        setNeighbors(3, 4);
        setNeighbors(4, 4);

    }

    public OthelloModel(int size) {
        this.SIZE = size;
        board = new Spot[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Spot();
            }
        }
        board[(SIZE / 2) - 1][(SIZE / 2) - 1].color = Spot.Colors.WHITE;
        board[(SIZE / 2) - 1][(SIZE / 2)].color = Spot.Colors.BLACK;
        board[(SIZE / 2)][(SIZE / 2) - 1].color = Spot.Colors.BLACK;
        board[(SIZE / 2)][(SIZE / 2)].color = Spot.Colors.WHITE;
        setNeighbors((SIZE / 2) - 1, (SIZE / 2) - 1);
        setNeighbors((SIZE / 2), (SIZE / 2));
        setNeighbors((SIZE / 2), (SIZE / 2) - 1);
        setNeighbors((SIZE / 2), (SIZE / 2));

    }

    void switchPlayers() {
        if (player == Spot.Colors.WHITE) player = Spot.Colors.BLACK;
        else player = Spot.Colors.WHITE;
    }

    private boolean isSpaceUnoccupied(int x, int y) {
        return board[y][x].color.equals(Spot.Colors.EMPTY);
    }

    private Spot.Colors getCurrentPlayer() {
        return player;
    }

    private Spot.Colors getOtherPlayer() {
        if (player == Spot.Colors.WHITE) return Spot.Colors.BLACK;
        else return Spot.Colors.WHITE;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    ArrayList<Point> flippable(int x, int y) {
        ArrayList<Point> flippable = new ArrayList<>();
        ArrayList<Point> potentials;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                if ((isInBounds(x + j, y + i)) && !(i == 0 && j == 0))
                    if ((board[y + i][x + j].color.equals(getOtherPlayer()))) {
                        potentials = new ArrayList<>();
                        potentials.add(new Point(x + j, y + i));
                        int multiplier = 2;
                        while (isInBounds(x + (j * multiplier), y + (i * multiplier))) {
                            if (isSpaceUnoccupied(x + (j * multiplier), y + (i * multiplier))) {
                                break;
                            } else if (board[y + (i * multiplier)][x + (j * multiplier)].color.equals(getCurrentPlayer())) {
                                flippable.addAll(potentials);
                                break;
                            } else potentials.add(new Point(x + (j * multiplier), y + (i * multiplier)));
                            multiplier++;
                        }
                    }
            }
        if (flippable.size() > 0) {
            flippable.add(new Point(x, y));
            return flippable;
        }
        return noPiecesToFlip;
    }

    void flip(ArrayList<Point> toFlip) {
        for (Point p : toFlip) {
            board[p.y][p.x].color = getCurrentPlayer();
        }
    }

    void setNeighbors(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isInBounds(x + j, y + i)) {
                    board[y + i][x + j].hasNeighbors = true;
                }
            }
        }

    }

    public ArrayList<Point> computerGo() {
        if (noMoveAvailable()) {
            otherPlayerHasNoMove = true;
            switchPlayers();
            return noPiecesToFlip;
        }
        otherPlayerHasNoMove = false;
        ArrayList<Point> current;
        int xIndex = -2;
        int yIndex = -2;
        ArrayList<Point> max = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isSpaceUnoccupied(j, i) && board[i][j].hasNeighbors) {
                    current = flippable(j, i);
                    if (current.size() > max.size()) {
                        max = current;
                        xIndex = j;
                        yIndex = i;
                    }
                }
            }
        }
        flip(max);
        switchPlayers();
        setNeighbors(xIndex, yIndex);
        return max;
    }

    public ArrayList<Point> makeMove(int x, int y) {
        if (noMoveAvailable()) {
            otherPlayerHasNoMove= true;
            switchPlayers();
            return noPiecesToFlip;
        }
        otherPlayerHasNoMove = false;
        ArrayList<Point> toFlip;
        if (!board[y][x].hasNeighbors) return noPiecesToFlip;
        if (!isSpaceUnoccupied(x, y)) return noPiecesToFlip;
        toFlip = flippable(x, y);
        if (toFlip.size() > 0) {
            flip(toFlip);
            switchPlayers();
            setNeighbors(x, y);
            return toFlip;
        }
        return noPiecesToFlip;
    }


    public String getColor(int x, int y) {
        return board[y][x].color.toIcon();
    }

    boolean noMoveAvailable() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].hasNeighbors) {
                    if (flippable(j, i).size() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int[] getScore() {
        int[] score = new int[2];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].color.equals(Spot.Colors.WHITE)) score[0]++;
                if (board[i][j].color.equals(Spot.Colors.BLACK)) score[1]++;
            }
        }
        return score;
    }

    public void restart() {
        player = Spot.Colors.WHITE;
        board = new Spot[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Spot();
            }
        }
        board[(SIZE / 2) - 1][(SIZE / 2) - 1].color = Spot.Colors.WHITE;
        board[(SIZE / 2) - 1][(SIZE / 2)].color = Spot.Colors.BLACK;
        board[(SIZE / 2)][(SIZE / 2) - 1].color = Spot.Colors.BLACK;
        board[(SIZE / 2)][(SIZE / 2)].color = Spot.Colors.WHITE;
        setNeighbors((SIZE / 2) - 1, (SIZE / 2) - 1);
        setNeighbors((SIZE / 2), (SIZE / 2));
        setNeighbors((SIZE / 2), (SIZE / 2) - 1);
        setNeighbors((SIZE / 2), (SIZE / 2));
    }
}


