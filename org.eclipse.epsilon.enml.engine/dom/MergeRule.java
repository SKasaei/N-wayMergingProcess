/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.runtime.RecognitionException;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.util.AstUtil;
import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.ecl.dom.MatchRule;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;

import strategy.DefaultMergingStrategy;
import execute.context.IEmlContext;
import nmlMatchTrace.nmlMatchTrace;
import parse.EmlParser;
import trace.MergeTrace;
import trace.Merges;

import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.ExpressionStatement;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.dom.TypeExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.erl.dom.ExtensibleNamedRule;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.execute.context.IEtlContext;
import org.eclipse.epsilon.etl.parse.EtlParser;

public class MergeRule extends ExtensibleNamedRule {
	
	protected ExecutableBlock<Boolean> guardBlock;
	protected StatementBlock bodyBlock;
	
	protected Parameter leftParameter, rightParameter ;
	protected Parameter baseParameter = null;
	protected Parameter sourceParameter = null;
	protected Parameter sourceModels = null;

	protected List<Parameter> targetParameters = new ArrayList<>();
	protected List<Parameter> rightParameters = new ArrayList<>();
	protected List<Parameter> priorityLists = new ArrayList<>();
	
	protected String existsInNumber =null;

	protected boolean auto = false;
	
	public MergeRule() { }
	
	@SuppressWarnings("unchecked")
	@Override
	public void build(AST cst, IModule module) {
		super.build(cst, module);
		this.guardBlock = (ExecutableBlock<Boolean>) module.createAst(AstUtil.getChild(cst, EmlParser.GUARD), this);
		this.bodyBlock = (StatementBlock) module.createAst(AstUtil.getChild(cst, EmlParser.BLOCK), this);
		
		if(cst.token.getType() == 301 || cst.token.getType() == 302) {
			
		
		//Parse the formal parameters
		leftParameter = (Parameter) module.createAst(cst.getSecondChild(), this);
		rightParameter = (Parameter) module.createAst(cst.getSecondChild(), this);
		
		for (AST rightParametersAst : cst.getThirdChild().getChildren()) {
			rightParameters.add((Parameter) module.createAst(rightParametersAst, this));
		}
		// ruleType 
		switch (cst.token.getType()) {
		// 301 Octopus
		case 301 :
		{
    	 switch (cst.getChildCount()) {
    	 // without withbase
	         case 5 :
	         {
	     		
	     		 for (AST mergedParameterAst : cst.getFourthChild().getChildren()) {
		     			targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
		     		} 
	         }
	         
	         break;
	         // with withbase
	         case 6 :
	         {
	        	 baseParameter = (Parameter) module.createAst(cst.getFourthChild(), this);
		     		for (AST mergedParameterAst : cst.getFifthChild().getChildren()) {
		    			targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
		    		}
	    	 }
	         
	         break;
    	 }
		}
        break;
		// 302 Ours
		case 302 :
		{
	    	 switch (cst.getChildCount()) {
	    	 // without withbase
	    	 case 6 :
	    	 {
	     		 for (AST priorityListsAst : cst.getFourthChild().getChildren()) {
	     			priorityLists.add((Parameter) module.createAst(priorityListsAst, this));
		     		} 
	     		 
	     		 for (AST mergedParameterAst : cst.getFifthChild().getChildren()) {
		     			targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
		     		} 
	    	 }
	    	 break;
	    	 // with withbase
	    	 case 7 :
	    	 {
	        	 baseParameter = (Parameter) module.createAst(cst.getFourthChild(), this);

	    		 for (AST priorityListsAst : cst.getFifthChild().getChildren()) {
		     			priorityLists.add((Parameter) module.createAst(priorityListsAst, this));
			     		} 
	    		  
		     	 for (AST mergedParameterAst : cst.getsixthChild().getChildren()) {
			     		targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
			     		} 
	    	 }
	    	 break;
	    	 // with withbase and exists_in number
	    	 case 8 :
	    	 {
	        	 baseParameter = (Parameter) module.createAst(cst.getFourthChild(), this);
	        	// existsInNumber = (Parameter) module.createAst(cst.getFifthChild(), this);
	        	 existsInNumber = cst.getFifthChild().token.getText();
	        	 
	    		 for (AST priorityListsAst : cst.getsixthChild().getChildren()) {
		     			priorityLists.add((Parameter) module.createAst(priorityListsAst, this));
			     		} 
		     	 for (AST mergedParameterAst : cst.getseventhChild().getChildren()) {
			     		targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
			     		} 
	    	 }
	    	 break;
	    	 }
	    	 }
		break;
		}
		}
		
		// transfer rules
		if(cst.token.getType() == 303) {
		
       	 sourceParameter = (Parameter) module.createAst(cst.getSecondChild(), this);
       	 sourceModels = (Parameter) module.createAst(cst.getThirdChild(), this);

     	 for (AST mergedParameterAst : cst.getFourthChild().getChildren()) {
	     		targetParameters.add((Parameter) module.createAst(mergedParameterAst, this));
	     		} 
		}
}
	public boolean isPrimary(IEmlContext context) throws EolRuntimeException {
		return getBooleanAnnotationValue("primary", context);
	}
	
	public boolean appliesTo(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		if (hasMerged(match)) return true;
		
		Object left = match.getLeft();
		Object right = match.getLeft();

		List<Object> rightObjects = match.getRight();
		int count = context.getModelRepository().getModels().size();
		int countRightObjects = rightObjects.size();
		count = count - 3;
		boolean appliesToTypes = false;
		if(countRightObjects == 1 || rightParameters.size() != countRightObjects-1) {
			if(countRightObjects == 1) {
			appliesToTypes = getAllInstances(baseParameter, context, !isGreedy(context)).contains(left);
			// && getAllInstances(leftParameter, context, !isGreedy(context)).contains(rightObjects.get(0));
			}else {
				
					 appliesToTypes = getAllInstances(baseParameter, context, !isGreedy(context)).contains(left);
		
			}
			}else {
		for (int i = 0 ; i < count ; i++) {
		 appliesToTypes = getAllInstances(baseParameter, context, !isGreedy(context)).contains(left) ;
				// && getAllInstances(leftParameter, context, !isGreedy(context)).contains(rightObjects.get(0)) && 
			//	getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(rightObjects.get(i+1));
		}
		}
		boolean guardSatisfied = true;
		
		if (appliesToTypes && guardBlock != null) {
			
			guardSatisfied = guardBlock.execute(context, 
				Variable.createReadOnlyVariable(leftParameter.getName(), left), 
				Variable.createReadOnlyVariable(rightParameter.getName(), right),
				Variable.createReadOnlyVariable("self", this));
		}
		
		return appliesToTypes && guardSatisfied;
	}
	
