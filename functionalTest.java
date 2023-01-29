import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

public class JsonPlaceholderTest {

    @Test
    public void testAPI() {
        // Get a random user
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/users/1");
        int userID = response.jsonPath().getInt("id");
        String email = response.jsonPath().getString("email");
        System.out.println("User email: " + email);

        // Get user's associated posts
        response = RestAssured.get("https://jsonplaceholder.typicode.com/posts?userId=" + userID);
        for (int i = 0; i < response.jsonPath().getList("id").size(); i++) {
            int postID = response.jsonPath().getInt("id[" + i + "]");
            Assert.assertTrue(postID >= 1 && postID <= 100);
        }

        // Post using same userID
        String title = "Test Post from Kella";
        String body = "Test Body from Kella";
        response = RestAssured.given()
                .contentType("application/json")
                .body("{\"title\":\"" + title + "\",\"body\":\"" + body + "\",\"userId\":\"" + userID + "\"}")
                .post("https://jsonplaceholder.typicode.com/posts");
        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals("Test Post from Kella", response.jsonPath().getString("title"));
        Assert.assertEquals("Test Body from Kella", response.jsonPath().getString("body"));
    }
}

