package com.oldguys.nje.ticktacktoe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    // Layout variables.
    private Button[][] buttons;
    private Button buttonReset;
    private Boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int Player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private static final String player1Mark = "X";
    private static final String player2Mark = "O";
    private static final int winAmount = 5;
    private static final int tableSize = 10;
    private int[][] winPatterns = new int[][] {
        {0, 1}, {1, 0}, {1, 1}, {-1, -1}
    };
    private String lastWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set layout variables.
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        generateButtons();

        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button click: ", "Reset button clicked.");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                Log.v("Button click","Camera selected");
                break;
            case R.id.nav_gallery:
                Log.v("Button click","Gallery selected");
                break;
            case R.id.nav_slideshow:
                Log.v("Button click","SlideShow selected");
                break;
            case R.id.nav_manage:
                Log.v("Button click","Manage selected");
                break;
            case R.id.nav_share:
                Log.v("Button click","Share selected");
                break;
            case R.id.nav_send:
                Log.v("Button click","Send selected");
                break;
            default:
                Log.v("Button click", "Unhandled click event!");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void generateButtons() {

        LinearLayout parent = findViewById(R.id.buttons_parent);

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
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(linearParams);
            parent.addView(row);
            for (int j = 0; j < tableSize; j++) {
                buttons[i][j] = new Button(this);
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

        player1Turn = lastWinner.equals(player1Mark);
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText( player1Mark );
        } else {
            ((Button) v).setText( player2Mark );
        }

        player1Turn = !player1Turn;

        roundCount++;

        lastWinner = checkForWin();
        if (!lastWinner.equals("")) {
            if (lastWinner.equals(player1Mark)) {
                Toast.makeText(this, "A Játékos 1 megnyerte a meccset!",
                    Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "A Játékos 2 megnyerte a meccset!",
                    Toast.LENGTH_LONG).show();
            }

            clearPlayingField();
        }
    }

    private String checkForWin() {

        for (int i = 0; i < winPatterns.length; i++) {
            if (testingPattern(winPatterns[i][0], winPatterns[i][1], player1Mark)) {
                return player1Mark;
            } else if (testingPattern(winPatterns[i][0], winPatterns[i][1], player2Mark)) {
                return player2Mark;
            }
        }

        return "";
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
