package algonquin.cst2335.moviemaster.model;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.moviemaster.database.MovieDatabase;
import algonquin.cst2335.moviemaster.databinding.MovieDetailsViewBinding;
import algonquin.cst2335.moviemaster.database.MovieDAO;


public class MovieDetailsFragment extends Fragment {
    private static final String baseUrl = "http://www.omdbapi.com/?apikey=6c9862c2&t=";
    private MovieDetailsViewBinding binding;
    private Movie selectedMovie;
    private RequestQueue requestQueue;
    private MovieDAO movieDAO;
    private MovieDatabase database;

    private MovieDetailsFragment() {}

    public MovieDetailsFragment(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Set Binding
        MovieDetailsViewBinding binding = MovieDetailsViewBinding.inflate(inflater);

        //DB access variables
        database = Room.databaseBuilder(getContext(), MovieDatabase.class, "Movies")
                .build();
        movieDAO = database.movieDAO();

        requestQueue = Volley.newRequestQueue(this.getContext());

        try {
            String url = baseUrl + URLEncoder.encode(selectedMovie.getName(), "UTF-8");
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            selectedMovie = MovieBuilder.create(selectedMovie)
                                    .setMainActors(response.getString("Actors"))
                                    .setPlot(response.getString("Plot"))
                                    .setRating(response.getString("Rated"))
                                    .setRunTime(response.getString("Runtime"))
                                    .build();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        binding.fragMovieTitle.setText(selectedMovie.getName());
                        binding.fragMovieRuntime.setText(selectedMovie.getRunTime());
                        binding.fragMovieYear.setText(selectedMovie.getYear());
                        binding.fragMoviePlot.setText(selectedMovie.getPlot());
                        binding.fragMovieActors.setText(selectedMovie.getMainActors());
                        binding.fragMovieRated.setText(selectedMovie.getRating());
                    },
                    (error) -> {
                        binding.fragMovieTitle.setText("ERROR");
                    }
            );

            ImageRequest imageRequest = new ImageRequest(
                    selectedMovie.getCoverURL(),
                    (Bitmap response) -> {
                        binding.fragMoviePoster.setImageBitmap(response);
                    },
                    200, 400, ImageView.ScaleType.CENTER, null,
                    error -> {
                        System.err.println("ERROR GETTING IMAGE");
                    }
            );

            requestQueue.add(jsonRequest);
            requestQueue.add(imageRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        binding.fragMovieAddFavouriteButton.setOnClickListener(click -> {

            //Saving movie
            Executor databaseThread = Executors.newSingleThreadExecutor();
            databaseThread.execute(() -> {
                movieDAO.insertMovie(selectedMovie);
            });

            //display toast message when adding to favourites
            Toast.makeText(
                    getContext(),
                    String.format("%s added to favourites", selectedMovie.getName()),
                    Toast.LENGTH_SHORT
            ).show();

            //Shutdown fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });


        return binding.getRoot();
    }
}
