package beer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class BeerManager {
    List<BeerCatalog> beers = new ArrayList<>();

    public void readJsonFile(Path path) {
        String readJsonFile;
        try {
            readJsonFile = new String(Files.readAllBytes(path));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Cannot read file!", ioe);
        }

        JSONArray jsonArray = new JSONArray((readJsonFile));
        initBeers(jsonArray);
    }

    private void initBeers(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String brand = jsonObject.getString("brand");
            String type = jsonObject.getString("type");
            int price = jsonObject.getInt("price");
            double alcohol = jsonObject.getDouble("alcohol");
            JSONArray ingredientsData = jsonObject.getJSONArray("ingredients");
            JSONArray jsonArrayIngredients = new JSONArray((ingredientsData));

            List<Ingredient> ingredients = initIngredients(jsonArrayIngredients);

            BeerCatalog bc = new BeerCatalog(id, name, brand, type, price, alcohol, ingredients);
            beers.add(bc);
        }
    }

    private List<Ingredient> initIngredients(JSONArray jsonArrayIngredients) {
        List<Ingredient> result = new ArrayList<>();
        for (int k = 0; k < jsonArrayIngredients.length(); k++) {
            JSONObject jsonObjectIngredient = jsonArrayIngredients.getJSONObject(k);
            String nameOfIngredients = jsonObjectIngredient.getString("name");
            double ratioOfIngredient = jsonObjectIngredient.getDouble("ratio");
            result.add(new Ingredient(nameOfIngredients, ratioOfIngredient));
        }
        return result;
    }

    public void groupBeersByBrand() {
        List<String> beersByGroups;
        Map<String, List<String>> brandAndBeers = new HashMap<>();

        for (BeerCatalog bc : beers) {
            beersByGroups = brandAndBeers.computeIfAbsent(bc.getBrand(), k -> new ArrayList<>());
            beersByGroups.add(bc.getId());
        }

        String stringFromJsonArrayResult = String.valueOf(addKeysToBrandAndBeers(brandAndBeers));

        String nameOfTheTask = "groupBeersByBrand";
        writeSolutionToJsonFile(stringFromJsonArrayResult, nameOfTheTask);
        System.out.println(stringFromJsonArrayResult);
    }

    private JSONArray addKeysToBrandAndBeers(Map<String, List<String>> brandAndBeers) {
        JSONArray jsonArray = new JSONArray();
        for (String s : brandAndBeers.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("brand", s);
            jsonObject.put("beers", brandAndBeers.get(s));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


    public void filterBeersByBeerType(String type) {
        List<String> beerIds = beers.stream()
                .filter(k -> k.getType().equals(type))
                .map(BeerCatalog::getId)
                .collect(Collectors.toList());
//                .toList();

        Gson gson = new Gson();
        String result = gson.toJson(beerIds);

        String nameOfTheTask = "filterBeersByBeerType";
        writeSolutionToJsonFile(result, nameOfTheTask);
        System.out.println(result);
    }

    public void getTheCheapestBrand() {
        Map<String, BrandsWithPrices> tempBrandsAndPrices = new HashMap<>();

        for (BeerCatalog bc : beers) {
            String brand = bc.getBrand();
            BrandsWithPrices tempBrandsWithPrices = tempBrandsAndPrices
                    .computeIfAbsent(brand, k -> new BrandsWithPrices(brand));
            tempBrandsWithPrices.incrementPrice(bc.getPrice());
        }

        BrandsWithPrices bwp = tempBrandsAndPrices.values().stream()
                .min(Comparator.comparing(BrandsWithPrices::getAveragePrice))
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));

        Gson gson = new Gson();
        String result = gson.toJson(bwp.getBrandName());

        String nameOfTheTask = "getTheCheapestBrand";
        writeSolutionToJsonFile(result, nameOfTheTask);
        System.out.println(result);
    }

    public void getIdsThatLackSpecificIngredient(String ingredient) {
        List<String> idsWithoutSpecificIngredient = new ArrayList<>();
        for (BeerCatalog actual : beers) {
            if (actual.checkIfIngredientNotInclude(ingredient)) {
                idsWithoutSpecificIngredient.add(actual.getId());
            }
        }
        Gson gson = new Gson();
        String result = gson.toJson(idsWithoutSpecificIngredient);
        String nameOfTheTask = "getIdsThatLackSpecificIngredient";
        writeSolutionToJsonFile(result, nameOfTheTask);
        System.out.println(result);
    }

    public void sortAllBeersByRemainingIngredientRatio() {
        List<String> beerIds = beers.stream()
                .sorted(Comparator.comparing(BeerCatalog::getWaterIngredient).thenComparing(BeerCatalog::getId))
                .map(BeerCatalog::getId)
                .toList();
        Gson gson = new Gson();
        String result = gson.toJson(beerIds);
        String nameOfTheTask = "sortAllBeersByRemainingIngredientRatio";
        writeSolutionToJsonFile(result, nameOfTheTask);
        System.out.println(result);
    }

    public void listBeersBasedOnTheirPriceWithATip() {
        Map<Integer, List<String>> beersAndRoundedPrices = new TreeMap<>();
        for(BeerCatalog bc : beers){
            int roundedPrice = ((bc.getPrice() / 100) + 1) * 100;
            List<String> tempBeers = beersAndRoundedPrices.computeIfAbsent(roundedPrice, k -> new ArrayList<>());
            tempBeers.add(bc.getId());
        }
        String stringFromJsonArrayResult = String.valueOf(addKeysToPriceAndBeers(beersAndRoundedPrices));

        String nameOfTheTask = "listBeersBasedOnTheirPriceWithATip";
        writeSolutionToJsonFile(stringFromJsonArrayResult, nameOfTheTask);
        System.out.println(stringFromJsonArrayResult);
    }

    private JSONObject addKeysToPriceAndBeers(Map<Integer, List<String>> beersAndRoundedPrices) {
        JSONObject jsonObject = new JSONObject();
        for (Integer actual : beersAndRoundedPrices.keySet()) {
            jsonObject.put(String.valueOf(actual), beersAndRoundedPrices.get(actual));
        }
        return jsonObject;
    }

    public void writeSolutionToJsonFile(String result, String nameOfTheTaks) {
        String path = "./" + nameOfTheTaks + "Solution.json";
        try (FileWriter file = new FileWriter(path)) {
            file.write(result);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Cannot write file!", ioe);
        }
    }


    public static void main(String[] args) {
        BeerManager beerManager = new BeerManager();
        beerManager.readJsonFile(Paths.get("./demo.json"));
        beerManager.groupBeersByBrand();
        beerManager.filterBeersByBeerType("Corn");
        beerManager.getTheCheapestBrand();
        beerManager.getIdsThatLackSpecificIngredient("corn");     //maybe a map for ingredients instead of list
        beerManager.sortAllBeersByRemainingIngredientRatio();
        beerManager.listBeersBasedOnTheirPriceWithATip();
    }
}
