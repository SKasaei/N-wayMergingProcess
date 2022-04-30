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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.models.IModel;

import enml.IEmlModule;
import nmlMatchTrace.nmlMatchTrace;
import strategy.DefaultMergingStrategy;
import strategy.IMergingStrategy;
import trace.MergeTrace;
import trace.transferTrace;
import org.eclipse.epsilon.etl.execute.context.EtlContext;

import org.eclipse.epsilon.eol.dom.Parameter;

public class EmlContext extends EtlContext implements IEmlContext {

	protected MatchTrace matchTrace = new MatchTrace();
	protected MergeTrace mergeTrace = new MergeTrace();
	protected transferTrace transferTrace = new transferTrace();
	
	protected List<IModel> BaseModel = new ArrayList<>();
	protected List<IModel> TargetModel = new ArrayList<>();
	
	protected List<IEclModule> eclModules = new ArrayList<>();
	protected List<nmlMatchTrace> OctopusMatchTrace = new ArrayList<>();
	protected List<nmlMatchTrace> OursMatchTrace = new ArrayList<>();
	
	protected List<nmlMatchTrace> OctopusMatchTraceWB = new ArrayList<>();
	protected List<nmlMatchTrace> OursMatchTraceWB = new ArrayList<>();
	
	protected List<Object> transferObjects = new ArrayList<>();
	protected List<String> transferObjectsParameter = new ArrayList<>();
	
	protected List<String> mergerReport = new ArrayList<>();
	
	private IMergingStrategy mergingStrategy = new DefaultMergingStrategy();
	
	public EmlContext() {
		super();
	}
	
	@Override
	public IMergingStrategy getMergingStrategy() {
		return mergingStrategy;
	}

	@Override
	public void setMergingStrategy(IMergingStrategy mergingStrategy) {
		this.mergingStrategy = mergingStrategy;
	}

	@Override
	public MatchTrace getMatchTrace() {
		return matchTrace;
	}

	@Override
	public MergeTrace getMergeTrace() {
		return mergeTrace;
	}
	
	public transferTrace getTransferTrace() {
		return transferTrace;
	}
	
	@Override
	public IEmlModule getModule() {
		return (IEmlModule) super.getModule();
	}
	
	@Override
	public void setModule(IEmlModule module) {
 		this.module = module;
	}

	@Override
	public void setMatchTrace(MatchTrace matchTrace) {
		this.matchTrace = matchTrace;
	}

	@Override
	public void setMergeTrace(MergeTrace mergeTrace) {
		this.mergeTrace = mergeTrace;
	}
	
	public void setTransferTrace(transferTrace transferTrace) {
		this.transferTrace = transferTrace;
	}
	
	public void setTransferObjects(Object transferObject) {
		this.transferObjects.add(transferObject);
	}
	
	public List<Object> getTransferObjects() {
		return this.transferObjects;
	}
	
	public void setTransferObjectsParameter(String transferObjectParameter) {
		this.transferObjectsParameter.add(transferObjectParameter);
	}
	
	public List<String> getTransferObjectsParameter() {
		return this.transferObjectsParameter;
	}
	
	public void addBModel(IModel... models) {
		if (models == null || models.length == 0) return;
		CollectionUtil.addCapacityIfArrayList(this.BaseModel, models.length);
		for (IModel model : models)
			addModelB(model);
	}
	public void addModelB(IModel model) {
		if (!BaseModel.contains(model)) {
			BaseModel.add(model);
		}
	}
	
	public void addTModel(IModel... models) {
		if (models == null || models.length == 0) return;
		CollectionUtil.addCapacityIfArrayList(this.TargetModel, models.length);
		for (IModel model : models)
			addModelT(model);
	}
	public void addModelT(IModel model) {
		if (!TargetModel.contains(model)) {
			TargetModel.add(model);
		}
	}
	
	public List<nmlMatchTrace> getOctopusMatchTrace() {
		
		return OctopusMatchTrace;
	}
	
	public List<nmlMatchTrace> getOursMatchTrace() {
		
		return OursMatchTrace;
	}
	
	public void setOctopusMatchTraceWB(List<nmlMatchTrace> nmlMatchTrace) {
		this.OctopusMatchTraceWB.addAll(nmlMatchTrace);
	}
	
	public void setOursMatchTraceWB(List<nmlMatchTrace> nmlMatchTrace) {
		this.OursMatchTraceWB.addAll(nmlMatchTrace);
	}
	
