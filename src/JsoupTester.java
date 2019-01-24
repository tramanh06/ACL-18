import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.aitools.aq.wrappers.CompositeWrapper;

import java.io.IOException;
import javax.xml.bind.JAXBException;

import de.aitools.ie.articles.Article;
import de.aitools.ie.articles.Veracity;

import java.io.File; 
import java.io.BufferedReader;
import java.io.FileReader;

public class JsoupTester {

    private static void processUrl(String url, String articleId, Veracity veracity) throws IOException, JAXBException {
        String baseLocation = "data/sgArticles/";
        Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .get();

        // StraitsTimesWrapper st = new StraitsTimesWrapper();
        CompositeWrapper compWrapper = new CompositeWrapper();
        Article article = compWrapper.parse(doc, url);
        article.setVeracity(veracity);

        System.out.println("author = "+ article);

        File f = new File(baseLocation + "f" + articleId + ".xml"); 
        article.write(f);
    }

    public static void main(String[] args) throws IOException, JAXBException {
        //String csvFile = "C:/git/fake-news-work/data/nonFakenewsCases_withUrl.csv";
        String csvFile = "C:/git/fake-news-work/data/fakenewsCases_withUrl.csv";
        String line = "";
        String cvsSplitBy = "\\|";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

               // use comma as separator
               String[] row = line.split(cvsSplitBy);

               String caseId = row[0];
               // Exclude case 11. Page currently cannot be loaded from jsoup
               if(caseId.equals("11")){
                   continue;
               }
               String title = row[1];
               String url = row[2];
               System.out.println("Non-fake news case [Case ID= " + caseId + " , title=" + title + " , url=" + url + "]");
               Veracity veracity = Veracity.MOSTLY_TRUE;
               processUrl(url, caseId, veracity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // private 
}