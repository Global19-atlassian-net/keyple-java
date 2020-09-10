/********************************************************************************
 * Copyright (c) 2019 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.eclipse.keyple.plugin.android.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;

import org.eclipse.keyple.core.seproxy.SeReader;
import org.eclipse.keyple.core.seproxy.event.ObservableReader;

/**
 * {@link SeReader} to communicate with NFC Tag though
 * Android {@link NfcAdapter}
 *
 * Configure NFCAdapter Protocols with {@link AndroidNfcReaderImpl#setParameter(String, String)}
 *
 * Optimized for android 4.4 (API 19) to  6.0 (API 23)
 */
public interface AndroidNfcReader extends ObservableReader{

    String READER_NAME = "AndroidNfcReaderImpl";
    String PLUGIN_NAME = AndroidNfcPlugin.PLUGIN_NAME;

    String FLAG_READER_SKIP_NDEF_CHECK = "FLAG_READER_SKIP_NDEF_CHECK";
    String FLAG_READER_NO_PLATFORM_SOUNDS = "FLAG_READER_NO_PLATFORM_SOUNDS";
    String FLAG_READER_PRESENCE_CHECK_DELAY =
            "FLAG_READER_PRESENCE_CHECK_DELAY";
    /**
     * Gets a string describing the low level description of the current tag.
     * <p>Used for logging purpose
     * @return string
     */
    public String printTagId();

    /**
     * Process data from NFC Intent
     *
     * @param intent : Intent received and filterByProtocol by xml tech_list
     */
    void processIntent(Intent intent);
}
