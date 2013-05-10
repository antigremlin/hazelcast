/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.collection;

import com.hazelcast.config.Config;
import com.hazelcast.config.MultiMapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.TransactionalList;
import com.hazelcast.core.TransactionalMultiMap;
import com.hazelcast.instance.StaticNodeFactory;
import com.hazelcast.transaction.TransactionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @ali 4/5/13
 */
@RunWith(com.hazelcast.util.RandomBlockJUnit4ClassRunner.class)
public class TxnMultiMapTest {
    @BeforeClass
    public static void init() {
//        System.setProperty("hazelcast.test.use.network","true");
    }

    @Before
    @After
    public void cleanup() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void testPutRemove(){
        Config config = new Config();
        final String name = "defMM";
        config.getMultiMapConfig(name).setValueCollectionType(MultiMapConfig.ValueCollectionType.SET);

        final int insCount = 4;
        final HazelcastInstance[] instances = StaticNodeFactory.newInstances(config, insCount);
        TransactionContext context = instances[0].newTransactionContext();
        try {
            context.beginTransaction();
            TransactionalMultiMap mm = context.getMultiMap(name);
            assertEquals(0, mm.get("key1").size());
            assertEquals(0, mm.valueCount("key1"));
            assertTrue(mm.put("key1","value1"));
            assertFalse(mm.put("key1", "value1"));
            assertEquals(1, mm.get("key1").size());
            assertEquals(1, mm.valueCount("key1"));
            assertFalse(mm.remove("key1","value2"));
            assertTrue(mm.remove("key1","value1"));

            assertFalse(mm.remove("key2","value2"));
            context.commitTransaction();
        } catch (Exception e){
            fail(e.getMessage());
            context.rollbackTransaction();
        }

        assertEquals(0, instances[1].getMultiMap(name).size());
        assertTrue(instances[2].getMultiMap(name).put("key1","value1"));
        assertTrue(instances[2].getMultiMap(name).put("key2","value2"));
    }

    @Test
    public void testPutRemoveList(){
        Config config = new Config();
        final String name = "defList";

        final int insCount = 4;
        final HazelcastInstance[] instances = StaticNodeFactory.newInstances(config, insCount);
        TransactionContext context = instances[0].newTransactionContext();
        try {
            context.beginTransaction();

            TransactionalList mm = context.getList(name);
            assertEquals(0, mm.size());
            assertTrue(mm.add("value1"));
            assertTrue(mm.add("value1"));
            assertEquals(2, mm.size());
            assertFalse(mm.remove("value2"));
            assertTrue(mm.remove("value1"));

            context.commitTransaction();
        } catch (Exception e){
            fail(e.getMessage());
            context.rollbackTransaction();
        }

        assertEquals(1, instances[1].getList(name).size());
        assertTrue(instances[2].getList(name).add("value1"));
    }
}