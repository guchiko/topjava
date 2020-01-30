package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 11, 0), "MC", 4410)
        );

        List<UserMealWithExcess> mealsToC = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToC.forEach(System.out::println);
        System.out.println();
        List<UserMealWithExcess> mealsToS = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToS.forEach(System.out::println);


    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> res = new ArrayList<UserMealWithExcess>();
        Map<LocalDate, Integer> eatedByDay = new HashMap<>();
//        for (UserMeal meal : meals) {
//            LocalDate day = meal.getDateTime().toLocalDate();
//            eatedByDay.put(day,eatedByDay.getOrDefault(day,0)+meal.getCalories());
//        }
        for (UserMeal meal : meals) {
            eatedByDay.merge(meal.getDate(),meal.getCalories(), (cal1,cal2) -> cal1+cal2);
        }
        for (UserMeal meal : meals) {
            if (meal.getTime().isAfter(startTime) && meal.getTime().isBefore(endTime)){
                res.add(new UserMealWithExcess(meal.getDateTime(),meal.getDescription(),meal.getCalories(),
                        eatedByDay.getOrDefault(meal.getDate(),0) > caloriesPerDay  ));
            }
        }
        return res;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> eatedByDay = meals.stream().collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream().filter(t->t.eatedBetweenTime(startTime,endTime))
                .map(meal->new UserMealWithExcess(meal.getDateTime(),meal.getDescription(),meal.getCalories(),
                        eatedByDay.getOrDefault(meal.getDate(),0)>caloriesPerDay
                        )).collect(Collectors.toList());
    }



}
