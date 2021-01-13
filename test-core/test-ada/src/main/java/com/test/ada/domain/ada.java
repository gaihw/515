package com.test.ada.domain;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ada {

        public class Block {
            public Epoch epoch;
            public Integer epochNo;
            public long fees;
            public Date forgedAt;
            public SlotLeader slotLeader;
            public String hash;
            public String merkelRoot;
            public Integer number;
            public String opCert;
            public Integer slotInEpoch;
            public Integer slotNo;
            public Block previousBlock;
            public String protocolVersion;
            public Block nextBlock;
            public long size;
            public List<Transaction> transactions;
            public TransactionAggregate transactionsAggregate;
            public String transactionsCount;
            public String vrfKey;
        }

        public class BlockAggregate {
            public Object aggregate;
        }

        public class BlockAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class BlockAvgFields {
            public Double fees;
            public Double size;
        }

        public class BlockMaxFields {
            public String fees;
            public String size;
        }

        public class BlockMinFields {
            public String fees;
            public String size;
        }

        public class BlockSumFields {
            public String fees;
            public String size;
        }

        public class ByronBlockVersionData {
            public Integer scriptVersion;
            public Integer slotDuration;
            public Integer maxBlockSize;
            public Integer maxHeaderSize;
            public Integer maxTxSize;
            public Integer maxProposalSize;
            public String mpcThd;
            public String heavyDelThd;
            public String updateVoteThd;
            public String updateProposalThd;
            public String updateImplicit;
            public Object softforkRule;
            public Object txFeePolicy;
            public String unlockStakeEpoch;
        }

        public class ByronGenesis {
            public Object bootStakeholders;
            public Object heavyDelegation;
            public Object startTime;
            public Object nonAvvmBalances;
            public Object blockVersionData;
            public Object protocolConsts;
            public Object avvmDistr;
        }

        public class ByronProtocolConsts {
            public Integer k;
            public Integer protocolMagic;
        }

        public class ByronSoftForkRule {
            public String initThd;
            public String minThd;
            public String thdDecrement;
        }

        public class ByronTxFeePolicy {
            public String summand;
            public String multiplier;
        }

        public class Cardano {
            public Object tip;
            public Object currentEpoch;
        }

        public class CardanoDbMeta {
            public Boolean initialized;
            public Object syncPercentage;
        }

        public class Delegation {
            public String address;
            public Object stakePool;
        }

        public class DelegationAggregate {
            public Object aggregate;
        }

        public class DelegationAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class DelegationAvgFields {
            public Object stakePool;
        }

        public class DelegationMaxFields {
            public Object stakePool;
        }

        public class DelegationMinFields {
            public Object stakePool;
        }

        public class DelegationSumFields {
            public Object stakePool;
        }

        public class Epoch {
            public List blocks;
            public Object blocksaggregate;
            public String blocksCount;
            public String output;
            public Integer number;
            public String transactionsCount;
            public Date startedAt;
            public Date lastBlockTime;
        }

        public class EpochAggregate {
            public Object aggregate;
        }

        public class EpochAggregateFields {
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class EpochMaxFields {
            public String blocksCount;
            public Integer number;
            public String output;
            public String transactionsCount;
        }

        public class EpochMinFields {
            public String blocksCount;
            public String output;
            public String transactionsCount;
        }

        public class EpochSumFields {
            public String blocksCount;
            public String output;
            public String transactionsCount;
        }

        public class ExtraEntropy {
            public String tag;
        }

        public class Genesis {
            public Object byron;
            public Object shelley;
        }

        public class ProtocolVersion {
            public Integer major;
            public Integer minor;
        }

        public class Query {
            public Object genesis;
            public List blocks;
            public Object blocksaggregate;
            public Object cardano;
            public Object cardanoDbMeta;
            public List delegations;
            public Object delegationsaggregate;
            public List epochs;
            public Object epochsaggregate;
            public List stakeDeregistrations;
            public Object stakeDeregistrationsaggregate;
            public List stakePools;
            public Object stakePoolsaggregate;
            public List stakeRegistrations;
            public Object stakeRegistrationsaggregate;
            public List transactions;
            public Object transactionsaggregate;
            public List utxos;
            public Object utxosaggregate;
            public List withdrawals;
            public Object withdrawalsaggregate;
        }

        public class Relay {
            public Object ipv4;
            public Object ipv6;
            public Object dnsName;
            public String dnsSrvName;
        }

        public class ShelleyGenesis {
            public Double activeSlotsCoeff;
            public Integer epochLength;
            public Object genDelegs;
            public Object initialFunds;
            public Integer maxKESEvolutions;
            public String maxLovelaceSupply;
            public String networkId;
            public Integer networkMagic;
            public Integer protocolMagicId;
            public Object protocolParams;
            public Integer securityParam;
            public Integer slotLength;
            public Integer slotsPerKESPeriod;
            public Object staking;
            public String systemStart;
            public Integer updateQuorum;
        }

        public class ShelleyProtocolParams {
            public Double a0;
            public Integer decentralisationParam;
            public Integer eMax;
            public Object extraEntropy;
            public Integer keyDeposit;
            public Integer maxBlockBodySize;
            public Integer maxBlockHeaderSize;
            public Integer maxTxSize;
            public Integer minFeeA;
            public Integer minFeeB;
            public Integer minPoolCost;
            public Integer minUTxOValue;
            public Integer nOpt;
            public Integer poolDeposit;
            public Object protocolVersion;
            public Double rho;
            public Double tau;
        }

        public class SlotLeader {
            public String description;
            public String hash;
            public Object stakePool;
        }

        public class StakeDeregistration {
            public String address;
            public Object transaction;
        }

        public class StakePool {
            public String fixedCost;
            public String hash;
            public Double margin;
            public String metadataHash;
            public List owners;
            public String pledge;
            public List relays;
            public Object retirement;
            public String rewardAddress;
            public Object updatedIn;
            public Object url;
            public List withdrawals;
        }

        public class StakePoolAggregate {
            public Object aggregate;
        }

        public class StakePoolAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class StakePoolAvgFields {
            public String fixedCost;
            public Double margin;
            public String pledge;
        }

        public class StakePoolMaxFields {
            public String fixedCost;
            public Double margin;
            public String pledge;
        }

        public class StakePoolMinFields {
            public String fixedCost;
            public Double margin;
            public String pledge;
        }

        public class StakePoolSumFields {
            public String fixedCost;
            public Double margin;
            public String pledge;
        }

        public class StakePoolOwner {
            public String hash;
        }

        public class StakePoolRetirement {
            public Object announcedIn;
            public Object inEffectFrom;
        }

        public class StakeRegistration {
            public String address;
            public Object transaction;
        }

        public class StakeRegistrationAggregate {
            public Object aggregate;
        }

        public class StakeRegistrationAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class StakeRegistrationAvgFields {
            public Object transaction;
        }

        public class StakeRegistrationMaxFields {
            public Object transaction;
        }

        public class StakeRegistrationMinFields {
            public Object transaction;
        }

        public class StakeRegistrationSumFields {
            public Object transaction;
        }

        public class Staking {
            public Object pools;
            public Object stake;
        }

        public class Transaction {
            public Block block;
            public Integer blockIndex;
            public long fee;
            public String hash;
            public List<TransactionInput> inputs;
            public Object inputsaggregate;
            public List<TransactionOutput> outputs;
            public Object outputsaggregate;
            public long size;
            public String totalOutput;
            public Date includedAt;
        }

        public class TransactionAggregate {
            public Object aggregate;
        }

        public class TransactionAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class TransactionAvgFields {
            public Double fee;
            public Double size;
            public Double totalOutput;
        }

        public class TransactionMaxFields {
            public String fee;
            public String size;
            public String totalOutput;
        }

        public class TransactionMinFields {
            public String fee;
            public String size;
            public String totalOutput;
        }

        public class TransactionSumFields {
            public String fee;
            public String size;
            public String totalOutput;
        }

        public class TransactionInput {
            public String address;
            public Object sourceTransaction;
            public String sourceTxHash;
            public Integer sourceTxIndex;
            public Object transaction;
            public String txHash;
            public String value;
        }

        public class TransactionInputAggregate {
            public Object aggregate;
        }

        public class TransactionInputAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class TransactionInputAvgFields {
            public String value;
        }

        public class TransactionInputMaxFields {
            public String value;
        }

        public class TransactionInputMinFields {
            public String value;
        }

        public class TransactionInputSumFields {
            public String value;
        }

        public class TransactionOutput {
            public String address;
            public Integer index;
            public Object transaction;
            public String txHash;
            public String value;
        }

        public class TransactionOutputAggregate {
            public Object aggregate;
        }

        public class TransactionOutputAggregateFields {
            public Object avg;
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class TransactionOutputAvgFields {
            public String value;
        }

        public class TransactionOutputMaxFields {
            public String value;
        }

        public class TransactionOutputMinFields {
            public String value;
        }

        public class TransactionOutputSumFields {
            public String value;
        }

        public class Withdrawal {
            public String address;
            public String amount;
            public Object transaction;
        }

        public class WithdrawalAggregate {
            public Object aggregate;
        }

        public class WithdrawalAggregateFields {
            public String count;
            public Object max;
            public Object min;
            public Object sum;
        }

        public class WithdrawalMaxFields {
            public String amount;
        }

        public class WithdrawalMinFields {
            public String amount;
        }

        public class WithdrawalSumFields {
            public String amount;
        }

    }

