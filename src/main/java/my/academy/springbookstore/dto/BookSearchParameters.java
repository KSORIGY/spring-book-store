package my.academy.springbookstore.dto;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns) {
}
