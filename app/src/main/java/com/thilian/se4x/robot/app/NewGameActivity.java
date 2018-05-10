package com.thilian.se4x.robot.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thilian.se4x.robot.game.Game;
import com.thilian.se4x.robot.game.enums.Difficulty;
import com.thilian.se4x.robot.game.enums.Scenarios;

public class NewGameActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_settings:
            {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView background = findViewById(R.id.background_image);
        Glide.with(this).load(R.drawable.ngc_7635_2560).into(background);

        Spinner difficultySpinner = findViewById(R.id.difficulty_spinner);
        final ArrayAdapter<Difficulty> difficultyAdapter = new ToStringArrayAdapter<>(this,R.layout.white_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        Spinner scenarioSpinner = findViewById(R.id.scenario_spinner);
        ArrayAdapter<Scenarios> scenarioAdapter = new ToStringArrayAdapter<>(this, R.layout.white_spinner_item, Scenarios.values());
        scenarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scenarioSpinner.setAdapter(scenarioAdapter);
        scenarioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                difficultyAdapter.clear();
                try {
                    difficultyAdapter.addAll(((Scenarios)parent.getItemAtPosition(position)).getClazz().newInstance().getDifficulties());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                difficultyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button  startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createGame();
                    startMainActivity();
                } catch (IllegalAccessException e) {
                    //TODO error dialog
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });

        Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Game game = new GameSaver().loadGame(this);
        initResumeButton(game);
    }

    public void initResumeButton(Game game){
        int visibility = game == null ? View.GONE : View.VISIBLE;
        findViewById(R.id.saved_game_found_text).setVisibility(visibility);
        findViewById(R.id.saved_game_details_text).setVisibility(visibility);
        findViewById(R.id.resume_button).setVisibility(visibility);
        if(game != null){
            TextView gameDetails = findViewById(R.id.saved_game_details_text);
            int scenarioId = getResources().getIdentifier(game.scenario.getClass().getSimpleName(), "string", getPackageName());
            String scenarioName = getResources().getString(scenarioId);
            int diffId = getResources().getIdentifier(game.getDifficulty().getName(), "string", getPackageName());
            String diffName = getResources().getString(diffId);
            String text = getResources().getString(R.string.saved_game_details_text, scenarioName, diffName, game.currentTurn);
            gameDetails.setText(text);
        }
    }

    public void createGame() throws IllegalAccessException, InstantiationException {
        Difficulty difficulty = (Difficulty)(((Spinner) findViewById(R.id.difficulty_spinner)).getSelectedItem());
        Scenarios scenario = (Scenarios)(((Spinner)findViewById(R.id.scenario_spinner)).getSelectedItem());
        Game game = Game.newGame(scenario.getClazz().newInstance(), difficulty);
        new GameSaver().saveGame(game, this);
    }

    private void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }

    private class ToStringArrayAdapter<T> extends ArrayAdapter<T>{

        public ToStringArrayAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
            super(context, resource, objects);
        }

        public ToStringArrayAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent, R.layout.white_spinner_item);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            int simple_spinner_item = android.R.layout.simple_spinner_item;
            return getView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent, int resource) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resource, parent, false);
            }

            T item = getItem(position);
            String string = NewGameActivity.this.getString(item);
            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(string);
            return convertView;
        }
    }

    @NonNull
    public String getString(Object item) {
        int sid = getResources().getIdentifier(item.toString(), "string", getPackageName());
        return getResources().getString(sid);
    }
}
