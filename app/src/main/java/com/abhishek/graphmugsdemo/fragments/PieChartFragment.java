package com.abhishek.graphmugsdemo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.abhishek.graphmugsdemo.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class PieChartFragment extends Fragment {

    //Variables
    private PieChart chart;
    private int m1;
    private int m2;
    private int m3;
    private int m4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        initialise(view);

        addToPieChart();

        return view;
    }

    private void addToPieChart() {
        chart.addPieSlice(new PieModel("40", m1, Color.parseColor("#33EAFF")));     //cyan
        chart.addPieSlice(new PieModel("30", m2, Color.parseColor("#FFEE33")));     //yellow
        chart.addPieSlice(new PieModel("20", m3, Color.parseColor("#76FF03")));     //light_green
        chart.addPieSlice(new PieModel("10", m4, Color.parseColor("#2979FF")));     //blue

        chart.startAnimation();

    }

    private void initialise(View view) {
        chart = view.findViewById(R.id.pie_chart);
        m1 = 40;
        m2 = 30;
        m3 = 20;
        m4 = 10;
    }
}