package com.test.usermanager.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

import com.test.usermanager.R;

/**
 * Settings screen for SUP in the preferences.
 */
public class SUPSettingsPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	/**
	 * preferences keys used inside application
	 */
	public static final String SUP_PREF_SUP_HOST_KEY = "sup_pref_host";
	public static final String SUP_PREF_SUP_PORT_KEY = "sup_pref_port";
	public static final String SUP_PREF_APPLICATION_ID_KEY = "sup_pref_application_id";
	public static final String SUP_PREF_SECURITY_CONFIGURATION_KEY = "sup_pref_security_config";
	public static final String SUP_PREF_FARMID_KEY = "sup_pref_farmid";
	public static final String SUP_PREF_DOMAIN_KEY = "sup_pref_domain";
    public static final String SUP_PREF_URLSUFFIX_KEY = "sup_pref_urlsuffix";

	/**
	 * preferences instances used inside the activity
	 */
	private EditTextPreference supHostPreference;
	private EditTextPreference supPortPreference;
	private EditTextPreference supDomainPreference;
	private EditTextPreference applicationIDPreference;
	private EditTextPreference securityConfigPreference;
	private EditTextPreference farmIDPreference;
	private EditTextPreference supUrlSuffix;

	
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);

	    addPreferencesFromResource(R.xml.sup_settings_preference_screen);
	    supHostPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_SUP_HOST_KEY);
	    supPortPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_SUP_PORT_KEY);
	    supDomainPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_DOMAIN_KEY);
	    applicationIDPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_APPLICATION_ID_KEY);
	    securityConfigPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_SECURITY_CONFIGURATION_KEY);	
	    farmIDPreference = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_FARMID_KEY);
	    supUrlSuffix = (EditTextPreference) getPreferenceScreen().findPreference(SUP_PREF_URLSUFFIX_KEY);
	    
	    supPortPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
	    {
			public boolean onPreferenceChange(Preference preference,Object newValue) 
			{
				String newSUPPort = newValue.toString();
				try 
				{
					Integer.parseInt(newSUPPort);
				} 
				catch (NumberFormatException e) 
				{
					Toast.makeText(getBaseContext(), getString(R.string.validation_msg), Toast.LENGTH_LONG).show();
					return false;
				}
				
				return true;
			}
	    });
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() 
	{
		super.onResume();
	    if (supHostPreference.getText() != null) 
	    {
	    	supHostPreference.setSummary(supHostPreference.getText()); 
		}
		
	    if (supPortPreference.getText() != null) 
	    {
	    	supPortPreference.setSummary(supPortPreference.getText()); 
		}
		
		if (supDomainPreference.getText() != null)
	    {
	        supDomainPreference.setSummary(supDomainPreference.getText());
	    }
		
	    if (applicationIDPreference.getText() != null) 
	    {
	    	applicationIDPreference.setSummary(applicationIDPreference.getText()); 
		}
		
	    if (securityConfigPreference.getText() != null) 
	    {
	    	securityConfigPreference.setSummary(securityConfigPreference.getText()); 
		}
		
	    if (farmIDPreference.getText() != null) 
	    {
	    	farmIDPreference.setSummary(farmIDPreference.getText()); 
		}
		
		if (supUrlSuffix.getText() != null)
	    {
	        supUrlSuffix.setSummary(supUrlSuffix.getText());
	    }
	    
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() 
	{
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
		
		PreferenceState preferenceState = new PreferenceState(getApplicationContext());
		if (!preferenceState.checkRequiredSUPSettings())
		{
			Toast.makeText(getBaseContext(), getString(R.string.sup_validation_msg), Toast.LENGTH_LONG).show();
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		if (key.equalsIgnoreCase(SUP_PREF_SUP_HOST_KEY)) 
		{
			supHostPreference.setText(supHostPreference.getText().trim());
	    	supHostPreference.setSummary(supHostPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(SUP_PREF_SUP_PORT_KEY)) 
		{
			supPortPreference.setText(supPortPreference.getText().trim());
	    	supPortPreference.setSummary(supPortPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(SUP_PREF_DOMAIN_KEY)) 
	    {
		    supDomainPreference.setText(supDomainPreference.getText().trim());
		    supDomainPreference.setSummary(supDomainPreference.getText()); 
	    }
		
		if (key.equalsIgnoreCase(SUP_PREF_APPLICATION_ID_KEY)) 
		{
			applicationIDPreference.setText(applicationIDPreference.getText().trim());
	    	applicationIDPreference.setSummary(applicationIDPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(SUP_PREF_SECURITY_CONFIGURATION_KEY)) 
		{
			securityConfigPreference.setText(securityConfigPreference.getText().trim());
	    	securityConfigPreference.setSummary(securityConfigPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(SUP_PREF_FARMID_KEY)) 
		{
			farmIDPreference.setText(farmIDPreference.getText().trim());
	    	farmIDPreference.setSummary(farmIDPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(SUP_PREF_URLSUFFIX_KEY)) 
	    {
		    supUrlSuffix.setText(supUrlSuffix.getText().trim());
		    supUrlSuffix.setSummary(supUrlSuffix.getText()); 
	    }
	}
}
