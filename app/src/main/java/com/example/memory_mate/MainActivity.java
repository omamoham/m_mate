package com.example.memory_mate;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private CustomTabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    private List<String> tabTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the TextViews in the layout
        TextView userNameTextView = findViewById(R.id.usernameTextView);
        TextView currentDateTextView = findViewById(R.id.currentDateTextView);

        // Set the user name (replace "User Name" with the actual user's name)
        userNameTextView.setText("John Doe"); // Replace with the user's name

        // Set the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = dateFormat.format(new Date());
        currentDateTextView.setText(currentDate);

        // Check if the user name is stored in SharedPreferences
        String userName = SharedPreferencesHelper.getUserName(this);

        if (userName.isEmpty()) {
            // Show the dialog to input the user name if it's not saved
            showNameInputDialog();
        } else {
            // User name is already saved, display it
            updateUserName(userName);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Retrieve saved tab names from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("TabNames", MODE_PRIVATE);
        for (int i = 0; i < tabTitles.size(); i++) {
            String savedTabName = preferences.getString("tab" + i, tabTitles.get(i));
            tabTitles.set(i, savedTabName);
        }

        // Initialize tabs and fragments
        initializeTabs();

        adapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        // Long press on a tab to rename it
        tabLayout.setOnTabLongClickListener(new CustomTabLayout.OnTabLongClickListener() {
            @Override
            public void onTabLongClick(TabLayout.Tab tab) {
                showRenameDialog(tab);
            }
        });

    }


    private void showNameInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameInputView = layoutInflater.inflate(R.layout.dialog_name_input, null);

        final EditText nameEditText = nameInputView.findViewById(R.id.editTextName);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setView(nameInputView)
                .setTitle("Enter Your Name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = nameEditText.getText().toString();
                        if (!userName.isEmpty()) {
                            SharedPreferencesHelper.saveUserName(MainActivity.this, userName);
                            updateUserName(userName);
                        }
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
    private void updateUserName(String userName) {
        // Update the user name TextView with the saved user name
        TextView userNameTextView = findViewById(R.id.usernameTextView);
        userNameTextView.setText(userName);
    }
    private void initializeTabs() {
        // Add initial tab names (e.g., "Home" and "Office")
        tabTitles.add("Home");
        tabTitles.add("Office");

        // Add tabs to TabLayout
        for (String title : tabTitles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void showRenameDialog(final TabLayout.Tab tab) {
        final int tabIndex = tab.getPosition();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename Tab");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_rename_tab, null);
        final EditText input = viewInflated.findViewById(R.id.renameEditText);
        input.setText(tabTitles.get(tabIndex));

        builder.setView(viewInflated);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                if (!newName.isEmpty()) {
                    // Update the tab name in the adapter and SharedPreferences
                    adapter.setTabName(tabIndex, newName);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {

        TabPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            // Replace with your fragment creation logic for each tab
            return PlaceholderFragment.newInstance(tabTitles.get(position));
        }

        @Override
        public int getCount() {
            return tabTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        // Store the tab names in SharedPreferences when they are renamed
        public void setTabName(int position, String newName) {
            tabTitles.set(position, newName);
            notifyDataSetChanged();

            // Store the updated tab names in SharedPreferences
            SharedPreferences preferences = getSharedPreferences("TabNames", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // Use a loop to save all tab names
            for (int i = 0; i < tabTitles.size(); i++) {
                editor.putString("tab" + i, tabTitles.get(i));
            }

            editor.apply();
        }
    }





}