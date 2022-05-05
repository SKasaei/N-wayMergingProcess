/*********************************************************************
 * Copyright (c) 2019 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package enml;

import java.util.ArrayList;
import java.util.List;
import dom.MergeRule;
import execute.context.INmlContext;
import org.eclipse.epsilon.etl.IEtlModule;

/**
 * 
 * 
 * @author Betty Sanchez
 * @since 1.6
 */
public interface INmlModule extends IEtlModule {

	List<MergeRule> getMergeRules();
	List<MergeRule> getOctopusRules();
	List<MergeRule> getOursRules();
	List<MergeRule> getTransferRules();

	
	ArrayList<String> getImportModel();
	ArrayList<String> getImportModelName();
	ArrayList<String> getImportModelFileName();



	List<MergeRule> getDeclaredMergeRules();

	@Override
	default INmlContext getContext() {
		return (INmlContext) ((IEtlModule)this).getContext();
	}
	
}
