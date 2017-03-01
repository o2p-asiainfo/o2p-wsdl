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

package com.eviware.soapui.impl.wsdl.support.wsdl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.xml.namespace.QName;



import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.WsaVersionTypeConfig;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.BindingImporter;
import com.eviware.soapui.impl.wsdl.support.policy.PolicyUtils;
import com.eviware.soapui.impl.wsdl.support.soap.Soap11HttpBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.Soap12HttpBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.SoapJMSBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.TibcoSoapJMSBindingImporter;
import com.eviware.soapui.settings.WsdlSettings;
import com.eviware.soapui.support.UISupport;

/**
 * Importer for WsdlInterfaces from WSDL urls / files
 * 
 * @author Ole.Matzura
 */

public final class WsdlImporter
{
	private static List<BindingImporter> bindingImporters = new ArrayList<BindingImporter>();
	@SuppressWarnings( "unused" )
	private static WsdlImporter instance;

	private static final Logger LOG = Logger.getLog( WsdlImporter.class );

	static
	{
		try
		{
			bindingImporters.add( new Soap11HttpBindingImporter() );
			bindingImporters.add( new Soap12HttpBindingImporter() );
			bindingImporters.add( new SoapJMSBindingImporter() );
			bindingImporters.add( new TibcoSoapJMSBindingImporter() );
		}
		catch( BusinessException e )
		{
			SoapUI.logError( e );
		}
	}
	
	private WsdlImporter(){}

	public static WsdlInterface[] importWsdl( WsdlProject project, String wsdlUrl ) throws Exception
	{
		return importWsdl( project, wsdlUrl, null );
	}

	public static WsdlInterface[] importWsdl( WsdlProject project, String wsdlUrl, QName bindingName ) throws Exception
	{
		return importWsdl( project, wsdlUrl, bindingName, null );
	}

	public static WsdlInterface[] importWsdl( WsdlProject project, String wsdlUrl, QName bindingName,
			WsdlLoader wsdlLoader ) throws Exception
	{
		String transformerWsdlUrl = wsdlUrl;
		transformerWsdlUrl = startsWithWsdlUrl(wsdlUrl, transformerWsdlUrl);

		WsdlContext wsdlContext = new WsdlContext( transformerWsdlUrl );
		if( !wsdlContext.load( wsdlLoader ) )
		{
			UISupport.showErrorMessage( "Failed to import WSDL" );
			return null;
		}

		Definition definition = wsdlContext.getDefinition();
		List<WsdlInterface> result = new ArrayList<WsdlInterface>();
		if( bindingName != null )
		{
			WsdlInterface iface = importBinding( project, wsdlContext,
					( Binding )definition.getAllBindings().get( bindingName ) );
			return iface == null ? new WsdlInterface[0] : new WsdlInterface[] { iface };
		}

		Map<Binding, WsdlInterface> importedBindings = new HashMap<Binding, WsdlInterface>();

		Map<?, ?> serviceMap = definition.getAllServices();
		judgeServiceMap(project, transformerWsdlUrl, wsdlContext, result,
				importedBindings, serviceMap);

		Map<?, ?> bindingMap = definition.getAllBindings();
		if( !bindingMap.isEmpty() )
		{
			Iterator<?> i = bindingMap.values().iterator();
			while( i.hasNext() )
			{
				Binding binding = ( Binding )i.next();
				if( importedBindings.containsKey( binding ) )
				{
					continue;
				}

				PortType portType = binding.getPortType();
				if( portType == null )
				{
					LOG.warn( "Missing portType for binding [" + binding.getQName().toString() + "]" );
				}
				else
				{
					String ifaceName = getInterfaceNameForBinding( binding );
					WsdlInterface ifc = ( WsdlInterface )project.getInterfaceByName( ifaceName );
					if( ifc != null && result.indexOf( ifc ) == -1 )
					{
						Boolean res = UISupport.confirmOrCancel( "Interface [" + ifc.getName()
								+ "] already exists in project, update instead?", "Import WSDL" );
						if( res == null ) {
							return new WsdlInterface[0];
						}
						if( res.booleanValue() )
						{
							if( ifc.updateDefinition( transformerWsdlUrl, false ) )
							{
								importedBindings.put( binding, ifc );
								result.add( ifc );
							}
						}

						continue;
					}

					WsdlInterface iface = importBinding( project, wsdlContext, binding );
					if( iface != null )
					{
						result.add( iface );
						importedBindings.put( binding, ifc );
					}
				}
			}
		}

		if( importedBindings.isEmpty() && serviceMap.isEmpty() && bindingMap.isEmpty() )
		{
			UISupport.showErrorMessage( "Found nothing to import in [" + transformerWsdlUrl + "]" );
		}

		// only the last gets the context
		if( result.size() > 0 )
		{
			result.get( result.size() - 1 ).setWsdlContext( wsdlContext );
		}

		return result.toArray( new WsdlInterface[result.size()] );
	}

