package org.xdevs23.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xdevs23.android.widget.XquidRelativeLayout;
import org.xdevs23.ui.dialog.templates.DismissDialogButton;
import org.xdevs23.ui.utils.DpUtil;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class EasyListView4 extends XquidRelativeLayout {

    private String[]
            wholeArray,
            row1,
            row2,
            row3,
            row4;

    public EasyListView4(Context context) {
        super(context);
        init();
    }

    public EasyListView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EasyListView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public EasyListView4(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public EasyListView4 setListArray(String[] array) {
        this.wholeArray = array;
        init();
        return this;
    }

    public EasyListView4 setListArray(@ArrayRes int res) {
        setListArray(getContext().getResources().getStringArray(res));
        return this;
    }

    private void filterRows() {
        int wal = wholeArray.length / 4;

        row1 = new String[wal];
        row2 = new String[wal];
        row3 = new String[wal];
        row4 = new String[wal];

        for ( int index, i = index = 0; i < wholeArray.length; i += 4, index++ ) {
            row1[index]     = wholeArray[i];
            row2[index]     = wholeArray[i+1];
            row3[index]     = wholeArray[i+2];
            row4[index]     = wholeArray[i+3];
        }
    }

    private LinearLayout createNewLinearLayout() {
        LinearLayout layout = new LinearLayout(getContext());

        layout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(p);

        return layout;
    }

    private ScrollView createNewScrollView() {
        ScrollView layout = new ScrollView(getContext());

        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(p);

        return layout;
    }

    private LinearLayout createNewItem() {
        LinearLayout layout = new LinearLayout(getContext());

        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        p.setMargins(
                DpUtil.dp2px(getContext(), 2),
                DpUtil.dp2px(getContext(), 2),
                DpUtil.dp2px(getContext(), 4),
                DpUtil.dp2px(getContext(), 2)
        );

        layout.setLayoutParams(p);
        return layout;
    }

    private TextView createNewRowTextView() {
        TextView textView = new TextView(getContext());

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        p.setMargins(
                DpUtil.dp2px(getContext(), 2),
                DpUtil.dp2px(getContext(), 2),
                DpUtil.dp2px(getContext(), 2),
                DpUtil.dp2px(getContext(), 2)
        );

        textView.setLayoutParams(p);

        return textView;
    }

    private void init() {
        if(wholeArray == null) return;
        filterRows();

        ScrollView parentLayout = createNewScrollView();

        LinearLayout mainLinearLayout = createNewLinearLayout();

        for ( int i = 0; i < row1.length; i++ ) {
            LinearLayout itemLayout = createNewItem();
            TextView
                    itemRow1 = createNewRowTextView(),
                    itemRow2 = createNewRowTextView(),
                    itemRow3 = createNewRowTextView(),
                    itemRow4 = createNewRowTextView();

            itemRow1.setText(row1[i]);
            itemRow2.setText(row2[i]);
            itemRow3.setText(row3[i]);
            itemRow4.setText(row4[i]);

            itemLayout.addView(itemRow1);
            itemLayout.addView(itemRow2);
            itemLayout.addView(itemRow3);
            itemLayout.addView(itemRow4);

            if(row4[i].contains("://")) {
                final int fi = i;
                final Context fContext = getContext();
                itemLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CornBrowser.readyToLoadUrl = row4[fi];
                        fContext.startActivity((new Intent(fContext, CornBrowser.class))
                                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    }
                });
            }

            itemLayout.setBackgroundColor(ColorUtil.getColor(R.color.grey_50));
            itemLayout.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_HOVER_ENTER:
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(ColorUtil.getColor(R.color.grey_300));
                            break;
                        case MotionEvent.ACTION_HOVER_EXIT:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_OUTSIDE:
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(ColorUtil.getColor(R.color.grey_50));
                            break;
                        default: break;
                    }
                    return false;
                }
            });

            mainLinearLayout.addView(itemLayout);
            mainLinearLayout.addView(new SimpleSeparator(getContext()));
        }

        parentLayout.addView(mainLinearLayout);

        addView(parentLayout);
    }

    public static void showDialogUsingPreference(Preference preference, @ArrayRes final int res, final Activity activity) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setView((new EasyListView4(activity).setListArray(res)))
                        .setPositiveButton(R.string.answer_ok, new DismissDialogButton())
                        .create().show();
                return false;
            }
        });
    }

}
