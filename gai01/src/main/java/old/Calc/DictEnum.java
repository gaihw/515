package old.Calc;

public enum DictEnum {

    BTC(1),LTC(2),ETH(3),DASH(5),
    ETC(6),USDT(8),EOS(22),XRP(191),
    BCHABC(196),BCHSV(197),USDT_ERC20(200),
    ZEC(201),HT(199),OKB(202);

    private int currency;

    DictEnum(int currency) {
        this.currency = currency;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
