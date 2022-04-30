/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package strategy;

import static org.eclipse.epsilon.common.util.OperatingSystem.getJavaVersion;
import static org.eclipse.epsilon.common.util.OperatingSystem.getOsNameAndVersion;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getAvailableMemory;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getCpuName;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getMaxMemory;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getNumberOfHardwareThreads;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.common.util.profiling.ProfileDiagnostic.MemoryUnit;
import org.eclipse.epsilon.ecl.trace.Match;
import dom.MergeRule;
import execute.context.IEmlContext;
import nmlMatchTrace.nmlMatchTrace;

import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolNullPointerException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.types.EolSequence;
import org.eclipse.epsilon.erl.execute.context.IErlContext;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.execute.context.IEtlContext;
import org.eclipse.epsilon.etl.strategy.DefaultTransformationStrategy;

public class DefaultMergingStrategy extends DefaultTransformationStrategy implements IMergingStrategy {
	
	protected IEmlContext context;
	
	protected boolean PassOctopus = false;
	protected boolean PassTransfer = false;
	@Override
	public void mergeModels(IEmlContext context) throws EolRuntimeException {
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");  
		LocalDateTime now = LocalDateTime.now();   
	       SimpleDateFormat sdf
           = new SimpleDateFormat(
               "dd-MM-yyyy HH:mm:ss.SSS");
	    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"); 
		String StartTimeMerge11 =   dtf1.format(now);
		Date StartTimeMerge1;
		
		this.context = context;
		// Octopus Merge Base
		for (nmlMatchTrace match : context.getOctopusMatchTrace()) {
			List<MergeRule> rules = getRulesForOctopus(match, context);
			for (MergeRule mergeRule : rules) {
				if (!mergeRule.isLazy(context) && !mergeRule.hasMerged(match)) {
					mergeRule.merge(match, context);
				}
			}
		}
		PassOctopus = true;
		// Ours Merge Base
		
		for (nmlMatchTrace match : context.getOursMatchTrace()) {
	
			List<MergeRule> rules = getRulesForOurs(match, context);
			for (MergeRule mergeRule : rules) {
				if (!cemetery.isEmpty()) {
					if (!cemetery.contains(findObjectContainer(match.getLeft() , "eContainer" , context))) {
						
				String strExistsInNumber = mergeRule.getExistsInNumber();
		        try{
		        	int numberExistsInNumber;
		        	
		        	if(strExistsInNumber == null) {
		        		numberExistsInNumber = (context.getModelRepository().getModels().size()-2)/2;
		        	}else {
		            numberExistsInNumber = Integer.parseInt(strExistsInNumber);
		        	}
		            
		        	if(numberExistsInNumber <= match.getModelsMatch()) 
					{	
						if (!mergeRule.isLazy(context) && !mergeRule.hasMerged(match)) {
							mergeRule.merge(match, context);
						}
					}else {
						setCemetery(match);
						context.setMergerReport("-------------------------------------------------");
						//System.out.println("-------------------------------------------------");
						context.setMergerReport("_______USER RESTRICTION_______");
						//System.out.println("_______USER RESTRICTION_______");
						context.setMergerReport("Element in Base Version = " + match.getLeft().toString());
						//System.out.println("Element in Base Version = " + match.getLeft().toString());
						context.setMergerReport("Rule Type = Ours");
						//System.out.println("Rule Type = Ours");
						context.setMergerReport("Names of Input models = " + match.getModelsInMatch());
						//System.out.println("Names of Input models = " + match.getModelsInMatch());
						context.setMergerReport("Exists In Number = " + mergeRule.getExistsInNumber());
						//System.out.println("Exists In Number = " + mergeRule.getExistsInNumber());
						context.setMergerReport("Models In Match = " + String.valueOf(match.getModelsMatch()));
						//System.out.println("Models In Match = " + String.valueOf(match.getModelsMatch()));
						context.setMergerReport("Result = Element Ignore");
						//System.out.println("Result = Element Ignore");
						
					}
		        	
		        }
		        catch (NumberFormatException ex){
		        	
		        	// ExistsInNumber Format Exception
		            ex.printStackTrace();
		        }
			
		        
					}else {
						setCemetery(match);
						context.setMergerReport("-------------------------------------------------");
						//System.out.println("-------------------------------------------------");
						context.setMergerReport("_______CONFLICT PREVENTION_______");
						//System.out.println("_______CONFLICT PREVENTION_______");
						context.setMergerReport("Element in Base Version = " + match.getLeft().toString());
						//System.out.println("Element in Base Version = " + match.getLeft().toString());
						context.setMergerReport("Rule Type = Ours");
						//System.out.println("Rule Type = Ours");
						context.setMergerReport("Names of Input models = " + match.getModelsInMatch());
						//System.out.println("Names of Input models = " + match.getModelsInMatch());
						context.setMergerReport("Exists In Number = " + mergeRule.getExistsInNumber());
						//System.out.println("Exists In Number = " + mergeRule.getExistsInNumber());
						context.setMergerReport("Models In Match = " + String.valueOf(match.getModelsMatch()));
						//System.out.println("Models In Match = " + String.valueOf(match.getModelsMatch()));
						context.setMergerReport("Result = Element Ignore");
						//System.out.println("Result = Element Ignore");
					}
				}else {
					String strExistsInNumber = mergeRule.getExistsInNumber();
			        try{
			        	int numberExistsInNumber;
			        	
			        	if(strExistsInNumber == null) {
			        		numberExistsInNumber = (context.getModelRepository().getModels().size()-2)/2;
			        	}else {
			            numberExistsInNumber = Integer.parseInt(strExistsInNumber);
			        	}
			            
			        	if(numberExistsInNumber <= match.getModelsMatch()) 
						{	
							if (!mergeRule.isLazy(context) && !mergeRule.hasMerged(match)) {
								mergeRule.merge(match, context);
							}
						}else {
							setCemetery(match);
							context.setMergerReport("-------------------------------------------------");
							//System.out.println("-------------------------------------------------");
							context.setMergerReport("_______USER RESTRICTION_______");
							//System.out.println("_______USER RESTRICTION_______");
							context.setMergerReport("Element in Base Version = " + match.getLeft().toString());
							//System.out.println("Element in Base Version = " + match.getLeft().toString());
							context.setMergerReport("Rule Type = Ours");
							//System.out.println("Rule Type = Ours");
							context.setMergerReport("Names of Input models = " + match.getModelsInMatch());
							//System.out.println("Names of Input models = " + match.getModelsInMatch());
							context.setMergerReport("Exists In Number = " + mergeRule.getExistsInNumber());
							//System.out.println("Exists In Number = " + mergeRule.getExistsInNumber());
							context.setMergerReport("Models In Match = " + String.valueOf(match.getModelsMatch()));
							//System.out.println("Models In Match = " + String.valueOf(match.getModelsMatch()));
							context.setMergerReport("Result = Element Ignore");
							//System.out.println("Result = Element Ignore");
						}
			        	
			        }
			        catch (NumberFormatException ex){
			        	
			        	// ExistsInNumber Format Exception
			            ex.printStackTrace();
			        }
				}
			}
		}
		
		now = LocalDateTime.now();   
		String EndTimeMerge11 =  dtf1.format(now);
		Date EndTimeMerge1;


		now = LocalDateTime.now();   
		String StartTimeMatch_WithOutBase =  dtf1.format(now);
		Date StartTimeMatch_WithOutBase1;
		//  Merge WithOutBase
			// first we create match list _ Octopus _
				for (MergeRule mergeRule : context.getModule().getOctopusRules()) {
					if(mergeRule.getbaseParameter() == null)
					mergeRule.nmlMergeWithOutBase(context, getExcluded(), 1 );
				}
								
			// first we create match list _ Ours _
				for (MergeRule mergeRule : context.getModule().getOursRules()) {
					if(mergeRule.getbaseParameter() == null)
					mergeRule.nmlMergeWithOutBase(context, getExcluded(), 2 );
				}
				
		now = LocalDateTime.now();   	
		String endTimeMatch_WithOutBase =  dtf1.format(now);
		Date endTimeMatch_WithOutBase1;
				
				//// merge time
				dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"); 
				now = LocalDateTime.now();   
				String StartTimeMerge22 =   dtf1.format(now);
				Date StartTimeMerge2;
				
		PassOctopus = false;

			//second we merge them _ Octopus _
				for (nmlMatchTrace match : context.getOctopusMatchTraceWB()) {
					List<MergeRule> rules = getRulesForOctopusWB(match, context);
					for (MergeRule mergeRule : rules) {
						if (!mergeRule.isLazy(context) && !mergeRule.hasMerged(match)) {
							mergeRule.merge(match, context);
						}
					}
				}
			
		PassOctopus = true;
			//second we merge them _ Ours _
				for (nmlMatchTrace match : context.getOursMatchTraceWB()) {
					List<MergeRule> rules = getRulesForOursWB(match, context);
					for (MergeRule mergeRule : rules) {
						if (!mergeRule.isLazy(context) && !mergeRule.hasMerged(match)) {
							mergeRule.merge(match, context);
						}
					}
				}
				
			//  _ Transfer _
		PassTransfer = true;
				for (MergeRule transferRule : getExecutableTransferRules(context)) {			
					transferRule.transferAll(context, getExcluded(), getExcludedWB(), getCemetery(),  true);
				}
				
				//// merge time
				now = LocalDateTime.now();   
				String EndTimeMerge22 =   dtf1.format(now);
				Date EndTimeMerge2;
				
				int TotalInputModels = (context.getModelRepository().getModels().size())-2;
				context.setMergerReport("-------------------------------------------------");
				//System.out.println(".........................................................");
				//System.err.println("Total ignored elements from base version and " 
			//	+ TotalInputModels
			//	+ " input models = "
			//	+ cemetery.size());
				int totalE = 0;
				if (excluded != null && excludedWB != null) {
				totalE = excluded.size() + excludedWB.size() + context.getTransferObjects().size();
				}
				if (excluded != null && excludedWB == null) {
					totalE = excluded.size()  + context.getTransferObjects().size();
					}
				if (excluded == null && excludedWB != null) {
					totalE =  excludedWB.size() + context.getTransferObjects().size();
					}
				
				context.setMergerReport("Total elements involved in merging process from base version and " 
						+ TotalInputModels
						+ " input models = "
						+ totalE);
				
				context.setMergerReport("Total ignored elements during merging process = " 
						+ cemetery.size());
				
				
				/////////// merge & match time 
				
				try {
					endTimeMatch_WithOutBase1 = sdf.parse(endTimeMatch_WithOutBase);
					StartTimeMatch_WithOutBase1 = sdf.parse(StartTimeMatch_WithOutBase);
					computeDiff(StartTimeMatch_WithOutBase1, endTimeMatch_WithOutBase1 , context);
					
					EndTimeMerge2 = sdf.parse(EndTimeMerge22);
					StartTimeMerge2 = sdf.parse(StartTimeMerge22);
					EndTimeMerge1 = sdf.parse(EndTimeMerge11);
					StartTimeMerge1 = sdf.parse(StartTimeMerge11);
					computeDiff(StartTimeMerge1, EndTimeMerge1 , StartTimeMerge2 , EndTimeMerge2 , context);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	//	transformModels(context);
		
	}
	
	protected List<MergeRule> getExecutableTransferRules(IEmlContext context) throws EolRuntimeException {
		Collection<MergeRule> allRules = context.getModule().getTransferRules();
		List<MergeRule> filtered = new ArrayList<>(allRules.size());
		for (MergeRule transferRule : allRules) {
			if (!transferRule.isAbstract(context) && !transferRule.isLazy(context)) {
				filtered.add(transferRule);
			}
		}
		return filtered;
	}

	public List<MergeRule> getRulesForOctopus(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		List<MergeRule> rules = new ArrayList<>();
		
		// First we try to find rules that apply to instance of type only
		for (MergeRule mergeRule : context.getModule().getOctopusRules()) {
			if (!mergeRule.isAbstract(context) && mergeRule.getbaseParameter() != null) {
				if (mergeRule.appliesTo(match, context)) { 
					rules.add(mergeRule);
				}
			}
		}
		
		return rules;
	}
	
	public List<MergeRule> getRulesForOctopusWB(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		List<MergeRule> rules = new ArrayList<>();
		
		// First we try to find rules that apply to instance of type only
		for (MergeRule mergeRule : context.getModule().getOctopusRules()) {
			if (!mergeRule.isAbstract(context) && mergeRule.getbaseParameter() == null) {
				if (mergeRule.appliesToWB(match, context)) { 
					rules.add(mergeRule);
				}
			}
		}
		
		return rules;
	}
	
	public List<MergeRule> getRulesForOurs(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		List<MergeRule> rules = new ArrayList<>();
		
		// First we try to find rules that apply to instance of type only
		for (MergeRule mergeRule : context.getModule().getOursRules()) {
			if (!mergeRule.isAbstract(context) && mergeRule.getbaseParameter() != null) {
				if (mergeRule.appliesTo(match, context)) { 
					rules.add(mergeRule);
				}
			}
		}
		
		return rules;
	}
	
	public List<MergeRule> getRulesForOursWB(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		List<MergeRule> rules = new ArrayList<>();
		
		// First we try to find rules that apply to instance of type only
		for (MergeRule mergeRule : context.getModule().getOursRules()) {
			if (!mergeRule.isAbstract(context) && mergeRule.getbaseParameter() == null) {
				if (mergeRule.appliesToWB(match, context)) { 
					rules.add(mergeRule);
				}
			}
		}
		
		return rules;
	}
	
	@Override
	public Collection<?> getEquivalents(Object source, IErlContext context, List<String> rules) throws EolRuntimeException {
		
		if (!getExcluded().contains(source) ) {
			if(getExcludedWB().contains(source)) {
				return merge(source, context, rules);
			}
			return transfer(source, context, rules);
			//return super.getEquivalents(source, context, rules);
		}
		else {
			return merge(source, context, rules);
		}
	}

	
	
	
	//TODO : Implement this
	private Collection<Object> merge(Object source, IEolContext context_,
			List<String> rules) throws EolRuntimeException {
		//++++++++++++++++++++++++================+++++++++++++++++++++++++++++
		//needs check

		int typeRuleMerging = 0;
		
		nmlMatchTrace nmlMatchTraceObject = new nmlMatchTrace();
		List<Object> targets = CollectionUtil.createDefaultList();
		
		if (PassTransfer == true) { 
			
			return mergeTransfer( source,  context_, rules);
			
			}
		
		
		if(PassOctopus == false) {
			List<nmlMatchTrace> matches = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTrace());
			typeRuleMerging = 1;
			if (matches == null) {
				matches = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTraceWB());
				typeRuleMerging = 2;
			}
		for (nmlMatchTrace match : matches) {
			if (typeRuleMerging == 1) {
			for (MergeRule mergeRule : getRulesForOctopus(match, context)) {
				if (rules == null || rules.contains(mergeRule.getName())) {
					
					Collection<?> merged = mergeRule.merge(match, context);
					
					if (!mergeRule.isPrimary(context)) {
						targets.addAll(merged);
					}
					else {
						int i = 0;
						for (Object target : merged) {
							targets.add(i, target);
							i++;
						}
					}
				}
			}
			}
			if (typeRuleMerging == 2) {
				for (MergeRule mergeRule : getRulesForOctopusWB(match, context)) {
					if (rules == null || rules.contains(mergeRule.getName())) {
						
						Collection<?> merged = mergeRule.merge(match, context);
						
						if (!mergeRule.isPrimary(context)) {
							targets.addAll(merged);
						}
						else {
							int i = 0;
							for (Object target : merged) {
								targets.add(i, target);
								i++;
							}
						}
					}
				}
				}
			
		}
		
		
	}else {
		List<nmlMatchTrace> matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOursMatchTrace());
		typeRuleMerging = 3;
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTrace());
		}
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOursMatchTraceWB());
			typeRuleMerging = 4;
		}
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTraceWB());
			typeRuleMerging = 4 ;
		}
		//Collection<nmlMatchTrace> matchesOurs = context.getOursMatchTrace();
		for (nmlMatchTrace match : matchesOurs) {
			if (typeRuleMerging == 3) {
			for (MergeRule mergeRule : getRulesForOurs(match, context)) {
				if (rules == null || rules.contains(mergeRule.getName())) {
					
					Collection<?> merged = mergeRule.merge(match, context);
					
					if (!mergeRule.isPrimary(context)) {
						targets.addAll(merged);
					}
					else {
						int i = 0;
						for (Object target : merged) {
							targets.add(i, target);
							i++;
						}
					}
				}
			}
		}
			if (typeRuleMerging == 4) {
				for (MergeRule mergeRule : getRulesForOursWB(match, context)) {
					if (rules == null || rules.contains(mergeRule.getName())) {
						
						Collection<?> merged = mergeRule.merge(match, context);
						
						if (!mergeRule.isPrimary(context)) {
							targets.addAll(merged);
						}
						else {
							int i = 0;
							for (Object target : merged) {
								targets.add(i, target);
								i++;
							}
						}
					}
				}
			}
		}
	}
		
		return targets;
	}
	
	private Collection<Object> mergeTransfer(Object source, IEolContext context_,
			List<String> rules) throws EolRuntimeException {
		//++++++++++++++++++++++++================+++++++++++++++++++++++++++++
		//needs check

		int typeRuleMerging = 0;
		
		nmlMatchTrace nmlMatchTraceObject = new nmlMatchTrace();
		List<Object> targets = CollectionUtil.createDefaultList();
		
		
			List<nmlMatchTrace> matches = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTrace());
			typeRuleMerging = 1;
			if (matches == null) {
				matches = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTraceWB());
				typeRuleMerging = 2;
			}
			
			if (matches != null) {
				for (nmlMatchTrace match : matches) {
					if (typeRuleMerging == 1) {
					for (MergeRule mergeRule : getRulesForOctopus(match, context)) {
						if (rules == null || rules.contains(mergeRule.getName())) {
							
							Collection<?> merged = mergeRule.merge(match, context);
							
							if (!mergeRule.isPrimary(context)) {
								targets.addAll(merged);
							}
							else {
								int i = 0;
								for (Object target : merged) {
									targets.add(i, target);
									i++;
								}
							}
						}
					}
					}
					if (typeRuleMerging == 2) {
						for (MergeRule mergeRule : getRulesForOctopusWB(match, context)) {
							if (rules == null || rules.contains(mergeRule.getName())) {
								
								Collection<?> merged = mergeRule.merge(match, context);
								
								if (!mergeRule.isPrimary(context)) {
									targets.addAll(merged);
								}
								else {
									int i = 0;
									for (Object target : merged) {
										targets.add(i, target);
										i++;
									}
								}
							}
						}
						}
					
				}
			}
		
	if (targets.isEmpty()) {
		List<nmlMatchTrace> matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOursMatchTrace());
		typeRuleMerging = 3;
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTrace());
		}
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOursMatchTraceWB());
			typeRuleMerging = 4;
		}
		if (matchesOurs == null) {
			matchesOurs = nmlMatchTraceObject.getNMLMatches(source, context.getOctopusMatchTraceWB());
			typeRuleMerging = 4 ;
		}
		//Collection<nmlMatchTrace> matchesOurs = context.getOursMatchTrace();
		for (nmlMatchTrace match : matchesOurs) {
			if (typeRuleMerging == 3) {
			for (MergeRule mergeRule : getRulesForOurs(match, context)) {
				if (rules == null || rules.contains(mergeRule.getName())) {
					
					Collection<?> merged = mergeRule.merge(match, context);
					
					if (!mergeRule.isPrimary(context)) {
						targets.addAll(merged);
					}
					else {
						int i = 0;
						for (Object target : merged) {
							targets.add(i, target);
							i++;
						}
					}
				}
			}
		}
			if (typeRuleMerging == 4) {
				for (MergeRule mergeRule : getRulesForOursWB(match, context)) {
					if (rules == null || rules.contains(mergeRule.getName())) {
						
						Collection<?> merged = mergeRule.merge(match, context);
						
						if (!mergeRule.isPrimary(context)) {
							targets.addAll(merged);
						}
						else {
							int i = 0;
							for (Object target : merged) {
								targets.add(i, target);
								i++;
							}
						}
					}
				}
			}
		}
	}
		
		return targets;
	}


	//TODO : Improve performance by turning this into a HashSet?
	protected List<Object> excluded = null;
	
	@Override
	public List<Object> getExcluded() {
		if (excluded == null) {
			Collection<nmlMatchTrace> matchesOctopus = context.getOctopusMatchTrace();
			Collection<nmlMatchTrace> matchesOurs = context.getOursMatchTrace();
			
			excluded = new ArrayList<>(matchesOurs.size()*2 + matchesOctopus.size()*2);
			for (nmlMatchTrace match : matchesOctopus) {
				excluded.add(match.getLeft());
				
				for (int i=0 ; i < match.getRight().size() ; i++ ) {
				excluded.add(match.getRight().get(i));
				}
			}
			
			for (nmlMatchTrace match : matchesOurs) {
				excluded.add(match.getLeft());
				
				for (int i=0 ; i < match.getRight().size() ; i++ ) {
				excluded.add(match.getRight().get(i));
				}
			}
			
		}
		return excluded;
	}
	
	protected List<Object> excludedWB = null;
	
	public List<Object> getExcludedWB() {
		if (excludedWB == null) {
			Collection<nmlMatchTrace> matchesOctopus = context.getOctopusMatchTraceWB();
			Collection<nmlMatchTrace> matchesOurs = context.getOursMatchTraceWB();
			
			excludedWB = new ArrayList<>(matchesOurs.size()*2 + matchesOctopus.size()*2);
			for (nmlMatchTrace match : matchesOctopus) {
				excludedWB.add(match.getLeft());
				
				for (int i=0 ; i < match.getRight().size() ; i++ ) {
					excludedWB.add(match.getRight().get(i));
				}
			}
			
			for (nmlMatchTrace match : matchesOurs) {
				excludedWB.add(match.getLeft());
				
				for (int i=0 ; i < match.getRight().size() ; i++ ) {
					excludedWB.add(match.getRight().get(i));
				}
			}
			
		}
		return excludedWB;
	}
	
	protected List<Object> cemetery = new ArrayList<>();
	
	public List<Object> getCemetery() {
		
		return cemetery;
	}
	
	public void setCemetery(nmlMatchTrace match) {
		
		cemetery.add(match.getLeft());
		for (int i=0 ; i < match.getRight().size() ; i++ ) {
			cemetery.add(match.getRight().get(i));
		}
	}
	
	
	////////////////////////////////////////////////////////////////////
	////////////////////Transfer///////////////////////////////////////
	
