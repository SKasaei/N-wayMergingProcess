/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package trace;

import java.util.Collection;

import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.etl.dom.TransformationRule;

import dom.MergeRule;
import nmlMatchTrace.nmlMatchTrace;

public class Transfer {
	
	protected Object source;
	protected Collection<Object> targets;
	protected MergeRule rule;
	
	public Transfer() {
	}
	
	public Transfer(Object source, Collection<Object> targets) {
		this.source = source;
		this.targets = targets;
	}
	
	public Transfer(Object source, Collection<Object> targets, MergeRule rule) {
		this(source, targets);
		this.rule = rule;
	}
	
	public Object getSource() {
		return source;
	}
	
	public void setSource(Object source) {
		this.source = source;
	}
	
	public Collection<Object> getTargets() {
		return targets;
	}

	public void setTargets(Collection<Object> targets) {
		this.targets = targets;
	}

	public boolean of(Object source) {
		return this.source == source;
	}

	public MergeRule getRule() {
		return rule;
	}

	public void setRule(MergeRule rule) {
		this.rule = rule;
	}
	
}