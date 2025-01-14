/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.paimon.trino;

import io.airlift.slice.Slice;
import io.trino.filesystem.TrinoInput;
import io.trino.filesystem.TrinoInputFile;
import io.trino.orc.AbstractOrcDataSource;
import io.trino.orc.OrcDataSourceId;
import io.trino.orc.OrcReader;
import io.trino.orc.OrcReaderOptions;

import java.io.IOException;

/** Data source to construct {@link OrcReader}. */
public class TrinoOrcDataSource extends AbstractOrcDataSource {

    private final TrinoInput input;

    public TrinoOrcDataSource(TrinoInputFile file, OrcReaderOptions options) throws IOException {
        super(new OrcDataSourceId(file.location().toString()), file.length(), options);
        this.input = file.newInput();
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    protected Slice readTailInternal(int length) throws IOException {
        return input.readTail(length);
    }

    @Override
    protected void readInternal(long position, byte[] buffer, int bufferOffset, int bufferLength)
            throws IOException {
        input.readFully(position, buffer, bufferOffset, bufferLength);
    }
}
