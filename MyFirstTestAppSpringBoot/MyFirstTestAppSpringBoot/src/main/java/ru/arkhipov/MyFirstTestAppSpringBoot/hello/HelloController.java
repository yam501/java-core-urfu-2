package ru.arkhipov.MyFirstTestAppSpringBoot.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class HelloController {

    private ArrayList<String> arrayList;
    private HashMap<Integer, String> hashMap;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    @GetMapping("/update-array")
    public String updateArrayList(@RequestParam String s) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(s);
        return "Added to ArrayList: " + s;
    }

    @GetMapping("/show-array")
    public String showArrayList() {
        if (arrayList == null || arrayList.isEmpty()) {
            return "ArrayList is empty";
        }
        return arrayList.toString();
    }

    @GetMapping("/update-map")
    public String updateHashMap(@RequestParam String s) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        hashMap.put(hashMap.size(), s);
        return "Added to HashMap: " + hashMap.size() + " -> " + s;
    }

    @GetMapping("/show-map")
    public String showHashMap() {
        if (hashMap == null || hashMap.isEmpty()) {
            return "HashMap is empty";
        }
        return hashMap.toString();
    }

    @GetMapping("/show-all-lenght")
    public String showAllLenght() {
        int arraySize = (arrayList == null) ? 0 : arrayList.size();
        int mapSize = (hashMap == null) ? 0 : hashMap.size();
        return "ArrayList size: " + arraySize + ", HashMap size: " + mapSize;
    }
}
