package com.savvy.floatingwindowexample;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

public class FloatingWindow extends Service {
	public static final String MY_SERVICE = "tk.eatheat.floatingexample.FlyBitch";

	private WindowManager windowManager;
	private ImageView chatHead;
	private Boolean _enable = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		 _enable=false;
		return null;
	}

	@Override 
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = new ImageView(this);
		
		chatHead.setImageResource(R.drawable.circle);
		
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(chatHead, params);

		chatHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 LayoutInflater layoutInflater   = (LayoutInflater)getBaseContext()
					      .getSystemService(LAYOUT_INFLATER_SERVICE);  
						 View popupView = layoutInflater.inflate(R.layout.popup, null); 
						 final PopupWindow popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT,  
					                     LayoutParams.WRAP_CONTENT);  
						// popupWindow.setFocusable(true);
						 popupWindow.update();
						 
						 Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
						 Button endService= (Button) popupView.findViewById(R.id.endService);
						 endService.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
								stopSelf();
								Toast.makeText(getApplicationContext(), "Service Terminated", Toast.LENGTH_LONG).show();
							}
						});
						 
						 btnDismiss.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), " Popup Terminated", Toast.LENGTH_LONG).show();
								  popupWindow.dismiss();
								  _enable=true;
							}
						});		
						 
						 if(_enable){
							 popupWindow.showAsDropDown(chatHead, 50, -30);
							 _enable=false;
						 }
						 else if(!_enable) {
							 //Toast.makeText(getApplicationContext(), " Popup Terminated", Toast.LENGTH_LONG).show();
						//not working dismiss here sad :(	 popupWindow.dismiss();
							 Log.d("FALSE", "FALSE");
							  
						 }
						 
						 
				//chatHead.setImageResource(R.drawable.ic_launcher);
			}
		});
		try {
			
			chatHead.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.

						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						windowManager.updateViewLayout(chatHead, paramsF);
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) windowManager.removeView(chatHead);
	}

}
