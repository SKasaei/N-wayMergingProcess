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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.epsilon.common.module.AbstractModuleElement;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.util.AstUtil;
import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.common.util.UriUtil;
import org.eclipse.epsilon.ecl.trace.Match;
import execute.context.IEmlContext;
import parse.EmlParser;
import trace.MergeTrace;
import trace.Merges;

import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.dom.StringLiteral;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.erl.dom.ExtensibleNamedRule;

public class importModel extends AbstractModuleElement {
	
	protected String modelName = null;
	protected String fileName = null;
	protected StringLiteral pathLiteral;
	private IEolModule parentModule;
	private boolean found = false;
	
	protected File sourceFile;
	protected URI sourceUri;

	public importModel() { }
	
	@Override
	public void build(AST cst, IModule module) {
		super.build(cst, module);

				module.createAst(cst.getFirstChild(), this);
				modelName =  cst.getFirstChild().getText();
				module.createAst(cst.getSecondChild(), this);
				fileName = cst.getSecondChild().getText();
				pathLiteral = (StringLiteral) module.createAst(cst.getSecondChild(), this);
				URI SourceUri = module.getSourceUri();
				if (SourceUri != null) {
					this.load(SourceUri);
					}
}
	
	public void load(URI baseUri) {
		try {
			final File file = new File(getPath());
			URI uri;
			
			if (file.isAbsolute()) {
				if (!file.exists()) return;
				uri = file.toURI();
			} else {
				uri = UriUtil.resolve(getPath(), baseUri);
			}
			
			this.sourceUri = uri;

			final String uriScheme = uri.getScheme();
			if ("file".equals(uriScheme)) {
				this.sourceFile = new File(uri);
			}
		}
		catch (Exception ex) {
			// Ignore the exception. The import's loaded flag is still false
			// and it's up to the importing module to do something about it.
		}
	}
	

	public String getPath() {
		return pathLiteral.getValue();
	}
	public String getPathModel() {
		return sourceFile.toString();
	}
	public String getModelName() {
		return modelName;
	}
	
	public String getModelFileName() {
		return fileName;
	}
	

	public AST getSuperRulesAst(AST cst) {
		return AstUtil.getChild(cst, EmlParser.EXTENDS);
	}
	
}
