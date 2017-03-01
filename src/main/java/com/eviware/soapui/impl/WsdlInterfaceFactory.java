/*
 *  SoapUI, copyright (C) 2004-2012 smartbear.com
 *
 *  SoapUI is free software; you can redistribute it and/or modify it under the
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  SoapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.namespace.QName;



import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.InterfaceConfig;
import com.eviware.soapui.config.WsdlInterfaceConfig;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.propertyexpansion.DefaultPropertyExpansionContext;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;
import com.eviware.soapui.model.propertyexpansion.PropertyExpansionContext;
import com.eviware.soapui.settings.WsdlSettings;
import com.eviware.soapui.support.SoapUIException;

public class WsdlInterfaceFactory implements InterfaceFactory<WsdlInterface>
{
	public static final String WSDL_TYPE = "wsdl";
	private static final Logger LOG = Logger.getLog( WsdlInterfaceFactory.class );

	public WsdlInterface build( WsdlProject project, InterfaceConfig config )
	{
		return new WsdlInterface( project, ( WsdlInterfaceConfig )config.changeType( WsdlInterfaceConfig.type ) );
	}

	public WsdlInterface createNew( WsdlProject project, String name )
	{
		WsdlInterface iface = new WsdlInterface( project, ( WsdlInterfaceConfig )project.getConfig().addNewInterface()
				.changeType( WsdlInterfaceConfig.type ) );
		iface.setName( name );

		return iface;
	}

	public static WsdlInterface[] importWsdl( WsdlProject project, String url, boolean createRequests )
			throws SoapUIException
	{
		return importWsdl( project, url, createRequests, null, null );
	}

	public static WsdlInterface[] importWsdl( WsdlProject project, String url, boolean createRequests,
			WsdlLoader wsdlLoader ) throws SoapUIException
	{
		return importWsdl( project, url, createRequests, null, wsdlLoader );
	}

	public static WsdlInterface[] importWsdl( WsdlProject project, String url, boolean createRequests,
			QName bindingName, WsdlLoader wsdlLoader ) throws SoapUIException
	{
		WsdlInterface[] result;

		PropertyExpansionContext context = new DefaultPropertyExpansionContext( project.getModelItem() );
		String newUrl = PropertyExpander.expandProperties( context, url );
		try
		{
			result = WsdlImporter.importWsdl( project, newUrl, bindingName, wsdlLoader );
		}
		catch( Exception e )
		{
			LOG.error( "Error importing wsdl: " + e );
			SoapUI.logError( e );
			throw new SoapUIException( "Error importing wsdl", e );
		}

		try
		{
			if( createRequests && result != null )
			{
				for( WsdlInterface iface : result )
				{
					getOperation(project, iface);
				}
			}
		}
		catch( BusinessException e )
		{
			LOG.error( "Error creating requests: " + e.getMessage() );
			throw new SoapUIException( "Error creating requests", e );
		}

		return result;
	}

	private static void getOperation(WsdlProject project, WsdlInterface iface) {
		for( int c = 0; c < iface.getOperationCount(); c++ )
		{
			WsdlOperation operation = iface.getOperationAt( c );
			WsdlRequest request = operation.addNewRequest( "Request 1" );
			try
			{
				String requestContent = operation.createRequest( project.getSettings().getBoolean(
						WsdlSettings.XML_GENERATION_ALWAYS_INCLUDE_OPTIONAL_ELEMENTS ) );
				request.setRequestContent( requestContent );
			}
			catch( BusinessException e )
			{
				SoapUI.logError( e );
			}
		}
	}

	public static void main( String[] args ) throws URISyntaxException, IOException
	{
		java.awt.Desktop.getDesktop().browse(new URI("http://www.sunet.se"));
	}
}
