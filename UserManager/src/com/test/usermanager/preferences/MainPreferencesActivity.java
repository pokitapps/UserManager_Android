package com.test.usermanager.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.test.usermanager.R;
import com.test.usermanager.preferences.MainPreferencesActivity.ConnectionType;

public class MainPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	public enum ConnectionType 
	{
		Gateway,
		SUP
	}
	
	/**
	 * flag key used in the intent that indicate if preferences are change
	 */
	public static final String PREFERENCES_CHANGED_FLAG_KEY = "preferences_changed_flag_key";
	
	/**
	 * preferences keys used inside application
	 */
	public static final String MAIN_PREF_CONNECTION_TYPE_KEY = "main_pref_connection_type";
	
	/**
	 * preferences keys used only inside activity
	 */
	private static final String MAIN_PREF_GATEWAY_SETTINGS_KEY = "main_pref_gateway_settings";
	private static final String MAIN_PREF_SUP_SETTINGS_KEY = "main_pref_sup_settings";
	
	/**
	 * preferences instances used inside the activity
	 */
	private ListPreference connectionTypePreference;
	private Preference gatewaySettingsPreference;
	private Preference supSettingsPreference;
	
	/**
	 * preferences state before make change and after
	 */
	private PreferenceState preferenceStateBefore;
	private PreferenceState preferenceStateAfter;
	
	
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	
	    addPreferencesFromResource(R.xml.main_preference_screen);
	    
	    connectionTypePreference = (ListPreference) getPreferenceScreen().findPreference(MAIN_PREF_CONNECTION_TYPE_KEY);
	    gatewaySettingsPreference = getPreferenceScreen().findPreference(MAIN_PREF_GATEWAY_SETTINGS_KEY);
	    supSettingsPreference = getPreferenceScreen().findPreference(MAIN_PREF_SUP_SETTINGS_KEY);	
	    preferenceStateBefore = new PreferenceState(getApplicationContext());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		if (connectionTypePreference.getEntry() != null) 
		{
			connectionTypePreference.setSummary(connectionTypePreference.getEntry());
		}
		
		enableDisableGatewayOrSUPPreference();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() 
	{	    	    
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}

	@Override
	public void onBackPressed() 
	{
		preferenceStateAfter = new PreferenceState(getApplicationContext());
		
	    Intent returnIntent = new Intent();
	    returnIntent.putExtra(PREFERENCES_CHANGED_FLAG_KEY,!preferenceStateAfter.equals(preferenceStateBefore));
	    setResult(RESULT_OK,returnIntent);     

		super.onBackPressed();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		if (key.equalsIgnoreCase(MAIN_PREF_CONNECTION_TYPE_KEY)) 
		{
			// set the summary according to the selected value
			connectionTypePreference.setSummary(connectionTypePreference.getEntry());
			
			// modify the other preferences Enable/Disable properties according to selection
			enableDisableGatewayOrSUPPreference();
		}
		
		// if change to SUP mode check if SUP Settings were configured.
		PreferenceState preferenceState = new PreferenceState(getApplicationContext());

		if (ConnectionType.valueOf(connectionTypePreference.getValue().toString()).equals(ConnectionType.SUP))
		{
			if (!preferenceState.checkRequiredSUPSettings())
			{
				Toast.makeText(getBaseContext(), getString(R.string.main_sup_validation), Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			// handle gateway mode.
			if (!preferenceState.checkRequiredGatewaySettings())
			{
				Toast.makeText(getBaseContext(), getString(R.string.main_gw_validation), Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private void enableDisableGatewayOrSUPPreference()
	{
		boolean isSelectedGateway = true;
		if (connectionTypePreference.getValue() != null) 
		{
			isSelectedGateway = ConnectionType.valueOf(connectionTypePreference.getValue().toString()).equals(ConnectionType.Gateway);
		}
		gatewaySettingsPreference.setEnabled(isSelectedGateway);
		supSettingsPreference.setEnabled(!isSelectedGateway);
	}
}

/**
 * A class that holds the preference state.
 */
class PreferenceState
{
	private String connectionType;
	private String gatewayUrl;
	private String gatewaySapClient;	
	private String supHost;
	private String supPort;
	private String supDomain;
	private String supApplicationID;
	private String supFarmID;
	private String supSecurityConfiguration;
	private String supUrlSuffix;
	private String authenticationType;
	private String portalUrl;
	
	/**
	 * Initializes the state with data from the preferences.
	 * @param context - application context.
	 */
	public PreferenceState(Context context)
	{
		SharedPreferences prefenrences = PreferenceManager.getDefaultSharedPreferences(context);
		PreferenceManager.setDefaultValues(context, R.xml.gateway_settings_preference_screen, false);		
		
		connectionType = prefenrences.getString(MainPreferencesActivity.MAIN_PREF_CONNECTION_TYPE_KEY, "");
		gatewayUrl = prefenrences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_URL_KEY, "");
		gatewaySapClient = prefenrences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_SAP_CLIENT_KEY, "");
		supHost = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SUP_HOST_KEY, "");
		supPort = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SUP_PORT_KEY, "");
		supDomain = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_DOMAIN_KEY, "");
		supApplicationID = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_APPLICATION_ID_KEY, "");
		supFarmID = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_FARMID_KEY, "");
		supSecurityConfiguration = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SECURITY_CONFIGURATION_KEY, "");
		supUrlSuffix = prefenrences.getString(SUPSettingsPreferencesActivity.SUP_PREF_URLSUFFIX_KEY, "");
		authenticationType = prefenrences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_AUTHENTICATION_METHOD_KEY, "");
		portalUrl = prefenrences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_PORTAL_URL_KEY, "");
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		result = prime * result + ((gatewaySapClient == null) ? 0 : gatewaySapClient.hashCode());
		result = prime * result + ((gatewayUrl == null) ? 0 : gatewayUrl.hashCode());
		result = prime * result + ((supApplicationID == null) ? 0 : supApplicationID.hashCode());
		result = prime * result + ((supFarmID == null) ? 0 : supFarmID.hashCode());
		result = prime * result + ((supHost == null) ? 0 : supHost.hashCode());
		result = prime * result + ((supPort == null) ? 0 : supPort.hashCode());
		result = prime * result + ((supDomain == null) ? 0 : supDomain.hashCode());
		result = prime * result + ((supSecurityConfiguration == null) ? 0 : supSecurityConfiguration.hashCode());
		result = prime * result + ((supUrlSuffix == null) ? 0 : supUrlSuffix.hashCode());
		result = prime * result + ((authenticationType == null) ? 0 : authenticationType.hashCode());
		result = prime * result + ((portalUrl == null) ? 0 : portalUrl.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		PreferenceState other = (PreferenceState) obj;
		
		if (connectionType == null) 
		{
			if (other.connectionType != null)
				return false;
		} 
		else if (!connectionType.equals(other.connectionType))
			return false;
		
		if (ConnectionType.valueOf(connectionType).equals(ConnectionType.Gateway))
		{
			if (authenticationType == null) 
			{
				if (other.authenticationType != null)
					return false;
			} 
			else if (!authenticationType.equals(other.authenticationType))
				return false;
			if (gatewaySapClient == null) 
			{
				if (other.gatewaySapClient != null)
					return false;
			} 
			else if (!gatewaySapClient.equals(other.gatewaySapClient))
				return false;
			if (gatewayUrl == null) 
			{
				if (other.gatewayUrl != null)
					return false;
			} 
			else if (!gatewayUrl.equals(other.gatewayUrl))
				return false;
			if (portalUrl == null) 
			{
				if (other.portalUrl != null)
					return false;
			} 
			else if (!portalUrl.equals(other.portalUrl))
				return false;
		}
		else if (ConnectionType.valueOf(connectionType).equals(ConnectionType.SUP)) 
		{
			if (supApplicationID == null) 
			{
				if (other.supApplicationID != null)
					return false;
			} 
			else if (!supApplicationID.equals(other.supApplicationID))
				return false;
			if (supFarmID == null) 
			{
				if (other.supFarmID != null)
					return false;
			} 
			else if (!supFarmID.equals(other.supFarmID))
				return false;
			if (supHost == null) 
			{
				if (other.supHost != null)
					return false;
			} 
			else if (!supHost.equals(other.supHost))
				return false;
			if (supPort == null) 
			{
				if (other.supPort != null)
					return false;
			} 
			else if (!supPort.equals(other.supPort))
				return false;
			if (supSecurityConfiguration == null) 
			{
				if (other.supSecurityConfiguration != null)
					return false;
			} 
			else if (!supSecurityConfiguration.equals(other.supSecurityConfiguration))
				return false;
			if (supDomain == null) 
			{
                if (other.supDomain != null)
                    return false;
            } 
            else if (!supDomain.equals(other.supDomain))
                return false;
			if (supUrlSuffix == null) 
			{
                if (other.supUrlSuffix != null)
                    return false;
            } 
            else if (!supUrlSuffix.equals(other.supUrlSuffix))
                return false;
		}
		
		return true;
	}
	
	/**
	 * Checks whether the required SUP settings are filled.
	 * @return - true whether the required SUP settings are filled,
	 * and false otherwise.
	 */
	public boolean checkRequiredSUPSettings()
	{
		if (!TextUtils.isEmpty(supHost) && 
				!TextUtils.isEmpty(supPort) &&
					!TextUtils.isEmpty(supApplicationID) &&
						!TextUtils.isEmpty(supFarmID) &&
							!TextUtils.isEmpty(supSecurityConfiguration)) 
		{
			return true;
		}
		else 
		{
			return false;
		}
			
	}
	
	/**
	 * Checks whether the required Gateway settings are filled.
	 * @return - true whether the required Gateway settings are filled,
	 * and false otherwise.
	 */
	public boolean checkRequiredGatewaySettings()
	{
		if (!TextUtils.isEmpty(gatewayUrl)){
			return true;
		}
		else 
		{
			return false;
		}
	}
}
