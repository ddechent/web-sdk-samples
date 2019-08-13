/* FOREWORD:

The code enables searching through a search directory for folders that can either be visible-only or hidden.
The change is only a matter of commenting out the object flag on search: EnumDSSXMLObjectFlags.DssXmlObjectFindHidden
If ObjectFindHidden is on - it find all files (including the hidden ones) If the flag is not there - only visible
folders will be returned. Later it also lists the folders with all the details. 

You can also use it for searching for other types of files (apart from folders) by changing the flag on searchTypes: 
search.types().add(EnumDSSXMLObjectTypes.DssXmlTypeFolder). 

The information that you have to fill in is: 
1. The package
2. The details of te I-Server session
3. The GUID of the folder that you want to search through - the sample one used is the Public Objects folder.

Apart from those details the code is ready to run.

Made with love by Edgar, based on [278906/DE124645/KB16830]
Did not use object.populate() but search.getResults() instead. Chose to search folders as needed it for practical purposes.

Find the code below, and ENJOY. */

package com.microstrategy.samples.searching;

import java.util.Enumeration;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebSearch;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLSearchFlags;

public class hiddenObjects {

	public static void main(String[] args) throws WebObjectsException { 

		// Starting the session
		WebIServerSession serverSession = startTheIServerSession();
		
		// Performing the search
		performTheSearch(serverSession);

	}
	
	public static WebIServerSession startTheIServerSession() {
	    
		System.out.println("Initiating the session.");

		// Connectivity details for the intelligence server
		String intelligenceServerName = "SERVER";
		String projectName = "MicroStrategy Tutorial";
		String microstrategyUsername = "Administrator";
		String microstrategyPassword = "";
    
		// Creating our I-Server Session
		WebIServerSession serverSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
		
		return serverSession;
	  }
	
	public static void performTheSearch(WebIServerSession serverSession) throws WebObjectsException {
	    
		System.out.println("Performing the search.");

		WebObjectSource source = serverSession.getFactory().getObjectSource();
		WebSearch search = source.getNewSearchObject();

		String folderGUID = "98FE182C2A10427EACE0CD30B6768258"; 

		search.setSearchRoot(folderGUID);
		search.setFlags(EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard | EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive | EnumDSSXMLObjectFlags.DssXmlObjectFindHidden);
		search.setAsync(false);

		search.types().add(EnumDSSXMLObjectTypes.DssXmlTypeFolder);
		search.submit();	

		System.out.println("Search submitted.");

		WebFolder folderWithResults = search.getResults();
		System.out.println("Filtering the results.");

		int size = folderWithResults.size();

		System.out.println("All of the folders from within the search directory: ");

		// Getting a list of results into Enumeration
		Enumeration<WebObjectInfo> results = folderWithResults.elements();

		// Iterating through each result, while results remain
		while (results.hasMoreElements()) {
			WebObjectInfo result = results.nextElement();
			System.out.println(result.getName());
		}
		
		System.out.println("Size of the folder is: " + size);
		
	  }
}	

