import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.Request;

import java.util.ArrayList;

public class ContactService {
    private String host;
    private int port;
    private String endpoint;
    private RestClient restClient;

    public ContactService(String host, int port, String endpoint) {
        this.host = host;
        this.port = port;
        this.endpoint = endpoint;
        this.restClient = RestClient.builder( new HttpHost(host,port)).build();
    }

    public String addContact(Contact contact) {
        Request elasticRequest = new Request("POST", endpoint + contact.getName());
        contact.inputCheck();
        elasticRequest.setJsonEntity(contact.toString());
        try {
            Response elasticResponse = restClient.performRequest(elasticRequest);
            return new Gson().toJson(new JsonResponse(StatusResponse.SUCCESS));
        }
        catch (Exception e) {
            return new Gson().toJson(new JsonResponse(StatusResponse.ERROR));
        }
    }

    public Contact getContact(String name) {
        Request elasticRequest = new Request("GET",endpoint+name);
        try {
            Response elasticResponse = restClient.performRequest(elasticRequest);
            String responseBody = EntityUtils.toString(elasticResponse.getEntity());
            JsonParser parser = new JsonParser();
            JsonElement responseObj = parser.parse(responseBody).getAsJsonObject().get("_source");
            Contact contact = new Gson().fromJson(responseObj,Contact.class);
            return contact;
        }
        catch (Exception e){
            return null;
        }
    }

    public String updateContact(String name, Contact contact) {
        contact.inputCheck();
        try {
            Contact prevContact = this.getContact(name);
            if (prevContact==null) { return new Gson().toJson(new JsonResponse(StatusResponse.ERROR)); }
            Request elasticRequestPut = new Request("PUT",endpoint + name);

            elasticRequestPut.setJsonEntity(contact.toString());
            Response elasticResponse2 = restClient.performRequest(elasticRequestPut);
            return new Gson().toJson(new JsonResponse(StatusResponse.SUCCESS));
        }
        catch (Exception e) {
            return new Gson().toJson(new JsonResponse(StatusResponse.ERROR));
        }
    }

    public String deleteContact(String name) {
        try {
            Contact prevContact = this.getContact(name);
            if (prevContact==null) { return new Gson().toJson(new JsonResponse(StatusResponse.ERROR)); }
            Request elasticRequestDel = new Request("DELETE",endpoint + name);
            Response elasticResponse2 = restClient.performRequest(elasticRequestDel);
            return new Gson().toJson(new JsonResponse(StatusResponse.SUCCESS));
        }
        catch (Exception e) {
            return new Gson().toJson(new JsonResponse(StatusResponse.ERROR));
        }
    }

    public ArrayList<Contact> getQuery(int pageSize, int page, String query) {
        Request elasticRequestAll;
        if (query!=null) {
            elasticRequestAll = new Request("GET",endpoint + "_search?" + query);
        }
        else {
            elasticRequestAll = new Request("GET",endpoint + "_search?");
        }
        try {
            Response elasticResponse = restClient.performRequest(elasticRequestAll);
            String responseBody = EntityUtils.toString(elasticResponse.getEntity());
            JsonParser parser = new JsonParser();
            JsonArray responseObj = parser.parse(responseBody).getAsJsonObject().get("hits").getAsJsonObject().getAsJsonArray("hits");
            ArrayList<Contact> list = new ArrayList<>();

            for (int i=0;i<responseObj.size();i++) {
                if (i < page*pageSize && i >= (page-1)*pageSize) {
                    JsonElement current = responseObj.get(i).getAsJsonObject().get("_source");
                    Contact contact = new Gson().fromJson(current,Contact.class);
                    list.add(contact);
                }
            }
            return list;
        }
        catch (Exception e) {
            return null;
        }
    }
}
