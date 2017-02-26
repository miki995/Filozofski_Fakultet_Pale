package com.inc.miki.filozofski_falutet_pale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.miki.filozofski_falutet_pale.Models.ClassRoom;
import com.inc.miki.filozofski_falutet_pale.Models.StudyProgram;
import com.inc.miki.filozofski_falutet_pale.Models.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.inc.miki.filozofski_falutet_pale.R.id.DateEditText;
import static com.inc.miki.filozofski_falutet_pale.R.string.studyPrograms;

public class SchedulesActivity extends AppCompatActivity {
    ArrayList<Teacher> teacherList;
    ArrayList<StudyProgram> studyProgramList;
    ArrayList<ClassRoom> classRoomList;

    static Calendar cal = Calendar.getInstance();
    static EditText dateEditText;

    public String getCurrentDate(){
    return  dateEditText.getText().toString();
    }
    public int getCurrentPosition(){Spinner spinner1 = (Spinner)findViewById(R.id.spinner);return  spinner1.getSelectedItemPosition();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        TextView text = (TextView) findViewById(R.id.text1);
        text.setText(studyPrograms);



        Button currentWeek = (Button) findViewById(R.id.currentWeek);
        currentWeek.performClick();

        dateEditText = (EditText) findViewById(R.id.DateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(v);
            }
        });


        teacherList = new ArrayList<>();
        studyProgramList = new ArrayList<>();
        classRoomList = new ArrayList<>();

        new GetStudyPrograms().execute();



        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.parseColor("#6ED3F5"), Color.parseColor("#30B1DB"),Color.parseColor("#0A6A8A"), Color.parseColor("#054257"));

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                swipeLayout.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override public void run() {

                        Button studyProgramButton = (Button) findViewById(R.id.StudyProgramButton);
                        ColorDrawable studyProgramButtonBackground = (ColorDrawable) studyProgramButton.getBackground();
                        int studyProgramColorId = studyProgramButtonBackground.getColor();

                        Button teacherButton = (Button) findViewById(R.id.TeacherButton);
                        ColorDrawable teacherButtonBackground = (ColorDrawable)teacherButton.getBackground();
                        int teachersColorId = teacherButtonBackground.getColor();

                        Button classRoomButton = (Button) findViewById(R.id.ClassRoomButton);
                        ColorDrawable classRoomButtonBackground = (ColorDrawable)classRoomButton.getBackground();
                        int classRoomsColorId = classRoomButtonBackground.getColor();

                        int clickedColor = ContextCompat.getColor(getApplicationContext(), R.color.clickedBackground);

                        Button b = (Button) findViewById(R.id.firstYear);

                        if(studyProgramColorId == clickedColor)
                        {
                            new GetStudyPrograms().execute();
                            b.performClick();
                            swipeLayout.setRefreshing(false);
                        }
                        else if(teachersColorId == clickedColor)
                        {
                            new GetTeachers().execute();
                            GetURL(1);
                            swipeLayout.setRefreshing(false);
                        }
                        else if(classRoomsColorId == clickedColor)
                        {
                            new GetClassRooms().execute();
                            GetURL(1);
                            swipeLayout.setRefreshing(false);
                        }
                        else
                        {
                            new GetStudyPrograms().execute();
                            swipeLayout.setRefreshing(false);
                        }

                    }
                }, 2000);
            }
        });

        dateEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Button b = (Button) findViewById(R.id.firstYear);
                b.performClick();
            }
        });
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Button b = (Button) findViewById(R.id.firstYear);
                b.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




    }

    public String GetURL(int numOfYear){
        String URL = "";
        Button studyProgramButton = (Button) findViewById(R.id.StudyProgramButton);
        ColorDrawable studyProgramButtonBackground = (ColorDrawable) studyProgramButton.getBackground();
        int studyProgramColorId = studyProgramButtonBackground.getColor();

        Button teacherButton = (Button) findViewById(R.id.TeacherButton);
        ColorDrawable teacherButtonBackground = (ColorDrawable)teacherButton.getBackground();
        int teachersColorId = teacherButtonBackground.getColor();

        Button classRoomButton = (Button) findViewById(R.id.ClassRoomButton);
        ColorDrawable classRoomButtonBackground = (ColorDrawable)classRoomButton.getBackground();
        int classRoomsColorId = classRoomButtonBackground.getColor();

        int clickedColor = ContextCompat.getColor(getApplicationContext(), R.color.clickedBackground);



        String Date = getCurrentDate();
        String[] parts = Date.split(" - ");
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String firstWeek = String.valueOf(year) + "-" + parts[0];
        String lastWeek = String.valueOf(year) + "-" +  parts[1];

        if(studyProgramColorId == clickedColor &&  studyProgramList.size() > 0)
        {

            StudyProgram s = studyProgramList.get(getCurrentPosition());

            String Base1 = "http://raspored.ffuis.edu.ba/breeze/data/Schedules?$filter=((StudyProgramId%20eq%20";
            String StudyProgramId = String.valueOf(s.getId());
            String Base2 = ")%20and%20(Date%20ge%20datetime%27";
            String Base3 = "T23%3A00%3A00.000Z%27)%20and%20(Date%20le%20datetime%27";
            String Base4 = "T22%3A59%3A59.999Z%27))%20and%20(YearOfStudy%20eq%20";
            String Base5 = ")&$orderby=Date&$expand=ScheduleItems%2FCourse%2CScheduleItems%2FTeacher%2CScheduleItems%2FClassroom";

             URL = Base1 + StudyProgramId + Base2 + firstWeek + Base3 + lastWeek + Base4 + numOfYear + Base5 ;


            EditText e = (EditText) findViewById(R.id.miki);
            e.setText(URL);
        }
        else if(teachersColorId == clickedColor && teacherList.size() > 0)
        {
            Teacher t = teacherList.get(getCurrentPosition());

            String Base1 = "http://raspored.ffuis.edu.ba/breeze/data/Schedules?$filter=(ScheduleItems%2Fany(x1%3A%20x1%2FTeacherId%20eq%20";
            String TeacherId = String.valueOf(t.getId());
            String Base2 = "))%20and%20((Date%20ge%20datetime%27";
            String Base3 = "T23%3A00%3A00.000Z%27)%20and%20(Date%20le%20datetime%27";
            String Base4 = "T22%3A59%3A59.999Z%27))&$orderby=Date&$expand=ScheduleItems%2FTeacher%2CScheduleItems%2FClassroom%2CScheduleItems%2FCourse";

             URL = Base1 + TeacherId + Base2 + firstWeek + Base3 + lastWeek + Base4;


            EditText e = (EditText) findViewById(R.id.miki);
            e.setText(URL);
        }
        else if(classRoomsColorId == clickedColor && classRoomList.size() > 0)
        {
            ClassRoom c = classRoomList.get(getCurrentPosition());

            String Base1 = "http://raspored.ffuis.edu.ba/breeze/data/Schedules?$filter=(ScheduleItems%2Fany(x1%3A%20x1%2FClassroomId%20eq%20";
            String ClassRoomId = String.valueOf(c.getId());
            String Base2 = "))%20and%20((Date%20ge%20datetime%27";
            String Base3 = "T23%3A00%3A00.000Z%27)%20and%20(Date%20le%20datetime%27";
            String Base4 = "T22%3A59%3A59.999Z%27))&$orderby=Date&$expand=ScheduleItems%2FTeacher%2CScheduleItems%2FClassroom%2CScheduleItems%2FCourse";

             URL = Base1 + ClassRoomId + Base2 + firstWeek + Base3 + lastWeek + Base4;


            EditText e = (EditText) findViewById(R.id.miki);
            e.setText(URL);

        }


        return  URL;
    }

    public void SetDate(View view)
    {
        Date date = new Date();

        cal.setTime(date);

        Calendar first = (Calendar) cal.clone();
        first.add(Calendar.DAY_OF_WEEK,
                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));


        Calendar last = (Calendar) first.clone();
        last.add(Calendar.DAY_OF_YEAR, 5);


        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        EditText editText = (EditText) findViewById(DateEditText) ;
        editText.setText(df.format(first.getTime()) + " - " + df.format(last.getTime()));
    }

    public void firstYearClick(View view) {
        Button firstYear = (Button) findViewById(R.id.firstYear);
        Button secondYear = (Button) findViewById(R.id.secondYear);
        Button thirdYear = (Button) findViewById(R.id.thirdYear);
        Button fourthYear = (Button) findViewById(R.id.fourthYear);
        firstYear.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        secondYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        thirdYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        fourthYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        GetURL(1);
    }
    public void secondYearClick(View view) {
        Button firstYear = (Button) findViewById(R.id.firstYear);
        Button secondYear = (Button) findViewById(R.id.secondYear);
        Button thirdYear = (Button) findViewById(R.id.thirdYear);
        Button fourthYear = (Button) findViewById(R.id.fourthYear);
        firstYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        secondYear.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        thirdYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        fourthYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        GetURL(2);

    }
    public void thirdYearClick(View view) {
        Button firstYear = (Button) findViewById(R.id.firstYear);
        Button secondYear = (Button) findViewById(R.id.secondYear);
        Button thirdYear = (Button) findViewById(R.id.thirdYear);
        Button fourthYear = (Button) findViewById(R.id.fourthYear);
        firstYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        secondYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        thirdYear.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        fourthYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        GetURL(3);
    }
    public void fourthYearClick(View view) {
        Button firstYear = (Button) findViewById(R.id.firstYear);
        Button secondYear = (Button) findViewById(R.id.secondYear);
        Button thirdYear = (Button) findViewById(R.id.thirdYear);
        Button fourthYear = (Button) findViewById(R.id.fourthYear);
        firstYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        secondYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        thirdYear.setBackgroundColor((ContextCompat.getColor(this,R.color.blueBackground)));
        fourthYear.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        GetURL(4);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CalendarView cv = dpd.getDatePicker().getCalendarView(); // should check for null
                long cur = cv.getDate();
                int d = cv.getFirstDayOfWeek();
                cv.setDate(cur + 1000L * 60 * 60 * 24 * 30);
                cv.setFirstDayOfWeek((d + 1) % 7);
                cv.setDate(cur);
                cv.setFirstDayOfWeek(d);
                cv.setFirstDayOfWeek(Calendar.MONDAY);
            }else {
                dpd.getDatePicker().setCalendarViewShown(false);
            }

            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);

            Calendar first = (Calendar) cal.clone();
            first.add(Calendar.DAY_OF_WEEK,
                    first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));


            Calendar last = (Calendar) first.clone();
            last.add(Calendar.DAY_OF_YEAR, 5);


            SimpleDateFormat df = new SimpleDateFormat("MM-dd");
            dateEditText.setText(df.format(first.getTime()) + " - " + df.format(last.getTime()));


        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void SearchByStudyPrograms(View view) {
        Button studyProgramButton = (Button) findViewById(R.id.StudyProgramButton);
        Button teacherButton = (Button) findViewById(R.id.TeacherButton);
        Button classRoomButton = (Button) findViewById(R.id.ClassRoomButton);
        TextView text = (TextView) findViewById(R.id.text1);

        studyProgramButton.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        teacherButton.setBackgroundColor((ContextCompat.getColor(this,R.color.standardBackground)));
        classRoomButton.setBackgroundColor((ContextCompat.getColor(this,R.color.standardBackground)));
        text.setText(studyPrograms);

        setYearsLayout();
        new GetStudyPrograms().execute();
    }

    public void SearchByTeachers(View view) {
        Button studyProgramButton = (Button) findViewById(R.id.StudyProgramButton);
        Button teacherButton = (Button) findViewById(R.id.TeacherButton);
        Button classRoomButton = (Button) findViewById(R.id.ClassRoomButton);
        TextView text = (TextView) findViewById(R.id.text1);

        studyProgramButton.setBackgroundColor(ContextCompat.getColor(this,R.color.standardBackground));
        teacherButton.setBackgroundColor(ContextCompat.getColor(this,R.color.clickedBackground));
        classRoomButton.setBackgroundColor(ContextCompat.getColor(this,R.color.standardBackground));
        text.setText(R.string.Teachers);

        removeYearsLayout();
        new GetTeachers().execute();
    }

    public void SearchByClassRoom(View view) {
        Button studyProgramButton = (Button) findViewById(R.id.StudyProgramButton);
        Button teacherButton = (Button) findViewById(R.id.TeacherButton);
        Button classRoomButton = (Button) findViewById(R.id.ClassRoomButton);
        TextView text = (TextView) findViewById(R.id.text1);

        studyProgramButton.setBackgroundColor((ContextCompat.getColor(this,R.color.standardBackground)));
        teacherButton.setBackgroundColor((ContextCompat.getColor(this,R.color.standardBackground)));
        classRoomButton.setBackgroundColor((ContextCompat.getColor(this,R.color.clickedBackground)));
        text.setText(R.string.ClassRooms);

        removeYearsLayout();
        new GetClassRooms().execute();
    }


    private  class GetTeachers extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "http://raspored.ffuis.edu.ba/breeze/data/Teachers?$orderby=FirstName%2CLastName&$select=Id%2CFirstName%2CLastName";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONArray teachers = new JSONArray(jsonStr);

                    for (int i = 0; i < teachers.length(); i++) {
                        JSONObject c = teachers.getJSONObject(i);
                        if(c != null)
                        {
                            String firstName = c.getString("FirstName");
                            String lastName = c.getString("LastName");
                            int id = c.getInt("Id");

                            teacherList.add(new Teacher(id,firstName,lastName));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });}

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Интернет конекција није пронаћена, повуците надоле ѕа освјежавање!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setAdapter(new ArrayAdapter<>(SchedulesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    teacherList));

        }
    }
    private  class GetStudyPrograms extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "http://raspored.ffuis.edu.ba/breeze/data/StudyPrograms?$orderby=Name&$select=Id%2CName";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONArray studyPrograms = new JSONArray(jsonStr);

                    for (int i = 0; i < studyPrograms.length(); i++) {
                        JSONObject c = studyPrograms.getJSONObject(i);
                        if(c != null)
                        {
                            String name = c.getString("Name");
                            int id = c.getInt("Id");

                            studyProgramList.add(new StudyProgram(id,name));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });}

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Интернет конекција није пронаћена, повуците надоле ѕа освјежавање!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setAdapter(new ArrayAdapter<>(SchedulesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    studyProgramList));


        }
    }
    private  class GetClassRooms  extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "http://raspored.ffuis.edu.ba/breeze/data/Classrooms?$orderby=Name&;$select=Id%2CName";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONArray classRooms = new JSONArray(jsonStr);

                    for (int i = 0; i < classRooms.length(); i++) {
                        JSONObject c = classRooms.getJSONObject(i);
                        if(c != null)
                        {
                            String name = c.getString("Name");
                            int id = c.getInt("Id");

                            classRoomList.add(new ClassRoom(id,name));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });}

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Интернет конекција није пронаћена, повуците надоле ѕа освјежавање!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setAdapter(new ArrayAdapter<>(SchedulesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    classRoomList));

        }
    }


    public void setYearsLayout(){
        findViewById(R.id.years1).setVisibility(View.VISIBLE);
        findViewById(R.id.years2).setVisibility(View.VISIBLE);
    }
    public void removeYearsLayout(){
        findViewById(R.id.years1).setVisibility(View.GONE);
        findViewById(R.id.years2).setVisibility(View.GONE);
    }

    private  class FillClassRooms  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {

            HttpHandler sh = new HttpHandler();
            String url = strings[0];
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONArray classRooms = new JSONArray(jsonStr);

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });}

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Интернет конекција није пронаћена, повуците надоле ѕа освјежавање!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }
    }
}
