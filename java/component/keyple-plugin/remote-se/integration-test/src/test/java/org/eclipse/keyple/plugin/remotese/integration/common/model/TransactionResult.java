/* **************************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.plugin.remotese.integration.common.model;

public class TransactionResult {
  private Boolean isSuccessful;
  private String userId;

  public TransactionResult setSuccessful(Boolean successful) {
    isSuccessful = successful;
    return this;
  }

  public TransactionResult setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public Boolean isSuccessful() {
    return isSuccessful;
  }

  public String getUserId() {
    return userId;
  }
}
