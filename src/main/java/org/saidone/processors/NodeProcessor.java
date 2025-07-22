/*
 *  Alfresco Node Processor - Do things with nodes
 *  Copyright (C) 2023-2025 Saidone
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.saidone.processors;

import org.saidone.model.config.ProcessorConfig;

import java.util.concurrent.CompletableFuture;

/**
 * Component that performs an operation on nodes retrieved from the queue.
 */
public interface NodeProcessor {

    /**
     * Start processing asynchronously.
     *
     * @param config processor configuration
     * @return future representing the asynchronous task
     */
    CompletableFuture<Void> process(ProcessorConfig config);

    /**
     * Process a single node.
     *
     * @param nodeId id of the node
     * @param config processor configuration
     */
    void processNode(String nodeId, ProcessorConfig config) throws Exception;

}
