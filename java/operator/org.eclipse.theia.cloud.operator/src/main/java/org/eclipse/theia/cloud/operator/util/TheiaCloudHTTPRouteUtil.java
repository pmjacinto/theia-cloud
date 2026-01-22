/********************************************************************************
 * Copyright (C) 2022 EclipseSource, Lockular, Ericsson, STMicroelectronics and 
 * others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.eclipse.theia.cloud.operator.util;

import io.fabric8.kubernetes.api.model.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.theia.cloud.common.k8s.resource.appdefinition.AppDefinition;
import org.eclipse.theia.cloud.common.k8s.resource.session.Session;
import org.eclipse.theia.cloud.common.util.NamingUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TheiaCloudHTTPRouteUtil {

    private static final Logger LOGGER = LogManager.getLogger(TheiaCloudHTTPRouteUtil.class);

    public static final String PLACEHOLDER_ROUTE_NAME = "placeholder-route-name";
    public static final String PLACEHOLDER_GATEWAY_NAME = "placeholder-gateway-name";
    public static final String PLACEHOLDER_HOSTNAMES = "placeholder-hostnames";
    public static final String PLACEHOLDER_SERVICE_NAME = "placeholder-service-name";
    public static final String PLACEHOLDER_SERVICE_PORT= "placeholder-service-port";

    private TheiaCloudHTTPRouteUtil() {
    }

    public static String getHTTPRouteName(Session session) {
        return NamingUtil.createName(session);
    }

    public static Map<String, String> getHTTPRouteReplacements(AppDefinition appDefinition, Session session, Service service, List<String> hostsToAdd) {
        Map<String, String> replacements = new LinkedHashMap<>();
        replacements.put(PLACEHOLDER_ROUTE_NAME, getHTTPRouteName(session));
        replacements.put(PLACEHOLDER_GATEWAY_NAME, appDefinition.getSpec().getGatewayName());
        replacements.put(PLACEHOLDER_SERVICE_NAME, service.getMetadata().getName());
        replacements.put(PLACEHOLDER_SERVICE_PORT, String.valueOf(appDefinition.getSpec().getPort()));

        String yamlHosts = "\n" + hostsToAdd.stream().map(host -> "    - '" + host + "'\n").collect(Collectors.joining());
        replacements.put(PLACEHOLDER_HOSTNAMES, yamlHosts);

        return replacements;
    }
}
