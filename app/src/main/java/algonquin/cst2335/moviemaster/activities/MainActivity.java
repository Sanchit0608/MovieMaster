package algonquin.cst2335.moviemaster.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.moviemaster.R;
import algonquin.cst2335.moviemaster.database.MovieDatabase;
import algonquin.cst2335.moviemaster.databinding.ActivityMainBinding;
import algonquin.cst2335.moviemaster.databinding.MovieTileViewBinding;
import algonquin.cst2335.moviemaster.model.MainActivityViewModel;
import algonquin.cst2335.moviemaster.model.Movie;
import algonquin.cst2335.moviemaster.model.MovieDetailsFragmentMain;
import algonquin.cst2335.moviemaster.database.MovieDAO;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView.Adapter<MovieRow> adapter;
    private ArrayList<Movie> movies = new ArrayList<>();
    private MainActivityViewModel viewModel;
    private RequestQueue requestQueue;
    private MovieDAO movieDAO;

    @Override
    protected void onResume() {
        super.onResume();

        Executor dataThread = Executors.newSingleThreadExecutor();
        dataThread.execute(() -> {
            movies = new ArrayList<>();
            movies.addAll(movieDAO.getAllMovies());
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.mainToolbar);
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        movies = viewModel.movies.getValue();

        requestQueue = Volley.newRequestQueue(this);

        MovieDatabase database = Room.databaseBuilder(getApplicationContext(), MovieDatabase.class, "Movies").build();
        movieDAO = database.movieDAO();

        if(movies == null) {
            viewModel.movies.postValue(movies = new ArrayList<>());
            Executor dataThread = Executors.newSingleThreadExecutor();
            dataThread.execute(() -> {
                movies.addAll(movieDAO.getAllMovies());
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            });
        }

        viewModel.selectedMovie.observe(this, (newMovieValue) -> {
            MovieDetailsFragmentMain movieDetailsFragment = new MovieDetailsFragmentMain(newMovieValue, adapter, movies);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainFragmentFrame, movieDetailsFragment)
                    .addToBackStack("")
                    .commit();
        });

        binding.mainMovieRecycler.setAdapter(adapter = new RecyclerView.Adapter<MovieRow>() {
            @NonNull
            @Override
            public MovieRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                MovieTileViewBinding binding = MovieTileViewBinding.inflate(getLayoutInflater());
                return new MovieRow(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MovieRow holder, int position) {
                ImageRequest imageRequest = new ImageRequest(
                        movies.get(position).getCoverURL(),
                        (Bitmap response) -> {
                            holder.movieImage.setImageBitmap(response);
                        },
                        200, 400, ImageView.ScaleType.CENTER, null,
                        error -> {
                            System.err.println("ERROR GETTING IMAGE");
                        }
                );
                requestQueue.add(imageRequest);

                //set TextView values
                holder.movieName.setText(movies.get(position).getName());
                holder.movieYear.setText(movies.get(position).getYear());
                holder.movieType.setText(movies.get(position).getType());
            }

            @Override
            public int getItemCount() {
                return movies.size();
            }
        });

        binding.mainMovieRecycler.setLayoutManager(new GridLayoutManager(this, 1));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_bar_search:
                startActivity(new Intent(this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieRow extends RecyclerView.ViewHolder {

        ImageView movieImage;
        TextView movieName;
        TextView movieYear;
        TextView movieType;

        public MovieRow(@NonNull View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.movieImage);
            movieName = itemView.findViewById(R.id.movieName);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieType = itemView.findViewById(R.id.movieType);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                Movie selectedMovie = movies.get(position);

                viewModel.selectedMovie.postValue(selectedMovie);
            });
        }
    }
}