package com.oldguys.nje.ticktacktoe;

import android.widget.Button;

public class TicTacToeAI {

    private int tableSize;
    String opponent;
    private int[][] winPatterns = new int[][] {
        {0, 1}, {1, 0}, {1, 1}, {1, -1}
    };
    private int tryCount = 0;

    public TicTacToeAI(int tableSize, String opponent) {
        this.tableSize = tableSize;
        this.opponent = opponent;
    }

    public int[] jump(Button[][] buttons) {
        int x = (int ) (Math.random() * tableSize);
        int y = (int ) (Math.random() * tableSize);

        if (tryCount > 10) {
            tryCount = 0;
            return new int[]{x, y};
        }
        tryCount++;

        int direction = (int ) (Math.random() * 4);
        int[] best = new int[]{0, 0, 0};

        for (int i = 0; i < winPatterns.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                for (int k = 0; k < buttons[j].length; k++) {
                    if (buttons[j][k].getText().toString().equals(opponent)) {
                        switch (direction) {
                            case 0:
                                best = new int[]{j-1, k, 0};
                                break;
                            case 1:
                                best = new int[]{j, k+1, 0};
                                break;
                            case 2:
                                best = new int[]{j+1, k, 0};
                                break;
                            case 3:
                                best = new int[]{j, k-1, 0};
                                break;
                            default:
                                best = new int[]{x, y, 0};
                        }
                    }
                }
            }
        }

        x = best[0];
        y = best[1];

        if (x > tableSize-1 || x < 0 || y > tableSize-1 || y < 0) {
            return jump(buttons);
        }

        if (!buttons[x][y].getText().toString().equals("")) {
            return jump(buttons);
        }

        return new int[]{x, y};
    }
}
