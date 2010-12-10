package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EmailActivity extends Activity {
	private Button clickBtn;
	private EditText sendToEditText;
	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		clickBtn = (Button) findViewById(R.id.button_send);
		clickBtn.setText(this.getResources().getString(R.string.button_send));
		clickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String sendTo;
				sendToEditText = (EditText) findViewById(R.id.TextViewSendTo);
				sendTo = sendToEditText.getText().toString().trim();
		
				if (sendTo.contains("@")){
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					
					
					String[] recipients = new String[]{sendTo, "",};
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getIntent().getStringExtra("EMAIL_SUBJECT"));
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getIntent().getStringExtra("EMAIL_MESSAGE"));
					
					emailIntent.setType("text/plain");
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					finish();					
				}
			}
		});

	}



}
