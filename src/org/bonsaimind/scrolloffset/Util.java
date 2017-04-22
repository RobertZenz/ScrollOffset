/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * Provides static utility functions.
 */
public final class Util {
	/**
	 * The cached {@link Method} {@code getSourceViewer} of
	 * {@link AbstractTextEditor}.
	 */
	private static Method getSourceViewerMethod = null;
	
	/**
	 * No instancing.
	 */
	private Util() {
		// No instancing.
	}
	
	/**
	 * Gets the {@link ITextViewer} from the given {@link AbstractTextEditor}.
	 * 
	 * @param textEditor The {@link AbstractTextEditor} from which to get the
	 *        {@link ITextViewer}.
	 * @return The {@link ITextViewer}. {@code null} if it could not be
	 *         returned.
	 */
	public static final ITextViewer getTextViewer(AbstractTextEditor textEditor) {
		if (textEditor == null) {
			return null;
		}
		
		if (getSourceViewerMethod == null) {
			if (!initGetSourceViewerMethod()) {
				return null;
			}
		}
		
		try {
			Object returnedValue = getSourceViewerMethod.invoke(textEditor);
			
			if (returnedValue instanceof ITextViewerExtension2) {
				return (ITextViewer)returnedValue;
			}
		} catch (Throwable th) {
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, th.getMessage()));
		}
		
		return null;
	}
	
	/**
	 * Initializes the {@link #getSourceViewerMethod}.
	 * 
	 * @return {@code true} if it was successfully initialized.
	 */
	private static final boolean initGetSourceViewerMethod() {
		try {
			Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
			method.setAccessible(true);
			
			getSourceViewerMethod = method;
			
			return true;
		} catch (Throwable th) {
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, th.getMessage()));
		}
		
		return false;
	}
}
