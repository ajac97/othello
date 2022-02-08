import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OthelloTest {
    @Test
    public void flippableTwoPieces() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.BLACK;
        model.board[0][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][1].color = OthelloModel.Spot.Colors.WHITE;
        model.board[0][2].color = OthelloModel.Spot.Colors.WHITE;
        List flippable = model.flippable(3, 0);
        Assert.assertEquals(3, flippable.size());
        Assert.assertEquals(flippable.get(2), new Point(3, 0));
        Assert.assertEquals(flippable.get(0), new Point(2, 0));
        Assert.assertEquals(flippable.get(1), new Point(1, 0));
    }

    @Test
    public void flippableOnePiece() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.BLACK;
        model.board[0][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][1].color = OthelloModel.Spot.Colors.WHITE;
        List flippable = model.flippable(2, 0);
        Assert.assertEquals(2, flippable.size());
        Assert.assertEquals(flippable.get(1), new Point(2, 0));
        Assert.assertEquals(flippable.get(0), new Point(1, 0));
    }

    @Test
    public void flippableNotLegal() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.BLACK;
        //model.board[0][1].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][1].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][2].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][3].color = OthelloModel.Spot.Colors.BLACK;
        List flippable2 = model.flippable(4, 0);
        Assert.assertEquals(0, flippable2.size());
    }

    @Test
    public void noMoveAvailableTest() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.BLACK;
        model.board[3][3].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[3][4].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[4][3].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[4][4].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[0][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][1].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][2].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][3].color = OthelloModel.Spot.Colors.BLACK;
        Assert.assertTrue(model.noMoveAvailable());
    }

    @Test
    public void flipTest() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.WHITE;
        model.board[0][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][1].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][2].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][3].color = OthelloModel.Spot.Colors.BLACK;
        ArrayList<Point> toFlipTest = new ArrayList<>();
        toFlipTest.add(new Point(0, 0));
        toFlipTest.add(new Point(1, 0));
        toFlipTest.add(new Point(2, 0));
        toFlipTest.add(new Point(3, 0));
        model.flip(toFlipTest);
        Assert.assertEquals(model.board[0][0].color, OthelloModel.Spot.Colors.WHITE);
        Assert.assertEquals(model.board[0][1].color, OthelloModel.Spot.Colors.WHITE);
        Assert.assertEquals(model.board[0][2].color, OthelloModel.Spot.Colors.WHITE);
        Assert.assertEquals(model.board[0][3].color, OthelloModel.Spot.Colors.WHITE);
    }

    @Test
    public void switchPlayersTest() {
        OthelloModel model = new OthelloModel();
        model.player = OthelloModel.Spot.Colors.WHITE;
        model.switchPlayers();
        Assert.assertEquals(model.player, OthelloModel.Spot.Colors.BLACK);
    }

    @Test
    public void computerGoTest() {
        OthelloModel model = new OthelloModel();
        model.board[3][3].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[3][4].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[4][3].color = OthelloModel.Spot.Colors.EMPTY;
        model.board[4][4].color = OthelloModel.Spot.Colors.EMPTY;
        model.player = OthelloModel.Spot.Colors.WHITE;
        model.board[0][0].color = OthelloModel.Spot.Colors.WHITE;
        model.board[0][1].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][2].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][3].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][4].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][5].color = OthelloModel.Spot.Colors.BLACK;
        model.board[0][6].color = OthelloModel.Spot.Colors.BLACK;
        model.board[1][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[2][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[3][0].color = OthelloModel.Spot.Colors.BLACK;
        model.board[4][0].color = OthelloModel.Spot.Colors.BLACK;
        model.setNeighbors(0, 0);
        model.setNeighbors(1, 0);
        model.setNeighbors(2, 0);
        model.setNeighbors(3, 0);
        model.setNeighbors(4, 0);
        model.setNeighbors(5, 0);
        model.setNeighbors(6, 0);
        model.setNeighbors(0, 1);
        model.setNeighbors(0, 2);
        model.setNeighbors(0, 3);
        model.setNeighbors(0, 4);
        Assert.assertEquals(model.noMoveAvailable(), false);
        ArrayList<Point> computerTest = model.computerGo();
        for (int i = 0; i < computerTest.size(); i++) {
            System.out.println(computerTest.get(i));

        }
        Assert.assertEquals(computerTest.size(), 7);
        Assert.assertEquals(computerTest.get(0), new Point(6, 0));
        Assert.assertEquals(computerTest.get(1), new Point(5, 0));
        Assert.assertEquals(computerTest.get(2), new Point(4, 0));
        Assert.assertEquals(computerTest.get(3), new Point(3, 0));
        Assert.assertEquals(computerTest.get(4), new Point(2, 0));
        Assert.assertEquals(computerTest.get(5), new Point(1, 0));
        Assert.assertEquals(computerTest.get(6), new Point(7, 0));
    }

}
