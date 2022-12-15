package algonquin.cst2335.moviemaster.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.moviemaster.databinding.FragmentSearchresultsBinding;

public class SearchResultsFragment extends Fragment {

    private FragmentSearchresultsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchResultsViewModel searchResultsViewModel =
                new ViewModelProvider(this).get(SearchResultsViewModel.class);

        binding = FragmentSearchresultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        searchResultsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
