/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.common.AndroidConstants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

/**
 * A {@link ILabelDecorator} associated with an org.eclipse.ui.decorators extension.
 * This is used to add android icons in some special folders in the package explorer.
 */
public class FolderDecorator implements ILightweightLabelDecorator {
    
    private ImageDescriptor mDescriptor;

    public FolderDecorator() {
        mDescriptor = AdtPlugin.getImageDescriptor("/icons/android_project.png");
    }

    public void decorate(Object element, IDecoration decoration) {
        if (element instanceof IFolder) {
            IFolder folder = (IFolder)element;
            
            // get the project and make sure this is an android project
            IProject project = folder.getProject();

            try {
                if (project.hasNature(AndroidConstants.NATURE)) {
                    // check the folder is directly under the project.
                    if (folder.getParent().getType() == IResource.PROJECT) {
                        String name = folder.getName();
                        if (name.equals(AndroidConstants.FD_ASSETS)) {
                            decorate(decoration, " [Android assets]");
                            decoration.addOverlay(mDescriptor, IDecoration.TOP_RIGHT);
                        } else if (name.equals(AndroidConstants.FD_RESOURCES)) {
                            decorate(decoration, " [Android resources]");
                            decoration.addOverlay(mDescriptor, IDecoration.TOP_RIGHT);
                        } else if (name.equals(AndroidConstants.FD_NATIVE_LIBS)) {
                            decorate(decoration, " [Native Libraries]");
                        }
                    }
                }
            } catch (CoreException e) {
                // log the error
                AdtPlugin.log(e, "Unable to get nature of project '%s'.", project.getName());
            }
        }
    }
    
    public void decorate(IDecoration decoration, String suffix) {
        decoration.addOverlay(mDescriptor, IDecoration.TOP_RIGHT);

        // this is broken as it changes the color of the whole text, not only of the decoration.
        // TODO: figure out how to change the color of the decoration only.
//        decoration.addSuffix(suffix);
//        ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
//        ColorRegistry registry = theme.getColorRegistry();
//        decoration.setForegroundColor(registry.get("org.eclipse.jdt.ui.ColoredLabels.decorations"));

    }

    public boolean isLabelProperty(Object element, String property) {
        // at this time return false.
        return false;
    }

    public void addListener(ILabelProviderListener listener) {
        // No state change will affect the rendering.
    }



    public void removeListener(ILabelProviderListener listener) {
        // No state change will affect the rendering.
    }

    public void dispose() {
        // nothind to dispose
    }
}