	public boolean appliesToWB(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		if (hasMerged(match)) return true;
		
		Object left = match.getLeft();
		Object right = match.getLeft();

		List<Object> rightObjects = match.getRight();
		int count = context.getModelRepository().getModels().size();
		
		count = count - 3;
		boolean appliesToTypes = false;

		appliesToTypes =  
						getAllInstances(leftParameter, context, !isGreedy(context)).contains(left);
		if(appliesToTypes == false) {
			for (int i = 0 ; i < rightObjects.size() ; i++) {
				appliesToTypes =  
							getAllInstances(leftParameter, context, !isGreedy(context)).contains(rightObjects.get(i)); 
				if(appliesToTypes == true) break;
					}
		}
		if(appliesToTypes == false) {
			for (int i = 0 ; i < rightParameters.size() ; i++) {
				appliesToTypes =  
							getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(left); 
				if(appliesToTypes == true) break;
					}
		}
		
		boolean guardSatisfied = true;
		
		if (appliesToTypes && guardBlock != null) {
			
			guardSatisfied = guardBlock.execute(context, 
				Variable.createReadOnlyVariable(leftParameter.getName(), left), 
				Variable.createReadOnlyVariable(rightParameter.getName(), right),
				Variable.createReadOnlyVariable("self", this));
		}
		
		return appliesToTypes && guardSatisfied;
	}
	

