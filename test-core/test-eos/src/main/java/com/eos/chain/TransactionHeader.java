package com.eos.chain;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.annotations.SerializedName;
import com.eos.utils.cypto.util.BitUtils;
import com.eos.utils.cypto.util.HexUtils;
import com.eos.utils.types.EosType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by swapnibble on 2017-09-12.
 */
@Getter
@Setter
public class TransactionHeader implements EosType.Packer {
 /*   @Expose
    private String expiration;

    @Expose
    private int ref_block_num = 0; // uint16_t

    @Expose
    private long ref_block_prefix= 0;// uint32_t

    @Expose
    private long max_net_usage_words; // fc::unsigned_int

    @Expose
    private long max_cpu_usage_ms;    // fc::unsigned_int

    @Expose
    private long delay_sec;     // fc::unsigned_int*/
    private String expiration;

    @SerializedName("ref_block_num")
    private Integer refBlockNum;

    @SerializedName("ref_block_prefix")
    private Long refBlockPrefix;

    @SerializedName("max_net_usage_words")
    private long maxNetUsageWords;

    @SerializedName("max_cpu_usage_ms")
    private long maxCpuUsageMs;

    @SerializedName("delay_sec")
    private Integer delaySec;

    public TransactionHeader(){
    }

    @Override
    public String toString() {
        return "TransactionHeader{" +
                "expiration='" + expiration + '\'' +
                ", ref_block_num=" + refBlockNum +
                ", ref_block_prefix=" + refBlockPrefix +
                ", max_net_usage_words=" + maxNetUsageWords +
                ", max_cpu_usage_ms=" + maxCpuUsageMs +
                ", delay_sec=" + delaySec +
                '}';
    }

    public TransactionHeader(TransactionHeader other ){
        this.expiration = other.expiration;
        this.refBlockNum = other.refBlockNum;
        this.refBlockPrefix = other.refBlockPrefix;
        this.maxNetUsageWords = other.maxNetUsageWords;
        this.maxCpuUsageMs = other.maxCpuUsageMs;
        this.delaySec = other.delaySec;
    }

    public String getExpiration() {
        return expiration;
    }

    public String setExpiration() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(tz);
        String expiration = df.format(Date.from(Instant.now().plusSeconds(30 * 60)));
        this.expiration = expiration;
        return expiration;

//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        df.setTimeZone(tz);
//
//        //String expiration = "2018-07-10T06:55:55";
//        String expiration = df.format(System.currentTimeMillis());
//        this.expiration = expiration;
//        return expiration;
    }

    public void setReferenceBlock( String refBlockIdAsSha256 ) {
        refBlockNum = new BigInteger( 1, HexUtils.toBytes(refBlockIdAsSha256.substring(0,8))).intValue();

        refBlockPrefix = //new BigInteger( 1, HexUtils.toBytesReversed( refBlockIdAsSha256.substring(16,24))).longValue();
                BitUtils.uint32ToLong(HexUtils.toBytes(refBlockIdAsSha256.substring(16,24)), 0); // BitUtils treats bytes in little endian.
        // so, no need to reverse bytes.
    }

    public int getRefBlockNum() {
        return refBlockNum;
    }
    public long getRefBlockPrefix() {
        return refBlockPrefix;
    }


    public Date getExpirationAsDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse( dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public void putNetUsageWords(long netUsage) {
        this.maxNetUsageWords = netUsage;
    }

    public void putKcpuUsage(long kCpuUsage) {
        this.maxNetUsageWords = kCpuUsage;
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putIntLE( (int)(getExpirationAsDate(expiration).getTime() / 1000) ); // ms -> sec

        writer.putShortLE( (short)(refBlockNum  & 0xFFFF) );  // uint16
        writer.putIntLE( (int)( refBlockPrefix & 0xFFFFFFFF) );// uint32

        // fc::unsigned_int
        writer.putVariableUInt(maxNetUsageWords);
        writer.putVariableUInt( maxCpuUsageMs);
        writer.putVariableUInt( delaySec);
    }
}