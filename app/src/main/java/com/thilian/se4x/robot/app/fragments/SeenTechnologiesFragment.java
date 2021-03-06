/*
 * Copyright (C) 2018 Balázs Péter
 *
 * This file is part of Alien Player 4X.
 *
 * Alien Player 4X is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alien Player 4X is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Alien Player 4X.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.thilian.se4x.robot.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thilian.se4x.robot.app.R;
import com.thilian.se4x.robot.app.SE4XGameActivity;
import com.thilian.se4x.robot.app.dialogs.PickerDialog;
import com.thilian.se4x.robot.game.enums.Seeable;
import com.thilian.se4x.robot.game.enums.Technology;
import com.thilian.se4x.robot.game.scenarios.scenario4.Scenario4;

public class SeenTechnologiesFragment extends Fragment {
    private SE4XGameActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SE4XGameActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seen_technologies_fragment, container, false);

        createTechnologyText(view, Technology.CLOAKING);
        createTechnologyText(view, Technology.SCANNER);
        createTechnologyText(view, Technology.POINT_DEFENSE);

        createCheckbox(view, R.id.fighters_checkbox, Seeable.FIGHTERS);
        createCheckbox(view, R.id.mines_checkbox, Seeable.MINES);

        createCheckbox(view, R.id.boarding_checkbox, Seeable.BOARDING_SHIPS);
        createCheckbox(view, R.id.veterans_checkbox, Seeable.VETERANS);
        createCheckbox(view, R.id.size3_checkbox, Seeable.SIZE_3_SHIPS);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initTechnologyText(Technology.CLOAKING);
        initTechnologyText(Technology.SCANNER);
        initTechnologyText(Technology.POINT_DEFENSE);

        initCheckbox(R.id.fighters_checkbox, Seeable.FIGHTERS);
        initCheckbox(R.id.mines_checkbox, Seeable.MINES);

        if (activity.getGame().scenario instanceof Scenario4) {
            initCheckbox(R.id.boarding_checkbox, Seeable.BOARDING_SHIPS);
            initCheckbox(R.id.veterans_checkbox, Seeable.VETERANS);
            initCheckbox(R.id.size3_checkbox, Seeable.SIZE_3_SHIPS);
        } else {
            getView().findViewById(R.id.boarding_checkbox).setVisibility(View.GONE);
            getView().findViewById(R.id.veterans_checkbox).setVisibility(View.GONE);
            getView().findViewById(R.id.size3_checkbox).setVisibility(View.GONE);
        }
    }

    public void initTechnologyText(Technology technology) {
        String textViewName = String.format("%s_text", technology.toString().toLowerCase());
        int textViewId = activity.getResources().getIdentifier(textViewName, "id", activity.getPackageName());
        if (textViewId != 0) {
            TextView textView = getView().findViewById(textViewId);
            int sid = activity.getResources().getIdentifier(technology.toString(), "string", activity.getPackageName());
            int level = activity.getGame().getSeenLevel(technology);
            textView.setText(getResources().getString(sid, level));
        } else {
            System.out.println(String.format("Not found <%s>", textViewName));
        }
    }

    private void createTechnologyText(View view, final Technology technology) {
        String textViewName = String.format("%s_text", technology.toString().toLowerCase());
        int textViewId = getResources().getIdentifier(textViewName, "id", getActivity().getPackageName());
        if (textViewId != 0) {
            TextView textView = view.findViewById(textViewId);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPickerDialog(technology);
                }
            });
        } else {
            System.out.println(String.format("Not found <%s>", textViewName));
        }
    }

    private void showPickerDialog(final Technology technology) {
        new PickerDialog(activity).showSeenLevelPickerDialog(technology, new PickerDialog.PickerClickAction() {
            @Override
            public void action(int value) {
                activity.getGame().setSeenLevel(technology, value);
                initTechnologyText(technology);
            }
        });
    }

    private void createCheckbox(View view, int id, Seeable seeable) {
        CheckBox checkBox = view.findViewById(id);
        checkBox.setOnClickListener(new CheckBoxListener(seeable));
    }

    private void initCheckbox(int id, Seeable seeable) {
        CheckBox checkBox = getView().findViewById(id);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setChecked(activity.getGame().isSeenThing(seeable));
    }

    private class CheckBoxListener implements View.OnClickListener {

        private Seeable seeable;

        CheckBoxListener(Seeable seeable) {
            this.seeable = seeable;
        }

        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();
            if (checked) {
                activity.getGame().addSeenThing(seeable);
            } else {
                activity.getGame().removeSeenThing(seeable);
            }
        }
    }
}
