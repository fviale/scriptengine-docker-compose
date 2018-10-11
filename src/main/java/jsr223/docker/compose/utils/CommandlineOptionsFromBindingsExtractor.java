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
package jsr223.docker.compose.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;

import jsr223.docker.compose.utils.CommandlineOptionsFromBindingsExtractor.OptionType;
import lombok.extern.log4j.Log4j;


/**
 * @author ActiveEon Team
 * @since 01/02/2018
 */
@Log4j
public class CommandlineOptionsFromBindingsExtractor {

    public final static String GENERIC_INFORMATION_KEY = "genericInformation";

    public final static String DOCKER_COMPOSE_UP_COMMANDLINE_OPTIONS_KEY = "docker-compose-up-options";

    public final static String DOCKER_COMPOSE_COMMANDLINE_OPTIONS_KEY = "docker-compose-options";

    public final static String DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY = "docker-compose-options-split-regex";

    public final static String DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_DEFAULT = " ";

    public final static String DOCKER_FILE_COMMANDLINE_OPTIONS_SPLIT_REGEX_DEFAULT = " ";

    public final static String DOCKER_FILE_COMMANDLINE_OPTIONS_KEY = "docker-file-options";

    public final static String DOCKER_FILE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY = "docker-file-options-split-regex";

    public enum OptionType {
        UP_OPTION,
        GENERAL_OPTION
    }

    public Map<OptionType, List<String>> getDockerComposeCommandOptions(Bindings bindings) {
        if (!bindings.containsKey(GENERIC_INFORMATION_KEY)) {
            return Collections.emptyMap();
        }

        Object bindingsObject = bindings.get(GENERIC_INFORMATION_KEY);
        if (bindingsObject instanceof Map) {
            Map<String, String> genericInformation = (Map<String, String>) bindings.get(GENERIC_INFORMATION_KEY);
            return extractDockerComposeUpCommandOptionsFromMap(genericInformation);
        } else {
            log.warn("Generic Information could not be retrieved. Docker command options could not be extracted.");
            return Collections.emptyMap();
        }

    }

    private Map<OptionType, List<String>>
            extractDockerComposeUpCommandOptionsFromMap(Map<String, String> genericInformationMap) {
        List<String> upCmdOptions = Collections.emptyList();
        List<String> generalCmdOptions = Collections.emptyList();
        String splitCharacter = DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_DEFAULT;
        if (genericInformationMap.get(DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY) != null) {
            splitCharacter = genericInformationMap.get(DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY);
        }

        if (genericInformationMap.get(DOCKER_COMPOSE_UP_COMMANDLINE_OPTIONS_KEY) != null) {
            upCmdOptions = Arrays.asList(genericInformationMap.get(DOCKER_COMPOSE_UP_COMMANDLINE_OPTIONS_KEY)
                                                              .split(splitCharacter));
        }

        if (genericInformationMap.get(DOCKER_COMPOSE_COMMANDLINE_OPTIONS_KEY) != null) {
            generalCmdOptions = Arrays.asList(genericInformationMap.get(DOCKER_COMPOSE_COMMANDLINE_OPTIONS_KEY)
                                                                   .split(splitCharacter));
        }

        EnumMap<OptionType, List<String>> options = new EnumMap<OptionType, List<String>>(OptionType.class);
        options.put(OptionType.GENERAL_OPTION, generalCmdOptions);
        options.put(OptionType.UP_OPTION, upCmdOptions);
        return options;
    }

    public Map<OptionType, List<String>> getDockerFileCommandOptions(Bindings bindings) {
        if (!bindings.containsKey(GENERIC_INFORMATION_KEY)) {
            return Collections.emptyMap();
        }

        Object bindingsObject = bindings.get(GENERIC_INFORMATION_KEY);
        if (bindingsObject instanceof Map) {
            Map<String, String> genericInformation = (Map<String, String>) bindings.get(GENERIC_INFORMATION_KEY);
            return extractDockerFileUpCommandOptionsFromMap(genericInformation);
        } else {
            log.warn("Generic Information could not be retrieved. Docker command options could not be extracted.");
            return Collections.emptyMap();
        }
    }

    private Map<OptionType, List<String>>
            extractDockerFileUpCommandOptionsFromMap(Map<String, String> genericInformationMap) {
        List<String> upCmdOptions = Collections.emptyList();
        List<String> generalCmdOptions = Collections.emptyList();
        String splitCharacter = DOCKER_FILE_COMMANDLINE_OPTIONS_SPLIT_REGEX_DEFAULT;
        if (genericInformationMap.get(DOCKER_FILE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY) != null) {
            splitCharacter = genericInformationMap.get(DOCKER_FILE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY);
        }

        if (genericInformationMap.get(DOCKER_FILE_COMMANDLINE_OPTIONS_KEY) != null) {
            generalCmdOptions = Arrays.asList(genericInformationMap.get(DOCKER_FILE_COMMANDLINE_OPTIONS_KEY)
                                                                   .split(splitCharacter));
        }

        EnumMap<OptionType, List<String>> options = new EnumMap<OptionType, List<String>>(OptionType.class);
        options.put(OptionType.GENERAL_OPTION, generalCmdOptions);
        options.put(OptionType.UP_OPTION, upCmdOptions);
        return options;
    }

}
