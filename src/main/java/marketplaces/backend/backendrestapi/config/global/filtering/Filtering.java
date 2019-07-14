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
}
