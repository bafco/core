/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.test.util;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Simple data holder for sequence of actions identified with {@link String}.
 *
 * Always call {@link #reset()} before your test code to remove previous sequences stored in static map!
 *
 * @author Martin Kouba
 */
public final class ActionSequence {

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_.]+");

    private String name;

    /**
     * Data - list of actions
     */
    private List<String> data = Collections.synchronizedList(new ArrayList<String>());

    public ActionSequence() {
        super();
        this.name = DEFAULT_SEQUENCE;
    }

    /**
     * @param name
     */
    public ActionSequence(String name) {
        super();
        checkStringValue(name);
        this.name = name;
    }

    /**
     * @param actionId
     * @return data holder
     */
    public ActionSequence add(String actionId) {
        checkStringValue(name);
        this.data.add(actionId);
        return this;
    }

    /**
     * @return read-only copy of sequence data
     */
    public List<String> getData() {
        return Collections.unmodifiableList(new ArrayList<String>(this.data));
    }

    /**
     * @return name of sequence
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param actions
     * @return <code>true</code> if sequence data contain all of the specified actions, <code>false</code> otherwise
     */
    public boolean containsAll(String... actions) {
        return getData().containsAll(Arrays.asList(actions));
    }

    /**
     *
     * @param actions
     * @return <code>true</code> if sequence data begins with the specified actions, <code>false</code> otherwise
     */
    public boolean beginsWith(String... actions) {

        List<String> sequenceData = getData();
        List<String> matchData = Arrays.asList(actions);

        if (sequenceData.size() < matchData.size()) {
            return false;
        }

        return sequenceData.subList(0, matchData.size()).equals(matchData);
    }

    /**
     *
     * @param actions
     * @return <code>true</code> if sequence data ends with the specified actions, <code>false</code> otherwise
     */
    public boolean endsWith(String... actions) {

        List<String> sequenceData = getData();
        List<String> matchData = Arrays.asList(actions);

        if (sequenceData.size() < matchData.size()) {
            return false;
        }

        return sequenceData.subList(sequenceData.size() - matchData.size(), sequenceData.size()).equals(matchData);
    }

    @Override
    public String toString() {
        return String.format("ActionSequence [name=%s, data=%s]", name, getData());
    }

    /**
     *
     * @return data in simple CSV format
     */
    public String dataToCsv() {
        if (data.isEmpty()) {
            return "";
        }
        StringBuilder csv = new StringBuilder();
        for (Iterator<String> iterator = data.iterator(); iterator.hasNext();) {
            String actionId = iterator.next();
            csv.append(actionId);
            if (iterator.hasNext()) {
                csv.append(",");
            }
        }
        return csv.toString();
    }

    /**
     * Assert that strings stored in this sequence equal (in order!) to the simple class names of {@code expected}.
     *
     * @param expected
     */
    public void assertDataEquals(Class<?>... expected) {
        assertEquals(data.size(), expected.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(data.get(i), expected[i].getSimpleName());
        }
    }

    // Static members

    private static final String DEFAULT_SEQUENCE = "default";

    /**
     * Static sequence map
     */
    private static Map<String, ActionSequence> sequences = new HashMap<String, ActionSequence>();

    /**
     * Remove all sequences.
     */
    public static void reset() {
        synchronized (sequences) {
            sequences.clear();
        }
    }

    /**
     * Add actionId to specified sequence. Add new sequence if needed.
     *
     * @param sequence
     * @param actionId
     * @return <code>true</code> if a new sequence was added, <code>false</code> otherwise
     */
    public static boolean addAction(String sequenceName, String actionId) {

        boolean newSequenceAdded = false;

        synchronized (sequences) {

            if (!sequences.containsKey(sequenceName)) {
                sequences.put(sequenceName, new ActionSequence(sequenceName));
                newSequenceAdded = true;
            }
            sequences.get(sequenceName).add(actionId);
        }
        return newSequenceAdded;
    }

    /**
     * Add actionId to default sequence.
     *
     * @param actionId
     * @return <code>true</code> if a new sequence was added, <code>false</code> otherwise
     */
    public static boolean addAction(String actionId) {
        return addAction(DEFAULT_SEQUENCE, actionId);
    }

    /**
     * @return default sequence or <code>null</code> if no such sequence exists
     */
    public static ActionSequence getSequence() {
        return getSequence(DEFAULT_SEQUENCE);
    }

    /**
     * @param name
     * @return specified sequence or <code>null</code> if no such sequence exists
     */
    public static ActionSequence getSequence(String sequenceName) {
        synchronized (sequences) {
            return sequences.get(sequenceName);
        }
    }

    /**
     * @return data of default sequence or <code>null</code> if no such sequence exists
     */
    public static List<String> getSequenceData() {
        return getSequenceData(DEFAULT_SEQUENCE);
    }

    /**
     * @param sequenceName
     * @return data of specified sequence or <code>null</code> if no such sequence exists
     */
    public static List<String> getSequenceData(String sequenceName) {
        synchronized (sequences) {
            return sequences.containsKey(sequenceName) ? sequences.get(sequenceName).getData() : null;
        }
    }

    /**
     * @return size of default sequence
     */
    public static int getSequenceSize() {
        return getSequenceSize(DEFAULT_SEQUENCE);
    }

    /**
     * @param sequence
     * @return size of specified sequence
     */
    public static int getSequenceSize(String sequenceName) {
        synchronized (sequences) {
            return sequences.containsKey(sequenceName) ? sequences.get(sequenceName).getData().size() : 0;
        }
    }

    /**
     *
     * @param csv
     * @return
     */
    public static ActionSequence buildFromCsvData(String csv) {

        if (csv == null) {
            throw new NullPointerException();
        }

        ActionSequence sequence = new ActionSequence();

        if (csv.length() != 0) {

            String[] data = csv.split(",");
            for (String actionId : data) {
                sequence.add(actionId);
            }
        }
        return sequence;
    }

    /**
     * Assert that strings stored in the default sequence equal (in order!) to the simple class names of {@code expected}. Do
     * nothing if there is no default sequence.
     *
     * @param expected
     */
    public static void assertSequenceDataEquals(Class<?>... expected) {
        if (getSequence() != null) {
            getSequence().assertDataEquals(expected);
        }
    }

    private static void checkStringValue(String value) {

        if (VALID_NAME_PATTERN.matcher(value).matches()) {
            return;
        }

        throw new IllegalArgumentException("Invalid name/id specified:" + value);
    }

}
