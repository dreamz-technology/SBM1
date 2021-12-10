package com.dreamz.samtaBhratruMandal.Utils;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItems extends AppCompatActivity {
    private List<String> locationList = new ArrayList<>();
    private List<String> GotraList = new ArrayList<>();
    private List<String> MinAgeList = new ArrayList<>();
    private List<String> MaxAgeList = new ArrayList<>();
    private List<String> MinHeightList = new ArrayList<>();
    private List<String> MaxHeightList = new ArrayList<>();
    private List<String> MinSalList = new ArrayList<>();
    private List<String> MaxSalList = new ArrayList<>();
    List<String> professionList= new ArrayList<>();
    private List<String> BLoodGroupList = new ArrayList<>();
    private List<String> EducationList = new ArrayList<>();
    Gson gson = new Gson();
    Spinner spProfessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

}