	private static void judgeServiceMap(WsdlProject project,
			String transformerWsdlUrl, WsdlContext wsdlContext,
			List<WsdlInterface> result,
			Map<Binding, WsdlInterface> importedBindings, Map<?, ?> serviceMap)
			throws Exception {
		if( serviceMap.isEmpty() ){
			LOG.info( "Missing services in [" + transformerWsdlUrl + "], check for bindings" );
		}
		else
		{
			Iterator<?> i = serviceMap.values().iterator();
			while( i.hasNext() )
			{
				Service service = ( Service )i.next();
				Map<?, ?> portMap = service.getPorts();
				Iterator<?> i2 = portMap.values().iterator();
				while( i2.hasNext() )
				{
					Port port = ( Port )i2.next();

					Binding binding = port.getBinding();
					if( importedBindings.containsKey( binding ) )
					{
						// add endpoint since it could differ from already imported
						// one..
						String endpoint = WsdlUtils.getSoapEndpoint( port );
						if( endpoint != null ){
							importedBindings.get( binding ).addEndpoint( endpoint );
						}
						continue;
					}

					String ifaceName = getInterfaceNameForBinding( binding );
					WsdlInterface ifc = ( WsdlInterface )project.getInterfaceByName( ifaceName );
					if( ifc != null )
					{
						getWsdlInterface(wsdlContext, result, importedBindings,
								port, binding, ifc);
					}else{
						WsdlInterface iface = importBinding( project, wsdlContext, binding );
						if( iface != null )
						{
							getWsdlInterface(wsdlContext, result,
									importedBindings, port, binding, iface);
						}
					}

					
				}
			}
		}
	}

	private static String startsWithWsdlUrl(String wsdlUrl,
			String transformerWsdlUrl) {
		if( wsdlUrl.startsWith( "file:" ) )
		{
			transformerWsdlUrl = transformerWsdlUrl.replace( '/', File.separatorChar );
			transformerWsdlUrl = transformerWsdlUrl.replace( '\\', File.separatorChar );
		}
		return transformerWsdlUrl;
	}

	private static void getWsdlInterface(WsdlContext wsdlContext,
			List<WsdlInterface> result,
			Map<Binding, WsdlInterface> importedBindings, Port port,
			Binding binding, WsdlInterface ifc) throws Exception {
		String endpoint = WsdlUtils.getSoapEndpoint( port );
		if( endpoint != null ){
			ifc.addEndpoint( endpoint );
		}

		if( ifc.getWsaVersion().equals( WsaVersionTypeConfig.NONE.toString() ) ) {
			ifc.setWsaVersion( WsdlUtils.getUsingAddressing( port ) );
		}
		if( ifc.getWsaVersion().equals( WsaVersionTypeConfig.NONE.toString() ) )
		{
			ifc.processPolicy( PolicyUtils.getAttachedPolicy( port, wsdlContext.getDefinition() ) );
		}

		result.add( ifc );
		importedBindings.put( binding, ifc );
	}

	public final static String getInterfaceNameForBinding( Binding binding )
	{
		if( SoapUI.getSettings().getBoolean( WsdlSettings.NAME_WITH_BINDING ) ) {
			return binding.getQName().getLocalPart();
		}
		else {
			return binding.getPortType().getQName().getLocalPart();
		}
	}

	private static WsdlInterface importBinding( WsdlProject project, WsdlContext wsdlContext, Binding binding )
			throws Exception
	{
		LOG.info( "Finding importer for " + binding.getQName() );
		for( int c = 0; c < bindingImporters.size(); c++ )
		{
			BindingImporter importer = bindingImporters.get( c );
			if( importer.canImport( binding ) )
			{
				LOG.info( "Importing binding " + binding.getQName() );
				WsdlInterface iface = importer.importBinding( project, wsdlContext, binding );

				String url = wsdlContext.getUrl();
				iface.setDefinition( url );

				return iface;
			}
		}
		LOG.info( "Missing importer for " + binding.getQName() );

		return null;
	}
}
