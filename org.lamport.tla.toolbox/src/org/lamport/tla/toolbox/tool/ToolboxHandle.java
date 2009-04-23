package org.lamport.tla.toolbox.tool;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.lamport.tla.toolbox.Activator;
import org.lamport.tla.toolbox.spec.Spec;
import org.lamport.tla.toolbox.util.pref.IPreferenceConstants;
import org.lamport.tla.toolbox.util.pref.PreferenceStoreHelper;
import org.osgi.framework.Bundle;

import tla2sany.modanalyzer.SpecObj;

/**
 * Provides shortcuts to the internal toolbox methods, that should be made accessible to the other tools
 * @author Simon Zambrovski
 * @version $Id$
 */
public class ToolboxHandle
{
    public static String I_RESTORE_LAST_SPEC = IPreferenceConstants.I_RESTORE_LAST_SPEC;

    /**
     * Retrieves the root file of the loaded spec or <code>null</code> if no spec selected
     */
    public static IFile getRootModule()
    {
        return ((Activator.getSpecManager().getSpecLoaded() == null) ? null : Activator.getSpecManager()
                .getSpecLoaded().getRootFile());
    }

    /**
     * Returns the instance preference store
     */
    public static IPreferenceStore getInstanceStore()
    {
        return PreferenceStoreHelper.getInstancePreferenceStore();
    }

    /**
     * Returns the SpecObj of the current loaded specification
     * @return SpecObj produced by the previous SANY run, or null
     */
    public static SpecObj getSpecObj()
    {
        Spec spec = null;
        try
        {
            spec = Activator.getSpecManager().getSpecLoaded();
        } catch (IllegalStateException e)
        {
            // this happens is the workspace is closed
            // just return null for e.G. JUnit tests
        }
        if (spec != null)
        {
            return spec.getValidRootModule();
        }
        return null;
    }

    /**
     * @return
     */
    public static Spec getCurrentSpec()
    {
        return Activator.getSpecManager().getSpecLoaded();
    }

    /**
     * @param name
     * @return
     */
    public static boolean isUserModule(String name)
    {
        if (name == null || name.isEmpty())
        {
            return false;
        }
        return Activator.getModuleDependencyStorage().hasModule(name);
    }

    /**
     * @return
     */
    public static IPath getTLAToolsClasspath()
    {
        IPath bundleBase = getBundleLocation();
        
        if (bundleBase != null)
        {
            IPath tlaToolsLocation = bundleBase.append("tla2tools.jar"); //$NON-NLS-1$
            return tlaToolsLocation;
        }
        return null;
    }

    /**
     * 
     */
    public static IPath getModulesClasspath()
    {
        IPath bundleBase = getBundleLocation();
        
        if (bundleBase != null)
        {
            IPath tlaToolsLocation = bundleBase.append("StandardModules/"); //$NON-NLS-1$
            return tlaToolsLocation;
        }
        return null;
    }

    
    public static IPath getBundleLocation()
    {
        Bundle bundle = Activator.getDefault().getBundle();
        if (bundle == null)
            return null;

        URL local = null;
        try
        {
            local = FileLocator.toFileURL(bundle.getEntry("/")); //$NON-NLS-1$
        } catch (IOException e)
        {
            return null;
        }
        String fullPath = new File(local.getPath()).getAbsolutePath();
        return Path.fromOSString(fullPath);
    }

}
