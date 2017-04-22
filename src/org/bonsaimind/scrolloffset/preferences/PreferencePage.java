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
import org.bonsaimind.scrolloffset.scrolling.Scroller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {
	private Button btnEnableDuringMousedown = null;
	private Button btnEnableScrolloffset = null;
	private Preferences preferences = null;
	private Text txtOffset = null;
	
	public PreferencePage() {
		super();
	}
	
	@Override
	public void init(IWorkbench workbench) {
		preferences = Activator.getDefault().getPreferences();
	}
	
	@Override
	public boolean performOk() {
		preferences.setEnabled(btnEnableScrolloffset.getSelection());
		preferences.setEnabledDuringMousedown(btnEnableDuringMousedown.getSelection());
		
		try {
			preferences.setOffset(Integer.parseInt(txtOffset.getText()));
		} catch (NumberFormatException e) {
			// Ignore any exception.
		}
		
		updateValuesFromPreferences();
		
		Scroller.updateFromPreferences();
		
		return super.performOk();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		btnEnableScrolloffset = new Button(parent, SWT.CHECK);
		btnEnableScrolloffset.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnEnableScrolloffset.setText("Enable ScrollOffset");
		
		Label lblOffset = new Label(parent, SWT.NONE);
		lblOffset.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblOffset.setText("Offset");
		
		txtOffset = new Text(parent, SWT.RIGHT);
		txtOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnEnableDuringMousedown = new Button(parent, SWT.CHECK);
		btnEnableDuringMousedown.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnEnableDuringMousedown.setText("Enable during mousedown");
		
		updateValuesFromPreferences();
		
		return null;
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		
		btnEnableScrolloffset.setSelection(true);
		btnEnableDuringMousedown.setSelection(false);
		txtOffset.setText("10");
	}
	
	private void updateValuesFromPreferences() {
		btnEnableScrolloffset.setSelection(preferences.isEnabled());
		btnEnableDuringMousedown.setSelection(preferences.isEnabledDuringMousedown());
		txtOffset.setText(Integer.toString(preferences.getOffset()));
	}
}
