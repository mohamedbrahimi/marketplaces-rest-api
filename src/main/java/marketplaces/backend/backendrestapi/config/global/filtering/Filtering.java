package marketplaces.backend.backendrestapi.config.global.filtering;

public class Filtering {
    private int size;
    private int page;
    private String text;
    private int status;
    

    public Filtering(int size, int page) {
        this.size = size;
        this.page = page;
    }

    public Filtering(int size, int page, String text) {
        this.size = size;
        this.page = page;
        this.text = text;
    }

    public Filtering(int size, int page, String text, int status) {
        this.size = size;
        this.page = page;
        this.text = text;
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getText() {
        return text != null ?  text : "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
