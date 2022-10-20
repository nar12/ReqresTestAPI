package APITests;

import API.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTestWithPOJO {
    private final static String URL = "https://reqres.in";

    @Test
    public void checkAvatarAndIdUsers() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        List<UserData> users = given().
                when().
                get("/api/users?page=2").
                then().extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); ++i) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void successLogin() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = "QpwL5tke4Pnpja7X4";
        Login user = new Login("eve.holt@reqres.in", "cityslicka");
        SuccessLogin successLogin = given().
                body(user).
                when().
                post("/api/login").
                then().log().all().
                extract().as(SuccessLogin.class);

        Assert.assertNotNull(successLogin.getToken());
        Assert.assertEquals(token, successLogin.getToken());
    }

    @Test
    public void UnSuccessLogin() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());

        String error = "Missing password";
        Login user = new Login("peter@klaven", "");
        UnSuccessLogin unSuccessLogin = given().
                body(user).
                when().
                post("/api/login").
                then().log().all().
                extract().as(UnSuccessLogin.class);

        Assert.assertNotNull(unSuccessLogin.getError());
        Assert.assertEquals(error, unSuccessLogin.getError());
    }

    @Test
    public void successRegister() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");

        SuccessRegister successRegister = given().
                body(user).
                when().
                post("/api/register").
                then().log().all().extract().as(SuccessRegister.class);

        Assert.assertNotNull(successRegister.getId());
        Assert.assertNotNull(successRegister.getToken());
        Assert.assertEquals(id, successRegister.getId());
        Assert.assertEquals(token, successRegister.getToken());
    }

    @Test
    public void unSuccessRegister() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());

        String error = "Missing password";
        Register user = new Register("sydney@fife", "");

        UnSuccessRegister unSuccessRegister = given().
                body(user).
                when().
                post("/api/register").
                then().log().all().extract().as(UnSuccessRegister.class);

        Assert.assertEquals(error, unSuccessRegister.getError());
        Assert.assertNotNull(unSuccessRegister.getError());

    }

    @Test
    public void sortedYearsForColorsList() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        List<ColorsData> colors = given().
                when().
                get("/api/unknown").
                then().log().all().extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
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