	public List<nmlMatchTrace> getOctopusMatchTraceWB() {
		
		return OctopusMatchTraceWB;
	}
	
	public List<nmlMatchTrace> getOursMatchTraceWB() {
		
		return OursMatchTraceWB;
	}
	
	public IEclModule getEclModules() {
		
		return eclModules.get(0);
	}
	
	public List<IEclModule> getEclModule_WithBase() {
		
		return eclModules;
	}
	
	public void setEclModule_WithBase(List<IEclModule> x ) {
		
		eclModules = x;
	}
	
	public IModel getBModel() {
		if(BaseModel.isEmpty()) {
			return null;
		}
		return BaseModel.get(0);
	}
	
	public IModel getTModel() {
		
		return TargetModel.get(0);
	}
	
	public List<String> getMergerReport() {
		
		return mergerReport;
	}
	
	public void setMergerReport(String Report) {
		
		mergerReport.add(Report);
	}
	
	public void nmlMatchTraceWithoutBase(File ECLFile ) {
		try {
			IEclModule eclModule = new EclModule();
			eclModule.parse(ECLFile);
			eclModule.getContext().getModelRepository().addModels(this.getModelRepository().getModels().get(0));
			eclModule.getContext().getModelRepository().addModels(this.getModelRepository().getModels().get(1));
			
		//	eclModule.execute();
			eclModules.add(eclModule);
		}
		catch (Exception evt) {
			System.err.println("nmlMatchTraceWithoutBase Error");
        }
	}
	
	public void comparingInputModels_withBase(File ECLFile ) {
		try {
		
		int count = this.getModelRepository().getModels().size();
		for (int i = 0 ; i < count ; i++) {
			
			eclModules.add(compareModelWithBase(ECLFile, this.BaseModel.get(0), this.getModelRepository().getModels().get(i)));
		}
}
		
		catch (Exception evt) {
			System.err.println("ERROR: Comparing Input Models With Base");
        }
	}
	
	public IEclModule compareModelWithBase(File comparisonFile , IModel modelBase , IModel modelInput) throws Exception{
		//ecl Module
				IEclModule eclModule = new EclModule();
				eclModule.parse(comparisonFile);
				eclModule.getContext().getModelRepository().addModels(modelBase,modelInput);
				eclModule.execute();

				return eclModule;
	}
	
	public void nmlMatchTrace( ) {
		try {
			
		int count = this.eclModules.size();
		nmlMatchTrace eclModuleModel = new nmlMatchTrace();
		nmlMatchTrace eclModuleModel2 = new nmlMatchTrace();
		List<nmlMatchTrace> OctopusMatchTraceTemp = new ArrayList<>();
		
		for (int i = 0 ; i < count ; i++) {
		
			//eclModules.add(eclModuleModel.compareModelWithBase(ECLFile, this.BaseModel.get(0), this.getModelRepository().getModels().get(i)));
			
			// OctopusMatchTrace Empty ... Run first time
			if (OctopusMatchTraceTemp.isEmpty()) {
				for (Match match : this.eclModules.get(i).getContext().getMatchTrace()) {
					if (match.isMatching()) {
						OctopusMatchTraceTemp.add(eclModuleModel.nml_createMatchTrace(match , this.getModelRepository().getModels().get(i).getName()));
						}
					}
			}else { // OctopusMatchTrace NOT Empty
				
				for (Match match : this.eclModules.get(i).getContext().getMatchTrace()) {
			
					eclModuleModel2 = eclModuleModel.nml_createMatchTrace2(match , OctopusMatchTraceTemp , this.eclModules.get(i).getContext() , this.getModelRepository().getModels().get(i).getName());
					if (eclModuleModel2 != null) {
					if (match.isMatching()) {
						OctopusMatchTraceTemp.add(eclModuleModel2);
							}
						}
					}
				}
			}
		int OctopusMatchTracesize = OctopusMatchTraceTemp.size();
		for(int i = 0 ; i < OctopusMatchTracesize ; i++) {
		if(eclModuleModel.OctopusMatchTraceSize(OctopusMatchTraceTemp, i) != this.getModelRepository().getModels().size()) {
			OursMatchTrace.add(OctopusMatchTraceTemp.get(i));
			//OctopusMatchTrace.remove(i);	
				}else {
					OctopusMatchTrace.add(OctopusMatchTraceTemp.get(i));
				}
			}
		}
		
		catch (Exception evt) {
			System.err.println("ERROR: Creating nmlMatchTrace With Base");
        }
	}
}
