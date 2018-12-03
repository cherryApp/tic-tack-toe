package com.oldguys.nje.ticktacktoe;

import android.widget.Button;

public class TicTacToeAI {

    private int tableSize;
    private int winAmount;
    String opponent;

    private String[][] table;

    public TicTacToeAI(int tableSize, int winAmount, String opponent) {
        this.tableSize = tableSize;
        this.winAmount = winAmount;
        this.opponent = opponent;

        table = new String[tableSize][tableSize];
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

            if (table[x][y].equals(mark))
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
                String mark = table[i][j];

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

    private TablePosition jumpRandom(Button[][] buttons) {

        int x = 0;
        int y = 0;

        do
        {
            x = (int)(Math.random() * tableSize);
            y = (int)(Math.random() * tableSize);
        } while (!buttons[x][y].getText().equals(""));

        return new TablePosition(x, y);
    }

    public TablePosition jump(Button[][] buttons) {

        for (int i = 0; i < tableSize; i++)
        {
            for (int j = 0; j < tableSize; j++)
            {
                table[i][j] = buttons[i][j].getText().toString();
            }
        }

        return jumpRandom(buttons);
    }
}