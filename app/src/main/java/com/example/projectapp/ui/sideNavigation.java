package com.example.projectapp.ui;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projectapp.R;

import com.google.android.material.navigation.NavigationView;

public class sideNavigation {

    DrawerLayout drawerLayout;

    public sideNavigation(DrawerLayout drawerLayout) {

        this.drawerLayout=drawerLayout;
   }

    private NavigationView.OnNavigationItemSelectedListener navListener= new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.h:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.m:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                  return true;
                case R.id.songs:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.message:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.logout:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;


            }
return false;

        }
    };

}
