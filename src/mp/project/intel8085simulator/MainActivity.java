package mp.project.intel8085simulator;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		View.OnFocusChangeListener, OnTouchListener {

	Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, bA, bB, bC, bD, bE, bF,
			prev, next, reset, exm, exr, go, exec, vi, singlestep, getins;
	int s = 0, z = 0, in = 0, ac = 0, p = 0, cy = 0;
	int inficount = 0;
	View space;
	RelativeLayout ll;
	LinearLayout flagbox;
	EditText t1, t2;
	TextView pcsp, info, sign, zero, acarry, parity, carry;
	int flagexm = 0, flagexr = 0, goflag = 0, regindex, temp = 0,
			firsttime = 1, firsttime2 = 1, t2focusflag = 0, resetflag = 0,
			visflag = 0, alterfocusflag = 0, ssflag = 0;
	int flagn = 0;
	CharSequence tempcsq;
	String tempstr, dispstr;
	
	Random generator = new Random();
	int rand;
	int pc;
	int sp;

	char[][] mem = new char[65536][2];
	char[][] abcdehlf = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 }, { 0, 0 }, { 0, 0 } }; // 8 by 2

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		b0 = (Button) findViewById(R.id.b0);
		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		b6 = (Button) findViewById(R.id.b6);
		b7 = (Button) findViewById(R.id.b7);
		b8 = (Button) findViewById(R.id.b8);
		b9 = (Button) findViewById(R.id.b9);
		bA = (Button) findViewById(R.id.bA);
		bB = (Button) findViewById(R.id.bB);
		bC = (Button) findViewById(R.id.bC);
		bD = (Button) findViewById(R.id.bD);
		bE = (Button) findViewById(R.id.bE);
		bF = (Button) findViewById(R.id.bF);
		prev = (Button) findViewById(R.id.prev);
		next = (Button) findViewById(R.id.next);
		reset = (Button) findViewById(R.id.reset);
		exm = (Button) findViewById(R.id.exm);
		exr = (Button) findViewById(R.id.exr);
		go = (Button) findViewById(R.id.go);
		exec = (Button) findViewById(R.id.exec);
		t1 = (EditText) findViewById(R.id.t1);
		t2 = (EditText) findViewById(R.id.t2);
		ll = (RelativeLayout) findViewById(R.id.ll);
		vi = (Button) findViewById(R.id.vi);
		pcsp = (TextView) findViewById(R.id.pcsp);
		info = (TextView) findViewById(R.id.info);
		flagbox = (LinearLayout) findViewById(R.id.flagbox);
		sign = (TextView) findViewById(R.id.sign);
		zero = (TextView) findViewById(R.id.zero);
		acarry = (TextView) findViewById(R.id.acarry);
		parity = (TextView) findViewById(R.id.parity);
		carry = (TextView) findViewById(R.id.carry);
		space = findViewById(R.id.space);
		singlestep = (Button) findViewById(R.id.singlestep);
		getins = (Button) findViewById(R.id.getins);
		
		b0.setOnClickListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		b5.setOnClickListener(this);
		b6.setOnClickListener(this);
		b7.setOnClickListener(this);
		b8.setOnClickListener(this);
		b9.setOnClickListener(this);
		bA.setOnClickListener(this);
		bB.setOnClickListener(this);
		bC.setOnClickListener(this);
		bD.setOnClickListener(this);
		bE.setOnClickListener(this);
		bF.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);
		reset.setOnClickListener(this);
		exm.setOnClickListener(this);
		exr.setOnClickListener(this);
		go.setOnClickListener(this);
		exec.setOnClickListener(this);
		singlestep.setOnClickListener(this);
		getins.setOnClickListener(this);
		
		t1.setOnTouchListener(this);
		t2.setOnTouchListener(this);
		t1.setOnFocusChangeListener(this);
		t2.setOnFocusChangeListener(this);

		getins.setText("?");
		
		vi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Vibration
				b0.performHapticFeedback(3);

				Intent i = new Intent("INS");
				i.putExtra("Address", t1.getText().toString());
				startActivityForResult(i, 1);

			}

		});

		t1.setFocusable(false);
		t2.setFocusable(false);
		reset.performClick();

		
		// Initializing memory with random data (from instruction set)
		try {
			
			for (int i = 0; i < 65536; i++) {
				rand = generator.nextInt(232);
				tempstr = Map.str2[rand];
				
				mem[i][0] = tempstr.charAt(0);
				mem[i][1] = tempstr.charAt(1);
			
			}
		} catch (Exception e) {
			// Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		
		//Adding HLT instructions in some addresses to prevent infinite loops
		for(int i = 2500; i<65000; i+=2500)
		{
			mem[i][0] = '7';
			mem[i][1] = '6';
		}
		
		

		// Initializing RST addresses with HLT
		// RST 0
		 //mem[0][0] = '7';
		 //mem[0][1] = '6';
		// RST 1
		  // mem[8][0] = '7';
		  //mem[8][1] = '6';
		// RST 2
		 //mem[16][0] = '7';
		 //mem[16][1] = '6';
		// RST 3
		//	mem[24][0] = '7';
		//	mem[24][1] = '6';
		// RST 4
		 //mem[32][0] = '7';
		 //mem[32][1] = '6';
		// RST 5
			mem[40][0] = '7';
			mem[40][1] = '6';
		// RST 6
		 //mem[48][0] = '7';
		 //mem[48][1] = '6';
		// RST 7
		//   mem[56][0] = '7';
		//   mem[56][1] = '6';

		// Initializing registers with data
		resetreg();
		sp = 65535; // FFFF
		pc = generator.nextInt(40960); // 0000 to 9FFF

		// displayMsg("Text fields can be edited by touching them:", 0, 0);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// Vibration
		b0.performHapticFeedback(3);

		switch (v.getId()) {
		case R.id.b0:
		case R.id.b1:
		case R.id.b2:
		case R.id.b3:
		case R.id.b4:
		case R.id.b5:
		case R.id.b6:
		case R.id.b7:
		case R.id.b8:
		case R.id.b9:
		case R.id.bA:
		case R.id.bB:
		case R.id.bC:
		case R.id.bD:
		case R.id.bE:
		case R.id.bF:

			/*
			 * ll.setBackgroundColor(Color.YELLOW);
			 * 
			 * Runnable r = new Runnable() { public void run() {
			 * ll.setBackgroundColor(Color.BLACK);; } };
			 * 
			 * Handler h = new Handler(); h.postDelayed(r, 500);
			 */

			Button b = (Button) v;

			if (flagexm == 0 && flagexr == 0 && goflag == 0
					&& t1.getText().toString().equals("-UPS") && firsttime == 1) {

				exm.performClick();

			}

			if (flagexm == 1) {
				// toasts used during debugging
				// Toast.makeText(this, "a" +
				// t1.getText().toString() + "b", Toast.LENGTH_SHORT).show();
				// Toast.makeText(this, "a" +
				// t2.getText().toString() + "b", Toast.LENGTH_SHORT).show();

				if (t1.getText().toString().equals("-UPS")) {
					t1.setText(null);
					t2.setText(null);
				}

				if (flagn == 0) {

					tempstr = t1.getText().toString() + b.getText().toString();

					if (tempstr.length() <= 4)
						tempstr = tempstr.substring(0);
					else
						tempstr = tempstr.substring(1);

					// Toast.makeText(this, tmp,
					// Toast.LENGTH_SHORT).show();
					t1.setText(tempstr);

					pc = Integer.parseInt(tempstr, 16);
					firsttime2 = 0;

				}

				else { // flagn = 1

					String tmp = t2.getText().toString()
							+ b.getText().toString();

					if (tmp.length() <= 2)
						tmp = tmp.substring(0);
					else
						tmp = tmp.substring(1);

					// Saving data in memory
					tempstr = t1.getText().toString();
					temp = Integer.parseInt(tempstr, 16);

					mem[temp][0] = tmp.charAt(0);
					mem[temp][1] = tmp.charAt(1);
					pc = temp;

					t2.setText(tmp);

				}

			}

			if (goflag == 1) {

				if (t1.getText().toString().equals("-UPS")) {
					t1.setText(null);

				}

				tempstr = t1.getText().toString() + b.getText().toString();

				if (tempstr.length() <= 4)
					tempstr = tempstr.substring(0);
				else
					tempstr = tempstr.substring(1);

				// Toast.makeText(this, tmp,
				// Toast.LENGTH_SHORT).show();
				t1.setText(tempstr);

				pc = Integer.parseInt(tempstr, 16);
				firsttime2 = 0;

			}

			if (flagexr == 1) {

				t1.setText(((Button) v).getText());
				regindex = Integer.parseInt((String) ((Button) v).getTag());
				tempcsq = Character.toString(abcdehlf[regindex][0])
						+ Character.toString(abcdehlf[regindex][1]);
				t2.setText(tempcsq);

			}

			break;

		case R.id.exm:

			flagexm = 1;

			flagn = 0;

			t1.setText("");
			t2.setText("");

			t2.setFocusableInTouchMode(true);
			t1.setFocusableInTouchMode(true);
			t1.requestFocus();

			// initializing t1 with PC and t2 with value at PC
			if (firsttime == 1) {
				t1.setText("");
				t2.setText("");
				firsttime = 0;

			} else {
				if (firsttime2 == 0) {
					tempstr = Integer.toHexString(pc).toUpperCase();
					t1.setText(prefixz(tempstr));

					tempstr = Character.toString(mem[pc][0]) + mem[pc][1];
					t2.setText(tempstr);

					if (alterfocusflag == 0) {
						t2.requestFocus();
						alterfocusflag = 1;
						flagn = 1;
					}

					else {
						t1.requestFocus();
						alterfocusflag = 0;
						flagn = 0;
					}

					if (t2focusflag == 0 && flagexr != 1) // To transfer focus
															// to t2 when exm is
					// pressed after go(after entering some data in t1). Tried
					// to do it just
					// by t2.requestFocus() but it just
					// didn't work. Even tried calling t2.requestFocus()
					// multiple
					// times; didn't work.
					{
						exm.setSoundEffectsEnabled(false);
						b0.setHapticFeedbackEnabled(false);
						t2focusflag = 1;
						exm.performClick();
						exm.performClick();
						exm.setSoundEffectsEnabled(true);
						b0.setHapticFeedbackEnabled(true);
					}

				} else {
					if (flagexr == 1) // For the case when exm is pressed after
										// exr
					{
						t1.setText("");
						t2.setText("");
					}

					if (t1.getText().toString().length() != 0) // Code for
																// handling the
																// case when exm
																// button is
																// pressed
																// repeatedly
																// with no input
					{
						firsttime2 = 0;

					} else if (t1.getText().toString().length() == 0
							&& goflag != 1 && flagexr != 1) {
						displayMsg("Enter address first.", 0, 0);

					}

				}
			}

			flagexr = 0;
			resetflag = 0;
			goflag = 0;

			makebuttonsvisible();
			getins.setVisibility(0);	//Visible
			b8.setText("8");
			b9.setText("9");

			break;

		case R.id.exr:
			flagexm = 0;
			flagexr = 1;
			t2focusflag = 0;
			alterfocusflag = 0;
			flagn = 0;
			goflag = 0;
			resetflag = 0;

			t1.setFocusable(false);
			t2.setFocusable(false);
			vi.setVisibility(8); // Gone
			getins.setVisibility(8);	//Gone
			t2.setVisibility(0); // Visible

			flagbox.setVisibility(0); // Visible
			space.setVisibility(8); // Gone

			b0.setVisibility(8); // Gone
			b1.setVisibility(8);
			b2.setVisibility(8);
			b3.setVisibility(8);
			
			info.setVisibility(0); // Visible
			pcsp.setVisibility(0);

			b4.setVisibility(8); // Gone
			b5.setVisibility(8);
			b6.setVisibility(8);
			b7.setVisibility(8);

			b8.setText("H");
			b9.setText("L");

			t1.setText("A");

			tempcsq = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			t2.setText(tempcsq);

			// Toast.makeText(this, "Here",
			// Toast.LENGTH_SHORT).show();

			// Displaying PC and SP
			tempstr = Integer.toHexString(pc).toUpperCase();

			while (tempstr.length() < 4)
				tempstr = "0" + tempstr;

			String tempstr2 = Integer.toHexString(sp).toUpperCase();

			while (tempstr2.length() < 4)
				tempstr2 = "0" + tempstr2;

			pcsp.setText("PC->" + tempstr + "           " + "SP->" + tempstr2);

			// Displaying BC, DE, HL pairs
			tempstr = "BC->" + Character.toString(abcdehlf[1][0])
					+ Character.toString(abcdehlf[1][1])
					+ Character.toString(abcdehlf[2][0])
					+ Character.toString(abcdehlf[2][1]);
			tempstr = tempstr + "     " + "DE->"
					+ Character.toString(abcdehlf[3][0])
					+ Character.toString(abcdehlf[3][1])
					+ Character.toString(abcdehlf[4][0])
					+ Character.toString(abcdehlf[4][1]);
			tempstr = tempstr + "     " + "HL->"
					+ Character.toString(abcdehlf[5][0])
					+ Character.toString(abcdehlf[5][1])
					+ Character.toString(abcdehlf[6][0])
					+ Character.toString(abcdehlf[6][1]);
			info.setText(tempstr);

			tempstr = Character.toString(abcdehlf[7][0])
					+ Character.toString(abcdehlf[7][1]);
			temp = Integer.parseInt(tempstr, 16);
			tempstr = Integer.toBinaryString(temp);
			while (tempstr.length() < 8)
				tempstr = "0" + tempstr;

			int origcolor = 0xffffffff; // White
			pcsp.setTextColor(origcolor);
			info.setTextColor(origcolor);

			if (tempstr.charAt(0) == '1')
				sign.setTextColor(0xffff0000);
			else
				sign.setTextColor(origcolor);

			if (tempstr.charAt(1) == '1')
				zero.setTextColor(0xffff0000);
			else
				zero.setTextColor(origcolor);

			if (tempstr.charAt(3) == '1')
				acarry.setTextColor(0xffff0000);
			else
				acarry.setTextColor(origcolor);

			if (tempstr.charAt(5) == '1')
				parity.setTextColor(0xffff0000);
			else
				parity.setTextColor(origcolor);

			if (tempstr.charAt(7) == '1')
				carry.setTextColor(0xffff0000);
			else
				carry.setTextColor(origcolor);

			break;

		case R.id.reset:
			resetflag = 1;
			flagexm = 0;
			flagexr = 0;
			goflag = 0;
			flagn = 0;
			t2focusflag = 0;
			alterfocusflag = 0;
			firsttime = 1;
			firsttime2 = 1;

			sp = 65535; // FFFF

			resetreg();

			t1.setFocusable(false);
			t2.setFocusable(false);

			t1.setText("-UPS");
			t2.setText("85");

			makebuttonsvisible();
			b8.setText("8");
			b9.setText("9");
			
			getins.setVisibility(8);	//Gone
			
			break;

		case R.id.next:

			nextcode();
			break;

		case R.id.prev:

			prevcode();
			break;

		case R.id.go:

			flagexm = 0;
			t2focusflag = 0;
			alterfocusflag = 0;
			flagn = 0;
			resetflag = 0;

			// initializing t1 with PC
			if (firsttime == 1) {
				t1.setText("");
				firsttime = 0;
			} else {
				if (firsttime2 == 0) {
					tempstr = Integer.toString(pc, 16).toUpperCase();
					t1.setText(prefixz(tempstr));

				} else { // For the case when go is pressed after exr
					if (flagexr == 1) {
						t1.setText("");

					}

					if (t1.getText().toString().length() != 0) // Code for
						// handling the
						// case when go
						// button is
						// pressed
						// repeatedly
						// with no input
						firsttime2 = 0;
				}
			}

			flagexr = 0;

			// tempstr = Integer.toString(pc, 16).toUpperCase();
			// t1.setText(prefixz(tempstr));

			t2.setText("");
			t1.setFocusable(false);
			t2.setFocusable(false);

			goflag = 1;

			makebuttonsvisible();
			b8.setText("8");
			b9.setText("9");

			vi.setVisibility(8); // Gone
			t2.setVisibility(8); // Gone
			getins.setVisibility(8); 	//Gone

			break;

		case R.id.singlestep:
			ssflag = 1;
		case R.id.exec:

			inficount = 0;
			b8.setText("8");
			b9.setText("9");

			// Toast.makeText(this, Integer.toString(firsttime2), 2000).show();
			if ((goflag == 0 && flagexm == 0 && flagexr == 0)
					|| (firsttime2 == 1) || (flagexr == 1) || (flagexm == 1)
					|| (goflag == 0)) {
				{
					displayMsg("Invalid Operation.", 0, 0);
					ssflag = 0;
				}

				// Log.d("tag", "Invalid Operation: Exec");
			}

			else {

				if (goflag == 1) {

					tempstr = t1.getText().toString();

					if (tempstr.length() != 0) {

								try {

									pc = Integer.parseInt(tempstr, 16);

									int overflowflag = 0;
									inficount = 0;
									while (!(mem[pc][0] == '7' && mem[pc][1] == '6')) {

										if (pc >= 65534) {
											dispstr = "Memory Overflow!";
											pc = 65535;
											overflowflag = 1;
											break;
										} 
										else if(inficount>100000)
										{
											dispstr = "Program taking too long to execute!";
											inficount = 0;
											break;
										}
										else {
											inficount++;
											execute();
											if (ssflag == 1)
												break;
										}

									}

									if (mem[pc][0] == '7' && mem[pc][1] == '6'
											&& ssflag != 1) {

										tempstr = Integer.toHexString(pc)
												.toUpperCase();
										while (tempstr.length() < 4)
											tempstr = "0" + tempstr;
										dispstr = "HLT encountered at "
												+ tempstr;

									}

									else if (ssflag == 1 && overflowflag != 1) {
										dispstr = "PC = "
												+ prefixz(Integer.toHexString(
														pc).toUpperCase());
									}

									
								}

								catch (Exception e) {

									tempstr = Integer.toHexString(pc)
											.toUpperCase();
									while (tempstr.length() < 4)
										tempstr = "0" + tempstr;

									dispstr = e.toString();
									// Toast.makeText(this, e.toString(),
									// Toast.LENGTH_LONG).show();
									// Toast.makeText(this, tempstr,
									// Toast.LENGTH_LONG).show();

								}
								
					
					
					
						b0.setHapticFeedbackEnabled(false);
						exr.performClick();
						b0.setHapticFeedbackEnabled(true);
					

					displayMsg(dispstr, 0, 0);
					
					/*

						t.start();

						try {
							int cnt = 0;

							while (cnt < 80) {
								cnt++;
								if (t.isAlive())
									Thread.sleep(50);
								else
									break;
							}

							

							if (cnt < 80) {

								displayMsg(dispstr, 0, 0);

							} else {
								displayMsg(
										"Program taking too long to execute!",
										0, 0);
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
*/
					}

				}

				// makebuttonsvisible();
				// t1.setFocusable(false);
				// t2.setFocusable(false);
				// firsttime = 1;
				// firsttime2 = 1;

				goflag = 0;
				flagexm = 0;
				ssflag = 0;
				resetflag = 0;
				inficount = 0;
			}

			break;

			
		case R.id.getins:
			
			
			tempstr = t2.getText().toString();
			if(tempstr.length()<2)
			{
				displayMsg("Enter instruction first!", 0, 0);
				break;
			}
			
			int pos = 232;
			for(int i=0; i<232; i++)
			{
				if(Map.str2[i].equals(tempstr))
				{
					pos = i;
				}
				
			}
			
				
			
				
			if(pos<232)
				{
					Toast t;
					t = Toast.makeText(this,Map.str1[pos], Toast.LENGTH_SHORT);
					t.setGravity(Gravity.TOP, 0, 150);
					t.show();
				}
			else
				{
					displayMsg("Invalid Instruction!", 0, 0);
				}
			
			break;
		}

	}

	private void displayMsg(String s, int x, int f) {

		Toast t = new Toast(this);
		if (f == 0)
			t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
		else
			t = Toast.makeText(this, s, Toast.LENGTH_LONG);

		t.setGravity(Gravity.TOP, x, 150);
		t.show();

		b0.setEnabled(false);
		b1.setEnabled(false);
		b2.setEnabled(false);
		b3.setEnabled(false);
		b4.setEnabled(false);
		b5.setEnabled(false);
		b6.setEnabled(false);
		b7.setEnabled(false);
		b8.setEnabled(false);
		b9.setEnabled(false);
		bA.setEnabled(false);
		bB.setEnabled(false);
		bC.setEnabled(false);
		bD.setEnabled(false);
		bE.setEnabled(false);
		bF.setEnabled(false);
		t1.setEnabled(false);
		t2.setEnabled(false);
		next.setEnabled(false);
		prev.setEnabled(false);
		reset.setEnabled(false);
		exr.setEnabled(false);
		exm.setEnabled(false);
		go.setEnabled(false);
		exec.setEnabled(false);
		vi.setEnabled(false);
		singlestep.setEnabled(false);
		getins.setEnabled(false);
		
		if (info.getVisibility() == 0 && pcsp.getVisibility() == 0
				&& flagbox.getVisibility() == 0) {
			info.setVisibility(4);
			pcsp.setVisibility(4);
			flagbox.setVisibility(4);
			visflag = 1;
		} else
			visflag = 0;

		Runnable r = new Runnable() {
			@Override
			public void run() {

				b0.setEnabled(true);
				b1.setEnabled(true);
				b2.setEnabled(true);
				b3.setEnabled(true);
				b4.setEnabled(true);
				b5.setEnabled(true);
				b6.setEnabled(true);
				b7.setEnabled(true);
				b8.setEnabled(true);
				b9.setEnabled(true);
				bA.setEnabled(true);
				bB.setEnabled(true);
				bC.setEnabled(true);
				bD.setEnabled(true);
				bE.setEnabled(true);
				bF.setEnabled(true);
				t1.setEnabled(true);
				t2.setEnabled(true);
				next.setEnabled(true);
				prev.setEnabled(true);
				reset.setEnabled(true);
				exr.setEnabled(true);
				exm.setEnabled(true);
				go.setEnabled(true);
				exec.setEnabled(true);
				vi.setEnabled(true);
				singlestep.setEnabled(true);
				getins.setEnabled(true);

				if (visflag == 1) {
					info.setVisibility(0);
					pcsp.setVisibility(0);
					flagbox.setVisibility(0);
					visflag = 0;
				}
			}
		};

		Handler h = new Handler();
		h.postDelayed(r, 700);

	}

	private void makebuttonsvisible() {

		b0.setVisibility(0); // Visible
		b1.setVisibility(0);
		b2.setVisibility(0);
		b3.setVisibility(0);
		b4.setVisibility(0);
		b5.setVisibility(0);
		b6.setVisibility(0);
		b7.setVisibility(0);
		vi.setVisibility(0);
		t2.setVisibility(0);
		space.setVisibility(0);

		info.setVisibility(8); // Gone
		pcsp.setVisibility(8);
		flagbox.setVisibility(8);

	}

	private void nextcode() {

		if (flagn == 1) {
			if (!t2.hasFocus()) {
				t2.requestFocus();
				alterfocusflag = 1;
			}
		} else {
			if (!t1.hasFocus()) {
				t1.requestFocus();
			}
		}

		// Code to show "Invalid Operation" message
		if (t1.getText().toString().equals("-UPS"))
			displayMsg("Invalid Operation.", 0, 0);

		// Code for 'examine memory' (next button)
		if (flagexm == 1) {
			temp = 0; // Initialized for the sake of resolving errors

			try {
				temp = Integer.parseInt(t1.getText().toString(), 16);
			} catch (Exception e) {
				// exception happens when the text in t1 is ""
				// Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
				displayMsg("Invalid Operation.", 0, 0);
			}

			// Toast below was used during debugging
			// Toast.makeText(this,
			// Boolean.toString(t1.getText().toString().equals("-UPS")),
			// Toast.LENGTH_SHORT).show();

			if (flagn == 0 && !(t1.getText().toString().equals("-UPS"))
					&& t1.getText().toString().length() != 0) {

				tempstr = t1.getText().toString();

				// Prefixing input in t1 with zeroes
				t1.setText(prefixz(tempstr));

				flagn = 1;

				// Focusing
				if (!t2.hasFocus()) {
					t2.requestFocus();
					alterfocusflag = 1;

				}

				tempcsq = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				t2.setText(tempcsq);

			}

			else if (flagn == 1) {

				// Storage of input in memory
				mem[temp][0] = t2.getText().toString().charAt(0);
				mem[temp][1] = t2.getText().toString().charAt(1);

				// The code below was written for the case when random
				// initialization (of memory) is not used in the app
				/*
				 * //Storage of input in memory tempstr =
				 * t2.getText().toString();
				 * 
				 * if (tempstr.charAt(0) == 0 && tempstr.charAt(1) != 0) {
				 * String tempstrn = tempstr.substring(1); tempstr = "0" +
				 * tempstrn;
				 * 
				 * } mem[temp][0] = tempstr.charAt(0); mem[temp][1] =
				 * tempstr.charAt(1);
				 */

				// Increment
				if (temp != 65535)
					temp = temp + 0x1;
				// temp++; is also valid

				tempstr = Integer.toHexString(temp).toUpperCase();

				// Prefixing t1 with zeroes
				t1.setText(prefixz(tempstr));

				tempcsq = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				t2.setText(tempcsq);
			}

			pc = temp;
		}

		// code for 'examine register' (next button)
		if (flagexr == 1) {

			if (t1.getText().toString().equals("L")) // For L
			{
				t1.setText("A");
				tempcsq = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				t2.setText(tempcsq);
			}

			else {
				regindex = t1.getText().toString().charAt(0) - 65;

				if (regindex == 4) {

					t1.setText("F");
					regindex = 7;

				} else if (regindex == 5) {
					t1.setText("H");
					regindex = 5;
				} else if (regindex == 7) {
					t1.setText("L");
					regindex = 6;
				} else {
					regindex++;
					t1.setText(Character.toString((char) (65 + regindex)));

				}

				tempcsq = Character.toString(abcdehlf[regindex][0])
						+ Character.toString(abcdehlf[regindex][1]);
				t2.setText(tempcsq);

			}
		}

		// Code for 'go' (next button)
		if (goflag == 1) {

			if (t1.getText().toString().length() == 0) {
				displayMsg("Invalid Operation.", 0, 0);
			} else {
				temp = Integer.parseInt(t1.getText().toString(), 16);

				if (temp != 65535)
					pc = ++temp;
				else
					pc = temp;

				tempstr = Integer.toString(pc, 16).toUpperCase();
				t1.setText(prefixz(tempstr));
			}
		}

	}

	private String prefixz(String tempstrn) {

		int len = tempstrn.length();

		String tempstr2 = "";

		for (int i = 0; i < 4 - len; i++)
			tempstr2 = tempstr2 + "0";

		tempstrn = tempstr2 + tempstrn;

		return tempstrn;

	}

	private void prevcode() {

		int exceptionflag = 0;

		if (t1.getText().toString().equals("-UPS"))
			displayMsg("Invalid Operation.", 0, 0);

		// Code for 'examine memory' (prev button)
		if (flagexm == 1) {
			temp = 0; // Initialized for the sake of resolving errors

			try {
				temp = Integer.parseInt(t1.getText().toString(), 16);
			} catch (Exception e) {
				// exception happens when the text in t1 is "-UPS" or ""
				// Toast.makeText(this, e.toString(),
				// Toast.LENGTH_LONG).show();
				displayMsg("Invalid Operation.", 0, 0);
				exceptionflag = 1;
			}

			if (temp != 0) {

				// Dealing with the case when the user
				// clicks exam mem, enters some data and then
				// immediately presses prev
				int tempflag = 0;
				if (t1.isFocused()) {
					tempflag = 1;
					t2.requestFocus();
					alterfocusflag = 1;
					flagn = 1;

				}

				// Storage of input in memory
				mem[temp][0] = t2.getText().toString().charAt(0);
				mem[temp][1] = t2.getText().toString().charAt(1);

				// The code below was written for the case when random
				// initialization (of memory) is not used in the app
				/*
				 * // Storage of input in memory
				 * 
				 * tempstr = t2.getText().toString();
				 * 
				 * if (tempstr.charAt(0) == 0 && tempstr.charAt(1) != 0) {
				 * String tempstrn = tempstr.substring(1); tempstr = "0" +
				 * tempstrn;
				 * 
				 * } mem[temp][0] = tempstr.charAt(0); mem[temp][1] =
				 * tempstr.charAt(1);
				 */

				// Decrement
				if (tempflag == 0) {
					temp = temp - 0x1;

				}
				// temp--; is also valid

				CharSequence tempcsq = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				t2.setText(tempcsq);

				tempstr = Integer.toHexString(temp).toUpperCase();

				// Prefixing t1 with zeroes
				t1.setText(prefixz(tempstr));

				tempcsq = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				t2.setText(tempcsq);
			}

			else // if temp ==0
			{
				if (exceptionflag != 1) {
					t1.setText("0000");

					tempcsq = Character.toString(mem[0][0])
							+ Character.toString(mem[0][1]);
					t2.requestFocus();
					alterfocusflag = 1;
					flagn = 1;
				}

			}

			pc = temp;
		}

		// code for 'examine register' (prev button)
		if (flagexr == 1) {

			if (t1.getText().toString().equals("A")) {
				t1.setText("L");
				tempcsq = Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				t2.setText(tempcsq);
			}

			else {
				regindex = t1.getText().toString().charAt(0) - 65;

				if (regindex == 11) // For L
				{
					t1.setText("H");
					regindex = 5;

				}

				else if (regindex == 7) // For H
				{
					t1.setText("F");
					regindex = 7;
				}

				else {
					regindex--;
					t1.setText(Character.toString((char) (65 + regindex)));

				}

				tempcsq = Character.toString(abcdehlf[regindex][0])
						+ Character.toString(abcdehlf[regindex][1]);
				t2.setText(tempcsq);
			}
		}

		// Code for 'go' (prev button)
		if (goflag == 1) {

			if (t1.getText().toString().length() == 0) {
				displayMsg("Invalid Operation.", 0, 0);
			} else {

				temp = Integer.parseInt(t1.getText().toString(), 16);
				if (temp != 0)
					pc = --temp;
				else
					pc = temp;

				tempstr = Integer.toString(pc, 16).toUpperCase();
				t1.setText(prefixz(tempstr));
			}
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		// Vibration
		if (!exm.isPressed())
			b0.performHapticFeedback(3);

		switch (v.getId()) {
		case R.id.t1:
			if (hasFocus) {

				flagn = 0;

			}

			break;

		case R.id.t2:

			if (hasFocus && flagexm == 1 && !(exec.isPressed())) {

				if (t1.getText().toString().length() == 0 && flagexr != 1
						&& resetflag != 1 && !(exm.isPressed())) {
					t1.requestFocus();
					flagn = 0;

					displayMsg("Enter address first.", 0, 0);

				} else if (flagexr != 1 && resetflag != 1 && (exm.isPressed())) {
					t2.requestFocus();
					flagn = 1;

				}

				else if (t1.getText().toString().length() != 0) {
					flagn = 1;

					tempstr = t1.getText().toString();

					// Prefixing input in t1 with zeroes
					t1.setText(prefixz(tempstr));

					// Code for displaying data on t2
					temp = Integer.parseInt(t1.getText().toString(), 16);
					pc = temp;
					tempstr = Character.toString(mem[temp][0])
							+ Character.toString(mem[temp][1]);
					t2.setText(tempstr);

				}

			}
			break;
		}

	}

	private void resetreg() {
		// TODO Auto-generated method stub

		for (int i = 0; i < 8; i++) {

			rand = generator.nextInt(256);
			tempstr = Integer.toHexString(rand).toUpperCase();
			if (tempstr.length() == 2) {
				abcdehlf[i][0] = tempstr.charAt(0);
				abcdehlf[i][1] = tempstr.charAt(1);
			} else {
				abcdehlf[i][0] = '0';
				abcdehlf[i][1] = tempstr.charAt(0);
			}
		}

		// Code for resetting s,z,in,ac,p,cy
		s = 0;
		z = 0;
		in = 0;
		ac = 0;
		p = 0;
		cy = 0;

		setflagreg();

	}

	private void execute() {

		// Toast.makeText(this, "Here", Toast.LENGTH_LONG).show();
		char c1 = mem[pc][0];
		char c2 = mem[pc][1];
		char t1, t2;
		String tempstr2;
		int count, i;
		int tempflag = 1;

		switch (c1) {

		// MOV
		// MOV X, Y (X=B, C, D, E, H, L)
		case '4':
		case '5':
		case '6':

			tempstr = Character.toString(abcdehlf[5][0])
					+ Character.toString(abcdehlf[5][1]);
			tempstr = tempstr + Character.toString(abcdehlf[6][0])
					+ Character.toString(abcdehlf[6][1]);
			temp = Integer.parseInt(tempstr, 16);
			// temp now contains address stored in H, L pair

			switch (c2) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':

				abcdehlf[(c1 - '4') * 2 + 1][0] = abcdehlf[c2 - '0' + 1][0];
				abcdehlf[(c1 - '4') * 2 + 1][1] = abcdehlf[c2 - '0' + 1][1];

				pc++;
				return;

			case '8':
			case '9':

				abcdehlf[(c1 - '4') * 2 + 2][0] = abcdehlf[c2 - '0' + 1 - 8][0];
				abcdehlf[(c1 - '4') * 2 + 2][1] = abcdehlf[c2 - '0' + 1 - 8][1];

				pc++;
				return;

			case 'A':
			case 'B':
			case 'C':
			case 'D':

				abcdehlf[(c1 - '4') * 2 + 2][0] = abcdehlf[c2 - 'A' + 3][0];
				abcdehlf[(c1 - '4') * 2 + 2][1] = abcdehlf[c2 - 'A' + 3][1];

				pc++;
				return;

				// MOV B, M; MOV D, M; MOV H, M
			case '6':

				abcdehlf[(c1 - '4') * 2 + 1][0] = mem[temp][0];
				abcdehlf[(c1 - '4') * 2 + 1][1] = mem[temp][1];
				pc++;
				return;

				// MOV C, M; MOV E, M; MOV L, M
			case 'E':
				abcdehlf[(c1 - '4') * 2 + 2][0] = mem[temp][0];
				abcdehlf[(c1 - '4') * 2 + 2][1] = mem[temp][1];
				pc++;
				return;

				// MOV X, A
			case '7':
				abcdehlf[(c1 - '4') * 2 + 1][0] = abcdehlf[0][0];
				abcdehlf[(c1 - '4') * 2 + 1][1] = abcdehlf[0][1];
				pc++;
				return;

			case 'F':
				abcdehlf[(c1 - '4') * 2 + 2][0] = abcdehlf[0][0];
				abcdehlf[(c1 - '4') * 2 + 2][1] = abcdehlf[0][1];
				pc++;
				return;

			}

			break;

		case '7':

			tempstr = Character.toString(abcdehlf[5][0])
					+ Character.toString(abcdehlf[5][1]);
			tempstr = tempstr + Character.toString(abcdehlf[6][0])
					+ Character.toString(abcdehlf[6][1]);
			temp = Integer.parseInt(tempstr, 16);
			// temp now contains address stored in H, L pair

			switch (c2) {

			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':

				mem[temp][0] = abcdehlf[c2 - '0' + 1][0];
				mem[temp][1] = abcdehlf[c2 - '0' + 1][1];

				pc++;
				return;

			case '7':

				mem[temp][0] = abcdehlf[0][0];
				mem[temp][1] = abcdehlf[0][1];

				pc++;
				return;

			case '8':
			case '9':

				abcdehlf[0][0] = abcdehlf[c2 - '0' + 1 - 8][0];
				abcdehlf[0][1] = abcdehlf[c2 - '0' + 1 - 8][1];

				pc++;
				return;

			case 'A':
			case 'B':
			case 'C':
			case 'D':

				abcdehlf[0][0] = abcdehlf[c2 - 'A' + 3][0];
				abcdehlf[0][1] = abcdehlf[c2 - 'A' + 3][1];

				pc++;
				return;

			case 'E':

				abcdehlf[0][0] = mem[temp][0];
				abcdehlf[0][1] = mem[temp][1];

				pc++;
				return;
			}

			break;

		// ADD && ADC
		case '8':

			switch (c2) {

			// ADD B; ADD C; ADD D; ADD E; ADD H; ADD L
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':

				tempstr2 = Character.toString(abcdehlf[c2 - '0' + 1][0])
						+ Character.toString(abcdehlf[c2 - '0' + 1][1]);
				addadc(tempstr2, 0);
				pc++;
				return;

				// ADD M
			case '6':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				temp = Integer.parseInt(tempstr, 16);
				tempstr2 = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				addadc(tempstr2, 0);
				pc++;
				return;

				// ADD A
			case '7':
				tempstr2 = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				addadc(tempstr2, 0);
				pc++;
				return;

				// ADC
				// ADC B; ADC C
			case '8':
			case '9':
				tempstr2 = Character.toString(abcdehlf[c2 - '8' + 1][0])
						+ Character.toString(abcdehlf[c2 - '8' + 1][1]);
				addadc(tempstr2, 1);
				pc++;
				return;

				// ADC D; ADC E; ADC H; ADC L
			case 'A':
			case 'B':
			case 'C':
			case 'D':
				tempstr2 = Character.toString(abcdehlf[c2 - 'A' + 3][0])
						+ Character.toString(abcdehlf[c2 - 'A' + 3][1]);
				addadc(tempstr2, 1);
				pc++;
				return;

				// ADC M
			case 'E':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				tempstr = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				;
				temp = Integer.parseInt(tempstr, 16);
				tempstr2 = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				addadc(tempstr2, 1);
				pc++;
				return;

				// ADC A
			case 'F':
				tempstr2 = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				addadc(tempstr2, 1);
				pc++;
				return;

			}

			break;

		// SUB

		case '9':

			switch (c2) {
			// SUB B; SUB C; SUB D; SUB E; SUB H; SUB L
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':

				tempstr2 = Character.toString(abcdehlf[c2 - '0' + 1][0])
						+ Character.toString(abcdehlf[c2 - '0' + 1][1]);
				subsbb(tempstr2, 0);
				pc++;
				return;

				// SUB M
			case '6':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				temp = Integer.parseInt(tempstr, 16);
				tempstr2 = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				subsbb(tempstr2, 0);
				pc++;
				return;

				// SUB A
			case '7':
				tempstr2 = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				subsbb(tempstr2, 0);
				pc++;
				return;

				// SBB
				// SBB B; SBB C
			case '8':
			case '9':
				tempstr2 = Character.toString(abcdehlf[c2 - '8' + 1][0])
						+ Character.toString(abcdehlf[c2 - '8' + 1][1]);
				subsbb(tempstr2, 1);
				pc++;
				return;

				// SBB D; SBB E; SBB H; SBB L
			case 'A':
			case 'B':
			case 'C':
			case 'D':
				tempstr2 = Character.toString(abcdehlf[c2 - 'A' + 3][0])
						+ Character.toString(abcdehlf[c2 - 'A' + 3][1]);
				subsbb(tempstr2, 1);
				pc++;
				return;

				// SBB M
			case 'E':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				tempstr = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				;
				temp = Integer.parseInt(tempstr, 16);
				tempstr2 = Character.toString(mem[temp][0])
						+ Character.toString(mem[temp][1]);
				subsbb(tempstr2, 1);
				pc++;
				return;

				// SBB A
			case 'F':
				tempstr2 = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				subsbb(tempstr2, 1);
				pc++;
				return;

			}

			break;

		case 'A': {

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);
			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8)
				tempstr = "0" + tempstr;

			// tempstr contains A's binary form

			switch (c2) {

			// ANA
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':

				tempflag = 0;
				// XRA
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':

				// ANA M, XRA M
				if (c2 == '6' || c2 == 'E') {
					String tempstr3 = Character.toString(abcdehlf[5][0])
							+ Character.toString(abcdehlf[5][1])
							+ Character.toString(abcdehlf[6][0])
							+ Character.toString(abcdehlf[6][1]);
					temp = Integer.parseInt(tempstr3, 16);
					tempstr2 = Character.toString(mem[temp][0])
							+ Character.toString(mem[temp][1]);

				}

				// ANA A, XRA A
				else if (c2 == '7' || c2 == 'F') {

					tempstr2 = Character.toString(abcdehlf[0][0])
							+ Character.toString(abcdehlf[0][1]);

				}
				// XRA B, XRA C
				else if (c2 == '8' || c2 == '9') {
					tempstr2 = Character.toString(abcdehlf[c2 - '8' + 1][0])
							+ Character.toString(abcdehlf[c2 - '8' + 1][1]);
				}
				// XRA D, E, H, L
				else if (c2 >= 'A') {
					tempstr2 = Character.toString(abcdehlf[c2 - 'A' + 3][0])
							+ Character.toString(abcdehlf[c2 - 'A' + 3][1]);
				}
				// ANA B, C, D, E, H, L
				else {
					tempstr2 = Character.toString(abcdehlf[c2 - '0' + 1][0])
							+ Character.toString(abcdehlf[c2 - '0' + 1][1]);

				}

				temp = Integer.parseInt(tempstr2, 16);
				tempstr2 = Integer.toBinaryString(temp);
				while (tempstr2.length() < 8)
					tempstr2 = "0" + tempstr2;

				count = 0;
				char[] chararr = new char[8];

				if (tempflag == 0) {
					for (i = 0; i < 8; i++) { // ANDing
						if (tempstr.charAt(i) == '1'
								&& tempstr2.charAt(i) == '1') {
							chararr[i] = '1';
							count++;
						} else
							chararr[i] = '0';
					}
				} else {
					for (i = 0; i < 8; i++) { // XORing
						if (tempstr.charAt(i) != tempstr2.charAt(i)) {
							chararr[i] = '1';
							count++;
						} else
							chararr[i] = '0';
					}
				}

				// Sign flag
				if (chararr[0] == '1')
					s = 1;
				else
					s = 0;

				// Parity flag
				if (count % 2 == 0)
					p = 1;
				else
					p = 0;

				tempstr = String.valueOf(chararr);
				temp = Integer.parseInt(tempstr, 2);

				// Zero flag
				if (temp == 0)
					z = 1;
				else
					z = 0;

				tempstr = Integer.toHexString(temp).toUpperCase();
				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				cy = 0;

				// AC is set in ANDing and reset in XORing
				if (tempflag == 0)
					ac = 1;
				else
					ac = 0;

				setflagreg();
				pc++;
				return;

			}

		}

			break;

		case 'B': {

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);
			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8)
				tempstr = "0" + tempstr;

			// tempstr contains A's binary form

			switch (c2) {

			// ORA
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':

				tempflag = 0;
				// CMP
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':

				// ORA M, CMP M
				if (c2 == '6' || c2 == 'E') {
					String tempstr3 = Character.toString(abcdehlf[5][0])
							+ Character.toString(abcdehlf[5][1])
							+ Character.toString(abcdehlf[6][0])
							+ Character.toString(abcdehlf[6][1]);
					temp = Integer.parseInt(tempstr3, 16);
					tempstr2 = Character.toString(mem[temp][0])
							+ Character.toString(mem[temp][1]);

				}

				// ORA A, CMP A
				else if (c2 == '7' || c2 == 'F') {

					tempstr2 = Character.toString(abcdehlf[0][0])
							+ Character.toString(abcdehlf[0][1]);

				}
				// CMP B, CMP C
				else if (c2 == '8' || c2 == '9') {
					tempstr2 = Character.toString(abcdehlf[c2 - '8' + 1][0])
							+ Character.toString(abcdehlf[c2 - '8' + 1][1]);
				}
				// CMP D, E, H, L
				else if (c2 >= 'A') {
					tempstr2 = Character.toString(abcdehlf[c2 - 'A' + 3][0])
							+ Character.toString(abcdehlf[c2 - 'A' + 3][1]);
				}
				// ORA B, C, D, E, H, L
				else {
					tempstr2 = Character.toString(abcdehlf[c2 - '0' + 1][0])
							+ Character.toString(abcdehlf[c2 - '0' + 1][1]);

				}

				// ORA
				if (tempflag == 0) {

					temp = Integer.parseInt(tempstr2, 16);
					tempstr2 = Integer.toBinaryString(temp);
					while (tempstr2.length() < 8)
						tempstr2 = "0" + tempstr2;

					count = 0;
					char[] chararr = new char[8];

					for (i = 0; i < 8; i++) { // ORing
						if (tempstr.charAt(i) == '1'
								|| tempstr2.charAt(i) == '1') {
							chararr[i] = '1';
							count++;
						} else
							chararr[i] = '0';
					}

					// Sign flag
					if (chararr[0] == '1')
						s = 1;
					else
						s = 0;

					// Parity flag
					if (count % 2 == 0)
						p = 1;
					else
						p = 0;

					tempstr = String.valueOf(chararr);
					temp = Integer.parseInt(tempstr, 2);

					// Zero flag
					if (temp == 0)
						z = 1;
					else
						z = 0;

					tempstr = Integer.toHexString(temp).toUpperCase();
					while (tempstr.length() < 2)
						tempstr = "0" + tempstr;
					abcdehlf[0][0] = tempstr.charAt(0);
					abcdehlf[0][1] = tempstr.charAt(1);

					// AC and CY are reset in ORing
					cy = 0;
					ac = 0;

					setflagreg();

				}

				// CMP
				else {

					String tempstr3 = Character.toString(abcdehlf[0][0])
							+ Character.toString(abcdehlf[0][1]);
					subsbb(tempstr2, 0);
					abcdehlf[0][0] = tempstr3.charAt(0);
					abcdehlf[0][1] = tempstr3.charAt(1);
				}

				pc++;
				return;

			}

		}

			break;

		}

		
		switch (c2) {

		case 'F':

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);

			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8) {
				tempstr = "0" + tempstr;
			}

			tempstr2 = "";

			switch (c1) {

			// RRC
			case '0':

				tempstr2 = tempstr.substring(0, 7);
				tempstr2 = Character.toString(tempstr.charAt(7)) + tempstr2;
				cy = tempstr.charAt(7) - '0';

				temp = Integer.parseInt(tempstr2, 2);
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				setflagreg();
				pc++;
				return;

				// RAR (Rotate Accumulator Right through Carry)
			case '1':

				tempstr2 = tempstr.substring(0, 7);

				tempstr2 = Integer.toString(cy) + tempstr2;
				cy = tempstr.charAt(7) - '0';

				temp = Integer.parseInt(tempstr2, 2);
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				setflagreg();

				pc++;
				return;

				// CMA (Complement Accumulator)
			case '2':

				for (i = 0; i < 8; i++) {
					if (tempstr.charAt(i) == '0')
						tempstr2 = tempstr2 + "1";

					else
						tempstr2 = tempstr2 + "0";
				}

				temp = Integer.parseInt(tempstr2, 2);
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				pc++;
				return;

				// CMC (Complement Carry)
			case '3':

				if (cy == 1)
					cy = 0;
				else
					cy = 1;

				setflagreg();
				pc++;
				return;

				// RST 1, 3, 5, 7
			case 'C':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				String str = "0008";

				pc = Integer.parseInt(str, 16);

				return;

			case 'D':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0018";

				pc = Integer.parseInt(str, 16);

				return;

			case 'E':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0028";

				pc = Integer.parseInt(str, 16);

				return;

			case 'F':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0038";

				pc = Integer.parseInt(str, 16);

				return;

			}

			break;

		// RZ, RC, RPE, RM
		case '8':

			switch (c1) {

			// RZ
			case 'C':
				if (z == 1)
					retrn();
				else
					pc++;
				return;

				// RC
			case 'D':
				if (cy == 1)
					retrn();
				else
					pc++;
				return;

				// RPE
			case 'E':
				if (p == 1)
					retrn();
				else
					pc++;
				return;

				// RM
			case 'F':
				if (s == 1)
					retrn();
				else
					pc++;
				return;

			}

			break;

		// DAD (Double Addition)
		case '9':

			switch (c1) {
			// DAD B, DAD D, DAD H
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 1][1]);
				tempstr = tempstr
						+ Character
								.toString(abcdehlf[(c1 - '0') * 2 + 1 + 1][0])
						+ Character
								.toString(abcdehlf[(c1 - '0') * 2 + 1 + 1][1]);
				temp = Integer.parseInt(tempstr, 16);

				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				temp = temp + Integer.parseInt(tempstr, 16);

				// //Doubt here whether cy is reset when there's no carry
				if (temp > 65535)
					cy = 1;
				else
					cy = 0;

				temp = temp % 65536;

				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;

				abcdehlf[5][0] = tempstr.charAt(0);
				abcdehlf[5][1] = tempstr.charAt(1);
				abcdehlf[6][0] = tempstr.charAt(2);
				abcdehlf[6][1] = tempstr.charAt(3);

				setflagreg();

				pc++;
				return;

				// DAD SP
			case '3':

				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				temp = sp + Integer.parseInt(tempstr, 16);

				// //Doubt here whether cy is reset when there's no carry
				if (temp > 65535)
					cy = 1;
				else
					cy = 0;

				temp = temp % 65536;

				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;

				abcdehlf[5][0] = tempstr.charAt(0);
				abcdehlf[5][1] = tempstr.charAt(1);
				abcdehlf[6][0] = tempstr.charAt(2);
				abcdehlf[6][1] = tempstr.charAt(3);

				setflagreg();

				pc++;
				return;

				// RET
			case 'C':
				retrn();

				return;

				// PCHL
			case 'E':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1])
						+ Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);

				pc = Integer.parseInt(tempstr, 16);

				return;

				// SPHL
			case 'F':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1])
						+ Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				sp = Integer.parseInt(tempstr, 16);

				pc++;
				return;
			}

			break;
		// INX
		case '3':

			switch (c1) {
			// INX B; INX D; INX H
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 1][1]);
				tempstr = tempstr
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][1]);
				temp = Integer.parseInt(tempstr, 16);
				temp++;
				temp = temp % 65536; // 2^16
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;

				tempstr = tempstr.toUpperCase();

				abcdehlf[(c1 - '0') * 2 + 1][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 1][1] = tempstr.charAt(1);
				abcdehlf[(c1 - '0') * 2 + 2][0] = tempstr.charAt(2);
				abcdehlf[(c1 - '0') * 2 + 2][1] = tempstr.charAt(3);

				pc++;
				return;

				// INX SP
			case '3':
				sp++;
				sp = sp % 65536; // 2^16
				pc++;
				return;

				// JMP addr
			case 'C':

				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;

				pc = Integer.parseInt(tempstr, 16);

				return;

				// XTHL
			case 'E':

				if (sp <= 65533) {
					tempstr = pop();
					push(Character.toString(abcdehlf[5][0])
							+ Character.toString(abcdehlf[5][1])
							+ Character.toString(abcdehlf[6][0])
							+ Character.toString(abcdehlf[6][1]));
					abcdehlf[5][0] = tempstr.charAt(0);
					abcdehlf[5][1] = tempstr.charAt(1);
					abcdehlf[6][0] = tempstr.charAt(2);
					abcdehlf[6][1] = tempstr.charAt(3);
				}
				pc++;
				return;
			}

			break;

		// DCX
		case 'B':

			switch (c1) {

			// DCX B; DCX D; DCX H
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 1][1]);
				tempstr = tempstr
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][1]);
				temp = Integer.parseInt(tempstr, 16);

				// Subtracting 1 by 2's complement method
				temp = temp + 65535; // 2^16 - 1
				temp = temp % 65536; // 2^16
				tempstr = Integer.toHexString(temp);

				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;

				tempstr = tempstr.toUpperCase();

				abcdehlf[(c1 - '0') * 2 + 1][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 1][1] = tempstr.charAt(1);
				abcdehlf[(c1 - '0') * 2 + 2][0] = tempstr.charAt(2);
				abcdehlf[(c1 - '0') * 2 + 2][1] = tempstr.charAt(3);

				pc++;
				return;

				// DCX SP
			case '3':
				sp = sp + 65535;
				sp = sp % 65536; // 2^16
				pc++;
				return;

				// XCHG
				// Exchanges HL and DE
			case 'E':
				char tempchar1,
				tempchar2;
				tempchar1 = abcdehlf[5][0];
				tempchar2 = abcdehlf[5][1];
				abcdehlf[5][0] = abcdehlf[3][0];
				abcdehlf[5][1] = abcdehlf[3][1];
				abcdehlf[3][0] = tempchar1;
				abcdehlf[3][1] = tempchar2;

				tempchar1 = abcdehlf[6][0];
				tempchar2 = abcdehlf[6][1];
				abcdehlf[6][0] = abcdehlf[4][0];
				abcdehlf[6][1] = abcdehlf[4][1];
				abcdehlf[4][0] = tempchar1;
				abcdehlf[4][1] = tempchar2;

				pc++;
				return;
			}

			break;

		// MVI
		// MVI A, MVI C, MVI E, MVI L
		// ACI, SBI, XRI, CPI
		case 'E':

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);
			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8)
				tempstr = "0" + tempstr;

			tempstr2 = Character.toString(mem[pc + 1][0])
					+ Character.toString(mem[pc + 1][1]);
			temp = Integer.parseInt(tempstr2, 16);
			tempstr2 = Integer.toBinaryString(temp);

			while (tempstr2.length() < 8)
				tempstr2 = "0" + tempstr2;

			count = 0;
			char[] chararr = new char[8];

			switch (c1) {
			// MVI C, MVI E, MVI L
			case '0':
			case '1':
			case '2':

				pc++;
				abcdehlf[(c1 - 48) * 2 + 2][0] = mem[pc][0];
				abcdehlf[(c1 - 48) * 2 + 2][1] = mem[pc][1];

				pc++;
				return;

				// MVI A
			case '3':

				pc++;
				abcdehlf[0][0] = mem[pc][0];
				abcdehlf[0][1] = mem[pc][1];

				pc++;
				return;

				// ACI
			case 'C':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				addadc(tempstr, 1);
				pc++;
				return;

				// SBI
			case 'D':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				subsbb(tempstr, 1);
				pc++;
				return;

				// XRI
			case 'E':

				pc++;

				for (i = 0; i < 8; i++) { // XORing
					if (tempstr.charAt(i) != tempstr2.charAt(i)) {
						chararr[i] = '1';
						count++;
					} else
						chararr[i] = '0';
				}

				// Sign flag
				if (chararr[0] == '1')
					s = 1;
				else
					s = 0;

				// Parity flag
				if (count % 2 == 0)
					p = 1;
				else
					p = 0;

				tempstr = String.valueOf(chararr);
				temp = Integer.parseInt(tempstr, 2);

				// Zero flag
				if (temp == 0)
					z = 1;
				else
					z = 0;

				tempstr = Integer.toHexString(temp).toUpperCase();
				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				cy = 0;
				ac = 0;
				setflagreg();

				pc++;
				return;

				// CPI
			case 'F':

				pc++;

				String tempstr3 = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				tempstr2 = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				subsbb(tempstr2, 0);
				abcdehlf[0][0] = tempstr3.charAt(0);
				abcdehlf[0][1] = tempstr3.charAt(1);

				pc++;
				return;

			}
			break;

		// MVI B, MVI D, MVI H
		// ADI, SUI, ANI, ORI
		case '6':

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);
			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8)
				tempstr = "0" + tempstr;

			tempstr2 = Character.toString(mem[pc + 1][0])
					+ Character.toString(mem[pc + 1][1]);
			temp = Integer.parseInt(tempstr2, 16);
			tempstr2 = Integer.toBinaryString(temp);

			while (tempstr2.length() < 8)
				tempstr2 = "0" + tempstr2;

			count = 0;
			chararr = new char[8];

			switch (c1) {

			case '0':
			case '1':
			case '2':
				pc++;
				abcdehlf[(c1 - 48) * 2 - 1 + 2][0] = mem[pc][0];
				abcdehlf[(c1 - 48) * 2 - 1 + 2][1] = mem[pc][1];

				pc++;
				return;

				// MVI M
			case '3':

				
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]) 
						+ Character.toString(abcdehlf[6][0]) 
						+ Character.toString(abcdehlf[6][1]);
				temp = Integer.parseInt(tempstr, 16);
				
				pc++;
				
				mem[temp][0] = mem[pc][0];
				mem[temp][1] = mem[pc][1];

				pc++;
				return;

				// ADI
			case 'C':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				addadc(tempstr, 0);
				pc++;
				return;

				// SUI
			case 'D':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				subsbb(tempstr, 0);
				pc++;
				return;

				// ANI
			case 'E':

				pc++;

				for (i = 0; i < 8; i++) { // ANDing
					if (tempstr.charAt(i) == '1' && tempstr2.charAt(i) == '1') {
						chararr[i] = '1';
						count++;
					} else
						chararr[i] = '0';
				}

				// Sign flag
				if (chararr[0] == '1')
					s = 1;
				else
					s = 0;

				// Parity flag
				if (count % 2 == 0)
					p = 1;
				else
					p = 0;

				tempstr = String.valueOf(chararr);
				temp = Integer.parseInt(tempstr, 2);

				// Zero flag
				if (temp == 0)
					z = 1;
				else
					z = 0;

				tempstr = Integer.toHexString(temp).toUpperCase();
				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				cy = 0;
				ac = 1;
				setflagreg();

				pc++;
				return;

				// ORI
			case 'F':

				pc++;

				for (i = 0; i < 8; i++) { // ORing
					if (tempstr.charAt(i) == '1' || tempstr2.charAt(i) == '1') {
						chararr[i] = '1';
						count++;
					} else
						chararr[i] = '0';
				}

				// Sign flag
				if (chararr[0] == '1')
					s = 1;
				else
					s = 0;

				// Parity flag
				if (count % 2 == 0)
					p = 1;
				else
					p = 0;

				tempstr = String.valueOf(chararr);
				temp = Integer.parseInt(tempstr, 2);

				// Zero flag
				if (temp == 0)
					z = 1;
				else
					z = 0;

				tempstr = Integer.toHexString(temp).toUpperCase();
				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				cy = 0;
				ac = 0;
				setflagreg();

				pc++;
				return;
			}

			break;

		// RNZ, RNC, RPO, RP
		case '0':

			switch (c1) {

			// NOP
			case '0':
				
				pc++;
				return;

				// RNZ
			case 'C':
				if (z == 0)
					retrn();
				else
					pc++;
				return;

				// RNC
			case 'D':
				if (cy == 0)
					retrn();
				else
					pc++;
				return;

				// RPO
			case 'E':
				if (p == 0)
					retrn();
				else
					pc++;
				return;

				// RP
			case 'F':
				if (s == 0)
					retrn();
				else
					pc++;
				return;
	
			}
			

			break;

		// LXI
		case '1':

			switch (c1) {
			// LXIB, LXID, LXIH
			case '0':
			case '1':
			case '2':

				pc++;
				abcdehlf[(c1 - '0') * 2 + 1 + 1][0] = mem[pc][0];
				abcdehlf[(c1 - '0') * 2 + 1 + 1][1] = mem[pc][1];
				pc++;
				abcdehlf[(c1 - '0') * 2 + 1][0] = mem[pc][0];
				abcdehlf[(c1 - '0') * 2 + 1][1] = mem[pc][1];

				pc++;
				return;

				// LXI SP
			case '3':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;
				sp = Integer.parseInt(tempstr, 16);

				pc++;
				return;

				// POP B, D, H
			case 'C':
			case 'D':
			case 'E':

				if (sp <= 65533) {
					tempstr = pop();
					abcdehlf[(c1 - 'C') * 2 + 1][0] = tempstr.charAt(0);
					abcdehlf[(c1 - 'C') * 2 + 1][1] = tempstr.charAt(1);
					abcdehlf[(c1 - 'C') * 2 + 2][0] = tempstr.charAt(2);
					abcdehlf[(c1 - 'C') * 2 + 2][1] = tempstr.charAt(3);
				}

				pc++;
				return;

				// POP PSW
			case 'F':
				if (sp <= 65533) {
					tempstr = pop();
					abcdehlf[0][0] = tempstr.charAt(0);
					abcdehlf[0][1] = tempstr.charAt(1);
					abcdehlf[7][0] = tempstr.charAt(2);
					abcdehlf[7][1] = tempstr.charAt(3);
				}

				pc++;
				return;

			}

			break;

		// Load Instructions
		case 'A':

			switch (c1) {
			// LDAX B
			case '0':

				tempstr = Character.toString(abcdehlf[1][0])
						+ Character.toString(abcdehlf[1][1]);
				tempstr = tempstr + Character.toString(abcdehlf[2][0])
						+ Character.toString(abcdehlf[2][1]);
				temp = Integer.parseInt(tempstr, 16);
				abcdehlf[0][0] = mem[temp][0];
				abcdehlf[0][1] = mem[temp][1];

				pc++;
				return;

				// LDAX D
			case '1':
				tempstr = Character.toString(abcdehlf[3][0])
						+ Character.toString(abcdehlf[3][1]);
				tempstr = tempstr + Character.toString(abcdehlf[4][0])
						+ Character.toString(abcdehlf[4][1]);
				temp = Integer.parseInt(tempstr, 16);
				abcdehlf[0][0] = mem[temp][0];
				abcdehlf[0][1] = mem[temp][1];

				pc++;
				return;

				// LHLD addr
			case '2':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;
				temp = Integer.parseInt(tempstr, 16);

				abcdehlf[5][0] = mem[temp + 1][0];
				abcdehlf[5][1] = mem[temp + 1][1];
				abcdehlf[6][0] = mem[temp][0];
				abcdehlf[6][1] = mem[temp][1];

				pc++;
				return;

				// LDA addr
			case '3':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;
				temp = Integer.parseInt(tempstr, 16);
				abcdehlf[0][0] = mem[temp][0];
				abcdehlf[0][1] = mem[temp][1];

				pc++;
				return;

				// Jump Instructions
			case 'C':

				if (z == 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;

				return;

			case 'D':

				if (cy == 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;
				return;

			case 'E':

				if (p == 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;

				return;

			case 'F':

				if (s == 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;
				return;
			}

			break;

		// Store Instructions
		case '2':

			switch (c1) {
			// STAX B
			case '0':
				tempstr = Character.toString(abcdehlf[1][0])
						+ Character.toString(abcdehlf[1][1]);
				tempstr = tempstr + Character.toString(abcdehlf[2][0])
						+ Character.toString(abcdehlf[2][1]);
				temp = Integer.parseInt(tempstr, 16);
				mem[temp][0] = abcdehlf[0][0];
				mem[temp][1] = abcdehlf[0][1];
				pc++;
				return;

				// STAX D
			case '1':
				tempstr = Character.toString(abcdehlf[2][0])
						+ Character.toString(abcdehlf[2][1]);
				tempstr = tempstr + Character.toString(abcdehlf[3][0])
						+ Character.toString(abcdehlf[3][1]);
				temp = Integer.parseInt(tempstr, 16);
				mem[temp][0] = abcdehlf[0][0];
				mem[temp][1] = abcdehlf[0][1];
				pc++;
				return;

				// SHLD addr
			case '2':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;
				temp = Integer.parseInt(tempstr, 16);

				mem[temp][0] = abcdehlf[6][0];
				mem[temp][1] = abcdehlf[6][1];
				mem[temp + 1][0] = abcdehlf[5][0];
				mem[temp + 1][1] = abcdehlf[5][1];

				pc++;
				return;

				// STA addr
			case '3':
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]);
				pc++;
				tempstr = Character.toString(mem[pc][0])
						+ Character.toString(mem[pc][1]) + tempstr;
				temp = Integer.parseInt(tempstr, 16);

				mem[temp][0] = abcdehlf[0][0];
				mem[temp][1] = abcdehlf[0][1];
				pc++;
				return;

				// Jump Instructions (not)
			case 'C':

				if (z != 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;

				return;

			case 'D':

				if (cy != 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;
				return;

			case 'E':

				if (p != 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;
				return;

			case 'F':

				if (s != 1) {
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]);
					pc++;
					tempstr = Character.toString(mem[pc][0])
							+ Character.toString(mem[pc][1]) + tempstr;

					// Subtracting 1 since it will be incremented at the end of
					// the function

					pc = Integer.parseInt(tempstr, 16);
				} else
					pc += 3;
				return;
			}

			break;

		// DCR
		// PUSH
		case '5':

			switch (c1) {
			// DCR B; DCR D; DCR H
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 1][1]);
				tempstr = dcrinr(tempstr, 0);
				abcdehlf[(c1 - '0') * 2 + 1][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 1][1] = tempstr.charAt(1);

				pc++;
				return;

				// DCR M
			case '3':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				int temp2 = Integer.parseInt(tempstr, 16);
				tempstr = Character.toString(mem[temp2][0])
						+ Character.toString(mem[temp2][1]);
				tempstr = dcrinr(tempstr, 0);
				mem[temp2][0] = tempstr.charAt(0);
				mem[temp2][1] = tempstr.charAt(1);

				pc++;
				return;

				// PUSH B, D, H
			case 'C':
			case 'D':
			case 'E':

				tempstr = Character.toString(abcdehlf[(c1 - 'C') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - 'C') * 2 + 1][1]);
				tempstr = tempstr
						+ Character.toString(abcdehlf[(c1 - 'C') * 2 + 2][0])
						+ Character.toString(abcdehlf[(c1 - 'C') * 2 + 2][1]);
				push(tempstr);
				pc++;
				return;

				// PUSH PSW
			case 'F':
				tempstr = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				tempstr = tempstr + Character.toString(abcdehlf[7][0])
						+ Character.toString(abcdehlf[7][1]);
				push(tempstr);
				pc++;
				return;
			}

			break;

		case 'D':

			switch (c1) {
			// DCR C; DCR E; DCR L
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 2][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][1]);
				tempstr = dcrinr(tempstr, 0);
				abcdehlf[(c1 - '0') * 2 + 2][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 2][1] = tempstr.charAt(1);

				pc++;
				return;

				// DCR A
			case '3':

				tempstr = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				tempstr = dcrinr(tempstr, 0);
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				pc++;
				return;

				// CALL
			case 'C':
				call();
				return;
			}

			break;

		// INR
		case '4':

			switch (c1) {
			// INR B; INR D; INR H
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 1][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 1][1]);
				tempstr = dcrinr(tempstr, 1);
				abcdehlf[(c1 - '0') * 2 + 1][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 1][1] = tempstr.charAt(1);

				pc++;
				return;

				// INR M
			case '3':
				tempstr = Character.toString(abcdehlf[5][0])
						+ Character.toString(abcdehlf[5][1]);
				tempstr = tempstr + Character.toString(abcdehlf[6][0])
						+ Character.toString(abcdehlf[6][1]);
				int temp2 = Integer.parseInt(tempstr, 16);
				tempstr = Character.toString(mem[temp2][0])
						+ Character.toString(mem[temp2][1]);
				tempstr = dcrinr(tempstr, 1);
				
				mem[temp2][0] = tempstr.charAt(0);
				mem[temp2][1] = tempstr.charAt(1);

				pc++;
				return;

				// CNZ, CNC, CPO, CP
			case 'C':
				if (z == 0)
					call();
				else
					pc += 3;
				return;

			case 'D':
				if (cy == 0)
					call();
				else
					pc += 3;
				return;

			case 'E':
				if (p == 0)
					call();
				else
					pc += 3;
				return;

			case 'F':
				if (s == 0)
					call();
				else
					pc += 3;
				return;

			}

			break;

		case 'C':

			switch (c1) {
			// INR C; INR E; INR L
			case '0':
			case '1':
			case '2':

				tempstr = Character.toString(abcdehlf[(c1 - '0') * 2 + 2][0])
						+ Character.toString(abcdehlf[(c1 - '0') * 2 + 2][1]);
				tempstr = dcrinr(tempstr, 1);
				abcdehlf[(c1 - '0') * 2 + 2][0] = tempstr.charAt(0);
				abcdehlf[(c1 - '0') * 2 + 2][1] = tempstr.charAt(1);

				pc++;
				return;

				// INR A
			case '3':

				tempstr = Character.toString(abcdehlf[0][0])
						+ Character.toString(abcdehlf[0][1]);
				tempstr = dcrinr(tempstr, 1);
				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				pc++;
				return;

				// CZ, CC, CPE, CM
			case 'C':
				if (z == 1)
					call();
				else
					pc += 3;
				return;

			case 'D':
				if (cy == 1)
					call();
				else
					pc += 3;
				return;

			case 'E':
				if (p == 1)
					call();
				else
					pc += 3;
				return;

			case 'F':
				if (s == 1)
					call();
				else
					pc += 3;
				return;
			}

			break;

		case '7':

			tempstr = Character.toString(abcdehlf[0][0])
					+ Character.toString(abcdehlf[0][1]);
			temp = Integer.parseInt(tempstr, 16);

			tempstr = Integer.toBinaryString(temp);

			while (tempstr.length() < 8) {
				tempstr = "0" + tempstr;
			}

			tempstr2 = "";

			switch (c1) {

			// RLC
			case '0':

				tempstr2 = tempstr.substring(1);
				tempstr2 = tempstr2 + Character.toString(tempstr.charAt(0));
				cy = tempstr.charAt(0) - '0';

				temp = Integer.parseInt(tempstr2, 2);
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				setflagreg();
				pc++;
				return;

				// RAL (Rotate Accumulator Left through Carry)
			case '1':

				tempstr2 = tempstr.substring(1);

				tempstr2 = tempstr2 + Integer.toString(cy);
				cy = tempstr.charAt(0) - '0';

				temp = Integer.parseInt(tempstr2, 2);
				tempstr = Integer.toHexString(temp).toUpperCase();

				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				setflagreg();

				pc++;
				return;

				// DAA (Decimal Adjust Accumulator)
			case '2':

				t1 = abcdehlf[0][0];
				t2 = abcdehlf[0][1];

				temp = Integer.parseInt(
						Character.toString(t1) + Character.toString(t2), 16);

				// BCD Conversion

				if (Integer.parseInt(Character.toString(t2), 16) > 9 || ac == 1) {
					temp = temp + 6;
					ac = 1;
				}

				if (Integer.parseInt(Character.toString(t1), 16) > 9 || cy == 1) {
					temp = temp + 96; // Binary: 0110 0000
					cy = 1;
				}

				temp = temp % 256;
				tempstr = Integer.toBinaryString(temp);

				while (tempstr.length() < 8)
					tempstr = "0" + tempstr;

				if (tempstr.charAt(0) == '1')
					s = 1;
				else
					s = 0;

				if (temp == 0)
					z = 1;
				else
					z = 0;

				count = 0;
				for (i = 0; i < 8; i++) {
					if (tempstr.charAt(i) == '1')
						count++;
				}

				if (count % 2 == 0)
					p = 1;
				else
					p = 0;

				tempstr = Integer.toHexString(temp).toUpperCase();
				while (tempstr.length() < 2)
					tempstr = "0" + tempstr;

				abcdehlf[0][0] = tempstr.charAt(0);
				abcdehlf[0][1] = tempstr.charAt(1);

				setflagreg();

				pc++;
				return;

				// STC
			case '3':

				cy = 1;
				setflagreg();
				pc++;
				return;

				// RST 0, 2, 4, 6
			case 'C':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				String str = "0000";

				pc = Integer.parseInt(str, 16);

				return;

			case 'D':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0010";

				pc = Integer.parseInt(str, 16);

				return;

			case 'E':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0020";

				pc = Integer.parseInt(str, 16);
				return;

			case 'F':
				pc++;
				tempstr = Integer.toHexString(pc).toUpperCase();
				while (tempstr.length() < 4)
					tempstr = "0" + tempstr;
				push(tempstr);

				str = "0030";

				pc = Integer.parseInt(str, 16);
				return;
			}

			break;
		}

		//Control reaches here when the code is not found in the instruction set
		//PC is increased by one in such a case
		pc++;
		
	}// Closing bracket of execute()

	private String dcrinr(String tempstr, int f) {

		// Auxiliary carry
		if (f == 0) {
			if (Integer.parseInt(tempstr.substring(1), 16) > 0)

			{
				ac = 1;
			} else
				ac = 0;
		} else {
			if (Integer.parseInt(tempstr.substring(1), 16) == 15)

			{
				ac = 1;
			} else
				ac = 0;
		}

		temp = Integer.parseInt(tempstr, 16);

		if (f == 0)
			temp = temp + 255; // Adding with 2's complement of 1
		else
			temp = temp + 1;

		temp = temp % 256;

		if (temp == 0)
			z = 1;
		else
			z = 0;

		tempstr = Integer.toBinaryString(temp);

		while (tempstr.length() < 8)
			tempstr = "0" + tempstr;

		int count = 0;
		for (int i = 0; i < 8; i++) {
			if (tempstr.charAt(i) == '1')
				count++;
		}
		if (count % 2 == 0)
			p = 1;
		else
			p = 0;

		tempstr = Integer.toHexString(temp);

		if (tempstr.length() < 2)
			tempstr = "0" + tempstr;

		if (Integer.parseInt(tempstr.substring(0, 1), 16) > 8)
			s = 1;
		else
			s = 0;

		setflagreg();
		return tempstr.toUpperCase();

	}

	// Toast.makeText(this, Integer.toString(pc),
	// Toast.LENGTH_SHORT).show();

	private void subsbb(String tempstr2, int f) {

		int i = 0; // for use in loops
		temp = Integer.parseInt(tempstr2, 16);

		// Adding carry/borrow flag to subtrahend in case of SBB
		if (f == 1)
			temp = temp + cy;

		temp = temp % 256;

		tempstr2 = Integer.toBinaryString(temp);

		while (tempstr2.length() < 8) {
			tempstr2 = "0" + tempstr2;
		}

		char[] tempstr3 = tempstr2.toCharArray();

		// 1's complement
		while (i < 8) {
			if (tempstr3[i] == '1') {
				tempstr3[i] = '0';
			}

			else {
				tempstr3[i] = '1';
			}

			i++;
		}

		// Converting char array to string
		tempstr2 = String.valueOf(tempstr3);

		temp = Integer.parseInt(tempstr2, 2);

		// Adding 1 to convert 1's complement to 2's complement
		temp++;

		temp = temp % 256;

		tempstr2 = Integer.toString(temp, 16);

		// Appending "0" to tempstr2 when needed
		while (tempstr2.length() < 2) {
			tempstr2 = "0" + tempstr2;
		}

		addadc(tempstr2, 0);

		// Complementing carry flag
		if (cy == 1)
			cy = 0;
		else
			cy = 1;

		setflagreg();

	}

	private void addadc(String tempstr2, int f) {

		int count;
		tempstr = Character.toString(abcdehlf[0][0])
				+ Character.toString(abcdehlf[0][1]);

		temp = Integer.parseInt(tempstr, 16) + Integer.parseInt(tempstr2, 16);

		if (f == 1) {
			temp = temp + cy; // Adding carry (for ADC)
			cy = 0;

			if ((Integer.parseInt(cy + tempstr.substring(1), 16) + Integer
					.parseInt(tempstr2.substring(1), 16)) > 15)
				ac = 1;
			else
				ac = 0;

		} else if (f == 0) {
			if (temp > 255) {
				cy = 1;
			} else
				cy = 0;

			if ((Integer.parseInt(tempstr.substring(1), 16) + Integer.parseInt(
					tempstr2.substring(1), 16)) > 15)
				ac = 1;
			else
				ac = 0;

		}

		temp = temp % 256;

		if (temp == 0) {
			z = 1;
		} else
			z = 0;

		tempstr = Integer.toBinaryString(temp);

		while (tempstr.length() < 8) {
			tempstr = "0" + tempstr;
		}

		if (tempstr.charAt(0) == '1')
			s = 1;
		else
			s = 0;

		count = 0;
		for (int i = 0; i < 8; i++) {
			if (tempstr.charAt(i) == '1')
				count++;
		}

		if (count % 2 == 0)
			p = 1;
		else
			p = 0;

		tempstr = Integer.toHexString(temp).toUpperCase();

		while (tempstr.length() < 2)
			tempstr = "0" + tempstr;

		abcdehlf[0][0] = tempstr.charAt(0);
		abcdehlf[0][1] = tempstr.charAt(1);

		setflagreg();

	}

	private void call() {
		pc++;
		String str = Character.toString(mem[pc][0])
				+ Character.toString(mem[pc][1]);
		pc++;
		str = Character.toString(mem[pc][0]) + Character.toString(mem[pc][1])
				+ str;
		pc++;
		tempstr = Integer.toHexString(pc).toUpperCase();

		while (tempstr.length() < 4)
			tempstr = "0" + tempstr;

		push(tempstr);

		pc = Integer.parseInt(str, 16);

	}

	private void retrn() {

		if (sp <= 65533) {
			tempstr = pop();

			pc = Integer.parseInt(tempstr, 16);

		}
		else
			pc++;

	}

	private void push(String str) {
		if (sp >= 2) {
			sp--;
			mem[sp][0] = str.charAt(0);
			mem[sp][1] = str.charAt(1);
			sp--;
			mem[sp][0] = str.charAt(2);
			mem[sp][1] = str.charAt(3);
		}

	}

	private String pop() {

		String str;
		str = Character.toString(mem[sp][0]) + Character.toString(mem[sp][1]);
		sp++;
		str = Character.toString(mem[sp][0]) + Character.toString(mem[sp][1])
				+ str;
		sp++;

		return str;

	}

	private void setflagreg() {

		temp = Integer.parseInt(
				Character.toString(abcdehlf[7][0])
						+ Character.toString(abcdehlf[7][1]), 16);
		tempstr = Integer.toBinaryString(temp);

		// Code to make the binary string 8 bit long if it's any smaller
		while (tempstr.length() < 8) {
			tempstr = "0" + tempstr;
		}

		char[] charr = tempstr.toCharArray();
		// Toast.makeText(this, tempstr + " " + String.valueOf(charr),
		// 2000).show();

		if (s == 1)
			charr[0] = '1';

		else
			charr[0] = '0';

		if (z == 1)
			charr[1] = '1';

		else
			charr[1] = '0';

		if (in == 1)
			charr[2] = '1';

		else
			charr[2] = '0';

		if (ac == 1)
			charr[3] = '1';

		else
			charr[3] = '0';

		if (p == 1)
			charr[5] = '1';

		else
			charr[5] = '0';

		if (cy == 1)
			charr[7] = '1';

		else
			charr[7] = '0';

		// Keep in mind that String.valueOf() should be used to convert
		// character array to string!!
		tempstr = String.valueOf(charr);
		temp = Integer.parseInt(tempstr, 2);

		tempstr = Integer.toHexString(temp).toUpperCase();

		while (tempstr.length() < 2)
			tempstr = "0" + tempstr;

		abcdehlf[7][0] = tempstr.charAt(0);
		abcdehlf[7][1] = tempstr.charAt(1);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.t1:
			alterfocusflag = 0;
			flagn = 0;
			break;

		case R.id.t2:
			if (t1.getText().toString().length() != 0) {
				alterfocusflag = 1;
				flagn = 1;
			}

			else {
				alterfocusflag = 0;
				flagn = 0;
			}
			break;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK
					&& !t1.getText().toString().equals("-UPS")
					&& t1.getText().toString().length() != 0) {
				if (flagn != 1)
					nextcode();

				tempstr = data.getStringExtra("hexcode");
				t2.setText(tempstr);
				mem[pc][0] = tempstr.charAt(0);
				mem[pc][1] = tempstr.charAt(1);

				Toast t = new Toast(this);
				t = Toast.makeText(this, data.getStringExtra("instruction"),
						Toast.LENGTH_SHORT);
				t.setGravity(Gravity.TOP, 0, 150);
				t.show();

			} else if (resultCode == RESULT_OK)
				displayMsg("Enter address to fetch Hex Code to!", 0, 0);
		}

	}

}
