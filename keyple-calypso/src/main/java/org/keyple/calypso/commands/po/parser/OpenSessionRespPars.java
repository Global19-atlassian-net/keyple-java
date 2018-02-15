/*
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License version 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 */

package org.keyple.calypso.commands.po.parser;

import org.keyple.calypso.commands.dto.SecureSession;
import org.keyple.calypso.commands.po.PoRevision;
import org.keyple.calypso.commands.utils.ResponseUtils;
import org.keyple.commands.ApduResponseParser;
import org.keyple.seproxy.ApduResponse;

/**
 * The Class OpenSessionRespPars. This class provides status code properties and the getters to
 * access to the structured fields of an Open Secure Session response.
 *
 * @author Ixxi
 *
 */
public class OpenSessionRespPars extends ApduResponseParser {

    /** The secure session. */
    private SecureSession secureSession;

    /**
     * Instantiates a new OpenSessionRespPars.
     *
     * @param response the response from Open secure session APDU command
     * @param revision the revision of the PO
     */
    public OpenSessionRespPars(ApduResponse response, PoRevision revision) {
        super(response);
        switch (revision) {
            case REV3_2:
                initStatusTable32();
                break;
            case REV3_1:
                initStatusTable31();
                break;
            case REV2_4:
                initStatusTable24();
                break;
            default:
                break;
        }

        if (isSuccessful()) {
            switch (revision) {
                case REV3_2:
                    if (response.isSuccessful()) {
                        secureSession = toSecureSessionRev32(response.getbytes());
                    }
                    break;
                case REV3_1:
                    if (response.isSuccessful()) {
                        secureSession = toSecureSessionRev3(response.getbytes());
                    }
                    break;
                case REV2_4:
                    if (response.isSuccessful()) {
                        secureSession = toSecureSessionRev2(response.getbytes());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method to get a Secure Session from the response in revision 3.2 mode.
     *
     * @param apduResponse the apdu response
     * @return a SecureSession
     */
    public static SecureSession toSecureSessionRev32(byte[] apduResponse) {
        byte flag = apduResponse[8];
        boolean previousSessionRatified = ResponseUtils.isBitSet(flag, 0x00);
        boolean manageSecureSessionAuthorized = ResponseUtils.isBitSet(flag, 1);

        byte kif = apduResponse[9];
        byte kvc = apduResponse[10];
        int dataLength = apduResponse[11];
        byte[] data = ResponseUtils.subArray(apduResponse, 12, 12 + dataLength);

        return new SecureSession(ResponseUtils.subArray(apduResponse, 0, 3),
                ResponseUtils.subArray(apduResponse, 3, 8), previousSessionRatified,
                manageSecureSessionAuthorized, kif, kvc, data, apduResponse);
    }

    /**
     * Method to get a Secure Session from the response in revision 3 mode.
     *
     * @param apduResponse the apdu response
     * @return a SecureSession
     */
    public static SecureSession toSecureSessionRev3(byte[] apduResponse) {
        boolean previousSessionRatified = apduResponse[4] == (byte) 0x01 ? true : false;
        boolean manageSecureSessionAuthorized = false;

        byte kif = apduResponse[5];
        byte kvc = apduResponse[6];
        int dataLength = apduResponse[7];
        byte[] data = ResponseUtils.subArray(apduResponse, 8, 8 + dataLength);

        return new SecureSession(ResponseUtils.subArray(apduResponse, 0, 3),
                ResponseUtils.subArray(apduResponse, 3, 4), previousSessionRatified,
                manageSecureSessionAuthorized, kif, kvc, data, apduResponse);
    }

    /**
     * Method to get a Secure Session from the response in revision 2 mode.
     *
     * @param apduResponse the apdu response
     * @return a SecureSession
     */
    public static SecureSession toSecureSessionRev2(byte[] apduResponse) {
        boolean previousSessionRatified = true;

        byte kvc = ResponseUtils.toKVCRev2(apduResponse);

        if (apduResponse.length < 6) {
            previousSessionRatified = false;
        }

        return new SecureSession(ResponseUtils.subArray(apduResponse, 1, 4),
                ResponseUtils.subArray(apduResponse, 4, 5), previousSessionRatified, false, kvc,
                null, apduResponse);
    }

    /**
     * Initializes the status table.
     */
    private void initStatusTable32() {
        statusTable.put(new byte[] {(byte) 0x67, (byte) 0x00},
                new StatusProperties(false, "Lc value not supported."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x00},
                new StatusProperties(false, "Transaction Counter is 0"));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x81}, new StatusProperties(false,
                "Command forbidden (read requested and current EF is a Binary file)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x82}, new StatusProperties(false,
                "Security conditions not fulfilled (PIN code not presented, encryption required). "));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x85}, new StatusProperties(false,
                "Access forbidden (Never access mode, Session already opened)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x86}, new StatusProperties(false,
                "Command not allowed (read requested and no current EF)."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x81},
                new StatusProperties(false, "Wrong key index."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x82},
                new StatusProperties(false, "File not found."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x83},
                new StatusProperties(false, "Record not found (record index is above NumRec)."));
        statusTable.put(new byte[] {(byte) 0x6B, (byte) 0x00}, new StatusProperties(false,
                "P1 or P2 value not supported (e.g. REV.3.2 mode not supported)."));
        statusTable.put(new byte[] {(byte) 0x90, (byte) 0x00},
                new StatusProperties(true, "Successful execution."));
    }

