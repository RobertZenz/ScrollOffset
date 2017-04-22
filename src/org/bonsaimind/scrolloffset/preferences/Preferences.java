/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset.preferences;

import org.bonsaimind.scrolloffset.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

public class Preferences {
	private static final String PREFERENCE_NAME_ENABLED = Activator.PLUGIN_ID + ".enabled";
	private static final String PREFERENCE_NAME_OFFSET = Activator.PLUGIN_ID + ".offset";
	private IPreferenceStore preferenceStore = null;
	
	public Preferences(IPreferenceStore preferenceStore) {
		super();
		
		this.preferenceStore = preferenceStore;
		
		preferenceStore.setDefault(PREFERENCE_NAME_ENABLED, true);
		preferenceStore.setDefault(PREFERENCE_NAME_OFFSET, 10);
	}
	
	protected Preferences() {
		super();
	}
	
	public int getOffset() {
		return preferenceStore.getInt(PREFERENCE_NAME_OFFSET);
	}
	
	public boolean isEnabled() {
		return preferenceStore.getBoolean(PREFERENCE_NAME_ENABLED);
	}
	
	public void restoreDefaults() {
		preferenceStore.setToDefault(PREFERENCE_NAME_ENABLED);
		preferenceStore.setToDefault(PREFERENCE_NAME_OFFSET);
	}
	
	public void setEnabled(boolean enabled) {
		preferenceStore.setValue(PREFERENCE_NAME_ENABLED, enabled);
	}
	
	public void setOffset(int offset) {
		preferenceStore.setValue(PREFERENCE_NAME_OFFSET, offset);
	}
}
