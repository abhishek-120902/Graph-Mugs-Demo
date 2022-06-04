package com.abhishek.graphmugsdemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.abhishek.graphmugsdemo.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GraphFragment extends Fragment {

    //Variables
    View view;
    String[] months = new String[12];
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterMonths;
    int MIN = 1, MAX = 20;
    ArrayList<Entry> yValues = new ArrayList<>();
    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_graph, container, false);

        //Initialise Variables
        initialise(view);

        //Set adapter to Auto Complete TextView
        autoCompleteTextView.setAdapter(adapterMonths);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {

            lineChart.invalidate();
            lineChart.clear();

            ArrayList<ILineDataSet> dataSets1 = showGraph(adapterView, i);

            LineData data1 = new LineData(dataSets1);
            lineChart.setData(data1);
            lineChart.animateX(1000, Easing.EaseInOutSine);
            lineChart.animateY(1000, Easing.EaseInOutSine);
            lineChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        });

        // Inflate the layout for this fragment
        return view;
    }


    private ArrayList<ILineDataSet> showGraph(AdapterView<?> adapterView, int i) {


        String month = adapterView.getItemAtPosition(i).toString();
        for (int j = 1; j <= numberOfDays(i); j++) {
            yValues.add(new Entry(j, getRandomNumber(MIN, MAX)));
        }

        Toast.makeText(getContext(), "Month : " + month, Toast.LENGTH_SHORT).show();

        LineDataSet set = new LineDataSet(yValues, month);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        set.setCircleColor(R.color.blue);
        set.setValueTextColor(R.color.light_green);
        set.setValueTextSize(8f);

        dataSets.add(set);

        return dataSets;

    }

    private int getRandomNumber(int min, int max) {
        return ThreadLocalRandom
                .current()
                .nextInt(min, max + 1);
    }

    private int numberOfDays(int m) {
        if ((m == 0) | (m == 2) | (m == 4) | (m == 6) | (m == 7) | (m == 9) | (m == 11))
            return 31;      // Jan Mar May Jul Aug Oct Dec
        else if ((m == 3) | (m == 5) | (m == 8) | (m == 10))
            return 30;      // Apr Jun Sep Nov
        else
            return 28;      // Feb
    }

    private void initialise(View view) {
        months = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        autoCompleteTextView = view.findViewById(R.id.autoCompleteText);
        adapterMonths = new ArrayAdapter<>(getContext(), R.layout.month_list_item, months);
        lineChart = view.findViewById(R.id.lineChart);
    }
}