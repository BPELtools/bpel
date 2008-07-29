/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.bpel.xpath10;

public class AllNodeStep extends Step {
	
	public AllNodeStep (int axis) {
		super(axis);
	}
	protected String asString () {
		return "node()";
	}
	
	protected String asText () {
		return asString();
	}
	
}
