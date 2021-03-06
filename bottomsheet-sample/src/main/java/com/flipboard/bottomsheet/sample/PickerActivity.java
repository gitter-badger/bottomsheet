package com.flipboard.bottomsheet.sample;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.R;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Comparator;

/**
 * Activity demonstrating the use of {@link IntentPickerSheetView}
 */
public class PickerActivity extends AppCompatActivity {

    protected BottomSheetLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        final TextView shareText = (TextView) findViewById(R.id.share_text);
        final Button shareButton = (Button) findViewById(R.id.share_button);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(shareText.getWindowToken(), 0);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.getText().toString());
                shareIntent.setType("text/plain");
                IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(PickerActivity.this, shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
                    @Override
                    public void onIntentPicked(Intent intent) {
                        bottomSheetLayout.dismissSheet();
                        startActivity(intent);
                    }
                });
                // Filter out built in sharing options such as bluetooth and beam.
                intentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
                    @Override
                    public boolean include(IntentPickerSheetView.ActivityInfo info) {
                        return !info.componentName.getPackageName().startsWith("com.android");
                    }
                });
                // Sort activities in reverse order for no good reason
                intentPickerSheet.setSortMethod(new Comparator<IntentPickerSheetView.ActivityInfo>() {
                    @Override
                    public int compare(IntentPickerSheetView.ActivityInfo lhs, IntentPickerSheetView.ActivityInfo rhs) {
                        return rhs.label.compareTo(lhs.label);
                    }
                });
                bottomSheetLayout.showWithSheetView(intentPickerSheet);
            }
        });
    }
}
