/*
 * This file is licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.basepom.m2e.axis2;

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractSourcesGenerationProjectConfigurator;
import org.sonatype.plexus.build.incremental.BuildContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Project configurator class for the Axistools code generator plugin.
 */
public class Axis2WSDL2CodeProjectConfigurator extends
        AbstractSourcesGenerationProjectConfigurator
{
    @Override
    public AbstractBuildParticipant getBuildParticipant(
        final IMavenProjectFacade projectFacade,
        final MojoExecution execution,
        final IPluginExecutionMetadata executionMetadata)
    {
        return new Axis2WSDL2CodeBuildParticipant(execution);
    }

    @Override
    public File[] getSourceFolders(final ProjectConfigurationRequest request,final MojoExecution mojoExecution, final IProgressMonitor monitor) throws CoreException
    {
        final List<File> files = new ArrayList<File>(2);
        final File generated = maven.getMojoParameterValue(request.getMavenProject(), mojoExecution, "outputDirectory", File.class, monitor);

        final File src = checkChildExists(generated, "src");
        if (src != null) {
            files.add(src);
        }

        final File resources = checkChildExists(generated, "resources");
        if (resources != null) {
            files.add(resources);
        }

        return files.toArray(new File [files.size()]);
    }

    static File checkChildExists(final File parent, final String name)
    {
        if (parent == null) {
            return null;
        }

        final File child = new File(parent, name);

        if (child.exists() && child.canRead() && child.canExecute() && child.isDirectory()) {
            return child;
        }

        return null;
    }

    public static class Axis2WSDL2CodeBuildParticipant extends
            MojoExecutionBuildParticipant
    {
        public Axis2WSDL2CodeBuildParticipant(final MojoExecution execution)
        {
            super(execution, true);
        }

        @Override
        public Set<IProject> build(final int kind, final IProgressMonitor monitor)
            throws Exception
        {
            final IMaven maven = MavenPlugin.getMaven();
            final BuildContext buildContext = getBuildContext();
            final MavenProject project = getMavenProjectFacade().getMavenProject();
            final MojoExecution mojoExecution = getMojoExecution();

            try {
                return super.build(kind, monitor);
            }
            finally {
                // tell m2e builder to refresh generated files
                final File generated = maven.getMojoParameterValue(project, mojoExecution, "outputDirectory", File.class, monitor);

                final File src = checkChildExists(generated,  "src");
                if (src != null) {
                    buildContext.refresh(src);
                }

                final File resources = checkChildExists(generated, "resources");
                if (resources != null) {
                    buildContext.refresh(resources);
                }
            }
        }
    }
}
