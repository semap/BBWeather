package ca.aequilibrium.bbweather.services;


import ca.aequilibrium.bbweather.BBWeatherApplication;

public class ServiceContext {
    public static BookmarkedLocationService bookmarkedLocationService = new BookmarkedLocationServiceImpl(BBWeatherApplication.getAppContext());

    public static BookmarkedLocationService getBookmarkedLocationService() {
        return bookmarkedLocationService;
    }

    public static void setBookmarkedLocationService(BookmarkedLocationService bookmarkedLocationService) {
        ServiceContext.bookmarkedLocationService = bookmarkedLocationService;
    }
}
