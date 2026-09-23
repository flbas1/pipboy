package flbas1.pipboy.model;

import java.util.List;

 public record MenuProfile(
         String profile,
         String title,
         String theme,
         Menu menu
 ) {
     public record Menu(
             List<String> food,
             List<String> treats,
             List<String> beverages,
             List<String> dessert
     ) {
     }
 }


