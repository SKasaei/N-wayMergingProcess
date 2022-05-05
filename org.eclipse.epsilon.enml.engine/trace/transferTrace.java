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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.epsilon.common.concurrent.ConcurrencyUtils;
import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.trace.Transformation;

import dom.MergeRule;
import nmlMatchTrace.nmlMatchTrace;

public class transferTrace {
	
	Map<Object, Collection<Transfer>> cache;
	Collection<Transfer> transformations;
	boolean isConcurrent;
	
	public transferTrace() {
		this(false);
	}
	
	public transferTrace(boolean concurrent) {
		this.isConcurrent = concurrent;
		cache = concurrent ? ConcurrencyUtils.concurrentMap() : new HashMap<>();
		transformations = newCollection();
	}
	
	<T> Collection<T> newCollection() {
		return isConcurrent ? new Vector<>() : new ArrayList<>();
	}
	
	public void addTransfer(Object source, Collection<Object> targets, MergeRule rule) {
		Transfer transformation = new Transfer(source, targets);
		transformation.setRule(rule);
		transformations.add(transformation);
		Collection<Transfer> transformations = cache.get(source);
		if (transformations == null) {
			cache.put(source, transformations = newCollection());
		}
		transformations.add(transformation);
	}
	
	public Collection<Transfer> getTransformations() {
		return transformations;
	}
	
	public  Collection<Transfer> getTransformations(Object source) {
		Collection<Transfer> t =  cache.get(source);
		return t != null ? t : newCollection();
	}

	public Collection<?> getTransferTargets(Object source, String rule) {
		Collection<Object> targets = CollectionUtil.createDefaultList();
		for (Transfer transformation : getTransformations()) {
			if (rule == null || rule.equals(transformation.getRule().getName())) {
				if (source.equals(transformation.source)) {					
					targets.addAll(transformation.getTargets());
				}
			}
		}
		return targets;
	}
	/*
	public boolean containsTransformedBy(TransformationRule rule) {
		return getTransformations().stream().anyMatch(t -> t.getRule() == rule);
	}
	*/

}
