package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

@Controller
public class TxtController {
    private List<Integer> data = new ArrayList<>();

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String content = IOUtils.toString(reader);
            String[] numbers = content.split(",");
            for (String number : numbers) {
                data.add(Integer.parseInt(number.trim()));
            }
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("data", data);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("number") int number, Model model) {
        long time = System.nanoTime();

        int index = interpolationSearch(data.toArray(new Integer[0]), number);

        time = System.nanoTime() - time;
        System.out.printf("Время %,9.3f ms\n", time/1_000_000.0);  // Замер исполнения функции

        model.addAttribute("index", index);

        return "index";
    }

    // Реализация интерполяционного поиска
    public static int interpolationSearch(Integer[] arr, int x) {
        Arrays.sort(arr);

        int kol_operation = 0;  //счетчик

        int low = 0, high = arr.length - 1;
        while (low <= high && x >= arr[low] && x <= arr[high]) {

            kol_operation += 1;
            System.out.println(kol_operation);     // Вывод количество циклов для нахождения

            int pos = low + ((x - arr[low]) * (high - low)) / (arr[high] - arr[low]); // вычисляется приблизительный индекс элемента

            if (arr[pos] == x)
                return pos;
            if (arr[pos] < x)
                low = pos + 1;
            else
                high = pos - 1;
        }

        return -1;
    }

    public static int binarySearch(Integer[] arr, int target) {
        Arrays.sort(arr);
        int left = 0;
        int right = arr.length - 1;

        int kol_operation = 0;

        while (left <= right) {

            kol_operation += 1;
            System.out.println(kol_operation);          // Вывод количество циклов дл нахождения

            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1; // Число не найдено
    }

}
