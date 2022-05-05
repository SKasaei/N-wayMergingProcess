/*********************************************************************
 * Copyright (c) 2020 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package launch;

import enml.NmlModule;
import enml.INmlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;

/**
 * 
 * @author Sina Madani
 * @since 2.0
 */
public class NmlRunConfiguration extends EtlRunConfiguration {

	public static class Builder<R extends NmlRunConfiguration, B extends Builder<R, B>> extends EtlRunConfiguration.Builder<R, B> {

		protected Builder() {
			super();
		}
		protected Builder(Class<R> runConfigClass) {
			super(runConfigClass);
		}
		
		@Override
		protected INmlModule createModule() {
			return new NmlModule();
		}
	}
	
	public static Builder<? extends NmlRunConfiguration, ?> Builder() {
		return new Builder<>(NmlRunConfiguration.class);
	}
	
	public NmlRunConfiguration(Builder<? extends EtlRunConfiguration, ?> builder) {
		super(builder);
	}
	
	public NmlRunConfiguration(NmlRunConfiguration other) {
		super(other);
	}

	@Override
	public INmlModule getModule() {
		return (INmlModule) super.getModule();
	}
}
