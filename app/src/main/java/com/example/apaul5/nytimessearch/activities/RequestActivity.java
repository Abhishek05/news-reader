package com.example.apaul5.nytimessearch.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.apaul5.nytimessearch.R;
import com.example.apaul5.nytimessearch.model.RequestModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    String desk;
    String begingDate;
    String sort;

    RequestModel request;

    EditText edDate;

    Map<Long, String> sortLookUp = new HashMap<>();
    {
        sortLookUp.put(0L, "newest");
        sortLookUp.put(1L, "oldest");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        Spinner spinner = (Spinner) findViewById(R.id.sort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        request = new RequestModel();

        edDate = (EditText)findViewById(R.id.etDate);

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new StartDatePicker();
                dialogFragment.show(getFragmentManager(), "start_date_picker");
            }
        });

    }

    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("ValidFragment")
    class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(RequestActivity.this, this, startYear, startMonth, startDay);
            return dialog;

        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            updateStartDateDisplay();
        }
    }

    private void updateStartDateDisplay() {
        c.set(startYear, startMonth, startDay);
        begingDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        Log.d("DEBUG","show date "+ begingDate);
        edDate.setText(new SimpleDateFormat("MM-dd-yyyy").format(c.getTime()));


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioArts:
                if (checked)
                    desk = "Arts";
                    break;
            case R.id.radioFashion:
                if (checked)
                    desk = "Fashion & Style";
                    break;
            case R.id.radioSports:
                if (checked)
                    desk = "Sports";
                    break;
        }
    }

    public void onSaveSettings(View view){

        Intent intent = new Intent();
        request.deskValue = desk;
        request.beginDate = begingDate;
        request.sort = sortLookUp.get(((Spinner)findViewById(R.id.sort)).getSelectedItemId());
        intent.putExtra("request",request);
        setResult(RESULT_OK, intent);
        this.finish();

    }

}
