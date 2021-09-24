package allin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kouling {

    static {
        System.setProperty("fileName", "kouling/kouling.log");
    }
    public static Logger log = LoggerFactory.getLogger(Kouling.class);
    public static void main(String[] args) {
        String param = "seckey=jNWQUT7XEwHELynK8pLuneVOOXwpPqDTQMR2HnDNwm4c4SkwsbtqUDaS/jbp1MCPUyLPJpRRpgVtw8b7f1yDUuwvUXdpO+30l+B5yu7nDiKI+I3vI6oXwTyCNcmh12g8eqkoC98cWb+Qa6aORoab6OO3HeRekKdUY3tzmo54VIs=&certFilePath=/opt/software/encryption_demo/test/client_cert.cer&privatekey=-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXgIBAAKBgQCbpOP6naqANrWLjI/xnzbE3rnz69dXfWiaaqk1uUOnuOLL9XG9\n" +
                "bARtohl6FnTPO6HUQPgIBgijRMkNvCgwfy0cF+H9sFqEryEgb0z7YKqAECBen7Zd\n" +
                "xWIV8ZlmFUiMKJccBykowjDGyVlDVrs8LLAZVM4kpS0OS+rlcNMfJg5eNwIDAQAB\n" +
                "AoGAA57b0MYfI7liErtz076M4xp9P9/SFA5uQv1NL1s+aCXkXLM6cJoVJkm5hv4D\n" +
                "vJhaBNp4CpO9WrLsbF3en3Yt4eqd+EwxZn2xqetShRCdRZGWYz8Rj3I/KUb4nJGc\n" +
                "jUQ+u2+tebkFDDE2VYGLW3EVmvu2ctwVs5IjP7QulW1FJl0CQQDRxP/lOBbtEEtq\n" +
                "z3zQ80cgTplExtNwufPFVoTYguvAwjB1UCG/6ioCO2mRyFEnexDRLLfIcmYxDPzi\n" +
                "VN01U37DAkEAvfIvYz2EfvREGf6llMfmbQOWmIMkmehGwhFrZruSVYc5leSMAWQM\n" +
                "JtQt620rQtPLQKPuk+yBAXTmDKPFMosTfQJBALC2PUAzLRUOGgpjumkn2C4+KF3J\n" +
                "BQw8ikAer5DZUZMzw7DADX5n3HW+6SM3Vi5lPPiJrFT/ElmV8F8WULeAnBMCQQCR\n" +
                "3x4ZXrMUNE7gj7llyybg7XKH6Z8AO2urumj62m4FFPZacMw8fTL/1s71ED+68KAV\n" +
                "IlAkdeqUzcumRVc5+6iZAkEA0Xh11m93YDiQ93BNtu0pAYuQ+Jk8QxFbPOeI2m0H\n" +
                "L/y/iiaBMMTkD0L+fBivozmksbKe3Vvt9boBoSmGOGcnhg==\n" +
                "-----END RSA PRIVATE KEY-----\n";

        String url = "http://10.70.0.126:7777/envelopeDecryption";

        System.out.println(BaseUtils.postByForm(url,param));
    }
}
