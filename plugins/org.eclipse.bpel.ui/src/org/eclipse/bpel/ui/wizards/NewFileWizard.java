/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation
 *******************************************************************************/


package org.eclipse.bpel.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;


/**
 * Wizard for the new process template. 
 *  
 * @author Michal Chmielewski (michal.chmielewski@oracle.com) 
 *
 */

public class NewFileWizard extends Wizard implements INewWizard {

	/** The id of our BPEL editor */
	static protected final String BPEL_EDITOR_ID = "org.eclipse.bpel.ui.bpeleditor"; //$NON-NLS-1$
		
	/** The container where the file(s) will be created */
	private IContainer mContainer ;
		
	private IWorkbench fWorkbench;
	
	/** The page of the wizard */
	private NewFileWizardPage1 fMainPage;

		
	/**
	 * Create a brand new shining Create Project Wizard for BPEL.
	 */

	public NewFileWizard() {
		
		setNeedsProgressMonitor(true);
		setDialogSettings(BPELUIPlugin.getPlugin().getDialogSettingsFor(this));		
		setHelpAvailable(false);
		
	}

	/**
	 * Initialize the wizard object through the normal eclipse initialization
	 * mechanism.
	 * 
	 * @param workbench
	 *            the workbench reference
	 * @param currentSelection
	 *            the current selection reference
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fWorkbench = workbench;
		mContainer = getClosestContainer( currentSelection.getFirstElement() );					
	}

	
	protected void selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, fWorkbench.getActiveWorkbenchWindow());
	}

	
	protected void openResource(final IFile resource) {
		if (resource.getType() != IResource.FILE) {
			return;
		}

		IWorkbenchWindow window = fWorkbench.getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}

		final IWorkbenchPage activePage = window.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					try {						
						IDE.openEditor(activePage, resource, BPEL_EDITOR_ID, true);
						// IDE.openEditor(activePage, resource,  true);
					} catch (PartInitException e) {
						BPELUIPlugin.log(e);
					}
				}
			});
			BasicNewResourceWizard.selectAndReveal(resource, activePage
					.getWorkbenchWindow());
		}
	}

	
	/**
	 * Perform cancel. Close the wizard and don't do anything else.
	 * 
	 * @return true if canceled
	 */
	public boolean performCancel() {
		return super.performCancel();
	}

	/**
	 * Add pages to the Open project wizard.
	 * 
	 */

	public void addPages() {
		super.addPages();
		fMainPage = new NewFileWizardPage1(Messages.NewFileWizard_1);
		addPage(fMainPage);
	}

	/**
	 * Perform the finish operation of the create project wizard.
	 * 
	 * @return true on success, false on failure
	 * 
	 * @see Wizard#performFinish
	 */

	public boolean performFinish() {

		BPELCreateOperation runnable = new BPELCreateOperation();
		runnable.setContainer( mContainer );
		runnable.setTemplate( fMainPage.getSelectedTemplate () );
		runnable.setArgs( fMainPage.getArgs () ) ;
		
		try {
			getContainer().run(false, true, runnable);			
		} catch (InvocationTargetException e) {
			BPELUIPlugin.log(e);
			return false;
		} catch (InterruptedException e) {
			BPELUIPlugin.log(e);
			return false;
		}

		IFile res = (IFile) runnable.getElementToOpen();
		if (res != null) {
			openResource(res);
		}
		return true;
	}

	
	/**
	 * Return the closest container in which we can generate 
	 * process from the template. 
	 * 
	 * @return the closest IContainer
	 */
	
	IContainer getClosestContainer ( Object obj ) {
		
		if (obj == null) {
			return null;
		}
		if (obj instanceof IFile) {
			IFile file = (IFile) obj;
			return file.getParent();
		}
		if (obj instanceof IContainer) {
			return (IContainer) obj;
		}
		return null;		
	}
	
	
}
