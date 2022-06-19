package com.example.qldatn

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.qldatn.Helper.Message
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var controller:NavController;
    lateinit var bottom_menu: BottomNavigationView;
    lateinit var bottom_teacher_menu: BottomNavigationView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            val MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100
            var permission =  Array<String>(1){Manifest.permission.READ_EXTERNAL_STORAGE}
            ActivityCompat.requestPermissions(this@MainActivity,
                permission,MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        } else {
            // da cap quyen
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        navHostFragment?.let {
            controller = navHostFragment.findNavController()
        }

        bottom_menu=findViewById(R.id.bottom_menu)
        bottom_teacher_menu=findViewById(R.id.bottom_teacher_menu)

        bottom_menu.setupWithNavController(controller)
        bottom_teacher_menu.setupWithNavController(controller)

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.detailCalendarFragment->bottom_menu.menu.findItem(R.id.calendarFragment).setChecked(true);

                R.id.detailProjectFragment->bottom_menu.menu.findItem(R.id.projectFragment).setChecked(true);
                R.id.createProjectFragment->bottom_menu.menu.findItem(R.id.projectFragment).setChecked(true);
                R.id.editProjectFragment->bottom_menu.menu.findItem(R.id.projectFragment).setChecked(true);

                R.id.detailTeacherFragment->bottom_menu.menu.findItem(R.id.teacherFragment).setChecked(true);

                R.id.infoStudentFragment->bottom_menu.menu.findItem(R.id.settingFragment).setChecked(true);
                R.id.changePwdFragment->bottom_menu.menu.findItem(R.id.settingFragment).setChecked(true);
                R.id.mySuggestFragment->bottom_menu.menu.findItem(R.id.settingFragment).setChecked(true);

                R.id.listCalenTeacherFragment->bottom_teacher_menu.menu.findItem(R.id.calenTeacherFragment).setChecked(true);
                R.id.detailCalenTeacherFragment->bottom_teacher_menu.menu.findItem(R.id.calenTeacherFragment).setChecked(true);

                R.id.detailSuggestFragment->bottom_teacher_menu.menu.findItem(R.id.suggestTeacherFragment).setChecked(true);
                R.id.infoStudentFragment->bottom_teacher_menu.menu.findItem(R.id.suggestTeacherFragment).setChecked(true);

                R.id.infoTeacherFragment->bottom_teacher_menu.menu.findItem(R.id.settingTeacherFragment).setChecked(true);
                R.id.changePwdFragment->bottom_teacher_menu.menu.findItem(R.id.settingTeacherFragment).setChecked(true);

            }
        }
    }


}