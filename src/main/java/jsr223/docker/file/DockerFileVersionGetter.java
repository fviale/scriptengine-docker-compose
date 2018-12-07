/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package jsr223.docker.file;

import java.io.IOException;
import java.io.StringWriter;

import jsr223.docker.file.DockerFilePropertyLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;


@Log4j
@RequiredArgsConstructor
public class DockerFileVersionGetter {

    @NonNull
    private ProcessBuilderUtilities processBuilderUtilities;

    /**
     * Retrieves the docker version.
     *
     * @return The currently installed version return by the docker command or an empty string
     * the version could not be determined.
     */
    public String getDockerFileVersion(ProcessBuilderFactory factory) {
        if (factory == null) {
            return "";
        }

        String result = ""; // Empty string for empty result if version recovery fails

        ProcessBuilder processBuilder = factory.getProcessBuilder(DockerFilePropertyLoader.getInstance()
                                                                                          .getDockerFileCommand(),
                                                                  "--version");

        try {
            Process process = processBuilder.start();

            // Attach stream to std output of process
            StringWriter commandOutput = new StringWriter();
            processBuilderUtilities.attachStreamsToProcess(process, commandOutput, null, null);

            // Wait for process to exit
            process.waitFor();

            // Extract output
            result = commandOutput.toString();
        } catch (IOException | InterruptedException | IndexOutOfBoundsException e) {
            log.debug("Failed to retrieve docker file version.", e);
        }
        return result;
    }

}
