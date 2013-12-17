package com.test.usermanager.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.test.usermanager.R;
import com.test.usermanager.preferences.MainPreferencesActivity.ConnectionType;
import com.test.usermanager.preferences.GatewaySettingsPreferencesActivity.PreferencesAuthenticationType;
import com.sap.gwpa.proxy.connectivity.ConnectivitySettings;
import com.sap.gwpa.proxy.connectivity.ConnectivitySettings.AuthenticationType;

/**
 * A utility class responsible for reading the application preferences 
 * (as the service URL, the SAP client and the SUP server configurations).
 */
public class PreferencesUtilities 
{
	private static PreferencesUtilities INSTANCE;
	private Context context;
	
	private PreferencesUtilities(Context context) 
	{
		this.context = context;
	}
	
	/**
	 * @return singleton instance of PreferencesUtilities.
	 * @param context The application context for accessing the default shared preferences.
	 */
	public static synchronized PreferencesUtilities getInstance(Context context) 
	{
		if (INSTANCE == null) 
		{
			INSTANCE = new PreferencesUtilities(context);
		}
		
		return INSTANCE;
	}
	
	
	/**
	 * Update ConnectivitySettings parameters from the application preferences.
	 */
	public void updateConnectivitySettingsFromPreferences(ConnectivitySettings connectivitySettings) 
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);				
		String mode = preferences.getString(MainPreferencesActivity.MAIN_PREF_CONNECTION_TYPE_KEY, "");
		if (mode.equals("")) {
			// Set default values if preferences has never been accessed before
			PreferenceManager.setDefaultValues(this.context, R.xml.main_preference_screen, true);
			PreferenceManager.setDefaultValues(this.context, R.xml.gateway_settings_preference_screen, true);
			PreferenceManager.setDefaultValues(this.context, R.xml.sup_settings_preference_screen, true);
			mode = preferences.getString(MainPreferencesActivity.MAIN_PREF_CONNECTION_TYPE_KEY, ""); //re-read mode
		}

		if (ConnectionType.valueOf(mode).equals(ConnectionType.SUP)) 
		{
			// SUP mode:
			connectivitySettings.setSUPMode(true);
			
			// Update SUP connectivity settings from shared preferences
			connectivitySettings.setSUPHost(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SUP_HOST_KEY, ""));
			connectivitySettings.setSUPPort(Integer.parseInt(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SUP_PORT_KEY, "0")));
			connectivitySettings.setDomain(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_DOMAIN_KEY, ""));
			connectivitySettings.setSUPAppID(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_APPLICATION_ID_KEY, ""));
			connectivitySettings.setSUPFarmID(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_FARMID_KEY, ""));
			connectivitySettings.setSUPSecurityConfiguration(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_SECURITY_CONFIGURATION_KEY, ""));
			connectivitySettings.setSupUrlSuffix(preferences.getString(SUPSettingsPreferencesActivity.SUP_PREF_URLSUFFIX_KEY, ""));
			
			// Reset GW connectivity settings
			connectivitySettings.setAuthenticationType(AuthenticationType.UsernamePasswordAuthenticationType);
			connectivitySettings.setPortalUrl("");
		}
		else 
		{
			// Gateway mode:
			connectivitySettings.setSUPMode(false);
			
			// Reset SUP connectivity settings
			connectivitySettings.setSUPHost(null);
			connectivitySettings.setSUPPort(0);
			connectivitySettings.setDomain(null);
			connectivitySettings.setSUPAppID(null);
			connectivitySettings.setSUPFarmID(null);
			connectivitySettings.setSUPSecurityConfiguration(null);
			connectivitySettings.setSupUrlSuffix(null);
			
			// set authentication type
			String authenticationMethodString = preferences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_AUTHENTICATION_METHOD_KEY, "");
			if (PreferencesAuthenticationType.valueOf(authenticationMethodString).equals(PreferencesAuthenticationType.Portal)) 
			{
				connectivitySettings.setAuthenticationType(AuthenticationType.PortalAuthenticationType);
				connectivitySettings.setPortalUrl(preferences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_PORTAL_URL_KEY, ""));
			}
			else 
			{
				connectivitySettings.setAuthenticationType(AuthenticationType.UsernamePasswordAuthenticationType);
			}
		}
	}
	
	/**
	 * @return The service URL from the application preferences.
	 */
	public String getServiceURL() 
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);		
		// Set default values if preferences has never been accessed before
		PreferenceManager.setDefaultValues(this.context, R.xml.gateway_settings_preference_screen, false); 
		String gatewayUrl = preferences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_URL_KEY, "");
		if (gatewayUrl.endsWith("/"))
		{
			return gatewayUrl;
		}
		else
		{
			return gatewayUrl + "/";
		}
	}
	
	/**
	 * @return The service SAP Client from the application preferences.
	 */
	public String getServiceClient() 
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
		// Set default values if preferences has never been accessed before
		PreferenceManager.setDefaultValues(this.context, R.xml.gateway_settings_preference_screen, false);		
		return preferences.getString(GatewaySettingsPreferencesActivity.GATEWAY_PREF_SAP_CLIENT_KEY, "");
	}
}
