package ca.aequilibrium.bbweather.views.adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;

public class BookmarkedCityDiffCallback extends DiffUtil.Callback {

    private List<BookmarkedCity> newListData;
    private List<BookmarkedCity> oldListData;

    public BookmarkedCityDiffCallback(List<BookmarkedCity> oldListData, List<BookmarkedCity> newListData) {
        this.oldListData = oldListData;
        this.newListData = newListData;
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        BookmarkedCity oldLocation = oldListData.get(oldItemPosition);
        BookmarkedCity newLocation = newListData.get(newItemPosition);
        return oldLocation.getId().equals(newLocation.getId());
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        BookmarkedCity oldLocation = oldListData.get(oldItemPosition);
        BookmarkedCity newLocation = newListData.get(newItemPosition);
        return oldLocation.getCoord().equals(newLocation.getCoord());
    }

    @Override
    public int getNewListSize() {
        return newListData.size();
    }

    @Override
    public int getOldListSize() {
        return oldListData.size();
    }
}
