import com.google.gson.*;
import java.util.ArrayList;

import static spark.Spark.*;

public class SparkAPI {
    public static void main(String[] args) {
        /*
        POST /contact
        GET /contact/{name}
        PUT /contact/{name}
        DELETE /contact/{name}
        GET /contact?pageSize={}&page={}&query={}
         */

        //modify contactService inputs to change host/port values
        ContactService contactService = new ContactService("localhost",9200,"/tbook/contacts/");

        post("/contact", (request,response) -> {
            response.type("application/json");
            Contact contact = new Gson().fromJson(request.body(),Contact.class);
            return contactService.addContact(contact);
        });

        get("/contact/:name", (request,response) -> {
           response.type("application/json");
           Contact ret = contactService.getContact(request.params(":name"));
           if (ret!=null) { return ret; }
           else { return new Gson().toJson(new JsonResponse(StatusResponse.ERROR)); }
        });

        put("/contact/:name", (request,response) -> {
            response.type("application/json");
            Contact contact = new Gson().fromJson(request.body(),Contact.class);
            return contactService.updateContact((request.params(":name")),contact);
        });

        delete("/contact/:name", (request,response) -> {
            response.type("application/json");
            return contactService.deleteContact(request.params(":name"));
        });

        //get for query wanted
        //query format should start at "q="
        //example: "q=company:Brightling"
        get("/contact/:pageSize/:page/:query", (request,response) -> {
            response.type("application/json");
            int pageSize = Integer.parseInt(request.params(":pageSize"));
            int page = Integer.parseInt(request.params(":page"));
            ArrayList<Contact> list = contactService.getQuery(pageSize,page,request.params(":query"));
            if (list==null) {return new Gson().toJson(new JsonResponse(StatusResponse.ERROR)); }
            else { return new Gson().toJson(list); }
        });

        //get for no query specified- returns all
        get("/contact/:pageSize/:page", (request,response) -> {
            response.type("application/json");
            int pageSize = Integer.parseInt(request.params(":pageSize"));
            int page = Integer.parseInt(request.params(":page"));
            ArrayList<Contact> list = contactService.getQuery(pageSize,page,null);
            if (list==null) {return new Gson().toJson(new JsonResponse(StatusResponse.ERROR)); }
            else { return new Gson().toJson(list); }
        });
    }
}
