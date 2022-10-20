package APITests;

import API.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqresTestWithoutPOJO {
    private final static String URL = "https://reqres.in";

    @Test
    public void checkAvatarAndIdUsers() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Response response = given().
                when().
                get("/api/users?page=2").
                then().extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> avatars = jsonPath.get("data.avatar");
        List<Integer> ids = jsonPath.get("data.id");
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }

        List<String> emails = jsonPath.get("data.email");

        Assert.assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
    }

    @Test
    public void successLogin() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = "QpwL5tke4Pnpja7X4";
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "cityslicka");

        given().
                body(user).
                when().
                post("/api/login").
                then().log().all().
                assertThat().
                body("token", equalTo(token));
    }

    @Test
    public void unSuccessLogin() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());

        String error = "Missing password";
        Map<String, String> user = new HashMap<>();
        user.put("email", "peter@klaven");

        given().
                body(user).
                when().
                post("/api/login").
                then().log().all().
                assertThat().
                body("error", equalTo(error));
    }


    @Test
    public void successRegister() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");

        given().
                body(user).
                when().
                post("/api/register").
                then().log().all().
                assertThat().
                body("id", equalTo(id)).
                body("token", equalTo(token));
    }

    @Test
    public void unSuccessRegister() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());

        String error = "Missing password";
        Map<String, String> user = new HashMap<>();
        user.put("email", "sydney@fife");

        given().
                body(user).
                when().
                post("/api/register").
                then().log().all().
                assertThat().
                body("error", equalTo(error));
    }

    @Test
    public void sortedYearsForColorsList() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Response response = given().
                when().
                get("/api/unknown").
                then().log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> years = jsonPath.get("data.year");
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(sortedYears, years);
    }

    @Test
    public void deleteUser() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUniq(204));

        given().
                when().
                delete("/api/users/2");
    }

}