	public Collection<?> merge(nmlMatchTrace match, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {
		
		MergeTrace mergeTrace =(context).getMergeTrace();
		Merges merges = mergeTrace.getMerges(match, this);
		
		if (!merges.isEmpty()) return merges.getTargets();
		
		executeSuperRulesAndBody(match,targets,context);
		
		return targets;
	}

	
	public boolean hasMerged(nmlMatchTrace match) {
		return mergedMatches.contains(match);
	}
	
	HashSet<nmlMatchTrace> mergedMatches = new HashSet<>();
	
	public Collection<?> merge(nmlMatchTrace match, IEmlContext context) throws EolRuntimeException{
		
		MergeTrace mergeTrace = context.getMergeTrace();
		
		List<nmlMatchTrace> mergedMatchesOurs = mergeTrace.getMergedMatches(mergeTrace);
		
		if (hasMerged(match)) {
			return mergeTrace.getMerges(match, this).getTargets();
		}else 
			//////////////
			if (mergedMatchesOurs != null) {
				if(mergedMatchesOurs.contains(match))
					{
				return mergeTrace.getMergesOurs(match, this).getTargets();
				}
			}
		
			/////////////
		
		else {
			mergedMatches.add(match);
		}
			
		Collection<Object> targets = CollectionUtil.createDefaultList();
		
		for (Parameter targetParameter : targetParameters) {
			EolType targetParameterType = targetParameter.getType(context);
			targets.add(targetParameterType.createInstance());
		}

		mergeTrace.add(match, targets, this);
	
		if(this.priorityLists.size() == 0) {
			if(this.baseParameter != null) {
			executeSuperRulesAndBody(match, targets, context); }else {
				executeSuperRulesAndBodyWB(match, targets, context);
			}
		}else {
			if(this.baseParameter != null) {
			executeSuperRulesAndBodyOURS(match, targets, context);}else {
				executeSuperRulesAndBodyOURSWB(match, targets, context);
			}
		}
		
		return targets;
	}
		
	@Override
	public String toString() {
		
		String OctopusType1 = null;
		String OctopusType2 =null;
		String OursType1 =null;
		String OursType2 =null;
		String OursType3 =null;
		String TransferType =null;
		
		if (sourceParameter != null) {
			TransferType = getName() +
					" (" +
					sourceParameter.getTypeName() + 
					") " +
					"( " +
					sourceModels.getTypeName() + 
					" )" +
					" :" +
					targetParameters.stream()
						.map(Parameter::getTypeName)
						.collect(Collectors.joining(", "));
			 return TransferType;
		}else
		if (priorityLists.isEmpty()) {
			if (baseParameter == null) {
		 OctopusType1 = getName() +
				" (" +
				leftParameter.getTypeName() + 
				", " +
				rightParameters.stream()
				.map(Parameter::getTypeName)
				.collect(Collectors.joining(", ")) + 
				") " +
				" :" +
				targetParameters.stream()
					.map(Parameter::getTypeName)
					.collect(Collectors.joining(", "));
		 return OctopusType1;
			} else {
		 OctopusType2 = getName() +
				" (" +
				leftParameter.getTypeName() + 
				", " +
				rightParameters.stream()
				.map(Parameter::getTypeName)
				.collect(Collectors.joining(", ")) + 
				") " +
				"( " + baseParameter.getTypeName() + " )" +
				" :" +
				targetParameters.stream()
					.map(Parameter::getTypeName)
					.collect(Collectors.joining(", "));
		 return OctopusType2;
			}
		}else {
			if (baseParameter == null) {
		 OursType1 = getName() +
				" (" +
				leftParameter.getTypeName() + 
				", " +
				rightParameters.stream()
				.map(Parameter::getTypeName)
				.collect(Collectors.joining(", ")) + 
				") " +
				"( " +
				priorityLists.stream()
				.map(Parameter::getName)
				.collect(Collectors.joining(", ")) +
				" )" +
				" :" +
				targetParameters.stream()
					.map(Parameter::getTypeName)
					.collect(Collectors.joining(", "));
		 return OursType1;
			} else {
				if (existsInNumber == null) {
		 OursType2 = getName() +
				" (" +
				leftParameter.getTypeName() + 
				", " +
				rightParameters.stream()
				.map(Parameter::getTypeName)
				.collect(Collectors.joining(", ")) + 
				") " +
				"( " + baseParameter.getTypeName() + " )" +
				"( " +
				priorityLists.stream()
				.map(Parameter::getName)
				.collect(Collectors.joining(", ")) +
				" )" +
				" :" +
				targetParameters.stream()
					.map(Parameter::getTypeName)
					.collect(Collectors.joining(", "));
	
		 return OursType2;
				} else {
		 OursType3 = getName() +
				" (" +
				leftParameter.getTypeName() + 
				", " +
				rightParameters.stream()
				.map(Parameter::getTypeName)
				.collect(Collectors.joining(", ")) + 
				") " +
				"( " + baseParameter.getTypeName() + "( " + existsInNumber + " )" + " )" +
				"( " +
				priorityLists.stream()
				.map(Parameter::getName)
				.collect(Collectors.joining(", ")) +
				" )" +
				" :" +
				targetParameters.stream()
					.map(Parameter::getTypeName)
					.collect(Collectors.joining(", "));
		 return OursType3;
				}
			}
		}
	}
	
	public void executeSuperRulesAndBody(nmlMatchTrace match, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {		
		// Execute the super rules
		for (ExtensibleNamedRule superRule : superRules){
			((MergeRule) superRule).merge(match, targets, context);
		}
		
		FrameStack scope = context.getFrameStack();
			
		scope.enterLocal(FrameType.PROTECTED, this);
		scope.put(
			new Variable(baseParameter.getName(), match.getLeft(), baseParameter.getType(context), true));
		
		scope.put(new Variable(leftParameter.getName(), match.getRight().get(0), leftParameter.getType(context), true));
		int count = context.getModelRepository().getModels().size();
		count = count - 3;
		int countRight = match.getRight().size();
		if(countRight != 1) {
		for (int i2 = 0 ; i2 < count ; i2++) {
		scope.put(new Variable(rightParameters.get(i2).getName(), match.getRight().get(i2+1), rightParameters.get(i2).getType(context), true));
		}
		}
		scope.put(Variable.createReadOnlyVariable("self", this)
		);
		
		assert targets.size() == targetParameters.size();
		Iterator<?> targetsIter = targets.iterator();
		
		for (Parameter targetParameter : targetParameters) {
			scope.put(new Variable(targetParameter.getName(), targetsIter.next(), targetParameter.getType(context), true));
		}
		
		ExecutorFactory executorFactory = context.getExecutorFactory();
		executorFactory.execute(bodyBlock, context);
		
		scope.leaveLocal(this);	
}
	
	public void executeSuperRulesAndBodyWB(nmlMatchTrace match, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {		
		// Execute the super rules
		for (ExtensibleNamedRule superRule : superRules){
			((MergeRule) superRule).merge(match, targets, context);
		}
		
		FrameStack scope = context.getFrameStack();
			
		scope.enterLocal(FrameType.PROTECTED, this);
	
		if ( match.getModelsInMatch().contains(leftParameter.getName()) ) {
			if(match.getModelsInMatch().indexOf(leftParameter.getName()) == 0) {
			scope.put(new Variable(leftParameter.getName(), match.getLeft(), leftParameter.getType(context), true));
			
			int count_rightParameters = rightParameters.size();
			for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
				
				if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
					int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
					scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
					
					}
				}
			}else {
				scope.put(new Variable(leftParameter.getName(), match.getRight().get(match.getModelsInMatch().indexOf(leftParameter.getName()) - 1 ), leftParameter.getType(context), true));
				int count_rightParameters = rightParameters.size();
				for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
					
					if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
						if ( match.getModelsInMatch().indexOf( rightParameters.get(jright).getName() ) == 0) {
							scope.put(new Variable(rightParameters.get(jright).getName(), match.getLeft(),rightParameters.get(jright).getType(context), true));
						}else {
						int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
						scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
							}
						}
					}
			}
		}else {
			int count_rightParameters = rightParameters.size();
			for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
				
				if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
					if ( match.getModelsInMatch().indexOf( rightParameters.get(jright).getName() ) == 0) {
						scope.put(new Variable(rightParameters.get(jright).getName(), match.getLeft(),rightParameters.get(jright).getType(context), true));
					}else {
					int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
					scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
						}
					}
				}
		}
		scope.put(Variable.createReadOnlyVariable("self", this)
		);
		
		assert targets.size() == targetParameters.size();
		Iterator<?> targetsIter = targets.iterator();
		
		for (Parameter targetParameter : targetParameters) {
			scope.put(new Variable(targetParameter.getName(), targetsIter.next(), targetParameter.getType(context), true));
		}
		
		ExecutorFactory executorFactory = context.getExecutorFactory();
		executorFactory.execute(bodyBlock, context);
		
		scope.leaveLocal(this);	
}
	
	// Return Type of Statement 
	public String getTypeStatement(String Statement) {
		char[] TypeStatementArr=Statement.toCharArray(); 
		String TypeStatement = "";
		while (Character.toString(TypeStatementArr[0]).equals(" ") == false)
		{
			TypeStatement = TypeStatement + Character.toString(TypeStatementArr[0]);
			StringBuilder sbb = new StringBuilder();
			sbb.append(TypeStatementArr);
			sbb.deleteCharAt(0);
			TypeStatementArr = sbb.toString().toCharArray();
			
			int chlenght = TypeStatementArr.length;
			if(chlenght == 0) break;
		}
		return TypeStatement;
	}

	public void executeSuperRulesAndBodyOURS(nmlMatchTrace match, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {		
		
		List<String> StatementsParameter = new ArrayList<>();
		
		// Execute the super rules
		for (ExtensibleNamedRule superRule : superRules){
			((MergeRule) superRule).merge(match, targets, context);
		}
		
		FrameStack scope = context.getFrameStack();
		
		//for(int PLPChar = 0 ; PLPChar < 2 ; PLPChar ++) 
		{
		try {
		
			for (int i = 0 ; i < this.bodyBlock.getStatements().size() ; i ++) {


				String TypeStatements = this.bodyBlock.getStatements().get(i).toString();
				String TypeStatement = getTypeStatement(TypeStatements);
				
				int TypeStatementCount = 0 ;
				if (TypeStatement.equals("AssignmentStatement")) {
					TypeStatementCount = 1 ;
				}
				if (TypeStatement.equals("ExpressionStatement")) {
					TypeStatementCount = 2 ;
				}
		
			int TypeStatementCount_2 = 0 ;
			switch (TypeStatementCount) {
        	case 1 :
        			{
        				String TypeAssignmentStatements = this.bodyBlock.getStatements().get(i).getChildren().get(1).toString();
        				String TypeAssignmentStatement = getTypeStatement(TypeAssignmentStatements);
        				if (TypeAssignmentStatement.equals("PropertyCallExpression")) {
        					TypeStatementCount_2 = 11;
        				}
        				if (TypeAssignmentStatement.equals("OperationCallExpression")) {
        					TypeStatementCount_2 = 12;
        				}
        			} break;
        	case 2 :
			{
				String TypeAssignmentStatements = this.bodyBlock.getStatements().get(i).getChildren().get(0).toString();
				String TypeAssignmentStatement = getTypeStatement(TypeAssignmentStatements);
				
				if (TypeAssignmentStatement.equals("OperationCallExpression")) {
					TypeStatementCount_2 = 22;
				}
			} break;
		}
			
			///////////////
			String NameOfP = "";
			
			if( TypeStatementCount_2 == 11) {
				NameOfP = this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getName();
				StatementsParameter.add(NameOfP);
				StatementsParameter.add(String.valueOf(TypeStatementCount_2));	
			}
			if( TypeStatementCount_2 == 12) {
				 NameOfP = this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getTargetExpression().getName();
				 StatementsParameter.add(NameOfP);
				 StatementsParameter.add(String.valueOf(TypeStatementCount_2));
			}
			if( TypeStatementCount_2 == 22) {
				 NameOfP = this.bodyBlock.getStatements().get(i).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().getName();
				 StatementsParameter.add(NameOfP);
				 StatementsParameter.add(String.valueOf(TypeStatementCount_2));
			}
			
			for(int j = 0 ; j < this.priorityLists.size() ; j ++) {
				
			String NameOfPL = priorityLists.get(j).getName();
			
			if (NameOfP.equals(NameOfPL)) {
			String NameOfPLP = priorityLists.get(j).getTypeName();
			
			char[] NameOfPLPChar=NameOfPLP.toCharArray(); 

			String ParameterName = "";
			
			//for(int PLPChar = 0 ; PLPChar < NameOfPLPChar.length ; PLPChar ++)
			{
		int NameOfPLPCharCount = (NameOfPLPChar.length-1)/2;	
					
		for(int PLPChar = 0 ; PLPChar < NameOfPLPCharCount ; PLPChar ++) {
		try {
			while (Character.toString(NameOfPLPChar[0]).equals(",") == false)
			{
				ParameterName = ParameterName + Character.toString(NameOfPLPChar[0]);
				StringBuilder sb = new StringBuilder();
				sb.append(NameOfPLPChar);
				sb.deleteCharAt(0);
				NameOfPLPChar = sb.toString().toCharArray();
				
				int chlenght = NameOfPLPChar.length;
				if(chlenght == 0) break;
			}
			/*
			c.setName(ParameterName);
			c.setModule(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getModule());
			c.setParent(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getParent());
			c.setRegion(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getRegion());
			c.setUri(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getUri());
			
			b.setNameExpression(c);
			b.setUri(this.bodyBlock.getStatements().get(i).getChildren().get(1).getParent().getUri());
			b.setModule(this.bodyBlock.getStatements().get(i).getChildren().get(1).getModule());
			b.setRegion(this.bodyBlock.getStatements().get(i).getChildren().get(1).getRegion());
			b.setTargetExpression(c);
			b.setParent(this.bodyBlock.getStatements().get(i).getChildren().get(1).getParent());
			*/
			//this.bodyBlock.getStatements().get(i).getChildren().remove(1);
			//this.bodyBlock.getStatements().get(i).getValueExpression().setTargetExpression(c);
			/////////
			
			if( TypeStatementCount_2 == 11) {
				 this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().setName(ParameterName);
				}
				if( TypeStatementCount_2 == 12) {
					 this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getTargetExpression().setName(ParameterName);
				}
				if( TypeStatementCount_2 == 22) {
					 this.bodyBlock.getStatements().get(i).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().setName(ParameterName);
				}
/*
			switch (TypeStatementCount) {
            	case 1 :
            			{
            				if (this.bodyBlock.getStatements().get(i).getChildren().get(1).getChildren().get(0).getChildren().size() == 0) {
            					this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().setName(ParameterName);
            				}else {
            				if (this.bodyBlock.getStatements().get(i).getChildren().get(1).getChildren().get(0).getChildren().size() != 0) {
            					this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getTargetExpression().setName(ParameterName);
            				}
            				}
            			} break;
            	case 2 :
    			{
    	
    			} break;
			}
            */
			/////////////////////
			
			
					scope.enterLocal(FrameType.PROTECTED, this);
					scope.put(
						new Variable(baseParameter.getName(), match.getLeft(), baseParameter.getType(context), true));
					
					int indexOf_leftParameter = leftParameter.getTypeName().indexOf("!");
					char[] NameOfleftParameter = new char[indexOf_leftParameter];
					leftParameter.getTypeName().getChars(0, indexOf_leftParameter, NameOfleftParameter, 0);

					if ( match.getModelsInMatch().contains(String.copyValueOf(NameOfleftParameter)) ) {
					
						scope.put(new Variable(leftParameter.getName(), match.getRight().get(0), leftParameter.getType(context), true));
						int count_rightParameters = rightParameters.size();
						int count_matchRights = 1;
						
						for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
							int indexOf_rightParameters = rightParameters.get(jright).getTypeName().indexOf("!");
							char[] NameOfrightParameters = new char[indexOf_leftParameter];
							rightParameters.get(jright).getTypeName().getChars(0, indexOf_rightParameters, NameOfrightParameters, 0);
							
							if ( match.getModelsInMatch().contains(String.copyValueOf(NameOfrightParameters)) ) {
								
								scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(count_matchRights), rightParameters.get(jright).getType(context), true));
								count_matchRights++;
							}
						}
					}else {
					
					int count_rightParameters = rightParameters.size();
					int count_matchRights = 0;
					
					for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
						int indexOf_rightParameters = rightParameters.get(jright).getTypeName().indexOf("!");
						char[] NameOfrightParameters = new char[indexOf_leftParameter];
						rightParameters.get(jright).getTypeName().getChars(0, indexOf_rightParameters, NameOfrightParameters, 0);
						
						if ( match.getModelsInMatch().contains(String.copyValueOf(NameOfrightParameters)) ) {
							
							scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(count_matchRights), rightParameters.get(jright).getType(context), true));
							count_matchRights++;
							}
						}
					}
					
					scope.put(Variable.createReadOnlyVariable("self", this)
					);
					
					assert targets.size() == targetParameters.size();
					Iterator<?> targetsIter = targets.iterator();
					
					for (Parameter targetParameter : targetParameters) {
						scope.put(new Variable(targetParameter.getName(), targetsIter.next(), targetParameter.getType(context), true));
					}
					
					ExecutorFactory executorFactory = context.getExecutorFactory();
					executorFactory.execute(bodyBlock, context);
					
					scope.leaveLocal(this);	
					
			 //////////////////////////////

					
					break;
				}
				catch (Exception ex) {
					String m = ex.toString();
				
					int indexOf_type = m.indexOf("'");
					int indexOf_type2 = m.indexOf("'", indexOf_type+1);
					char[] ch = new char[indexOf_type2-indexOf_type-1];
					m.getChars(indexOf_type+1, indexOf_type2, ch, 0);
					String h = String.copyValueOf(ch);
					
					if (ParameterName.equals(h) == false) {
						break;
					}
					StringBuilder sb = new StringBuilder();
					sb.append(NameOfPLPChar);
					sb.deleteCharAt(0);
					sb.deleteCharAt(0);
					NameOfPLPChar = sb.toString().toCharArray();
					ParameterName = "";

				}
		}
				
					}
				}
			}
		}
	
	}
		catch (Exception ex) {
			
			System.out.println(ex);
		}
		
		}
		
		for(int f = 0 ; f <= StatementsParameter.size()/2 ; f++) {
			if( StatementsParameter.get(f+1).equals("11") ) {
					this.bodyBlock.getStatements().get(f/2).getValueExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
			if( StatementsParameter.get(f+1).equals("12") ) {
					 this.bodyBlock.getStatements().get(f/2).getValueExpression().getTargetExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
			if( StatementsParameter.get(f+1).equals("22") ) {
					 this.bodyBlock.getStatements().get(f/2).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
				f++;
			}

}
	
	public void executeSuperRulesAndBodyOURSWB(nmlMatchTrace match, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {		
		
		List<String> StatementsParameter = new ArrayList<>();
		
		// Execute the super rules
		for (ExtensibleNamedRule superRule : superRules){
			((MergeRule) superRule).merge(match, targets, context);
		}
		
		FrameStack scope = context.getFrameStack();
		
		//for(int PLPChar = 0 ; PLPChar < 2 ; PLPChar ++) 
		{
		try {
		
			for (int i = 0 ; i < this.bodyBlock.getStatements().size() ; i ++) {
				
				
				String TypeStatements = this.bodyBlock.getStatements().get(i).toString();
				String TypeStatement = getTypeStatement(TypeStatements);
				
				int TypeStatementCount = 0 ;
				if (TypeStatement.equals("AssignmentStatement")) {
					TypeStatementCount = 1 ;
				}
				if (TypeStatement.equals("ExpressionStatement")) {
					TypeStatementCount = 2 ;
				}
		
			int TypeStatementCount_2 = 0 ;
			switch (TypeStatementCount) {
        	case 1 :
        			{
        				String TypeAssignmentStatements = this.bodyBlock.getStatements().get(i).getChildren().get(1).toString();
        				String TypeAssignmentStatement = getTypeStatement(TypeAssignmentStatements);
        				if (TypeAssignmentStatement.equals("PropertyCallExpression")) {
        					TypeStatementCount_2 = 11;
        				}
        				if (TypeAssignmentStatement.equals("OperationCallExpression")) {
        					TypeStatementCount_2 = 12;
        				}
        			} break;
        	case 2 :
			{
				String TypeAssignmentStatements = this.bodyBlock.getStatements().get(i).getChildren().get(0).toString();
				String TypeAssignmentStatement = getTypeStatement(TypeAssignmentStatements);
				
				if (TypeAssignmentStatement.equals("OperationCallExpression")) {
					TypeStatementCount_2 = 22;
				}
			} break;
		}
			
			///////////////
			String NameOfP = "";
			
			if( TypeStatementCount_2 == 11) {
				NameOfP = this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getName();
				StatementsParameter.add(NameOfP);
				StatementsParameter.add(String.valueOf(TypeStatementCount_2));	
			}
			if( TypeStatementCount_2 == 12) {
				 NameOfP = this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getTargetExpression().getName();
				 StatementsParameter.add(NameOfP);
				 StatementsParameter.add(String.valueOf(TypeStatementCount_2));
			}
			if( TypeStatementCount_2 == 22) {
				 NameOfP = this.bodyBlock.getStatements().get(i).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().getName();
				 StatementsParameter.add(NameOfP);
				 StatementsParameter.add(String.valueOf(TypeStatementCount_2));
			}
			
			for(int j = 0 ; j < this.priorityLists.size() ; j ++) {
				
			String NameOfPL = priorityLists.get(j).getName();
			
			if (NameOfP.equals(NameOfPL)) {
			String NameOfPLP = priorityLists.get(j).getTypeName();
			
			char[] NameOfPLPChar=NameOfPLP.toCharArray(); 

			String ParameterName = "";
			
			//for(int PLPChar = 0 ; PLPChar < NameOfPLPChar.length ; PLPChar ++)
			{
				
		int NameOfPLPCharCount = (NameOfPLPChar.length-1)/2;
				
		for(int PLPChar = 0 ; PLPChar < NameOfPLPCharCount ; PLPChar ++) {
		try {
			while (Character.toString(NameOfPLPChar[0]).equals(",") == false)
			{
				ParameterName = ParameterName + Character.toString(NameOfPLPChar[0]);
				StringBuilder sb = new StringBuilder();
				sb.append(NameOfPLPChar);
				sb.deleteCharAt(0);
				NameOfPLPChar = sb.toString().toCharArray();
				
				int chlenght = NameOfPLPChar.length;
				if(chlenght == 0) break;
			}
			
			/*
			c.setName(ParameterName);
			c.setModule(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getModule());
			c.setParent(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getParent());
			c.setRegion(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getRegion());
			c.setUri(this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getUri());
			
			b.setNameExpression(c);
			b.setUri(this.bodyBlock.getStatements().get(i).getChildren().get(1).getParent().getUri());
			b.setModule(this.bodyBlock.getStatements().get(i).getChildren().get(1).getModule());
			b.setRegion(this.bodyBlock.getStatements().get(i).getChildren().get(1).getRegion());
			b.setTargetExpression(c);
			b.setParent(this.bodyBlock.getStatements().get(i).getChildren().get(1).getParent());
			
			this.bodyBlock.getStatements().get(i).getChildren().remove(1);
			this.bodyBlock.getStatements().get(i).getValueExpression().setTargetExpression(c);
			*/
			if( TypeStatementCount_2 == 11) {
				 this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().setName(ParameterName);
				}
				if( TypeStatementCount_2 == 12) {
					 this.bodyBlock.getStatements().get(i).getValueExpression().getTargetExpression().getTargetExpression().setName(ParameterName);
				}
				if( TypeStatementCount_2 == 22) {
					 this.bodyBlock.getStatements().get(i).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().setName(ParameterName);
				}
				
					scope.enterLocal(FrameType.PROTECTED, this);
					
					if ( match.getModelsInMatch().contains(leftParameter.getName()) ) {
						if(match.getModelsInMatch().indexOf(leftParameter.getName()) == 0) {
						scope.put(new Variable(leftParameter.getName(), match.getLeft(), leftParameter.getType(context), true));
						
						int count_rightParameters = rightParameters.size();
						for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
							
							if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
								int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
								scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
								
								}
							}
						}else {
							scope.put(new Variable(leftParameter.getName(), match.getRight().get(match.getModelsInMatch().indexOf(leftParameter.getName()) - 1 ), leftParameter.getType(context), true));
							int count_rightParameters = rightParameters.size();
							for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
								
								if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
									if ( match.getModelsInMatch().indexOf( rightParameters.get(jright).getName() ) == 0) {
										scope.put(new Variable(rightParameters.get(jright).getName(), match.getLeft(),rightParameters.get(jright).getType(context), true));
									}else {
									int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
									scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
										}
									}
								}
						}
					}else {
						int count_rightParameters = rightParameters.size();
						for(int jright = 0 ; jright < count_rightParameters ; jright ++) {
							
							if ( match.getModelsInMatch().contains( rightParameters.get(jright).getName() )) {
								if ( match.getModelsInMatch().indexOf( rightParameters.get(jright).getName() ) == 0) {
									scope.put(new Variable(rightParameters.get(jright).getName(), match.getLeft(),rightParameters.get(jright).getType(context), true));
								}else {
								int a = match.getModelsInMatch().indexOf(rightParameters.get(jright).getName());
								scope.put(new Variable(rightParameters.get(jright).getName(), match.getRight().get(a-1), rightParameters.get(jright).getType(context), true));
									}
								}
							}
					}
					
					scope.put(Variable.createReadOnlyVariable("self", this)
					);
					
					assert targets.size() == targetParameters.size();
					Iterator<?> targetsIter = targets.iterator();
					
					for (Parameter targetParameter : targetParameters) {
						scope.put(new Variable(targetParameter.getName(), targetsIter.next(), targetParameter.getType(context), true));
					}
					
					ExecutorFactory executorFactory = context.getExecutorFactory();
					executorFactory.execute(bodyBlock, context);
					
					scope.leaveLocal(this);	
					
					///////////////////
				
					 
					break;
				}
				catch (Exception ex) {
					String m = ex.toString();
				
					int indexOf_type = m.indexOf("'");
					int indexOf_type2 = m.indexOf("'", indexOf_type+1);
					char[] ch = new char[indexOf_type2-indexOf_type-1];
					m.getChars(indexOf_type+1, indexOf_type2, ch, 0);
					String h = String.copyValueOf(ch);
					
					if (ParameterName.equals(h) == false) {
						break;
					}
					StringBuilder sb = new StringBuilder();
					sb.append(NameOfPLPChar);
					sb.deleteCharAt(0);
					sb.deleteCharAt(0);
					NameOfPLPChar = sb.toString().toCharArray();
					ParameterName = "";

				}
		}
				
					}
				}
			}
		}
	
	}
		catch (Exception ex) {
			
			System.out.println(ex);
		}
		
		}
		
		for(int f = 0 ; f <= StatementsParameter.size()/2 ; f++) {
			if( StatementsParameter.get(f+1).equals("11") ) {
					this.bodyBlock.getStatements().get(f/2).getValueExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
			if( StatementsParameter.get(f+1).equals("12") ) {
					 this.bodyBlock.getStatements().get(f/2).getValueExpression().getTargetExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
			if( StatementsParameter.get(f+1).equals("22") ) {
					 this.bodyBlock.getStatements().get(f/2).getExpression1().getTargetExpression().getTargetExpression().getTargetExpression().getTargetExpression().setName(StatementsParameter.get(f));
				}
				f++;
			}
		
}
		
	

	@Override
	public AST getSuperRulesAst(AST cst) {
		return AstUtil.getChild(cst, EmlParser.EXTENDS);
	}
	
	public Parameter  getbaseParameter() {
		return baseParameter;
	}
	
	//================================
	
	public void nmlMergeWithOutBase(IEmlContext context, Collection<Object> excluded, int ruleType ) throws EolRuntimeException {
			
		Collection<?> all = getAllInstances(context);
		List<Object> objects = new ArrayList<Object>();
		List<String> modelNameInMatch = new ArrayList<String>();
		List<String> modelNameInMatchWB = new ArrayList<String>();
		MatchTrace TrueMatch = new MatchTrace();
		nmlMatchTrace nmlMatchTrace = new nmlMatchTrace();
		boolean FalseMatch = false;
		Match match = null;
		

		String ModelType = leftParameter.getTypeName();
		char[] ModelNamearr = new char[ ModelType.toCharArray().length - ModelType.indexOf("!") - 1 ];
		ModelType.getChars(ModelType.indexOf("!") + 1 , ModelType.toCharArray().length , ModelNamearr, 0);
		String typeMergeRule = String.copyValueOf(ModelNamearr);
		
		for (Object instance : all) {			
			if (shouldBeMerged(instance, excluded, context)) {
				if(objects.isEmpty()) {
				objects.add(instance);
				
				}else {
					for (MatchRule matchRule : context.getEclModules().getMatchRules()) {
						Parameter LeftParameterRule = matchRule.getLeftParameterRule();
						String ModelType_matchRule = LeftParameterRule.getTypeName();
						char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
						ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
						String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
						
						if(typeMergeRule.equals(typeMatchRule)) 
						{
						
						for (Object right : objects) {
							//matchRule.matchPair(context.getEclModules().getContext(), true, instance, right);
							match = matchRule.nmlMatch(context , instance, right, null, false);
							if (match == null) {break;}
							if (match.isMatching() == true) {
								TrueMatch.add(match);
								modelNameInMatch.addAll(findNameOfModelInMatch(match, context));
								modelNameInMatchWB.addAll(findNameOfModelInMatchWB(match, context));
								FalseMatch = false;
								break;
							}else {
								FalseMatch = true;
							}
						}
						if (FalseMatch == true && match !=null) {
							objects.add(instance);
							}
				    	
						}
					}
				}

			}
		}
		if(!TrueMatch.isEmpty()) {
			if(ruleType == 1) {
			context.setOctopusMatchTraceWB(nmlMatchTrace.nml_createMatchTraceWithoutBase(TrueMatch , modelNameInMatch , modelNameInMatchWB));
			
			for(int i = 0 ; i < context.getOctopusMatchTraceWB().size() ; i++) {
				if(context.getBModel() == null) {
					if(context.getOctopusMatchTraceWB().get(i).getModelsMatch() != context.getModelRepository().getModels().size() - 1) {
						context.getOctopusMatchTraceWB().remove(i);	
						i--;
							}
				}else
				if(context.getOctopusMatchTraceWB().get(i).getModelsMatch() != context.getModelRepository().getModels().size() - 2) {
					context.getOctopusMatchTraceWB().remove(i);	
					i--;
						}
					}
				}
			
			if(ruleType == 2) {
			context.setOursMatchTraceWB(nmlMatchTrace.nml_createMatchTraceWithoutBase(TrueMatch, modelNameInMatch , modelNameInMatchWB));
			
			for(int i = 0 ; i < context.getOursMatchTraceWB().size() ; i++) {
				if(context.getBModel() == null) {
					if(context.getOursMatchTraceWB().get(i).getModelsMatch() == context.getModelRepository().getModels().size() - 1) {
						context.getOursMatchTraceWB().remove(i);	
						i--;
							}
					
				}else
				if(context.getOursMatchTraceWB().get(i).getModelsMatch() == context.getModelRepository().getModels().size() - 2) {
					context.getOursMatchTraceWB().remove(i);	
					i--;
						}
					}
				}
			
			
			}
		}
	
	public Collection<?> getAllInstances(IEmlContext context) throws EolRuntimeException {
		Collection<?> all_L = getAllInstances(leftParameter, context, !isGreedy(context));
		Collection<?> all_R = getAllInstances(rightParameters.get(0), context, !isGreedy(context));
		Collection<?> all = Stream.of(all_L, all_R)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
		if(rightParameters.size() > 1) {
		for ( int i = 1; i < rightParameters.size(); i++ ) {
			Collection<?> all_Rs = getAllInstances(rightParameters.get(i), context, !isGreedy(context));
			all = Stream.of(all, all_Rs)
	                .flatMap(Collection::stream)
	                .collect(Collectors.toList());
		}
		}
		return all;
	}
	
	public boolean shouldBeMerged(Object instance, Collection<Object> excluded, IEmlContext context) throws EolRuntimeException {
		return (!isLazy(context))
			&& !isAbstract(context)
			&& (excluded == null || !excluded.contains(instance));
	}
	
	public List<String> findNameOfModelInMatch(Match match, IEmlContext context) throws EolRuntimeException {
		Object left = match.getLeft();
		Object right = match.getRight();
		List<String> modelName = new ArrayList<String>();
		
		if(getAllInstances(leftParameter, context, !isGreedy(context)).contains(left)) modelName.add(leftParameter.getName());
		if(getAllInstances(leftParameter, context, !isGreedy(context)).contains(right)) modelName.add(leftParameter.getName());
		for(int i = 0; i<rightParameters.size() ; i++) {
			if(getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(left)) modelName.add(rightParameters.get(i).getName());
			if(getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(right)) modelName.add(rightParameters.get(i).getName());
			}
		return modelName;
		}
	
	public List<String> findNameOfModelInMatchWB(Match match, IEmlContext context) throws EolRuntimeException {
		Object left = match.getLeft();
		Object right = match.getRight();
		List<String> modelName = new ArrayList<String>();
		
		if(getAllInstances(leftParameter, context, !isGreedy(context)).contains(left)) modelName.add(findNameOfModelByParameter(leftParameter , context));
		if(getAllInstances(leftParameter, context, !isGreedy(context)).contains(right)) modelName.add(findNameOfModelByParameter(leftParameter , context));
		for(int i = 0; i<rightParameters.size() ; i++) {
			if(getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(left)) modelName.add(findNameOfModelByParameter(rightParameters.get(i) , context));
			if(getAllInstances(rightParameters.get(i), context, !isGreedy(context)).contains(right)) modelName.add(findNameOfModelByParameter(rightParameters.get(i) , context));
			}
		return modelName;
		}
	
	public String findNameOfModelByParameter(Parameter Parameter, IEmlContext context) throws EolRuntimeException {
		
		Parameter LeftParameterRule = Parameter;
		String ModelType_matchRule = LeftParameterRule.getTypeName();
	
		char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ( ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") )  ];
		ModelType_matchRule.getChars(0 , ModelType_matchRule.indexOf("!") , ModelNamearr_matchRule, 0 );
		String NameOfModelByParameter = String.copyValueOf(ModelNamearr_matchRule);
		
		return NameOfModelByParameter;
		}
	
	
	public String getExistsInNumber() throws EolRuntimeException {
	
		return existsInNumber;
		}
		
	//=========================================================================
	//========================== Transfer =====================================
	//=========================================================================
	protected Parameter sourceParameterTemp = null;
	public void transferAll(IEmlContext context, Collection<Object> excluded, Collection<Object> excludedWB, Collection<Object> cemetery, boolean includeLazy) throws EolRuntimeException {
		
		sourceParameterTemp = sourceParameter;
		TypeExpression g = new TypeExpression();
		
		String ModelName = "";
	char[] NamesourceModel=sourceModels.getTypeName().toCharArray(); 
	for(int modelNum = 0 ; modelNum < sourceModels.getChildren().get(1).getChildren().size() ; modelNum ++) {
		
		try {
		while (Character.toString(NamesourceModel[0]).equals(",") == false)
		{
			ModelName = ModelName + Character.toString(NamesourceModel[0]);
			StringBuilder sb = new StringBuilder();
			sb.append(NamesourceModel);
			sb.deleteCharAt(0);
			NamesourceModel = sb.toString().toCharArray();
			
			int chlenght = NamesourceModel.length;
			if(chlenght == 0) break;
		}
		g = sourceParameter.getTypeExpression();
		String ModelType = g.getName();

		char[] ModelNamearr = new char[ ModelType.toCharArray().length - ModelType.indexOf("!") ];
		ModelType.getChars(ModelType.indexOf("!") , ModelType.toCharArray().length, ModelNamearr, 0);
		ModelName = ModelName + String.valueOf(ModelNamearr);
		g.setName(ModelName);
		sourceParameterTemp.setTypeExpression(g);
		
		transfersourceParameterTemp( context,  excluded,  excludedWB, cemetery,  includeLazy , ModelName);
		
		if(NamesourceModel.length == 0) break;
		StringBuilder sbb = new StringBuilder();
		sbb.append(NamesourceModel);
		
		sbb.deleteCharAt(0);
		sbb.deleteCharAt(0);
		NamesourceModel = sbb.toString().toCharArray();
		 ModelName = "";
		
	}
	catch (Exception ex) {
		System.out.println(ex.toString());
	}
	}
		
	}
	
	public void transfersourceParameterTemp(IEmlContext context, Collection<Object> excluded, Collection<Object> excludedWB, Collection<Object> cemetery, boolean includeLazy , String ModelName) throws EolRuntimeException {
		
		Collection<?> all = getAllInstancesTransfer(context);
		for (Object instance : all) {
			if (shouldBeTransfer(instance, excluded, excludedWB, context, includeLazy)) {
				context.setTransferObjects(instance);
				context.setTransferObjectsParameter(ModelName);
				if (!cemetery.isEmpty()) {
				if(!cemetery.contains(findObjectContainer(instance , "eContainer" , context))) {
				transfer(instance, context);
					}else {
						cemetery.add(instance);
						context.setMergerReport("-------------------------------------------------");
						//System.out.println(".........................................................");
						context.setMergerReport("_______CONFLICT PREVENTION_______");
						//System.out.println("_______CONFLICT PREVENTION_______");
						context.setMergerReport("Element in Input Version = " + instance.toString());
						//System.out.println("Element in Input Version = " + instance.toString());
						context.setMergerReport("Rule Type = Transfer");
						//System.out.println("Rule Type = Transfer");
						context.setMergerReport("Result = Element Ignore");
						//System.out.println("Result = Element Ignore");
					}
				}else {
					transfer(instance, context);
				}
			}
		}
		
	}
	
	public boolean shouldBeTransfer(Object instance, Collection<Object> excluded, Collection<Object> excludedWB, IEmlContext context, boolean overrideLazy) throws EolRuntimeException {
		return (overrideLazy || !isLazy(context))
			&& !isAbstract(context)
			&& (excluded == null || !excluded.contains(instance))
			&& (excludedWB == null || !excludedWB.contains(instance))
			&& TransferappliesTo(instance, context, false);
	}
	
	protected Collection<Object> rejected = new HashSet<>();
	
	public boolean TransferappliesTo(Object source, IEmlContext context, boolean asSuperRule) throws EolRuntimeException {
		return TransferappliesTo(source, context, asSuperRule, true);
	}
	
	//TODO: Add support for rejected objects to other languages as well!
	public boolean TransferappliesTo(Object source, IEmlContext context, boolean asSuperRule, boolean checkTypes) throws EolRuntimeException {
		
		if (hasTransformed(source)) return true;
		if (rejected.contains(source)) return false;
		
		boolean appliesToTypes;
		
		if (!checkTypes) {
			appliesToTypes = true;
		}
		else {
			boolean ofTypeOnly = !(isGreedy(context) || asSuperRule);
			
			if(sourceParameterTemp == null) {
				return false;
				}
			EolType type = sourceParameterTemp.getType(context);
				
			appliesToTypes = ofTypeOnly ? type.isType(source) : type.isKind(source);
		}
		
		boolean guardSatisfied = true;
		
		if (appliesToTypes && guardBlock != null) {
			guardSatisfied = guardBlock.execute(context, 
				Variable.createReadOnlyVariable(sourceParameterTemp.getName(), source), 
				Variable.createReadOnlyVariable("self", this)
			);	
		}
		
		boolean applies = appliesToTypes && guardSatisfied;
		
		for (ExtensibleNamedRule superRule : getSuperRules()) {
			MergeRule rule = (MergeRule) superRule;
			if (!rule.TransferappliesTo(source, context, true)) {
				applies = false;
				break;
			}
		}
		
		if (!applies) {
			rejected.add(source);
		}
		
		return applies;
	}
	
	public boolean hasTransformed(Object source) {
		return transferElements.contains(source);
	}
	
	protected Set<Object> transferElements = new HashSet<>();
	
	public Collection<?> transfer(Object source, IEmlContext context) throws EolRuntimeException {
		if (hasTransformed(source)) {
			return context.getTransferTrace().getTransferTargets(source, getName());
		}
		else {
			transferElements.add(source);
		}
		
		Collection<Object> targets = CollectionUtil.createDefaultList();
		for (Parameter targetParameter : targetParameters) {
			EolType targetParameterType = targetParameter.getType(context);
			targets.add(targetParameterType.createInstance());
		}
		
		context.getTransferTrace().addTransfer(source, targets, this);
	//	context.getTransferTrace().add(source, targets, this);
		executeSuperRulesAndBodyTransfer(source, targets, context);
		return targets;
	}
	
	protected void executeSuperRulesAndBodyTransfer(Object source, Collection<Object> targets_, IEmlContext context) throws EolRuntimeException {
		
		List<Object> targets = CollectionUtil.asList(targets_);
		// Execute super-rules
		for (ExtensibleNamedRule rule : superRules) {
			MergeRule superRule = (MergeRule) rule;
			superRule.transfer(source, targets, context);
		}
		/*
		Variable[] variables = new Variable[targetParameters.size() + 2];
		variables[0] = Variable.createReadOnlyVariable("self", this);
		variables[1] = Variable.createReadOnlyVariable(sourceParameter.getName(), source);
		
		for (int i = 2; i < variables.length; ++i) {
			int offset = i-2;
			Parameter tp = targetParameters.get(offset);
			variables[i] = Variable.createReadOnlyVariable(tp.getName(), targets.get(offset));
		}
		
		body.execute(context, variables);
		*/
		
		FrameStack scope = context.getFrameStack();
		
		scope.enterLocal(FrameType.PROTECTED, this);
		scope.put(
			new Variable(sourceParameterTemp.getName(), source, sourceParameterTemp.getType(context), true));
		
		scope.put(Variable.createReadOnlyVariable("self", this)
		);
		
		assert targets.size() == targetParameters.size();
		Iterator<?> targetsIter = targets.iterator();
		
		for (Parameter targetParameter : targetParameters) {
			scope.put(new Variable(targetParameter.getName(), targetsIter.next(), targetParameter.getType(context), true));
		}
		
		ExecutorFactory executorFactory = context.getExecutorFactory();
		executorFactory.execute(bodyBlock, context);
		
		scope.leaveLocal(this);	
	}
	
	public Collection<?> transfer(Object source, Collection<Object> targets, IEmlContext context) throws EolRuntimeException {
		transferElements.add(source);
		executeSuperRulesAndBodyTransfer(source, targets, context);
		return targets;
	}
	
	public Collection<?> getAllInstancesTransfer(IEmlContext context) throws EolRuntimeException {
		final Map<Parameter, Collection<?>> cache = !isGreedy(context) ? ofTypeCache : ofKindCache;
		cache.clear();
		sourceParameterTemp.clearCache();;
		return getAllInstances(sourceParameterTemp, context, !isGreedy(context));
	}
	

/////////////////////////////////////////////////////////////////

public Object findObjectContainer(Object source, String propertyName, IEolContext context) throws EolRuntimeException {

IPropertyGetter getter = context.getIntrospectionManager().getPropertyGetterFor(source, propertyName, context);

Object sourceContainer = getter.invoke(source, propertyName, context);
return sourceContainer;

}
	
}