    /**
     * Initializes the status table.
     */
    private void initStatusTable31() {
        statusTable.put(new byte[] {(byte) 0x67, (byte) 0x00},
                new StatusProperties(false, "Lc value not supported."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x00},
                new StatusProperties(false, "Transaction Counter is 0"));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x81}, new StatusProperties(false,
                "Command forbidden (read requested and current EF is a Binary file)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x82}, new StatusProperties(false,
                "Security conditions not fulfilled (PIN code not presented, AES key forbidding the "
                        + "Revision 3 mode, encryption required)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x85}, new StatusProperties(false,
                "Access forbidden (Never access mode, Session already opened)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x86}, new StatusProperties(false,
                "Command not allowed (read requested and no current EF)."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x81},
                new StatusProperties(false, "Wrong key index."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x82},
                new StatusProperties(false, "File not found."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x83},
                new StatusProperties(false, "Record not found (record index is above NumRec)."));
        statusTable.put(new byte[] {(byte) 0x6B, (byte) 0x00},
                new StatusProperties(false, "P1 or P2 value not supported."));
        statusTable.put(new byte[] {(byte) 0x90, (byte) 0x00},
                new StatusProperties(true, "Successful execution."));
    }

    /**
     * Initializes the status table.
     */
    private void initStatusTable24() {
        statusTable.put(new byte[] {(byte) 0x67, (byte) 0x00},
                new StatusProperties(false, "Lc value not supported."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x00},
                new StatusProperties(false, "Transaction Counter is 0"));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x81}, new StatusProperties(false,
                "Command forbidden (read requested and current EF is a Binary file)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x82}, new StatusProperties(false,
                "Security conditions not fulfilled (PIN code not presented, AES key forbidding the "
                        + "compatibility mode, encryption required)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x85}, new StatusProperties(false,
                "Access forbidden (Never access mode, Session already opened)."));
        statusTable.put(new byte[] {(byte) 0x69, (byte) 0x86}, new StatusProperties(false,
                "Command not allowed (read requested and no current EF)."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x81},
                new StatusProperties(false, "Wrong key index."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x82},
                new StatusProperties(false, "File not found."));
        statusTable.put(new byte[] {(byte) 0x6A, (byte) 0x83},
                new StatusProperties(false, "Record not found (record index is above NumRec)."));
        statusTable.put(new byte[] {(byte) 0x6B, (byte) 0x00}, new StatusProperties(false,
                "P1 or P2 value not supported (key index incorrect, wrong P2)."));
        statusTable.put(new byte[] {(byte) 0x90, (byte) 0x00},
                new StatusProperties(true, "Successful execution."));
    }

    public byte[] getPoChallenge() {
        return secureSession.getChallengeRandomNumber();
    }

    public int getTransactionCounterValue() {
        return java.nio.ByteBuffer.wrap(secureSession.getChallengeTransactionCounter())
                .order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public boolean wasRatified() {
        return secureSession.isPreviousSessionRatified();
    }

    public boolean isManageSecureSessionAuthorized() {
        return secureSession.isManageSecureSessionAuthorized();
    }

    public byte getSelectedKif() {
        return secureSession.getKIF();
    }

    public byte getSelectedKvc() {
        return secureSession.getKVC();
    }

    public byte[] getRecordDataRead() {
        return secureSession.getSecureSessionData();
    }
}