import POJO.PostPojo;
import POJO.UserPojo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredHW {

    private RequestSpecification reqSpec;
    private UserPojo user;
    private UserPojo editedUser;
    private PostPojo post;
    private PostPojo editedPost;

    @BeforeClass
    public void setUp(){

       RestAssured.baseURI = "https://gorest.co.in";

       reqSpec =  given()
               .log().uri()
               .header("Authorization","Bearer 590c3804ec38d2386ebdac97b091e4b51fc1325dd65049749c97008bba36b413")
               .contentType(ContentType.JSON);

       user = new UserPojo();
       user.setName("Laaaaaaafghjhavdfjk");
       user.setEmail("ijgdwtgbczdddmvfdvdfavail@email.com");
       user.setGender("male");
       user.setStatus("active");

       editedUser = new UserPojo();
       editedUser.setName("bkhjblgvkjhgvbvSacsdcb");
       editedUser.setEmail("laabvghvfvfedavmvkhvhbjh@email.com");


       post = new PostPojo();
       user.getId();
       post.setTitle("ttttasasdweddfghjmkl");
       post.setBody("bbbhlıcdscdknknç");



    }

    @Test
    public void createNewUser(){

       user.setId(given()
               .spec(reqSpec)
               .body(user)
               .when()
               .post("/public/v2/users")
               .then()
               .log().body()
               .statusCode(201)
               .body("name",equalTo(user.getName()))
               .body("email",equalTo(user.getEmail()))
               .body("gender",equalTo(user.getGender()))
               .extract().jsonPath().getString("id"));

        System.out.println("New Created User Id = " + user.getId());

    }

    @Test(dependsOnMethods = "createNewUser")
    public void createUserNegativeTest(){

        given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .statusCode(422);
    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void createPost(){

        post.setPostId( given()
                .spec(reqSpec)
                .body(post)
                .when()
                .post("/public/v2/users/" + user.getId() + "/posts")
                .then()
                .statusCode(201)
                .body("title",equalTo(post.getTitle()))
                .extract().jsonPath().getString("id"));

        System.out.println(" New Created Post Id = " + post.getPostId());
    }

    @Test(dependsOnMethods = "createPost")
    public void editPost(){

        editedPost = new PostPojo();
        editedPost.setBody("dxtucjkvhbjçjşıoljkceadjmh");

        given()
                .spec(reqSpec)
                .body(editedPost)
                .when()
                .post("/public/v2/posts/" + post.getPostId())
                .then()
                .statusCode(201)
                .body("body",equalTo(editedPost.getBody()));
    }

    @Test(dependsOnMethods = "editPost")
    public void deletePost(){

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/" + post.getPostId())
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePost")
    public void deletePostNegativeTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/" + post.getPostId())
                .then()
                .statusCode(404);
    }

    @Test(dependsOnMethods = "deletePostNegativeTest")
    public void deleteUser(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/" + post.getPostId())
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegativeTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/" + post.getPostId())
                .then()
                .statusCode(404);
    }

}
