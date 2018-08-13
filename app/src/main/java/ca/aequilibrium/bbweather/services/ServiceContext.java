package ca.aequilibrium.bbweather.services;



public class ServiceContext {
    public static BookmarkedLocationService bookmarkedCityManager = new BookmarkedLocationServiceImpl();

    public static BookmarkedLocationService getBookmarkedCityManager() {
        return bookmarkedCityManager;
    }

    public static void setBookmarkedCityManager(BookmarkedLocationService bookmarkedCityManager) {
        ServiceContext.bookmarkedCityManager = bookmarkedCityManager;
    }
}
