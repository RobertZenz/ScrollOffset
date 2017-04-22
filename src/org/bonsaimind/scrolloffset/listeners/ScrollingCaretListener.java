/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.scrolloffset.listeners;

import org.bonsaimind.scrolloffset.scrolling.Scroller;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

/**
 * The {@link ScrollingCaretListener} is the main class which does scroll
 * according to the caret position.
 */
public class ScrollingCaretListener implements CaretListener, MouseListener {
	private boolean mouseDown = false;
	private ITextViewer textViewer = null;
	
	/**
	 * Creates a new instance of {@link ScrollingCaretListener}.
	 * 
	 * @param textViewer The "parent" {@link ITextViewer}.
	 */
	public ScrollingCaretListener(ITextViewer textViewer) {
		super();
		
		this.textViewer = textViewer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void caretMoved(CaretEvent event) {
		// Display.getDefault().asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		System.out.println("move");
		Scroller.scroll(textViewer, mouseDown);
		// }
		// });
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Not needed.
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		mouseDown = true;
	}
	
	//
	// @Override
	// public void mouseEnter(MouseEvent e) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void mouseExit(MouseEvent e) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void mouseHover(MouseEvent e) {
	// // TODO Auto-generated method stub
	// System.out.println(e.button);
	// }
	//
	@Override
	public void mouseUp(MouseEvent e) {
		mouseDown = false;
		
		Scroller.scroll(textViewer, mouseDown);
	}
	
	// @Override
	// public void mouseMove(MouseEvent e) {
	// // TODO Auto-generated method stub
	// System.out.println(e.stateMask);
	// }
}
