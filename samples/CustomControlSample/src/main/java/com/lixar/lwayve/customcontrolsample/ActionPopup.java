package com.lixar.lwayve.customcontrolsample;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lixar.lwayve.sdk.experience.ClipAction;

import java.util.Set;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ActionPopup {

    private Context context;
    private View anchorView;
    private ActionClickListener listener;

    public ActionPopup(Context context, View anchorView, ActionClickListener listener) {
        this.context = context;
        this.anchorView = anchorView;
        this.listener = listener;
    }

    public void show(Set<ClipAction> actions) {
        LinearLayout layout = createPopupView();

        PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(WRAP_CONTENT);
        popup.setWidth(dimenToPx(R.dimen.popup_menu_width));
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popup.setElevation(dimenToPx(R.dimen.popup_elevation));
        }


        populateActions(popup, layout, actions);

        popup.showAsDropDown(anchorView);
    }

    @NonNull
    private LinearLayout createPopupView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        return layout;
    }

    private void populateActions(PopupWindow popup, ViewGroup layout, Set<ClipAction> actions) {
        for (ClipAction action : actions) {
            layout.addView(createActionItem(popup, action));
        }
    }

    private View createActionItem(final PopupWindow popup, final ClipAction action) {
        int iconSize = dimenToPx(R.dimen.popup_icon_size);
        int paddingPx = dimenToPx(R.dimen.popup_padding);
        int textSize = dimenToPx(R.dimen.popup_text_size);

        Drawable drawable = context.getResources().getDrawable(action.iconRes);
        drawable.setBounds(0, 0, iconSize, iconSize);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

        TextView view = new TextView(context);
        view.setClickable(true);
        view.setBackgroundResource(typedValue.resourceId);
        view.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        view.setText(action.stringRes);
        view.setTextSize(COMPLEX_UNIT_PX, textSize);
        view.setGravity(CENTER_VERTICAL);
        view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(paddingPx * 2);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onActionClicked(action);
                }
                popup.dismiss();
            }
        });

        return view;
    }

    private int dimenToPx(int dimenRes) {
        return context.getResources().getDimensionPixelSize(dimenRes);
    }

    public interface ActionClickListener {

        void onActionClicked(ClipAction action);

    }

}
