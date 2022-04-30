/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package loadingModel;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import java.net.URISyntaxException;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;

import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.uml2.uml.UMLPackage;


public class loadingModel {
	
	public loadingModel() { }
	
	
	public PlainXmlModel loadPlainXmlModel(String x , String y) throws Exception{
	
		PlainXmlModel XmlModelLoaded = new PlainXmlModel();
		XmlModelLoaded.setName(y);
		XmlModelLoaded.getAliases().add("In");
		XmlModelLoaded.setFile(new File(x));
		XmlModelLoaded.load();
		System.err.println("load " + y);
		return XmlModelLoaded;
		
	}
	
	public EmfModel loadEMFModel(String x , String y , String z) throws Exception{
		
		EmfModel EMFModelLoaded = new EmfModel();
		EMFModelLoaded.setName(z);
		EMFModelLoaded.getAliases().add("In");
		EMFModelLoaded.setMetamodelFile(y);
		EMFModelLoaded.setModelFile(x);
		
		EMFModelLoaded.load();
		System.err.println("load " + z);
		return EMFModelLoaded;
		
	}
	
	public IModel loadUMLModel(String modelName , String modelURI ) throws Exception{
		
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		System.err.println(UMLPackage.eNS_URI);
		List<IModel> models = new ArrayList<IModel>();
		models.add(createEmfModelByURI(modelName , modelURI , UMLPackage.eNS_URI, true, true));
		System.err.println("load " + modelName);
		models.get(0).getAliases().add("In");
		return models.get(0);
		
	}
	
	protected static EmfModel createEmfModelByURI(String modelName, String modelURI, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, modelName);
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, 
				"file:///"+modelURI );
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
}
	

