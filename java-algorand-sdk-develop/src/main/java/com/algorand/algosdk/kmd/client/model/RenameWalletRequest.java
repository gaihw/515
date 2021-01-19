/*
 * for KMD HTTP API
 * API for KMD (Key Management Daemon)
 *
 * OpenAPI spec version: 0.0.1
 * Contact: contact@algorand.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.algorand.algosdk.kmd.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.ObjectUtils;

/**
 * APIV1POSTWalletRenameRequest is the request for &#x60;POST /v1/wallet/rename&#x60;
 */
@ApiModel(description = "APIV1POSTWalletRenameRequest is the request for `POST /v1/wallet/rename`")

public class RenameWalletRequest {
  @SerializedName("wallet_id")
  private String walletId = null;

  @SerializedName("wallet_name")
  private String walletName = null;

  @SerializedName("wallet_password")
  private String walletPassword = null;

  public RenameWalletRequest walletId(String walletId) {
    this.walletId = walletId;
    return this;
  }

   /**
   * Get walletId
   * @return walletId
  **/
  @ApiModelProperty(value = "")
  public String getWalletId() {
    return walletId;
  }

  public void setWalletId(String walletId) {
    this.walletId = walletId;
  }

  public RenameWalletRequest walletName(String walletName) {
    this.walletName = walletName;
    return this;
  }

   /**
   * Get walletName
   * @return walletName
  **/
  @ApiModelProperty(value = "")
  public String getWalletName() {
    return walletName;
  }

  public void setWalletName(String walletName) {
    this.walletName = walletName;
  }

  public RenameWalletRequest walletPassword(String walletPassword) {
    this.walletPassword = walletPassword;
    return this;
  }

   /**
   * Get walletPassword
   * @return walletPassword
  **/
  @ApiModelProperty(value = "")
  public String getWalletPassword() {
    return walletPassword;
  }

  public void setWalletPassword(String walletPassword) {
    this.walletPassword = walletPassword;
  }


  @Override
  public boolean equals(java.lang.Object o) {
  if (this == o) {
    return true;
  }
  if (o == null || getClass() != o.getClass()) {
    return false;
  }
    RenameWalletRequest renameWalletRequest = (RenameWalletRequest) o;
    return ObjectUtils.equals(this.walletId, renameWalletRequest.walletId) &&
    ObjectUtils.equals(this.walletName, renameWalletRequest.walletName) &&
    ObjectUtils.equals(this.walletPassword, renameWalletRequest.walletPassword);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeMulti(walletId, walletName, walletPassword);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RenameWalletRequest {\n");
    
    sb.append("    walletId: ").append(toIndentedString(walletId)).append("\n");
    sb.append("    walletName: ").append(toIndentedString(walletName)).append("\n");
    sb.append("    walletPassword: ").append(toIndentedString(walletPassword)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

