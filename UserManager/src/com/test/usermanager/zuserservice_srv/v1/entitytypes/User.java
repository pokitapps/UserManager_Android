package com.test.usermanager.zuserservice_srv.v1.entitytypes;
/*

 Auto-Generated by SAP NetWeaver Gateway Productivity Accelerator, Version 1.1.1

*/
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.gwpa.proxy.BaseEntityType;
import com.sap.gwpa.proxy.ODataQuery;
import com.sap.gwpa.proxy.TypeConverter;
import com.sap.mobile.lib.parser.IODataEntry;
import com.sap.mobile.lib.parser.IODataProperty;
import com.sap.mobile.lib.parser.IODataSchema;
import com.sap.mobile.lib.parser.IODataServiceDocument;
import com.sap.mobile.lib.parser.IParser;
import com.sap.mobile.lib.parser.ODataEntry;

/**
 * User Entity Type 
 *
 * <br>key (Username)
 */
public class User extends BaseEntityType 
{
	// User properties
    private String Username;
    private String Firstname;
    private String Lastname;
    private String Telephone;
    private String Email;

	// reference to the parser
	private IParser parser;
	// reference to the schema
	private IODataSchema schema;

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


	private static Map<String, String> userLabels;

	private static Map<String, String> userTypes;	
	
	/**
	 * Constructor
	 * @param entry
	 * @throws MalformedURLException
	 */
	public User(final IODataEntry entry, IParser parser, IODataSchema schema) throws MalformedURLException
	{
		super(entry);
		this.Username =  TypeConverter.getAsString(entry.getPropertyValue("Username"));
		this.Firstname =  TypeConverter.getAsString(entry.getPropertyValue("Firstname"));
		this.Lastname =  TypeConverter.getAsString(entry.getPropertyValue("Lastname"));
		this.Telephone =  TypeConverter.getAsString(entry.getPropertyValue("Telephone"));
		this.Email =  TypeConverter.getAsString(entry.getPropertyValue("Email"));
	    this.parser = parser;
        this.schema = schema;
 	}
	
	/**
	 * User Constructor</br>
	 * Dummy values may apply
	 *
	 */
	public User( String Username) 
	{
		super(new ODataEntry());
		
        this.setUsername(Username);
	}



	// User properties getters and setters
		
	/**
	 * @return - String User
	 */
	public String getUsername()
	{
		return this.Username;
	}
	
	/**
	 * @param Username - User
	 */
	public void setUsername(String Username)
	{
		this.Username = Username;
		
		getEntry().putPropertyValue("Username", Username);
	}
		
	/**
	 * @return - String First name
	 */
	public String getFirstname()
	{
		return this.Firstname;
	}
	
	/**
	 * @param Firstname - First name
	 */
	public void setFirstname(String Firstname)
	{
		this.Firstname = Firstname;
		
		getEntry().putPropertyValue("Firstname", Firstname);
	}
		
	/**
	 * @return - String Last name
	 */
	public String getLastname()
	{
		return this.Lastname;
	}
	
	/**
	 * @param Lastname - Last name
	 */
	public void setLastname(String Lastname)
	{
		this.Lastname = Lastname;
		
		getEntry().putPropertyValue("Lastname", Lastname);
	}
		
	/**
	 * @return - String Telephone
	 */
	public String getTelephone()
	{
		return this.Telephone;
	}
	
	/**
	 * @param Telephone - Telephone
	 */
	public void setTelephone(String Telephone)
	{
		this.Telephone = Telephone;
		
		getEntry().putPropertyValue("Telephone", Telephone);
	}
		
	/**
	 * @return - String E-Mail Address
	 */
	public String getEmail()
	{
		return this.Email;
	}
	
	/**
	 * @param Email - E-Mail Address
	 */
	public void setEmail(String Email)
	{
		this.Email = Email;
		
		getEntry().putPropertyValue("Email", Email);
	}
	
	/**
	 * @return - representation of the Entity Type object in OData4SAP format
	 */
	public String getStringPayload()  
	{
		String xml = null;
		
		xml = getEntry().toXMLString();
			
		return xml;
	}
	
	/**
	 * @return - self ODataQuery object
	 * @throws MalformedURLException 
	 */
	public ODataQuery getEntityQuery() throws MalformedURLException  
	{
		return new ODataQuery(getEntry().getSelfLink().getUrl());
	}
	
	/**
	 * @return - the date format.
	 */
	public DateFormat getDateFormat()
	{
		return this.dateFormat;
	}


	/**
    * Static method that loads all of the entity type property labels. 
    * This method is called when the service class is initialized.
    * @param service Service document object containing all of the entity type properties.
   	*/	
    public static void loadLabels(IODataServiceDocument service)
    {
    	List<IODataProperty> properties = getSchemaPropertiesFromCollection(service, "Users" );
        
    	userLabels = new HashMap<String, String>();
    	userTypes = new HashMap<String, String>();
    	
    	if (properties != null)
    	{
	        for (IODataProperty property : properties) 
	        {
	        	userLabels.put(property.getName(), property.getLabel());
	        	userTypes.put(property.getName(), property.getType());
			}
    	}
    }
    
    
    /**
    * Static method that returns the type for a given property name.
    * @param propertyName Property name.
    * @return Property label.
   	*/
    public static String getTypeForProperty(String propertyName)
    {
        return getLabelFromDictionary(userTypes, propertyName);
    }
    
    
    /**
    * Static method that returns the label for a given property name.
    * @param propertyName Property name.
    * @return Property label.
   	*/
    public static String getLabelForProperty(String propertyName)
    {
        return getLabelFromDictionary(userLabels, propertyName);
    }
}