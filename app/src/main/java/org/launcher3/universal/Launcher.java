package org.launcher3.universal;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Standalone, user-installable compatibility shell derived from the Launcher3 project line.
 * It deliberately uses only public Android SDK APIs so that it can be installed without
 * platform signing or privileged permissions.
 */
public final class Launcher extends Activity {
    private final ArrayList<AppEntry> apps = new ArrayList<>();
    private AppAdapter adapter;
    private GridView appGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContent());
        reloadApps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadApps();
    }

    private View createContent() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setPadding(dp(16), dp(20), dp(16), dp(12));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout titleBlock = new LinearLayout(this);
        titleBlock.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText(getString(R.string.app_name));
        title.setTextColor(Color.rgb(32, 33, 36));
        title.setTextSize(24);
        title.setTypeface(null, 1);
        titleBlock.addView(title);
        TextView subtitle = new TextView(this);
        subtitle.setText(getString(R.string.app_subtitle));
        subtitle.setTextColor(Color.rgb(95, 99, 104));
        subtitle.setTextSize(12);
        titleBlock.addView(subtitle);
        header.addView(titleBlock, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView reload = new TextView(this);
        reload.setText(getString(R.string.reload));
        reload.setTextColor(Color.rgb(11, 87, 208));
        reload.setTextSize(15);
        reload.setGravity(Gravity.CENTER);
        reload.setPadding(dp(16), dp(12), dp(4), dp(12));
        reload.setOnClickListener(v -> reloadApps());
        header.addView(reload, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(header, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        appGrid = new GridView(this);
        appGrid.setNumColumns(GridView.AUTO_FIT);
        appGrid.setColumnWidth(dp(88));
        appGrid.setHorizontalSpacing(dp(8));
        appGrid.setVerticalSpacing(dp(12));
        appGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        appGrid.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        appGrid.setClipToPadding(false);
        appGrid.setPadding(0, dp(20), 0, dp(10));
        adapter = new AppAdapter();
        appGrid.setAdapter(adapter);
        appGrid.setOnItemClickListener((parent, view, position, id) -> launch(apps.get(position)));
        root.addView(appGrid, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        return root;
    }

    private void reloadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL);
        apps.clear();
        for (ResolveInfo info : resolved) {
            ActivityInfo activity = info.activityInfo;
            if (activity == null || getPackageName().equals(activity.packageName)) {
                continue;
            }
            CharSequence label = info.loadLabel(getPackageManager());
            apps.add(new AppEntry(
                    label == null ? activity.name : label.toString(),
                    info.loadIcon(getPackageManager()),
                    new ComponentName(activity.packageName, activity.name)));
        }
        final Collator collator = Collator.getInstance();
        Collections.sort(apps, new Comparator<AppEntry>() {
            @Override
            public int compare(AppEntry left, AppEntry right) {
                return collator.compare(left.label, right.label);
            }
        });
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void launch(AppEntry entry) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(entry.component);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try {
            startActivity(intent);
        } catch (RuntimeException e) {
            Toast.makeText(this, entry.label + " を起動できませんでした", Toast.LENGTH_SHORT).show();
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private final class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return apps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppTile tile;
            if (convertView instanceof AppTile) {
                tile = (AppTile) convertView;
            } else {
                tile = new AppTile();
            }
            tile.bind(apps.get(position));
            return tile;
        }
    }

    private final class AppTile extends LinearLayout {
        private final TextView icon;
        private final TextView label;

        AppTile() {
            super(Launcher.this);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER_HORIZONTAL);
            setPadding(dp(4), dp(4), dp(4), dp(4));
            icon = new TextView(Launcher.this);
            icon.setGravity(Gravity.CENTER);
            addView(icon, new LinearLayout.LayoutParams(dp(52), dp(52)));
            label = new TextView(Launcher.this);
            label.setGravity(Gravity.CENTER);
            label.setTextColor(Color.rgb(32, 33, 36));
            label.setTextSize(12);
            label.setMaxLines(2);
            label.setPadding(0, dp(5), 0, 0);
            addView(label, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        void bind(AppEntry entry) {
            icon.setBackground(entry.icon);
            label.setText(entry.label);
            setContentDescription(entry.label);
        }
    }

    private static final class AppEntry {
        final String label;
        final Drawable icon;
        final ComponentName component;

        AppEntry(String label, Drawable icon, ComponentName component) {
            this.label = label;
            this.icon = icon;
            this.component = component;
        }
    }
}
