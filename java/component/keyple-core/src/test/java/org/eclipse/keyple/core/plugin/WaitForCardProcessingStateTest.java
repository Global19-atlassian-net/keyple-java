/* **************************************************************************************
 * Copyright (c) 2019 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.core.plugin;

import static org.eclipse.keyple.core.plugin.AbstractObservableState.MonitoringState.WAIT_FOR_SE_INSERTION;
import static org.eclipse.keyple.core.plugin.AbstractObservableState.MonitoringState.WAIT_FOR_SE_REMOVAL;
import static org.eclipse.keyple.core.plugin.AbstractObservableState.MonitoringState.WAIT_FOR_START_DETECTION;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.keyple.core.CoreBaseTest;
import org.eclipse.keyple.core.service.event.ObservableReader;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitForCardProcessingStateTest extends CoreBaseTest {

  private static final Logger logger =
      LoggerFactory.getLogger(WaitForCardProcessingStateTest.class);

  final String PLUGIN_NAME = "WaitForCardProcessingTestP";
  final String READER_NAME = "WaitForCardProcessingStateTest";

  final ExecutorService executorService = Executors.newSingleThreadExecutor();
  BlankPresenceWaitForCardBlockingThreadedReader r;
  WaitForCardProcessingState waitForCardProcessingState;

  @Before
  public void setUp() {
    logger.info("------------------------------");
    logger.info("Test {}", name.getMethodName() + "");
    logger.info("------------------------------");

    r = AbsSmartPresenceTheadedReaderTest.getSmartPresenceMock(PLUGIN_NAME, READER_NAME);
    waitForCardProcessingState =
        new WaitForCardProcessingState(r, new SmartRemovalMonitoringJob(r), executorService);
  }

  @Before
  public void tearDown() {
    logger.info("------------------------------");
    waitForCardProcessingState.onDeactivate();
  }

  @Test
  public void waitForProcessed_processed() throws Exception {
    /*
     * ------------ input polling mode is CONTINUE card has been processed within timeout
     */

    doReturn(ObservableReader.PollingMode.REPEATING).when(r).getPollingMode();
    doReturn(false).when(r).waitForCardAbsentNative();

    /* test */
    waitForCardProcessingState.onActivate();
    waitForCardProcessingState.onEvent(AbstractObservableLocalReader.InternalEvent.SE_PROCESSED);

    /* Assert */
    // Assert.assertEquals(WAIT_FOR_START_DETECTION, r.getCurrentState().getMonitoringState());
    verify(r, times(1)).switchState(WAIT_FOR_SE_REMOVAL);
  }

  @Test
  public void smart_waitForProcessed_STOP() throws Exception {
    // flaky test
    /*
     * ------------ input polling mode is STOP card has been REMOVED within timeout
     */
    doReturn(ObservableReader.PollingMode.SINGLESHOT).when(r).getPollingMode();
    doReturn(true).when(r).waitForCardAbsentNative();

    /* test */
    waitForCardProcessingState.onActivate();

    Thread.sleep(500l); // avoid flaky

    /* Assert */
    verify(r, times(1)).switchState(WAIT_FOR_START_DETECTION);
  }

  @Test
  public void smart_waitForProcessed_CONTINUE() throws Exception {
    /*
     * ------------ input polling mode is CONTINUE card has been removed within timeout
     */
    doReturn(ObservableReader.PollingMode.REPEATING).when(r).getPollingMode();
    doReturn(true).when(r).waitForCardAbsentNative();

    /* test */
    waitForCardProcessingState.onActivate();

    Thread.sleep(50l); // wait

    /* Assert */
    verify(r, times(1)).switchState(WAIT_FOR_SE_INSERTION);
  }
}
