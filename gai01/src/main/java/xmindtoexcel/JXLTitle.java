package xmindtoexcel;

public class JXLTitle {

    private String titleName;
    private int titleLength;

    public JXLTitle(){
        super();
    }

    public JXLTitle(String titleName, int titleLength){
        this.titleName = titleName;
        this.titleLength = titleLength;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getTitleLength() {
        return titleLength;
    }

    public void setTitleLength(int titleLength) {
        this.titleLength = titleLength;
    }
}
