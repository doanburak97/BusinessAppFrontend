package com.doanburak.businessappfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sp_company;
    EditText start_date_time_input, end_date_time_input;
    TextView tv_startAndEndDate, tv_prediction;
    String startDate, endDate, company;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_company = findViewById(R.id.sp_company);
        start_date_time_input = findViewById(R.id.start_date_time_input);
        end_date_time_input = findViewById(R.id.end_date_time_input);
        tv_startAndEndDate = findViewById(R.id.tv_startAndEndDate);
        tv_prediction = findViewById(R.id.tv_prediction);
        btn_send = findViewById(R.id.btn_send);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.companies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_company.setAdapter(adapter);
        sp_company.setOnItemSelectedListener(this);

        start_date_time_input.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showStartDateTimeDialog(start_date_time_input);
            }
        });

        end_date_time_input.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showEndDateTimeDialog(end_date_time_input);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if(isDateAfter(startDate, endDate)){
                    return;
                }

                tv_startAndEndDate.setText("Selected dates : " + "\n" + startDate + " " + endDate +
                        "\nSelected Company : " + company);
                tv_prediction.setText("Buraya tahmin edilmiş veri gelecek.");

                String url = "";
                StringRequest sr = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, response.trim(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("startDate",startDate);
                        params.put("endDate",endDate);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(sr);
            }
        });
    }

    private void showStartDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                startDate = simpleDateFormat.format(calendar.getTime());
            }
        };

        new DatePickerDialog(MainActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showEndDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                endDate = simpleDateFormat.format(calendar.getTime());
            }
        };

        new DatePickerDialog(MainActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedCompany = parent.getItemAtPosition(position).toString();
        int spinner_pos = sp_company.getSelectedItemPosition();
        String[] dataName = getResources().getStringArray(R.array.dataName);
        company = dataName[spinner_pos];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isDateAfter(String startDate, String endDate){

        try{

            String dateFormat = "yyyy-MM-dd";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            Date date1 = df.parse(startDate);
            Date date2 = df.parse(endDate);

            if (date1 != null && date1.after(date2)){
                Toast.makeText(MainActivity.this, "The first date can not be later!", Toast.LENGTH_SHORT).show();
                return true;
            }

        }catch(Exception e){
            return false;
        }

        return false;

    }
}