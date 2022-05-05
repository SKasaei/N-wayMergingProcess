/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package enml;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.TokenStream;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.parse.EpsilonParser;
import org.eclipse.epsilon.common.util.AstUtil;

import dom.MergeRule;
import dom.importModel;
import dom.EquivalentAssignmentStatement;
import execute.context.NmlContext;
import execute.context.INmlContext;
import execute.operations.NmlOperationFactory;

import parse.EmlLexer;
import parse.EmlParser;
import org.eclipse.epsilon.eol.dom.Import;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;

import org.eclipse.epsilon.erl.dom.NamedRuleList;
import org.eclipse.epsilon.etl.EtlModule;

public class NmlModule extends EtlModule implements INmlModule {
	
	protected NamedRuleList<MergeRule> declaredMergeRules = new NamedRuleList<>();
	protected NamedRuleList<MergeRule> mergeRules;
	//======
	protected NamedRuleList<MergeRule> octopusRules;
	protected NamedRuleList<MergeRule> oursRules;
	protected NamedRuleList<MergeRule> transferRules;
	protected NamedRuleList<MergeRule> declaredOctopusRules = new NamedRuleList<>();
	protected NamedRuleList<MergeRule> declaredOursRules = new NamedRuleList<>();
	protected NamedRuleList<MergeRule> declaredTransferRules = new NamedRuleList<>();
	protected List<importModel> importModel = new ArrayList<>();
	
	public NmlModule() {
		this(null);
	}
	
	/**
	 * Instantiates the module with the specified execution context.
	 * 
	 * @param context The execution context
	 * @since 1.6
	 */
	public NmlModule(INmlContext context) {
		super(context != null ? context : new NmlContext());
	}
	
	@Override
	protected Lexer createLexer(ANTLRInputStream inputStream) {
		return new EmlLexer(inputStream);
	}

	@Override
	public EpsilonParser createParser(TokenStream tokenStream) {
		return new EmlParser(tokenStream);
	}

	@Override
	public String getMainRule() {
		return "emlModule";
	}

	@Override
	public HashMap<String, Class<?>> getImportConfiguration() {
		HashMap<String, Class<?>> importConfiguration = super.getImportConfiguration();
		importConfiguration.put("nml", NmlModule.class);
		return importConfiguration;
	}

	@Override
	public void build(AST cst, IModule module) {
		super.build(cst, module);
		
		// Parse the import model 
		for (AST importAst : AstUtil.getChildren(cst, EmlParser.IMPORTMODEL)) {
			importModel.add((importModel) module.createAst(importAst, this));
		}

		// Parse the octopus rules
		for (AST mergeRuleAst : AstUtil.getChildren(cst, EmlParser.OCTOPUS)) {
			declaredOctopusRules.add((MergeRule) module.createAst(mergeRuleAst, this));
		}
		
		// Parse the ours rules
		for (AST mergeRuleAst : AstUtil.getChildren(cst, EmlParser.OURS)) {
			declaredOursRules.add((MergeRule) module.createAst(mergeRuleAst, this));
		}
		
		// Parse the transfer rules
		for (AST transferRuleAst : AstUtil.getChildren(cst, EmlParser.TRANSFER)) {
			declaredTransferRules.add((MergeRule) module.createAst(transferRuleAst, this));
		}
	}
	
	public ArrayList<String> getImportModel() {
		int i = importModel.size();
		ArrayList<String> importModelList = new ArrayList<String>();
		for( int j = 0 ; j < i ; j++ ) {
		String importModeluri = importModel.get(j).getPathModel();
		importModelList.add(importModeluri);
		}
		return importModelList;
	}
	public ArrayList<String> getImportModelName() {
		int i = importModel.size();
		ArrayList<String> importModelNames = new ArrayList<String>();
		for( int j = 0 ; j < i ; j++ ) {
		String importModelName = importModel.get(j).getModelName();
		importModelNames.add(importModelName);
		}
		return importModelNames;
	}
	
