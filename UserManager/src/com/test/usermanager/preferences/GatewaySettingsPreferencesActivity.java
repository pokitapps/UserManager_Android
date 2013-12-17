package com.test.usermanager.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.test.usermanager.R;

public class GatewaySettingsPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	public enum PreferencesAuthenticationType 
	{
		Gateway,
		Portal
	}

	/**
	 * preferences used in the application
	 */
 	public static final String GATEWAY_PREF_AUTHENTICATION_METHOD_KEY = "gateway_pref_auth_method";
	public static final String GATEWAY_PREF_URL_KEY = "gateway_pref_url";
	public static final String GATEWAY_PREF_SAP_CLIENT_KEY = "gateway_pref_sap_client";
	public static final String GATEWAY_PREF_PORTAL_URL_KEY = "gateway_pref_portal_url";

	/**
	 * preferences instances used inside the activity
	 */
	private ListPreference authenticationMethodPreference;
	private EditTextPreference urlPreference;
	private EditTextPreference sapClientPreference;
	private EditTextPreference portalUrlPreference;
	
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	
	    addPreferencesFromResource(R.xml.gateway_settings_preference_screen);
	    authenticationMethodPreference = (ListPreference) getPreferenceScreen().findPreference(GATEWAY_PREF_AUTHENTICATION_METHOD_KEY);
	    urlPreference = (EditTextPreference) getPreferenceScreen().findPreference(GATEWAY_PREF_URL_KEY);
	    sapClientPreference = (EditTextPreference) getPreferenceScreen().findPreference(GATEWAY_PREF_SAP_CLIENT_KEY);	
	    portalUrlPreference = (EditTextPreference) getPreferenceScreen().findPreference(GATEWAY_PREF_PORTAL_URL_KEY);
	   
	    urlPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
	    {
			public boolean onPreferenceChange(Preference preference,Object newValue) 
			{
				String newUrl = newValue.toString().trim();
				if (URLUtil.isHttpUrl(newUrl) || URLUtil.isHttpsUrl(newUrl)) 
				{
					return true;
				}
				else
				{
					Toast.makeText(getBaseContext(), getString(R.string.validation_msg), Toast.LENGTH_LONG).show();
					return false;
				}
			}
	    });
	    
	    sapClientPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
	    {
			public boolean onPreferenceChange(Preference preference,Object newValue) 
			{
				String newSAPClient = newValue.toString().trim();
				// in case - default Sap client
				if (!TextUtils.isEmpty(newSAPClient)) 
				{
					try 
					{
						Integer.parseInt(newSAPClient);
					} 
					catch (NumberFormatException e) 
					{
						Toast.makeText(getBaseContext(), getString(R.string.validation_msg), Toast.LENGTH_LONG).show();
						return false;
					}
				}
				return true;
			}
	    });
	    
		portalUrlPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
			public boolean onPreferenceChange(Preference preference, Object newValue) 
			{
				String newUrl = newValue.toString().trim();
				if (URLUtil.isHttpUrl(newUrl) || URLUtil.isHttpsUrl(newUrl)) 
				{
					return true;
				}
				else
				{
					Toast.makeText(getBaseContext(), getString(R.string.validation_msg), Toast.LENGTH_LONG).show();
					return false;
				}
			}
		});
	    
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (authenticationMethodPreference.getEntry() != null) 
		{
			authenticationMethodPreference.setSummary(authenticationMethodPreference.getEntry());
		}
	    if (urlPreference.getText() != null) 
	    {
	    	urlPreference.setSummary(urlPreference.getText()); 
		}
	    if (sapClientPreference.getText() != null) 
	    {
	    	sapClientPreference.setSummary(sapClientPreference.getText()); 
		}
	    if (portalUrlPreference.getText() != null) 
	    {
	    	portalUrlPreference.setSummary(portalUrlPreference.getText()); 
		}

	    enableDisablePortalPreference();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() 
	{
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
		
		PreferenceState preferenceState = new PreferenceState(getApplicationContext());
		if (!preferenceState.checkRequiredGatewaySettings())
		{
			Toast.makeText(getBaseContext(), getString(R.string.gw_validation_msg), Toast.LENGTH_LONG).show();
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) 
	{
		if (key.equalsIgnoreCase(GATEWAY_PREF_AUTHENTICATION_METHOD_KEY)) 
		{
			// set the summary according to the selected value
			authenticationMethodPreference.setSummary(authenticationMethodPreference.getEntry());
			
			// modify the other preferences Enable/Disable properties according to selection
			enableDisablePortalPreference();
		}
		
		if (key.equalsIgnoreCase(GATEWAY_PREF_URL_KEY)) 
		{
			urlPreference.setText(urlPreference.getText().trim());
	    	urlPreference.setSummary(urlPreference.getText()); 	    	
		}
		
		if (key.equalsIgnoreCase(GATEWAY_PREF_SAP_CLIENT_KEY)) 
		{
			sapClientPreference.setText(sapClientPreference.getText().trim());
	    	sapClientPreference.setSummary(sapClientPreference.getText()); 
		}
		
		if (key.equalsIgnoreCase(GATEWAY_PREF_PORTAL_URL_KEY)) 
		{
			portalUrlPreference.setText(portalUrlPreference.getText().trim());
			portalUrlPreference.setSummary(portalUrlPreference.getText()); 	    	
		}
	}
	
	private void enableDisablePortalPreference() 
	{
		boolean isSelectedPortalAuthentication = false;
		if (authenticationMethodPreference.getValue() != null)
		{
			isSelectedPortalAuthentication = PreferencesAuthenticationType.valueOf(authenticationMethodPreference.getValue().toString()).equals(PreferencesAuthenticationType.Portal);
		}
		portalUrlPreference.setEnabled(isSelectedPortalAuthentication);
	}

	

}
