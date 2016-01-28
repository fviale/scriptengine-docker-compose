/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package jsr223.docker.compose.bindings;

import java.util.Map;

import javax.script.Bindings;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class StringBindingsAdder {

    @NonNull
    private MapBindingsAdder mapBindingsAdder;

    /**
     * Adds all bindings which are from type @String to the environment map. All other bindings are printed
     * with toString() to log file.
     *
     * @param bindings    Bindings which will be read and added to environment. If null,
     *                    this method will return immediately.
     * @param environment Map<String,String> which will get all Entry<String,String> from the @Bindings. If null,
     *                    this method will return immediately.
     */
    public void addBindingToStringMap(Bindings bindings, Map<String, String> environment) {
        if (bindings == null || environment == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : bindings.entrySet()) {
            if (hasKeyAndValue(entry) && entry.getValue() instanceof String) {
                addEntryToEnvironmentWhichIsAPureString(environment, entry);
            } else { // Go through maps and add String values to the environment map.
                mapBindingsAdder.addEntryToEnvironmentOtherThanPureStrings(environment, entry);
            }
        }
    }

    private void addEntryToEnvironmentWhichIsAPureString(Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        if (environmentAndEntryExist(environment,
                entry) && hasKeyAndValue(entry)) {
            environment.put(entry.getKey(), (String) entry.getValue());
            log.debug("Added binding: " + entry.getKey() + ":" + entry.getValue().toString());
        }
    }

    private boolean hasKeyAndValue(Map.Entry<String, Object> entry) {
        return entry.getKey() != null && entry.getValue() != null;
    }

    private boolean environmentAndEntryExist(Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        return environment != null && entry != null;
    }
}
