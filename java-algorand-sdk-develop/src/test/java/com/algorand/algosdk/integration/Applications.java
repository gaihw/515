package com.algorand.algosdk.integration;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.builder.transaction.ApplicationBaseTransactionBuilder;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.algorand.algosdk.cucumber.shared.TransactionSteps.loadTEALProgramFromFile;
import static com.algorand.algosdk.util.ConversionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Applications {
    private final Clients clients;
    private final Stepdefs base;

    private Transaction transaction;
    private Account transientAccount = null;
    private String txId = null;
    private Long appId = 0L;

    public Applications(Clients clients, Stepdefs base) {
        this.clients = clients;
        this.base = base;
    }

    @Given("I build an application transaction with the transient account, the current application, suggested params, operation {string}, approval-program {string}, clear-program {string}, global-bytes {long}, global-ints {long}, local-bytes {long}, local-ints {long}, app-args {string}, foreign-apps {string}, app-accounts {string}")
    public void buildAnApplicationTransactions(String operation, String approvalProgramFile, String clearProgramFile, Long globalBytes, Long globalInts, Long localBytes, Long localInts, String appArgs, String foreignApps, String appAccounts) throws Exception {
        ApplicationBaseTransactionBuilder builder = null;

        // Create builder and apply builder-specific parameters
        switch (operation) {
            case "create":
                builder = Transaction.ApplicationCreateTransactionBuilder()
                        .approvalProgram(loadTEALProgramFromFile(approvalProgramFile))
                        .clearStateProgram(loadTEALProgramFromFile(clearProgramFile))
                        .globalStateSchema(new StateSchema(globalInts, globalBytes))
                        .localStateSchema(new StateSchema(localInts, localBytes));
                break;
            case "update":
                builder = Transaction.ApplicationUpdateTransactionBuilder()
                        .approvalProgram(loadTEALProgramFromFile(approvalProgramFile))
                        .clearStateProgram(loadTEALProgramFromFile(clearProgramFile));
                break;
            case "call":
                builder = Transaction.ApplicationCallTransactionBuilder();
                break;
            case "optin":
                builder = Transaction.ApplicationOptInTransactionBuilder();
                break;
            case "clear":
                builder = Transaction.ApplicationClearTransactionBuilder();
                break;
            case "closeout":
                builder = Transaction.ApplicationCloseTransactionBuilder();
                break;
            case "delete":
                builder = Transaction.ApplicationDeleteTransactionBuilder();
                break;
            default:
                Assertions.fail("Need an option to build: " + operation);
        }

        // Shared base fields
        if (StringUtils.isNotEmpty(appArgs)) {
            builder.args(convertArgs(appArgs));
        }
        if (StringUtils.isNotEmpty(foreignApps)) {
            builder.foreignApps(convertForeignApps(foreignApps));
        }
        if (StringUtils.isNotEmpty(appAccounts)) {
            builder.accounts(convertAccounts(appAccounts));
        }

        // Send with transient account, suggested params and current application
        builder.sender(this.transientAccount.getAddress());
        builder.lookupParams(this.clients.v2Client);
        if (this.appId != 0) {
            builder.applicationId(appId);
        }

        this.transaction = builder.build();
    }

    @Given("I create a new transient account and fund it with {long} microalgos.")
    public void createAndFundTransientAccount(Long amount) throws Exception {
        // Create a new account.
        this.transientAccount = new Account();

        // Fund it with one of the base wallets.
        Address sender = base.getAddress(1);

        Transaction tx = Transaction.PaymentTransactionBuilder()
                .sender(sender)
                .receiver(this.transientAccount.getAddress())
                .amount(amount)
                .lookupParams(clients.v2Client)
                .build();
        SignedTransaction stx = base.signWithAddress(tx, sender);

        Response<PostTransactionsResponse> rPost = clients.v2Client.RawTransaction().rawtxn(Encoder.encodeToMsgPack(stx)).execute();

        // Save the txid and hook into another step to help us out.
        this.txId = rPost.body().txId;
        waitForTransactionToBeConfirmed();
    }

    @Given("I sign and submit the transaction, saving the txid. If there is an error it is {string}.")
    public void sendTransactionWithTransientAccountAndCheckForError(String error) throws Exception {
        SignedTransaction stx = this.transientAccount.signTransaction(this.transaction);

        // Submit
        Response<PostTransactionsResponse> rPost = clients.v2Client.RawTransaction().rawtxn(Encoder.encodeToMsgPack(stx)).execute();

        // If an error was expected, make sure it is set correctly.
        if (StringUtils.isNotEmpty(error)) {
            assertThat(rPost.isSuccessful()).isFalse();
            assertThat(rPost.message()).containsIgnoringCase(error);
            return;
        }

        // Otherwise make sure the transaction was submitted successfully.
        assertThat(rPost.isSuccessful()).isTrue();

        // And save the txId for later
        this.txId = rPost.body().txId;
    }

    @Given("I wait for the transaction to be confirmed.")
    public void waitForTransactionToBeConfirmed() throws Exception {
        Duration timeout = Duration.ofSeconds(10);
        Long start = System.currentTimeMillis();
        Response<PendingTransactionResponse> r;
        // Keep checking until the timeout.
        do {
            r = clients.v2Client.PendingTransactionInformation(txId).execute();
            // If the transaction has been confirmed, exit.
            if (r.body().confirmedRound != null) {
                return;
            }
            Thread.sleep(250);
        } while ( (System.currentTimeMillis() - start) < timeout.toMillis());
    }

    // TODO: Use V2 Pending Transaction endpoint when it is available.
    //       The initial implementation hacks into the v1 endpoint to manually extract the new data.
    @Given("I remember the new application ID.")
    public void rememberTheNewApplicatoinId() {
        try {
            com.squareup.okhttp.Call call = base.acl.pendingTransactionInformationCall(txId, null, null);
            com.squareup.okhttp.Response r2 = call.execute();
            String raw = r2.body().string();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(raw, Map.class);
            if (responseMap.containsKey("txresults")) {
                Object next = responseMap.get("txresults");
                if (next instanceof Map) {
                    if (((Map) next).containsKey("createdapp")) {
                        Object appId = ((Map) next).get("createdapp");
                        this.appId = Long.valueOf(String.valueOf(appId));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail("Oops: " + e.getMessage(), e);
        }
    }

    @Then("The transient account should have the created app {string} and total schema byte-slices {long} and uints {long}, the application {string} state contains key {string} with value {string}")
    public void checkAccountData(
            String created,
            Long numByteSlices,
            Long numUints,
            String stateLocation,
            String hasKey,
            String keyValue
    ) throws Exception {
        Response<com.algorand.algosdk.v2.client.model.Account> acctResponse = clients.v2Client.AccountInformation(transientAccount.getAddress()).execute();

        com.algorand.algosdk.v2.client.model.Account acct = acctResponse.body();

        // Check the total schema sizes
        assertThat(acct.appsTotalSchema.numByteSlice).isEqualTo(numByteSlices);
        assertThat(acct.appsTotalSchema.numUint).isEqualTo(numUints);

        // If we don't expect the app to exist, verify that it isn't there and exit.
        if (Boolean.parseBoolean(created) == false) {
            assertThat(acct.createdApps).extracting("id").doesNotContain(appId);
            return;
        }

        // Top level assertions.
        assertThat(acct.createdApps).extracting("id").contains(appId);

        // If there is no key to check, we're done.
        if (StringUtils.isEmpty(hasKey)) {
            return;
        }

        // Verify the key-value is set
        boolean found = false;

        List<TealKeyValue> keyValues = null;

        // Find global or local key-values
        if (StringUtils.equalsIgnoreCase(stateLocation, "local")) {
            List<ApplicationLocalState> matches = acct.appsLocalState.stream()
                    .filter(app -> app.id.equals(appId))
                    .collect(Collectors.toList());
            assertThat(matches).hasSize(1);
            keyValues = matches.get(0).keyValue;
        }
        if (StringUtils.equalsIgnoreCase(stateLocation, "global")) {
            List<ApplicationParams> matches = acct.createdApps.stream()
                    .filter(app -> app.id.equals(appId))
                    .map(app -> app.params)
                    .collect(Collectors.toList());
            assertThat(matches).hasSize(1);
            keyValues = matches.get(0).globalState;
        }

        // Check for expected key-value
        assertThat(keyValues).hasSizeGreaterThan(0);
        for (TealKeyValue kv : keyValues) {
            if (StringUtils.equals(kv.key, hasKey)) {
                if (kv.value.type.equals(1)) {
                    assertThat(kv.value.bytes).isEqualTo(keyValue);
                } else if (kv.value.type.equals(0)) {
                    assertThat(kv.value.uint).isEqualTo(Long.parseLong(keyValue));
                }
                // Check that the values are equal.
                found = true;
            }
        }

        assertThat(found).as("Couldn't find key '%s'", hasKey).isTrue();
    }
}
