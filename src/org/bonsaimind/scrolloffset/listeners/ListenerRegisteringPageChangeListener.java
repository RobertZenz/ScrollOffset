/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset.listeners;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.bonsaimind.scrolloffset.Util;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * The {@link ListenerRegisteringPartListener} is adding listeners to all pages
 * as soon as they are being activated.
 */
public class ListenerRegisteringPageChangeListener implements IPageChangedListener {
	/**
	 * The {@link Set} of all pages on which listeners have already been
	 * registered.
	 */
	private Set<Object> registeredOnPages = Collections.newSetFromMap(new WeakHashMap<Object, Boolean>());
	
	/**
	 * Creates a new instance of {@link ListenerRegisteringPageChangeListener}.
	 */
	public ListenerRegisteringPageChangeListener() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pageChanged(PageChangedEvent event) {
		registerListener(event.getSelectedPage());
	}
	
	/**
	 * Registers a new listener on the given page, if no previous listener has
	 * been registered.
	 * 
	 * @param page The page on which to, possibly, register a listener.
	 * @return This instance.
	 */
	public ListenerRegisteringPageChangeListener registerListener(Object page) {
		if (!registeredOnPages.contains(page)) {
			registeredOnPages.add(page);
			
			if (page instanceof AbstractTextEditor) {
				AbstractTextEditor textEditor = (AbstractTextEditor)page;
				
				ITextViewer textViewer = Util.getTextViewer(textEditor);
				
				if (textViewer != null) {
					Util.registerScrollListeners(textViewer);
				}
			}
		}
		
		return this;
	}
}
