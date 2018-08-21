/*
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License version 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 */

package org.eclipse.keyple.command.po.builder;

import org.eclipse.keyple.calypso.command.po.PoRevision;
import org.eclipse.keyple.calypso.command.po.builder.PoGetChallengeCmdBuild;
import org.eclipse.keyple.command.AbstractApduCommandBuilder;
import org.eclipse.keyple.seproxy.ApduRequest;
import org.eclipse.keyple.util.ByteBufferUtils;
import org.junit.Assert;
import org.junit.Test;

public class POGetChallengeCmdBuildTest {

    @Test
    public void POGetChallenge_Rev2_4() {

        byte[] request = {(byte) 0x94, (byte) 0x84, 0x01, 0x10, 0x08};

        AbstractApduCommandBuilder apduCommandBuilder =
                new PoGetChallengeCmdBuild(PoRevision.REV2_4);
        ApduRequest apduRequest = apduCommandBuilder.getApduRequest();

        Assert.assertArrayEquals(request, ByteBufferUtils.toBytes(apduRequest.getBytes()));

    }

    @Test
    public void POGetChallenge_Rev3_1() {

        byte[] request = {(byte) 0x00, (byte) 0x84, 0x01, 0x10, 0x08};

        AbstractApduCommandBuilder apduCommandBuilder =
                new PoGetChallengeCmdBuild(PoRevision.REV3_1);
        ApduRequest apduRequest = apduCommandBuilder.getApduRequest();

        Assert.assertArrayEquals(request, ByteBufferUtils.toBytes(apduRequest.getBytes()));

    }

    @Test
    public void POGetChallenge_Rev3_2() {

        byte[] request = {(byte) 0x00, (byte) 0x84, 0x01, 0x10, 0x08};

        AbstractApduCommandBuilder apduCommandBuilder =
                new PoGetChallengeCmdBuild(PoRevision.REV3_2);
        ApduRequest apduRequest = apduCommandBuilder.getApduRequest();

        Assert.assertArrayEquals(request, ByteBufferUtils.toBytes(apduRequest.getBytes()));

    }


}