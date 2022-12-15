package algonquin.cst2335.moviemaster.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchResultsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SearchResultsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
