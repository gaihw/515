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

import java.util.ArrayList;
import java.util.List;

/**
 * APIV1POSTKeyListResponse is the response to &#x60;POST /v1/key/list&#x60; friendly:ListKeysResponse
 */
@ApiModel(description = "APIV1POSTKeyListResponse is the response to `POST /v1/key/list` friendly:ListKeysResponse")

public class APIV1POSTKeyListResponse {
  @SerializedName("addresses")
  private List<String> addresses = null;

  @SerializedName("error")
  private Boolean error = null;

  @SerializedName("message")
  private String message = null;

  public APIV1POSTKeyListResponse addresses(List<String> addresses) {
    this.addresses = addresses;
    return this;
  }

  public APIV1POSTKeyListResponse addAddressesItem(String addressesItem) {
    if (this.addresses == null) {
      this.addresses = new ArrayList<String>();
    }
    this.addresses.add(addressesItem);
    return this;
  }

   /**
   * Get addresses
   * @return addresses
  **/
  @ApiModelProperty(value = "")
  public List<String> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<String> addresses) {
    this.addresses = addresses;
  }

  public APIV1POSTKeyListResponse error(Boolean error) {
    this.error = error;
    return this;
  }

   /**
   * Get error
   * @return error
  **/
  @ApiModelProperty(value = "")
  public Boolean isError() {
    return error;
  }

  public void setError(Boolean error) {
    this.error = error;
  }

  public APIV1POSTKeyListResponse message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(value = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
  if (this == o) {
    return true;
  }
  if (o == null || getClass() != o.getClass()) {
    return false;
  }
    APIV1POSTKeyListResponse apIV1POSTKeyListResponse = (APIV1POSTKeyListResponse) o;
    return ObjectUtils.equals(this.addresses, apIV1POSTKeyListResponse.addresses) &&
    ObjectUtils.equals(this.error, apIV1POSTKeyListResponse.error) &&
    ObjectUtils.equals(this.message, apIV1POSTKeyListResponse.message);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeMulti(addresses, error, message);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class APIV1POSTKeyListResponse {\n");
    
    sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

