package com.lixar.lwayve.prebuiltcontrolsample;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lixar.lwayve.decisiontree.experience.Location;
import com.lixar.lwayve.decisiontree.experience.Tag;
import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TagDialogFragment extends DialogFragment {

    private LwayveSdk lwayveSdk;

    private EditText locationsInput;
    private EditText likesInput;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);

        setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View inputLayout = inflater.inflate(R.layout.dialog_tag, null, false);

        locationsInput = inputLayout.findViewById(R.id.txt_locations);
        likesInput = inputLayout.findViewById(R.id.txt_likes);

        LwayveSdk.connect(new LwayveConnectionCallback() {
            @Override
            public void onConnected(@NonNull final LwayveSdk lwayveSdk) {
                TagDialogFragment.this.lwayveSdk = lwayveSdk;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationsInput.setText(locationsToString(lwayveSdk.getUserLocations()));
                        likesInput.setText(likesToString(lwayveSdk.getUserLikes()));
                    }
                });
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(R.string.tag_dialog_title)
                .setMessage(R.string.tag_dialog_msg)
                .setView(inputLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateLocations();
                        updateLikes();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private String likesToString(Set<Tag> tags) {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : tags) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(tag.getName());
        }
        return builder.toString();
    }

    private String locationsToString(Set<Location> locations) {
        StringBuilder builder = new StringBuilder();
        for (Location location : locations) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(location.getName());
        }
        return builder.toString();
    }

    private void updateLocations() {
        Set<String> locations = new HashSet<>();

        String input = locationsInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Set<String> newLocations = extractTags(input);
            locations.addAll(newLocations);
        }

        lwayveSdk.setUserLocations(locations);
    }

    private void updateLikes() {
        Set<String> userLikes = new HashSet<>();

        String input = likesInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Set<String> newLikes = extractTags(input);
            userLikes.addAll(newLikes);
        }

        lwayveSdk.setUserLikes(userLikes);
    }

    @NonNull
    private HashSet<String> extractTags(String input) {
        return new HashSet<>(new ArrayList<>(Arrays.asList(input.trim().split("\\s*,\\s*"))));
    }

}
