package com.crisandsoft.easterncalc;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import android.net.http.SslCertificate;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
	{

		private static final String TAG = "CRISANDSOFT";

		TextView _zona_texto;
		EditText _seleccion_anno;

		Resources _res;

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);

				_zona_texto = (TextView) findViewById(R.id.textView1);
				_seleccion_anno = (EditText) findViewById(R.id.editText1);

				_seleccion_anno.addTextChangedListener(new TextWatcher()
					{
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after)
							{
								// no nos interesa esa callback
							}

						public void onTextChanged(CharSequence s, int start, int before,
								int count)
							{
								if (s.length() > 0)
									{
										GregorianCalendar greg = calcular_domingo_resurrecion(Integer
												.parseInt(_seleccion_anno.getText().toString()));
										poblar_fechas(greg);
										greg=null;
									}
								
							}

						public void afterTextChanged(Editable S)
							{
								// no nos interesa esa callback
							}
					});
				_res = this.getResources();

			}

		public void poblar_fechas(GregorianCalendar domingo_resurreccion)
			{

				String[] fechas_relevantes = _res
						.getStringArray(R.array.fechas_relevantes);

				int[] desfases_relevantes = _res
						.getIntArray(R.array.desafes_fechas_relevantes);
				GregorianCalendar cal_aux;
				cal_aux = domingo_resurreccion;
				String linea_texto = getText(R.string.domingo_ramos) + " " + " "
						+ cal_aux.get(Calendar.DATE) + "/" + (cal_aux.get(Calendar.MONTH) +1)
						+ "\n";

				int cuantas = fechas_relevantes.length;

				for (int cont = 0; cont < cuantas; cont++)
					{
						// if (Log.isLoggable(TAG, Log.DEBUG))
						// {
						// Log.d(TAG, "[" + cont + "][" + desfases_relevantes[cont] + "]["
						// + fechas_relevantes[cont] + "]");
						// }
						cal_aux = (GregorianCalendar) domingo_resurreccion.clone();
						cal_aux.add(Calendar.DAY_OF_YEAR, desfases_relevantes[cont]);
						linea_texto += fechas_relevantes[cont] + " "
								+ cal_aux.get(Calendar.DATE) + "/"
								+ (cal_aux.get(Calendar.MONTH)+1) + "\n";
						cal_aux = null; // liberamos
					}
				_zona_texto.setText(linea_texto);

			}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				// Inflate the menu; this adds items to the action bar if it is
				// present.
				getMenuInflater().inflate(R.menu.activity_main, menu);
				return true;
			}

		public GregorianCalendar calcular_domingo_resurrecion(int anno)
			{

				int a, b, c, d, e, day, month;

				a = anno % 19;
				b = anno >> 2;
				c = b;// 25 + 1
				d = (c * 3) >> 2;
				e = ((a * 19) - ((c * 8 + 5) / 25) + d + 15) % 30;
				e += (29578 - a - e * 32) >> 10;
				e -= ((anno % 7) + b - d + e + 2) % 7;
				d = e >> 5;
				day = e - d * 31;
				month = d + 3;

				Log.d(TAG, "[" + anno + "][" + month + "][" + day + "]");

				GregorianCalendar dr = new GregorianCalendar(anno, month - 1, day); // los
																																						// meses
																																						// empiezan
																																						// en
																																						// 0
				Log.d(
						TAG,
						"fecha obtenida [" + dr.get(Calendar.DATE) + "]/["
								+ dr.get(Calendar.MONTH) + "]");
				// get sunday
				dr.add(Calendar.DAY_OF_YEAR, 2);
				Log.d(TAG,
						"ajuste [" + dr.get(Calendar.DATE) + "]/[" + dr.get(Calendar.MONTH)
								+ "]");
				return dr;
			}

	}
