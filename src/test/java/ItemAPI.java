import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

public class ItemAPI {
    private static final String EMAIL = "Enter your email";
    private static final String PASSWORD = "Enter your password";
    private static final String ITEMS_URL = "/items";
    private static final String ITEM_URL = "/item";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static {
        RestAssured.baseURI = "Enter your base url"; //example https://st2016.inv.bg
        RestAssured.basePath = "/RESTapi";
        RestAssured.authentication = RestAssured.preemptive().basic(EMAIL, PASSWORD);
    }


    public static Response getAllItems(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .when()
                .get(ITEMS_URL);
        System.out.println("Response:");
        response.prettyPrint();
        return response;
    }

    public static void deleteAllItems(){
        Response allItemsResp = getAllItems(); //Retrieves all items from the system as json
        List<Integer> ids = JsonPath.read(allItemsResp.body().asString(), "$.items..id"); //Extracts all ids from the json
        System.out.println("Ids found for deletion:" + ids.toString());
        //Delete all ids
        ids.forEach(id -> deleteItem(String.valueOf(id))); //Deletes all items one by one
    }

    public static Response getItem(String id){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON) //Content type head
                .accept(ContentType.JSON) //Accept header
                .log().all() //Logs request
                .when()
                .get(ITEM_URL + "/" + id); //Sets the http verb to GET
        System.out.println("Response:");
        response.prettyPrint(); //Prints the response
        return response;
    }

    public static Response deleteItem(String id){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .when()
                .delete(ITEM_URL + "/" + id);
        System.out.println("Response:");
        response.prettyPrint();
        return response;
    }

    public static Response createItem(Item item){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .body(GSON.toJson(item))
                .when()
                .post(ITEM_URL);
        System.out.println("Response:");
        response.prettyPrint();
        return response;
    }
}
