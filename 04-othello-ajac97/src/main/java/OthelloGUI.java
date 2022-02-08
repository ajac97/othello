import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OthelloGUI extends JFrame {
    private OthelloModel model;
    private JButton[][] buttons;
    private JLabel score = new JLabel();
    private JPanel statusBar = new JPanel();
    private JButton restartButton = new JButton("Restart Game");

    public OthelloGUI(OthelloModel model) {
        this.model = model;
        buttons = new JButton[model.SIZE][model.SIZE];
        setTitle("Othello");
        setSize(600, 620);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(model.SIZE, model.SIZE));
        add(grid, BorderLayout.CENTER);
        statusBar.setSize(600, 40);
        statusBar.add(score);
        restartButton.addActionListener(actionEvent -> {
            model.restart();
            resetBoard();
        });
        statusBar.add(restartButton);
        add(statusBar, BorderLayout.NORTH);
        ActionListener al = new ButtonListener();
        Font f = new Font("", Font.BOLD, 40);
        for (int i = 0; i < model.SIZE; i++) {
            for (int j = 0; j < model.SIZE; j++) {
                grid.add(buttons[i][j] = new MyJButton(new Point(j, i)));
                buttons[i][j].addActionListener(al);
                buttons[i][j].setFont(f);
                buttons[i][j].setBackground(new Color(0, 140, 0));
            }

        }
        ArrayList<Point> startPieces = new ArrayList<>();
        startPieces.add(new Point(model.SIZE / 2 - 1, model.SIZE / 2 - 1));
        startPieces.add(new Point(model.SIZE / 2 - 1, model.SIZE / 2));
        startPieces.add(new Point(model.SIZE / 2, model.SIZE / 2 - 1));
        startPieces.add(new Point(model.SIZE / 2, model.SIZE / 2));
        setBoard(startPieces);
        setVisible(true);
    }

    private void setBoard(ArrayList<Point> toFlip) {
        for (Point p : toFlip) {
            buttons[p.y][p.x].setText(model.getColor(p.x, p.y));
        }

    }

    private class ButtonListener implements ActionListener {
        int num = 0;

        public ButtonListener() {


        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            MyJButton b = (MyJButton) actionEvent.getSource();
            Point pushed = b.p;
            ArrayList<Point> flipped = model.makeMove(pushed.x, pushed.y);
            for (Point piece : flipped) {
                buttons[piece.y][piece.x].setText(model.getColor(piece.x, piece.y));
            }
            if (flipped.size() > 0 || model.otherPlayerHasNoMove) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        for (Point piece : model.computerGo()) {
                            buttons[piece.y][piece.x].setText(model.getColor(piece.x, piece.y));
                        }
                        updateScore();

                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                });
            }
            updateScore();
        }


    }

    private static class MyJButton extends JButton {
        Point p;

        MyJButton(Point p) {
            this.p = p;
        }
    }

    private void updateScore() {
        score.setText(String.format("%s : %d", OthelloModel.Spot.Colors.WHITE,
                model.getScore()[0]) + String.format(" %s : %d", OthelloModel.Spot.Colors.BLACK, model.getScore()[1]));
    }


    public static void main(String[] args) {
        new OthelloGUI(new OthelloModel());
    }

    void resetBoard() {
        for (int i = 0; i < model.SIZE; i++) {
            for (int j = 0; j < model.SIZE; j++) {
                buttons[i][j].setText(model.getColor(j, i));
            }

        }
    }
}

