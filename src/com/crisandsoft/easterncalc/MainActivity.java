package com.crisandsoft.easterncalc;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{

    private static final String TAG = "CRISANDSOFT";

    TextView _textView;
    EditText _yearEdit;

    Resources _res;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _textView = (TextView) findViewById(R.id.textView1);
        _yearEdit = (EditText) findViewById(R.id.editText1);

        _yearEdit.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
                // Not interested in this callback
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                if (s.length() > 0)
                {
                    GregorianCalendar gregorianCalendar = getGoodSunday(Integer
                            .parseInt(_yearEdit.getText().toString()));
                    getRelevantDates(gregorianCalendar);
                    gregorianCalendar = null;
                }

            }

            public void afterTextChanged(Editable S)
            {
                // Not intersested
            }
        });
        _res = this.getResources();

    }

    /**
     * Get all the relevant dates.
     * <p>
     * They are calculated based on goodSunday.
     * </p>
     *
     * @param goodSunday Pivot around which we will calc
     */
    public void getRelevantDates(GregorianCalendar goodSunday)
    {

        String[] _resStringArray = _res
                .getStringArray(R.array.fechas_relevantes);

        int[] desfases_relevantes = _res
                .getIntArray(R.array.desafes_fechas_relevantes);
        GregorianCalendar cal_aux;
        cal_aux = goodSunday;
        String textLine = getText(R.string.domingo_resurrecion) + " " + " "
                + cal_aux.get(Calendar.DATE) + "/" + (cal_aux.get(Calendar.MONTH) + 1)
                + "\n";

        int cuantas = _resStringArray.length;

        // now, we calculate the rest of values based on goodSunday
        for (int cont = 0; cont < cuantas; cont++)
        {
            // if (Log.isLoggable(TAG, Log.DEBUG))
            // {
            // Log.d(TAG, "[" + cont + "][" + desfases_relevantes[cont] + "]["
            // + _resStringArray[cont] + "]");
            // }
            cal_aux = (GregorianCalendar) goodSunday.clone();
            cal_aux.add(Calendar.DAY_OF_YEAR, desfases_relevantes[cont]);
            textLine += _resStringArray[cont] + " "
                    + cal_aux.get(Calendar.DATE) + "/"
                    + (cal_aux.get(Calendar.MONTH) + 1) + "\n";
            cal_aux = null; // liberamos
        }
        _textView.setText(textLine);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is
        // present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Find out the date of Good Sunday for a given year
     *
     * @param target_year which year are we interested in?
     * @return GoodSunday
     */
    public GregorianCalendar getGoodSunday(int target_year)
    {

        int a, b, c, d, e, day, month;

        a = target_year % 19;
        b = target_year >> 2;
        c = b;// 25 + 1
        d = (c * 3) >> 2;
        e = ((a * 19) - ((c * 8 + 5) / 25) + d + 15) % 30;
        e += (29578 - a - e * 32) >> 10;
        e -= ((target_year % 7) + b - d + e + 2) % 7;
        d = e >> 5;
        day = e - d * 31;
        month = d + 3;
        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG, "[" + target_year + "][" + month + "][" + day + "]");
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar(target_year, month - 1, day); // los
        // meses
        // empiezan
        // en
        // 0
        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(
                    TAG,
                    "fecha obtenida [" + gregorianCalendar.get(Calendar.DATE) + "]/["
                            + gregorianCalendar.get(Calendar.MONTH) + "]");
        }
        // get sunday
        gregorianCalendar.add(Calendar.DAY_OF_YEAR, 2);
        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG,
                    "ajuste [" + gregorianCalendar.get(Calendar.DATE) + "]/[" + gregorianCalendar.get(Calendar.MONTH)
                            + "]");
        }
        return gregorianCalendar;
    }

    /**
     * Get the Good Sunday ( easternSunday )
     * <p>
     *     fetched from https://en.wikipedia.org/wiki/Computus
     * </p>
     * @param year which year re we interested in?
     * @return easter Sunday as calendar
     */
    public GregorianCalendar getEasterDate(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int n = (h + l - 7 * m + 114) / 31;
        int p = (h + l - 7 * m + 114) % 31;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(year,n-1, p+1);
        return gregorianCalendar;
    }

}
