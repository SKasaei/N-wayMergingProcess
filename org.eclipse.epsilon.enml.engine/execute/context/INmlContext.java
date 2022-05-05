/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package execute.context;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.models.IModel;

import enml.INmlModule;
import nmlMatchTrace.nmlMatchTrace;
import strategy.IMergingStrategy;
import trace.MergeTrace;
import trace.transferTrace;

import org.eclipse.epsilon.etl.execute.context.IEtlContext;

import org.eclipse.epsilon.eol.dom.Parameter;

public interface INmlContext extends IEtlContext {

	public IMergingStrategy getMergingStrategy();

	public void setMergingStrategy(IMergingStrategy mergingStrategy);

	public MatchTrace getMatchTrace();

	public MergeTrace getMergeTrace();
	
	public transferTrace getTransferTrace();
	
	public void setMergerReport(String Report);
	public List<String> getMergerReport();
	
	public void setTransferObjects(Object transferObject);
	public List<Object> getTransferObjects();
	
	public void setTransferObjectsParameter(String transferObjectParameter);
	public List<String> getTransferObjectsParameter();
	
	void comparingInputModels_withBase(File ECLFile);
	void nmlMatchTrace();
	void nmlMatchTraceWithoutBase(File ECLFile);
	
	public List<nmlMatchTrace> getOctopusMatchTrace();
	public List<nmlMatchTrace> getOursMatchTrace();
	
	public IEclModule getEclModules();
	public List<IEclModule> getEclModule_WithBase();
	public void setEclModule_WithBase(List<IEclModule> ECLMODULES);
	
	public void setOctopusMatchTraceWB(List<nmlMatchTrace> nmlMatchTrace);
	public void setOursMatchTraceWB(List<nmlMatchTrace> nmlMatchTrace);
	public List<nmlMatchTrace> getOctopusMatchTraceWB();
	public List<nmlMatchTrace> getOursMatchTraceWB();
	
	
	void addBModel(IModel... models);
	void addTModel(IModel... models);
	
	IModel getBModel();
	IModel getTModel();

	@Override
	public default INmlModule getModule() {
		return (INmlModule) ((IEtlContext)this).getModule();
	}
	
	public void setModule(INmlModule module);
	
	public void setMatchTrace(MatchTrace matchTrace);

	public void setMergeTrace(MergeTrace mergeTrace);
	
	public void setTransferTrace(transferTrace transferTrace);

}
