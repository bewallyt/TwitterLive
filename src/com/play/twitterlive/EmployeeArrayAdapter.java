package com.play.twitterlive;

import java.util.Date;

import android.content.Context;

public class EmployeeArrayAdapter extends TwoLineArrayAdapter<Employee> {
    public EmployeeArrayAdapter(Context context, Employee[] employees) {
        super(context, employees);
    }

    @Override
    public String lineOneText(Employee e) {
        return e.name;
    }

    @Override
    public String lineTwoText(Employee e) {
        return e.title;
    }
}
