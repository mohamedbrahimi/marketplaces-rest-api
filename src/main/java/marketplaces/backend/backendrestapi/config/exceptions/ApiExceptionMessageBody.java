package marketplaces.backend.backendrestapi.config.exceptions;

public class ApiExceptionMessageBody {
    private String fr;
    private String ar;
    private String en;

    public ApiExceptionMessageBody(String fr, String ar, String en) {
        this.fr = fr;
        this.ar = ar;
        this.en = en;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}