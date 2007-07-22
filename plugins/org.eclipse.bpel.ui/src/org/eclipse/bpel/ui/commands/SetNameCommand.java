/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.commands;

import org.eclipse.bpel.model.ExtensibleElement;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.adapters.INamedElement;
import org.eclipse.bpel.ui.uiextensionmodel.StartNode;
import org.eclipse.bpel.ui.util.BPELEditorUtil;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.w3c.dom.Element;


/** 
 * Sets the "name" property of a model object.  This is done using the
 * INamedElement adapter interface, which is supported for Activity and
 * Process objects.
 */
public class SetNameCommand extends SetCommand {
	
	private class MyBPELWriter extends  org.eclipse.bpel.model.resource.BPELWriter {
	}

	public String getDefaultLabel() { return IBPELUIConstants.CMD_EDIT_NAME; }

	public SetNameCommand(EObject target, String newName)  {
		super(target, newName);
	}

	public boolean canDoExecute() {
		if (BPELUtil.adapt(target, INamedElement.class) == null) return false;
		return super.canDoExecute();
	}

	public Object get() {
		INamedElement namedElement = (INamedElement)BPELUtil.adapt(target, INamedElement.class);
		return namedElement.getName(target);
	}
	public void set(Object o) {
		INamedElement namedElement = (INamedElement)BPELUtil.adapt(target, INamedElement.class);
		namedElement.setName(target, (String)o);
		
		Element element = BPELEditorUtil.getInstance().getElementForObject(target);
	    if (element != null) { 
	    	element.setAttribute("name", (String)o); //$NON-NLS-1$
	    }
	}
}
