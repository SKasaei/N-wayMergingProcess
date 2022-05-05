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
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.trace.Transformation;

import dom.MergeRule;
import nmlMatchTrace.nmlMatchTrace;

public class MergeTrace extends ArrayList<Merge> {
	
	public void add(nmlMatchTrace match, Collection<Object> targets, MergeRule rule) {
		Merge merge = new Merge();
		merge.match = match;
		merge.setTargets(targets);
		merge.setRule(rule);
		add(merge);
	}
	
	public Merges getMerges(nmlMatchTrace match) {
		ListIterator<Merge> li = listIterator();
		Merges merges = new Merges();
		while (li.hasNext()) {
			Merge merge = li.next();
			if (merge.getMatch() == match) {
				merges.add(merge);
			}
		}
		return merges;
	}

	public Merges getMerges(nmlMatchTrace match, MergeRule mergeRule) {
		Merges merges = new Merges();
		for (Merge merge : this) {
			if (merge.getMatch() == match ) {
				if(merge.getRule() == mergeRule) {
				merges.add(merge);
				}
			}
		}
		return merges;
	}
///////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<nmlMatchTrace> getMergedMatches(MergeTrace mergeDone) {
		if(mergeDone.isEmpty())
		{
			return null;
		}else {
			
		ListIterator<Merge> li = listIterator();
		
		Merge merge = li.next();
		nmlMatchTrace MergedMatche = merge.match;
		List<nmlMatchTrace> MergedMatches = new ArrayList<>();
		MergedMatches.add(MergedMatche);
		//List<nmlMatchTrace> MergedMatches = Arrays.asList(MergedMatche) ;
	
		while (li.hasNext()) {
			merge = li.next();
			MergedMatches.add(merge.match);

		}
		return MergedMatches;
		}
	}
	
	public Merges getMergesOurs(nmlMatchTrace match, MergeRule mergeRule) {
		Merges merges = new Merges();
		for (Merge merge : this) {
			if (merge.getMatch() == match ) {
				
				merges.add(merge);
				
			}
		}
		return merges;
	}
	

	
}
