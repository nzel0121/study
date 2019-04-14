package practice.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonPracticeTest {

    @Test
    public void fileOpenUsingGetResources()  {

        URL resource = this.getClass().getResource("input.json");
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
        } catch (IOException e) {
            Assert.fail();
        }

    }

    @Test
    public void fromJson() throws IOException, URISyntaxException {

        /* contents of input.json
        {
            "page":1,
            "per_page":10,
            "total":13,
            "total_pages":2,
            "data":[
                {
                    "name":"terry",
                    "age":36,
                    "role":"father"
                },
                {
                    "name":"beautifulSiver",
                    "age":36,
                    "role":"mother"
                },
                {
                    "name":"westLake",
                    "age":5,
                    "role":"son"
                }
            ]
        }
        */
        URL resource = this.getClass().getResource("input.json");
        String text = new String(Files.readAllBytes(Paths.get(resource.toURI())), StandardCharsets.UTF_8);

        JsonObject jsonObject = new Gson().fromJson(text, JsonObject.class);
        Assert.assertTrue(jsonObject.has("data"));

        JsonArray data = jsonObject.getAsJsonArray("data");

        List<String> names = new ArrayList<>();
        data.iterator().forEachRemaining(j ->{
            names.add(j.getAsJsonObject().get("name").getAsString());
        });
        Assert.assertTrue(names.contains("terry"));
    }
}