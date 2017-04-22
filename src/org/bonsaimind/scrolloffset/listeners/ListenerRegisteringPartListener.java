/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset.listeners;

import org.bonsaimind.scrolloffset.Util;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * The {@link ListenerRegisteringPartListener} is adding listeners to all parts.
 */
public class ListenerRegisteringPartListener implements IPartListener2 {
	/** The shared instance which should be used whenever possible. */
	public static final ListenerRegisteringPartListener INSTANCE = new ListenerRegisteringPartListener();
	
	/**
	 * Creates a new instance of {@link ListenerRegisteringPartListener}.
	 */
	public ListenerRegisteringPartListener() {
		super();
	}
	
	/**
	 * Registers the listeners on the given {@link IWorkbenchPartReference}.
	 * 
	 * @param partRef The {@link IWorkbenchPartReference} on which to register
	 *        the listeners.
	 */
	public static void registerListener(IWorkbenchPartReference partRef) {
		if (partRef != null) {
			IWorkbenchPart part = partRef.getPart(false);
			
			if (part instanceof AbstractTextEditor) {
				AbstractTextEditor textEditor = (AbstractTextEditor)part;
				ITextViewer textViewer = Util.getTextViewer(textEditor);
				
				if (textViewer != null) {
					registerListenersBefore(textViewer);
				}
			}
		}
	}
	
	/**
	 * Registers a caret and mouse listener on the given {@link ITextViewer},
	 * but adds the mouse listener before those of the widget.
	 * 
	 * @param textViewer The {@link ITextViewer} on which to register the
	 *        listeners.
	 */
	private static void registerListenersBefore(ITextViewer textViewer) {
		ScrollingCaretListener scrollingCaretListener = new ScrollingCaretListener(textViewer);
		
		StyledText styledText = textViewer.getTextWidget();
		
		// We want to know when the mouse button is down, however, obviously the
		// widget has its listeners registered before us. That means that we
		// would get the caret moved event first and the mouse event second,
		// which is not exactly good for our use case.
		//
		// So we remove the listeners of the widget first, add our own and then
		// we will re-add the formerly removed listeners.
		Listener[] mouseDownListeners = styledText.getListeners(SWT.MouseDown);
		Listener[] mouseUpListeners = styledText.getListeners(SWT.MouseUp);
		
		for (Listener listener : mouseDownListeners) {
			styledText.removeListener(SWT.MouseDown, listener);
		}
		for (Listener listener : mouseUpListeners) {
			styledText.removeListener(SWT.MouseUp, listener);
		}
		
		styledText.addCaretListener(scrollingCaretListener);
		styledText.addMouseListener(scrollingCaretListener);
		
		for (Listener listener : mouseDownListeners) {
			styledText.addListener(SWT.MouseDown, listener);
		}
		for (Listener listener : mouseUpListeners) {
			styledText.addListener(SWT.MouseUp, listener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		registerListener(partRef);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
}