public List<MergeRule> getRulesForTransfer(Object source, IEmlContext context) throws EolRuntimeException{
		
		List<MergeRule> rules = new ArrayList<>();
		
		// First we try to find rules that apply to instance of type only
		for (MergeRule rule : context.getModule().getTransferRules()) {
			if (!rule.isAbstract(context) && rule.TransferappliesTo(source, context, false)) {
				rules.add(rule);
			}
		}
		
		return rules;
	}

public Collection<?> transfer(Object source, IEolContext context_, List<String> rules) throws EolRuntimeException{	
	List<Object> targets = CollectionUtil.createDefaultList();
	
	//TODO : Change this to be less restrictive...
	if (!canTransform(source)) return targets;
	
	for (MergeRule rule : getRulesForTransfer(source, context)) {
		if (rules == null || rules.contains(rule.getName())) {
			
			Collection<?> transformed = rule.transfer(source, context);
			
			if (!rule.isPrimary(context)) {
				targets.addAll(transformed);
			}
			else {
				int i = 0;
				for (Object target : transformed) {
					targets.add(i++, target);
				}
			}
		}
	}
	
	return targets;
}

public boolean canTransform(Object source) {
	return !getExcludedTransfer().contains(source);
}

protected Collection<Object> getExcludedTransfer() {
	return Collections.emptyList();
}

