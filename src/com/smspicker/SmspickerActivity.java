package com.smspicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SmspickerActivity extends ListActivity {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final ListView lv = (ListView) findViewById(android.R.id.list);
		populateList();

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> selectedPosition = (HashMap<String, String>) lv
						.getItemAtPosition(arg2);

				Toast.makeText(getApplicationContext(),
						"Number : " + selectedPosition.get("Number"),
						Toast.LENGTH_SHORT).show();

				// intent.putExtra("number", selectedPosition.get("Number"));
				// setResult(RESULT_OK, intent);

			}
		});

	}

	public ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	private void populateList() {
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.sms_row,
				new String[] { "Body", "Number", "Date" }, new int[] {
						R.id.textMessage, R.id.textNumber, R.id.textDate }

		);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd MMM yyyy HH:mm:ss");

		Uri uri = Uri.parse("content://sms/inbox");
		Cursor msg_cursor = getContentResolver().query(uri, null, null, null,
				"DATE desc");

		startManagingCursor(msg_cursor);

		if (msg_cursor.getCount() == 0) {
			Toast.makeText(this, "No Messages Found", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		msg_cursor.moveToFirst();
		while (!msg_cursor.isAfterLast()) {
			HashMap<String, String> temp = new HashMap<String, String>();

			String body = msg_cursor.getString(
					msg_cursor.getColumnIndexOrThrow("body")).toString();
			Log.v("message body", body);
			if (body.equals("null")) {
				temp.put("Body", "");
			} else {
				temp.put("Body", body);
			}

			String Number = msg_cursor.getString(
					msg_cursor.getColumnIndexOrThrow("address")).toString();
			temp.put("Number", Number);
			Log.v("number", Number);

			long dateInMillis = Long.valueOf(msg_cursor.getString(msg_cursor
					.getColumnIndexOrThrow("date")));

			Calendar calendar = (Calendar) getCallDate(dateInMillis);
			temp.put("Date", formatter.format(calendar.getTime()));
			list.add(temp);
			msg_cursor.moveToNext();
		}
		setListAdapter(adapter);

	}

	public Object getCallDate(long dateInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInMillis);
		return calendar;
	}

}