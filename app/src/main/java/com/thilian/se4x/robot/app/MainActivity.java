package com.thilian.se4x.robot.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thilian.se4x.robot.game.AlienPlayer;
import com.thilian.se4x.robot.game.EconPhaseResult;
import com.thilian.se4x.robot.game.Game;

import java.util.List;

public class MainActivity extends SE4XActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout players = findViewById(R.id.players);

        final Game game = getGame();
        for (int i = 0; i < game.aliens.size(); i++) {
            AlienPlayer ap = game.aliens.get(i);
            PlayerView playerView = new PlayerView(this, game, ap);
            final int finalI = i;
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, FleetsActivity.class);
                    intent.putExtra(Intent.EXTRA_INDEX, finalI);
                    startActivity(intent);
                }
            });
            players.addView(playerView);
        }

        final Button econButton = (Button) findViewById(R.id.econPhase);
        econButton.setText(getString(R.string.econPhase, game.currentTurn));
        econButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<EconPhaseResult> results = game.doEconomicPhase();
                saveGame();
                if(results.size() != 0) {
                    showEconPhaseResult(results);
                    LinearLayout players = findViewById(R.id.players);
                    for (int i = 0; i < players.getChildCount(); i++) {
                        ((PlayerView) players.getChildAt(i)).update(isShowDetails());
                    }
                }
                if(game.currentTurn  <= 20)
                    econButton.setText(getString(R.string.econPhase, game.currentTurn));
                else {
                    econButton.setText(getString(R.string.gameOver));
                    econButton.setEnabled(false);
                }
            }});

        Button setSeenLevelButton = findViewById(R.id.set_seen_levels_button);
        setSeenLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SeenTechnologyActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout players = findViewById(R.id.players);
        PlayerView view;
        for(int i = 0; i < players.getChildCount(); i++){
            view = (PlayerView) players.getChildAt(i);
            view.update(isShowDetails());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.areYouSure)
            .setMessage(R.string.finish_game)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteGame();
                    MainActivity.super.onBackPressed();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            })
            .show();
    }
}