/////////////////////////////////////////////////////////////////

public Object findObjectContainer(Object source, String propertyName, IEolContext context) throws EolRuntimeException {

	IPropertyGetter getter = context.getIntrospectionManager().getPropertyGetterFor(source, propertyName, context);

	Object sourceContainer = getter.invoke(source, propertyName, context);
	return sourceContainer;
	
}

public static void computeDiff(Date S1, Date E2 , Date S11, Date E22 , IEmlContext context) {

    long diffInMillies = (E2.getTime() + E22.getTime()) - (S1.getTime() + S11.getTime());

    //create the list
    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
    Collections.reverse(units);

    //create the result map of TimeUnit and difference
    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
    long milliesRest = diffInMillies;

    for ( TimeUnit unit : units ) {

        //calculate difference in millisecond 
        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
        long diffInMilliesForUnit = unit.toMillis(diff);
        milliesRest = milliesRest - diffInMilliesForUnit;

        //put the result in the map
        result.put(unit,diff);
    }
    
/////////// merge time
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport( " Merging time is: " + result.get(TimeUnit.DAYS) + " days, " +
    	result.get(TimeUnit.HOURS) + " hours, " +
    		result.get(TimeUnit.MINUTES) + " minutes, " +
				result.get(TimeUnit.SECONDS) + " seconds, " +
					result.get(TimeUnit.MILLISECONDS) + " milliseconds");
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport("-------------------------------------------------");
    
}

public static void computeDiff(Date S1, Date E2 , IEmlContext context) {

    long diffInMillies = (E2.getTime() ) - (S1.getTime() );

    //create the list
    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
    Collections.reverse(units);

    //create the result map of TimeUnit and difference
    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
    long milliesRest = diffInMillies;

    for ( TimeUnit unit : units ) {

        //calculate difference in millisecond 
        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
        long diffInMilliesForUnit = unit.toMillis(diff);
        milliesRest = milliesRest - diffInMilliesForUnit;

        //put the result in the map
        result.put(unit,diff);
    }
    
/////////// match time
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport( " Matching with out base version time is:" + result.get(TimeUnit.DAYS) + " days, " +
    	result.get(TimeUnit.HOURS) + " hours, " +
    		result.get(TimeUnit.MINUTES) + " minutes, " +
				result.get(TimeUnit.SECONDS) + " seconds, " +
					result.get(TimeUnit.MILLISECONDS) + " milliseconds");
			context.setMergerReport("-------------------------------------------------");
			context.setMergerReport("-------------------------------------------------");
    
}

}
