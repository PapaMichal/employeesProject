package com.example.employeesassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.employeesassignment.database.SqlHandler;
import com.example.employeesassignment.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private NavigationView navView;
    private NavOptions navOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessHelper sessHelper = new SessHelper(this);

        //Set the action bar to the tool bar
        setSupportActionBar(binding.appBarMain.toolbar);

        //Initialize view references
        DrawerLayout drawer = binding.drawerLayout;
        navView = binding.navView;

        //Put ids of fragments that are used in the menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.addEmployee, R.id.employeeList)
                .setOpenableLayout(drawer)
                .build();

        //Navigate to the host fragment
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build();
        /*
        Closes the drawer and navigates to the register if logout is clicked on the menu.
        This is done to prevent the drawer staying open on logout and allowing access
        to other pages
        */
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        //logout
                        navController.navigate(R.id.action_global_login, null, navOptions);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        SessHelper.getInstance(getParent()).logout();
                        return true;
                    default:
                        navController.navigate(menuItem.getItemId(), null, navOptions);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return false;
                }
            }
        });
        navView.bringToFront();

        /*
        Hides the menu when on the register/login page, in order to prevent access
        */
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                binding.appBarMain.toolbar.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        SqlHandler.getInstance(binding.getRoot().getContext());

        //Bind the music service to main activity
        doBindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                // Code to switch to the settings fragment
                navController.navigate(R.id.action_global_settings, null, navOptions);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //BINDER CODE
    private MusicService boundService;
    private boolean isBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundService = ((MusicService.LocalBinder) service).getService();


        }

        public void onServiceDisconnected(ComponentName className) {
            boundService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(MainActivity.this,
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            isBound = false;
        }
    }

    public void setNavHeaderUserAndEmail() {
        View headerView = navView.getHeaderView(0);
        TextView tvNavHeaderUsername = (TextView)headerView.findViewById(R.id.nav_header_username);
        TextView tvNavHeaderEmail = (TextView)headerView.findViewById(R.id.nav_header_email);
        SessHelper sessHelper = SessHelper.getInstance(this);
        tvNavHeaderUsername.setText(sessHelper.getUsername());
        tvNavHeaderEmail.setText(sessHelper.getEmail());
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}