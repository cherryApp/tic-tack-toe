package com.oldguys.nje.ticktacktoe;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends AppCompatActivity implements View.OnClickListener {

    // Class context.
    private Context context;

    // Layout variables.
    private Button[][] buttons;
    private Button buttonReset;
    private Boolean player1Turn = true;
    private int player1Points = 0;
    private int player2Points = 0;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private static final String player1Mark = "X";
    private static final String player2Mark = "O";
    private static final int winAmount = 5;
    private static final int tableSize = 15;
    private int[][] winPatterns = new int[][] {
            {0, 1}, {1, 0}, {1, 1}, {1, -1}
    };
    private String lastWinner = "";
    private TicTacToeAI AI;
    private boolean againstToComputer = false;
    private String player2Name = "Játékos 2";

    public Game(Context context) {
        this.context = context;

        AI = new TicTacToeAI(tableSize, player1Mark);

        // Set layout variables.
        textViewPlayer1 = ((Activity) context).findViewById(R.id.text_view_p1);
        textViewPlayer2 = ((Activity) context).findViewById(R.id.text_view_p2);

        generateButtons();

        buttonReset = ((Activity) context).findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPlayingField();
            }
        });
    }

    public void toggleGameMode(boolean mode) {
        againstToComputer = mode;
        player2Name = againstToComputer ? "Computer" : "Játékos 2";
        player1Points = 0;
        player2Points = 0;
        clearPlayingField();
        showPlayerPoints();
    }

    private void generateButtons() {

        LinearLayout parent = ((Activity) context).findViewById(R.id.buttons_parent);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0
        );
        linearParams.weight = 1;
        linearParams.setMargins(2, -8, 2, -8);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT
        );
        buttonParams.weight = 1;
        buttonParams.setMargins(-5, -2, -5, 0);

        buttons = new Button[tableSize][tableSize];
        for (int i = 0; i < tableSize; i++) {
            LinearLayout row = new LinearLayout(context);
            row.setLayoutParams(linearParams);
            parent.addView(row);
            for (int j = 0; j < tableSize; j++) {
                buttons[i][j] = new Button(context);
                buttons[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 12.0);
                row.addView(buttons[i][j], buttonParams);
                buttons[i][j].setOnClickListener(this);
            }
        }

    }

    private void clearPlayingField() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
            }
        }

        player1Turn = !lastWinner.equals(player1Mark);
        if (againstToComputer) {
            player1Turn = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText( player1Mark );
            if (againstToComputer) {
                int[] pos = AI.jump(buttons);
                buttons[pos[0]][pos[1]].setText( player2Mark );
            } else {
                player1Turn = !player1Turn;
            }
        } else {
            ((Button) v).setText( player2Mark );
            player1Turn = !player1Turn;
        }

        lastWinner = checkForWin();
        if (!lastWinner.equals("")) {
            if (lastWinner.equals(player1Mark)) {
                player1Points++;
                Toast.makeText(context, "A Játékos 1 megnyerte a meccset!",
                        Toast.LENGTH_LONG).show();
            } else {
                player2Points++;
                Toast.makeText(context, player2Name +" megnyerte a meccset!",
                        Toast.LENGTH_LONG).show();
            }

            clearPlayingField();
            showPlayerPoints();
        }
    }

    private void showPlayerPoints() {
        textViewPlayer1.setText( "Játékos 1: " + player1Points );
        textViewPlayer2.setText( player2Name + ": " + player2Points );
    }

    private boolean checkField(int x, int y, int[] dir, String mark) {
        int sum = 0,
            xx = 0,
            yy = 0;

        for (int i = 0; i < winAmount; i++) {
            if (xx > tableSize-1 || yy > tableSize-1) {
                return false;
            }
            if (buttons[xx][yy].getText().toString() == mark) {
                sum++;
            } else {
                break;
            }
            xx += dir[0];
            yy += dir[1];
        }
        if (sum == winAmount) {
            return true;
        }

        return false;
    }

    private boolean isWinningPosition(int x, int y, String mark) {
        int sum = 0;
        // Top to bottom.
        if (checkField(x, y, new int[]{1,0}, mark)) {
            return true;
        }

        return false;
    }

    private String checkForWin() {
        for (int x = 0; x < tableSize; x++) {
            for (int y = 0; y < tableSize; y++) {
                String mark = buttons[x][y].getText().toString();
                if (mark.equals("")) {
                    continue;
                }
                if (isWinningPosition(x, y, mark)) {
                    return mark;
                }
            }
        }

        return "";
        /*
        for (int i = 0; i < winPatterns.length; i++) {
            if (testingPattern(winPatterns[i][0], winPatterns[i][1], player1Mark)) {
                return player1Mark;
            } else if (testingPattern(winPatterns[i][0], winPatterns[i][1], player2Mark)) {
                return player2Mark;
            }
        }

        return "";
        */
    }

    private boolean testingPattern(int increaseX, int increaseY, String player) {
        int lastRow = 0;
        int lastCol = 0;
        int y = increaseY > -1 ? 0 : tableSize - 1;

        int x = increaseX > -1 ? 0 : tableSize - 1;
        while (lastRow < tableSize) {
            x = increaseX > -1 ? (0 + lastRow) : tableSize - 1;
            if (checkRect(x, y, increaseX, increaseY, player)) {
                return true;
            }
            lastRow++;
        }

        x = increaseX > -1 ? 0 : tableSize - 1;
        while (lastCol < tableSize) {
            y = increaseY > -1 ? (0 + lastCol) : tableSize - 1;
            if (checkRect(x, y, increaseX, increaseY, player)) {
                return true;
            }
            lastCol++;
        }

        return false;
    }

    private boolean checkRect(int x, int y, int increaseX, int increaseY, String player) {
        Boolean running = true;
        int playerCount = 0;

        while (running) {
            String current = (String) buttons[x][y].getText();
            Log.d("Pos: ", x + " " + y);
            if (current.equals("")) {
                playerCount = 0;
            } else {
                if (current.equals(player)) {
                    playerCount++;
                } else {
                    playerCount = 0;
                }
            }

            if (playerCount == winAmount) {
                return true;
            }

            x += increaseX;
            y += increaseY;

            if ( x >= buttons.length || y >= buttons[0].length || x < 0 || y < 0 ) running = false;
        }

        return false;
    }
}
