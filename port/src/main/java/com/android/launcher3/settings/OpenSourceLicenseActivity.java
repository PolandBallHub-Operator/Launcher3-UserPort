/*
 * Copyright (C) 2026 Launcher3 User Port contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.launcher3.settings;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.launcher3.R;

/** Displays the license text shipped with this Launcher3 user-app port. */
public class OpenSourceLicenseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.open_source_licenses_title);

        TextView licenseText = new TextView(this);
        licenseText.setText(readLicenseText());
        licenseText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        licenseText.setTextIsSelectable(true);
        licenseText.setMovementMethod(ScrollingMovementMethod.getInstance());
        int padding = (int) (getResources().getDisplayMetrics().density * 20);
        licenseText.setPadding(padding, padding, padding, padding);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(licenseText, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(scrollView);
    }

    private String readLicenseText() {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getResources().openRawResource(R.raw.open_source_license)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException e) {
            return getString(R.string.open_source_licenses_summary);
        }
        return text.toString();
    }
}
