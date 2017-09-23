package au.com.edimoto;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SimpleServlet extends HttpServlet {

    private static final long serialVersionUID = -4751096228274971485L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo;

        String dbName = request.getParameter("db");

        if (dbName != null && dbName.equalsIgnoreCase("openshift")) {

            logDetails();

            MongoCredential credential = MongoCredential.createCredential(getMongoUser(), getDatabaseName(), getPassword());
            mongo = new MongoClient(new ServerAddress(getHostName(), 27017), Arrays.asList(credential));
        } else {
            mongo = new MongoClient("localhost", 27017);
        }

        DB db = mongo.getDB("soypepo");
        db.getCollectionNames().stream().forEach(collectionName -> {
            System.out.println(String.format("Collection Name: %s ",collectionName));
        });

//        Set<String> collections = db.getCollectionNames();
//        System.out.println(collections); // [datas, names, system.indexes, users]

        List<String> dbs = mongo.getDatabaseNames();
        System.out.println(dbs); // [journaldev, local, admin]

        response.getWriter().println(String.format("Hello World!!! %s",db.getCollectionNames()));
    }

    private void logDetails() {
        System.out.println(String.format("MONGODB_DB_HOST:%s MONGODB_USER:%s MONGODB_PASSWORD:%s MONGODB_DATABASE:%s", getHostName(), getMongoUser(), getPassword(), getDatabaseName()));
    }

    private String getHostName() {
        return System.getenv("MONGODB_DB_HOST");
    }

    private char[] getPassword() {
        return System.getenv("MONGODB_PASSWORD").toCharArray();
    }

    private String getDatabaseName() {
        return System.getenv("MONGODB_DATABASE");
    }

    private String getMongoUser() {
        return System.getenv("MONGODB_USER");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + this.getServletName() + " has stopped");
    }
}
