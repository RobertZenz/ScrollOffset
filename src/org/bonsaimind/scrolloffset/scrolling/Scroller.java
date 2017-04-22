/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset.scrolling;

import org.bonsaimind.scrolloffset.Activator;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;

/**
 * {@link Scroller} is the main class which knows how to scroll.
 */
public class Scroller {
	/** If the scrolling offset is enabled. */
	private static boolean enabled = true;
	
	/** The offset in lines. */
	private static int offset = 10;
	
	static {
		updateFromPreferences();
	}
	
	/**
	 * No instances, static utility.
	 */
	private Scroller() {
		// Nothing to do, no instancing.
	}
	
	/**
	 * Performs the scroll.
	 * 
	 * @param textViewer The {@link ITextViewer} to scroll.
	 * @param mouseDown If the mouse is currently pressed.
	 */
	public static final void scroll(ITextViewer textViewer, boolean mouseDown) {
		if (!enabled) {
			return;
		}
		
		if (mouseDown) {
			return;
		}
		
		StyledText styledText = textViewer.getTextWidget();
		
		int visibleLineCount = textViewer.getBottomIndex() - textViewer.getTopIndex();
		int offsetToUse = Math.min(offset, visibleLineCount / 2);
		int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
		
		if (currentLine >= offsetToUse && currentLine <= styledText.getLineCount() - offsetToUse) {
			if (currentLine <= (textViewer.getTopIndex() + offsetToUse)) {
				textViewer.setTopIndex(currentLine - offsetToUse);
			} else if (currentLine >= (textViewer.getBottomIndex() - offsetToUse)) {
				textViewer.setTopIndex(currentLine + offsetToUse - visibleLineCount);
			}
		}
	}
	
	/**
	 * Updates to the new preferences.
	 */
	public static void updateFromPreferences() {
		enabled = Activator.getDefault().getPreferences().isEnabled();
		offset = Activator.getDefault().getPreferences().getOffset();
	}
}
