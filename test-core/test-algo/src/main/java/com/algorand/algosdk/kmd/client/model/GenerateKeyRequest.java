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
 * APIV1POSTKeyRequest is the request for &#x60;POST /v1/key&#x60;
 */
@ApiModel(description = "APIV1POSTKeyRequest is the request for `POST /v1/key`")

public class GenerateKeyRequest {
  @SerializedName("display_mnemonic")
  private Boolean displayMnemonic = null;

  @SerializedName("wallet_handle_token")
  private String walletHandleToken = null;

  public GenerateKeyRequest displayMnemonic(Boolean displayMnemonic) {
    this.displayMnemonic = displayMnemonic;
    return this;
  }

   /**
   * Get displayMnemonic
   * @return displayMnemonic
  **/
  @ApiModelProperty(value = "")
  public Boolean isDisplayMnemonic() {
    return displayMnemonic;
  }

  public void setDisplayMnemonic(Boolean displayMnemonic) {
    this.displayMnemonic = displayMnemonic;
  }

  public GenerateKeyRequest walletHandleToken(String walletHandleToken) {
    this.walletHandleToken = walletHandleToken;
    return this;
  }

   /**
   * Get walletHandleToken
   * @return walletHandleToken
  **/
  @ApiModelProperty(value = "")
  public String getWalletHandleToken() {
    return walletHandleToken;
  }

  public void setWalletHandleToken(String walletHandleToken) {
    this.walletHandleToken = walletHandleToken;
  }


  @Override
  public boolean equals(java.lang.Object o) {
  if (this == o) {
    return true;
  }
  if (o == null || getClass() != o.getClass()) {
    return false;
  }
    GenerateKeyRequest generateKeyRequest = (GenerateKeyRequest) o;
    return ObjectUtils.equals(this.displayMnemonic, generateKeyRequest.displayMnemonic) &&
    ObjectUtils.equals(this.walletHandleToken, generateKeyRequest.walletHandleToken);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeMulti(displayMnemonic, walletHandleToken);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateKeyRequest {\n");
    
    sb.append("    displayMnemonic: ").append(toIndentedString(displayMnemonic)).append("\n");
    sb.append("    walletHandleToken: ").append(toIndentedString(walletHandleToken)).append("\n");
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

