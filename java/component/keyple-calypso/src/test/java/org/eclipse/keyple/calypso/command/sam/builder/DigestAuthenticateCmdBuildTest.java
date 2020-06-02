/********************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.eclipse.keyple.calypso.command.sam.builder;


import org.eclipse.keyple.calypso.command.sam.builder.security.DigestAuthenticateCmdBuild;
import org.eclipse.keyple.core.command.AbstractApduCommandBuilder;
import org.eclipse.keyple.core.seproxy.message.ApduRequest;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DigestAuthenticateCmdBuildTest {

    @Test
    public void digestAuthenticate() {

        byte[] signaturePO = new byte[] {0x00, 0x01, 0x02, 0x03};
        byte[] request =
                new byte[] {(byte) 0x94, (byte) 0x82, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03};

        AbstractApduCommandBuilder apduCommandBuilder =
                new DigestAuthenticateCmdBuild(null, signaturePO);
        ApduRequest ApduRequest = apduCommandBuilder.getApduRequest();

        Assert.assertEquals(ByteArrayUtil.toHex(request),
                ByteArrayUtil.toHex(ApduRequest.getBytes()));
    }
}
