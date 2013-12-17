package com.test.usermanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Splash screen
 */
public class SplashScreen extends Activity
{
	protected boolean _active = true;
	protected int _splashTime = 2000; // time to display the splash screen in ms

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.test.usermanager.R.layout.splash);

		// thread for displaying the SplashScreen
		Thread splashThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					long startTime = System.currentTimeMillis();
					sleep(100);
					long waited = System.currentTimeMillis() - startTime;
					while (_active && (waited < _splashTime))
					{
						sleep(100);
						if (_active)
						{
							waited += 100;
						}
					}
				}
				catch (InterruptedException e)
				{
					// do nothing
				}
				finally
				{
					startActivity(new Intent(getApplicationContext(), LoginActivity.class));
					finish();
				}
			}
		};
		
		splashThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			_active = false;
		}
		return true;
	}
}
