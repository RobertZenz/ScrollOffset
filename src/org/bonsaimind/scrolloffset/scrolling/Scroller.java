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
import org.bonsaimind.scrolloffset.preferences.Preferences;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

/**
 * {@link Scroller} is the main class which knows how to scroll.
 */
public class Scroller {
	/** If the scrolling offset is enabled. */
	private static boolean enabled = true;
	
	/** If the scrolling offset is enabled during mousedown. */
	private static boolean enabledDuringMousedown = false;
	
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
	 * @param foldingTextViewer The {@link ITextViewerExtension5} to scroll.
	 * @param mouseDown If the mouse is currently pressed.
	 */
	public static final void scroll(ITextViewer textViewer, ITextViewerExtension5 foldingTextViewer, boolean mouseDown) {
		if (!enabled) {
			return;
		}
		
		if (!enabledDuringMousedown && mouseDown) {
			return;
		}
		
		StyledText styledText = textViewer.getTextWidget();
		
		int topLineIndex = textViewer.getTopIndex();
		int bottomLineIndex = textViewer.getBottomIndex();
		
		if (foldingTextViewer != null) {
			topLineIndex = foldingTextViewer.modelLine2WidgetLine(topLineIndex);
			bottomLineIndex = foldingTextViewer.modelLine2WidgetLine(bottomLineIndex);
		}
		
		int visibleLineCount = bottomLineIndex - topLineIndex;
		
		int offsetToUse = Math.min(offset, visibleLineCount / 2);
		int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
		
		int lastLineIndex = foldingTextViewer.modelLine2WidgetLine(textViewer.getDocument().getNumberOfLines() - 1);
		
		if (currentLine >= offsetToUse && currentLine <= lastLineIndex - offsetToUse) {
			int newTopLineIndex = topLineIndex;
			
			if (currentLine <= (topLineIndex + offsetToUse)) {
				newTopLineIndex = currentLine - offsetToUse;
			} else if (currentLine >= (bottomLineIndex - offsetToUse)) {
				newTopLineIndex = currentLine + offsetToUse - visibleLineCount;
			}
			
			if (foldingTextViewer != null) {
				newTopLineIndex = foldingTextViewer.widgetLine2ModelLine(newTopLineIndex);
			}
			
			if (newTopLineIndex != currentLine) {
				textViewer.setTopIndex(newTopLineIndex);
			}
		}
	}
	
	/**
	 * Moves the caret out of the "scroll zones".
	 * 
	 * @param textViewer The {@link ITextViewer} to move the caret.
	 * @param foldingTextViewer The {@link ITextViewerExtension5} to move the
	 *        caret.
	 * @param pageUpDown Whether the Page Up or Down key was pressed.
	 */
	public static void moveCaret(ITextViewer textViewer, ITextViewerExtension5 foldingTextViewer, int pageUpDown) {
		StyledText styledText = textViewer.getTextWidget();
		
		int topLineIndex = textViewer.getTopIndex();
		int bottomLineIndex = textViewer.getBottomIndex();
		
		if (foldingTextViewer != null) {
			topLineIndex = foldingTextViewer.modelLine2WidgetLine(topLineIndex);
			bottomLineIndex = foldingTextViewer.modelLine2WidgetLine(bottomLineIndex);
		}
		
		int visibleLineCount = bottomLineIndex - topLineIndex;
		
		int offsetToUse = Math.min(offset, visibleLineCount / 2);
		int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
		int currentLineOffset = styledText.getCaretOffset() - styledText.getOffsetAtLine(currentLine);
		
		int lastLineIndex = foldingTextViewer.modelLine2WidgetLine(textViewer.getDocument().getNumberOfLines() - 1);
		
		if (currentLine >= offsetToUse && currentLine <= lastLineIndex - offsetToUse) {
			int newCaretLineIndex = currentLine;
			
			if (currentLine <= topLineIndex + offsetToUse) {
				if (pageUpDown == SWT.PAGE_UP) {
					newCaretLineIndex = topLineIndex + offsetToUse;
				} else {
					newCaretLineIndex = bottomLineIndex - offsetToUse;
				}
			} else if (currentLine >= bottomLineIndex - offsetToUse) {
				if (pageUpDown == SWT.PAGE_DOWN) {
					newCaretLineIndex = bottomLineIndex - offsetToUse;
				} else {
					newCaretLineIndex = topLineIndex + offsetToUse;
				}
			}
			
			if (newCaretLineIndex != currentLine) {
				int newCaretOffset = styledText.getOffsetAtLine(newCaretLineIndex);
				int newCaretLineLength = styledText.getOffsetAtLine(newCaretLineIndex + 1) - newCaretOffset;
				
				if (newCaretLineLength > currentLineOffset) {
					newCaretOffset = newCaretOffset + currentLineOffset;
				} else {
					newCaretOffset = newCaretOffset + newCaretLineLength;
				}
				
				styledText.setCaretOffset(newCaretOffset);
			}
		}
	}
	
	/**
	 * Updates to the new preferences.
	 */
	public static void updateFromPreferences() {
		Preferences preferences = Activator.getDefault().getPreferences();
		
		enabled = preferences.isEnabled();
		enabledDuringMousedown = preferences.isEnabledDuringMousedown();
		offset = preferences.getOffset();
	}
}
