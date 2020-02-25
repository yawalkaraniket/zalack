package com.zalack.android.ui.fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.zalack.android.R;
import com.zalack.android.ui.common.FontTextView;

import static com.zalack.android.utils.UIUtils.dp;

public class BaseFragment extends Fragment {

    protected ProgressBar progressBar;
    protected View blockLayout;
    protected FontTextView loading;

    public final static int PROGRESS_TYPE_NO_BLOCK = 0;
    public final static int PROGRESS_TYPE_BLOCK_TRANSPARENT = 1;

    /**
     * Adding progress bar
     * Link: https://www.zoftino.com/adding-views-&-constraints-to-android-constraint-layout-programmatically : Adding constraint layout programatically
     *
     * @param layout Parent layout
     */
    protected void setUpProgress(View layout, Activity activity) {

        if (layout instanceof ConstraintLayout) {

            ConstraintLayout constraintLayout = (ConstraintLayout) layout;

            // Setting icon and color for the text view.
            progressBar = new ProgressBar(layout.getContext());
            progressBar.setId(R.id.navigation_layout_container);
            progressBar.setElevation(10.0f);
            progressBar.setPadding((int) (7 * dp()), (int) (7 * dp()), (int) (7 * dp()), (int) (7 * dp()));
            progressBar.setBackgroundResource(R.drawable.white_circle);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams((int) (40 * dp()), (int) (40 * dp())));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.BLACK));
            } else {
                // Add the code for below API 21.
            }

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            constraintLayout.addView(progressBar);

            // Adding layout canter to the parent
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(progressBar.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 18);
            constraintSet.connect(progressBar.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 18);
            constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 18);
            constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 18);
            constraintSet.applyTo(constraintLayout);

            // Layout for the block layout
            blockLayout = new ConstraintLayout(activity);
            blockLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
            constraintLayout.addView(blockLayout);

            // Setting visibility for all layout gone for the first time.
            blockLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            // Adding parent listener
            blockLayout.setOnClickListener(v -> {
            });

        } else if (layout instanceof RelativeLayout) {

            RelativeLayout relativeLayout = (RelativeLayout) layout;
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            progressBar = new ProgressBar(layout.getContext());
            progressBar.setElevation(10.0f);
            progressBar.setBackgroundResource(R.drawable.white_circle);
            progressBar.setPadding((int) (7 * dp()), (int) (7 * dp()), (int) (7 * dp()), (int) (7 * dp()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.BLACK));
            } else {
                // Add the code for below API 21.
            }
            progressBar.setLayoutParams(new LinearLayout.LayoutParams((int) (40 * dp()), (int) (40 * dp())));

            RelativeLayout.LayoutParams params;
            linearLayout.setLayoutParams(params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.CENTER);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);

            loading = new FontTextView(activity);
            loading.setText(getString(R.string.loading_generic_message));
            loading.setTextColor(getResources().getColor(R.color.white));
            loading.setGravity(Gravity.CENTER);

            linearLayout.addView(progressBar);
            linearLayout.addView(loading);

            blockLayout = new RelativeLayout(activity);
            blockLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            relativeLayout.addView(blockLayout);
            blockLayout.setOnClickListener(click -> {
            });

            relativeLayout.addView(linearLayout);
            blockLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        }
    }

    void showProgress() {
        showProgress(PROGRESS_TYPE_BLOCK_TRANSPARENT);
    }

    private void showProgress(int type) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (type == PROGRESS_TYPE_BLOCK_TRANSPARENT && blockLayout != null) {
            blockLayout.setVisibility(View.VISIBLE);
        }
        if (type == PROGRESS_TYPE_NO_BLOCK && blockLayout != null) {
            blockLayout.setVisibility(View.GONE);
        }
    }

    void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (blockLayout != null) {
            blockLayout.setVisibility(View.GONE);
        }
        if (loading != null) {
            loading.setVisibility(View.GONE);
        }
    }

}
