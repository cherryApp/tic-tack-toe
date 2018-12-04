package com.oldguys.nje.ticktacktoe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
    private static final int tableSize = 10;

    private String lastWinner = "";
    private TicTacToeAI AI;
    private boolean againstToComputer = false;
    private String player2Name = "Játékos 2";

    public Game(Context context) {
        this.context = context;

        AI = new TicTacToeAI(tableSize, winAmount, player2Mark, player1Mark);

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

    private void setMark(Button button, String mark)
    {
        if (mark.equals("X")) {
            button.setTextColor(Color.BLUE);
        }
        else {
            button.setTextColor(Color.RED);
        }

        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setText(mark);
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().equals("")) {
            return;
        }

        if (player1Turn) {
            setMark((Button)v, player1Mark);

            if (checkForWin().equals("")) {
                if (againstToComputer) {
                    TablePosition pos = AI.jump(buttons);
                    setMark(buttons[pos.X][pos.Y], player2Mark);
                } else {
                    player1Turn = !player1Turn;
                }
            }
        } else {
            setMark((Button)v, player2Mark);
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

    private boolean checkFieldForWin(int x, int y, int dirX, int dirY, String mark)
    {
        int sum = 0;

        for (int i = 0; i < winAmount; i++)
        {
            if (x < 0 || y < 0 || x >= tableSize || y >= tableSize)
            {
                return false;
            }

            if (buttons[x][y].getText().equals(mark))
            {
                sum++;
            }
            else
            {
                return false;
            }

            x += dirX;
            y += dirY;
        }

        return (sum == winAmount);
    }

    private String checkForWin() {

        for (int i = 0; i < tableSize; i++)
        {
            for (int j = 0; j < tableSize; j++)
            {
                String mark = buttons[i][j].getText().toString();

                if (!mark.equals(""))
                {
                    if (checkFieldForWin(i, j, 1, 0, mark)) return mark;
                    if (checkFieldForWin(i, j, 0, 1, mark)) return mark;
                    if (checkFieldForWin(i, j, 1, 1, mark)) return mark;
                    if (checkFieldForWin(i, j, 1, -1, mark)) return mark;
                }
            }
        }

        return "";
    }
}