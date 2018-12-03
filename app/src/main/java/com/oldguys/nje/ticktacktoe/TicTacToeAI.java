package com.oldguys.nje.ticktacktoe;

import android.widget.Button;

public class TicTacToeAI {

    private int tableSize;
    private int winAmount;
    String computerPlayer;
    String opponent;

    private String[][] table;

    public TicTacToeAI(int tableSize, int winAmount, String computerPlayer, String opponent) {
        this.tableSize = tableSize;
        this.winAmount = winAmount;
        this.computerPlayer = computerPlayer;
        this.opponent = opponent;

        table = new String[tableSize][tableSize];
    }

    private void initTable(Button[][] buttons)
    {
        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                table[i][j] = buttons[i][j].getText().toString();
            }
        }
    }

    private boolean checkField(int x, int y, int dirX, int dirY, String mark, int amount)
    {
        int sum = 0;

        for (int i = 0; i < amount; i++) {
            if (x < 0 || y < 0 || x >= tableSize || y >= tableSize) {
                return false;
            }

            if (table[x][y].equals(mark)) {
                sum++;
            }
            else {
                return false;
            }

            x += dirX;
            y += dirY;
        }

        return (sum == amount);
    }

    private boolean checkTable(String mark, int amount) {

        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                String m = table[i][j];

                if (m.equals(mark)) {
                    if (checkField(i, j, 1, 0, mark, amount)) return true;
                    if (checkField(i, j, 0, 1, mark, amount)) return true;
                    if (checkField(i, j, 1, 1, mark, amount)) return true;
                    if (checkField(i, j, 1, -1, mark, amount)) return true;
                }
            }
        }

        return false;
    }

    private TablePosition searchNextPosition(String mark, int amount)
    {
        for (int x = 0; x < tableSize; x++) {
            for (int y = 0; y < tableSize; y++) {
                if (!table[x][y].equals("")) continue;

                table[x][y] = mark;

                if (checkTable(mark, amount)) {
                    return new TablePosition(x, y);
                }

                table[x][y] = "";
            }
        }

        return null;
    }

    private TablePosition jumpRandom() {

        int x = 0;
        int y = 0;

        do {
            x = (int)(Math.random() * tableSize);
            y = (int)(Math.random() * tableSize);
        } while (!table[x][y].equals(""));

        return new TablePosition(x, y);
    }

    public TablePosition jump(Button[][] buttons) {

        initTable(buttons);

        TablePosition tp = null;

        tp = searchNextPosition(computerPlayer, winAmount);
        if (tp != null) {
            return tp;
        }

        for (int i = 0; i <= 2; i++) {
            tp = searchNextPosition(opponent, winAmount - i);
            if (tp != null) {
                return tp;
            }
        }

        tp = searchNextPosition(computerPlayer, winAmount - 1);
        if (tp != null) {
            return tp;
        }

        return jumpRandom();
    }
}