	public ArrayList<String> getImportModelFileName() {
		int i = importModel.size();
		ArrayList<String> importModelFileNames = new ArrayList<String>();
		for( int j = 0 ; j < i ; j++ ) {
		String importModelFileName = importModel.get(j).getModelFileName();
		importModelFileNames.add(importModelFileName);
		}
		return importModelFileNames;
	}
	
	
	
	@Override
	protected void prepareContext() throws EolRuntimeException {
		super.prepareContext();
		INmlContext context = getContext();
		context.setOperationFactory(new NmlOperationFactory());
		context.getFrameStack().put(
			//Variable.createReadOnlyVariable("matchTrace", context.getMatchTrace()),
			Variable.createReadOnlyVariable("matchTraceOctopus", context.getOctopusMatchTrace()),
			Variable.createReadOnlyVariable("matchTraceOurs", context.getOursMatchTrace()),
			Variable.createReadOnlyVariable("mergeTrace", context.getMergeTrace()),
			Variable.createReadOnlyVariable("transTrace", context.getTransformationTrace()),
			Variable.createReadOnlyVariable("context", context),
			Variable.createReadOnlyVariable("thisModule", this)
		);
	}
	
	/**
	 * Main execution logic.
	 * 
	 * @throws EolRuntimeException
	 * @since 1.6
	 */
	@Override
	protected Object processRules() throws EolRuntimeException {
		INmlContext context = getContext();
		context.getMergingStrategy().mergeModels(context);
		return null;
	}
	
	@Override
	public INmlContext getContext() {
		return (INmlContext) super.getContext();
	}
	
	// edite for octopus and ours rules
	@Override
	public ModuleElement adapt(AST cst, ModuleElement parentAst) {
		if ( cst.getType() == EmlParser.IMPORTMODEL) {
			return new importModel();
		}
		else if (cst.getType() == EmlParser.OCTOPUS | cst.getType() == EmlParser.OURS | cst.getType() == EmlParser.TRANSFER ) {
			return new MergeRule();
		}
		else if (cst.getType() == EmlParser.SPECIAL_ASSIGNMENT) {
			return new EquivalentAssignmentStatement();
		}
		return super.adapt(cst, parentAst);
	}
	
	@Override
	public List<MergeRule> getDeclaredMergeRules(){
		return declaredMergeRules;
	}
	
	@Override
	public List<MergeRule> getMergeRules() {
		if (mergeRules == null) {
			mergeRules = new NamedRuleList<>();
			for (Import import_ : imports) {
				if (import_.isLoaded() && (import_.getModule() instanceof NmlModule)) {
					INmlModule module = (INmlModule) import_.getModule();
					mergeRules.addAll(module.getMergeRules());
				}
			}
			mergeRules.addAll(declaredMergeRules);
		}
		return mergeRules;
	}
	
	public List<MergeRule> getOctopusRules() {
		if (octopusRules == null) {
			octopusRules = new NamedRuleList<>();
			for (Import import_ : imports) {
				if (import_.isLoaded() && (import_.getModule() instanceof NmlModule)) {
					INmlModule module = (INmlModule) import_.getModule();
					octopusRules.addAll(module.getOctopusRules());
				}
			}
			octopusRules.addAll(declaredOctopusRules);
		}
		return octopusRules;
	}
	
	public List<MergeRule> getOursRules() {
		if (oursRules == null) {
			oursRules = new NamedRuleList<>();
			for (Import import_ : imports) {
				if (import_.isLoaded() && (import_.getModule() instanceof NmlModule)) {
					INmlModule module = (INmlModule) import_.getModule();
					oursRules.addAll(module.getOursRules());
				}
			}
			oursRules.addAll(declaredOursRules);
		}
		return oursRules;
	}
	
	public List<MergeRule> getTransferRules() {
		if (transferRules == null) {
			transferRules = new NamedRuleList<>();
			for (Import import_ : imports) {
				if (import_.isLoaded() && (import_.getModule() instanceof NmlModule)) {
					INmlModule module = (INmlModule) import_.getModule();
					transferRules.addAll(module.getTransferRules());
				}
			}
			transferRules.addAll(declaredTransferRules);
		}
		return transferRules;
	}
	
}
