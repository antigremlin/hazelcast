/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map.operation;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.OperationFactory;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MapGetAllOperationFactory implements OperationFactory {

    String name;
    Set<Data> keys = new HashSet<Data>();


    public MapGetAllOperationFactory() {
    }

    public MapGetAllOperationFactory(String name, Set<Data> keys) {
        this.name = name;
        this.keys = keys;
    }

    @Override
    public Operation createOperation() {
        return new GetAllOperation(name, keys);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(keys.size());
        for (Data key : keys) {
            key.writeData(out);
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        name = in.readUTF();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Data data = new Data();
            data.readData(in);
            keys.add(data);
        }
    }
}
