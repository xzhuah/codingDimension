package common.io.web.models;

/**
 * Created by Xinyu Zhu on 7/1/2020, 9:15 AM
 * common.io.web.models in AllInOne
 */
public class WebpageRawHtmlDTO implements ResponseProcessResult {
    private String rawHtml;

    public WebpageRawHtmlDTO(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public WebpageRawHtmlDTO setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
        return this;
    }

    @Override
    public String toString() {
        return "WebpageRawHtmlDTO{" +
                "rawHtml='" + rawHtml + '\'' +
                '}';
    }
}
