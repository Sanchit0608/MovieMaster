package algonquin.cst2335.moviemaster.model;

public class MovieBuilder {

    private String name, year, coverURL, rating, plot, mainActors, runTime, type;

    //Force use of create()
    private MovieBuilder() {}

    //Return an instance of a builder
    public static MovieBuilder create() {
        return new MovieBuilder();
    }

    //Return an instance of a builder from Movie passed in parameter
    public static MovieBuilder create(Movie movie) {
        return new MovieBuilder()
                .setName(movie.getName())
                .setYear(movie.getYear())
                .setRunTime(movie.getRunTime())
                .setRating(movie.getRating())
                .setPlot(movie.getPlot())
                .setType(movie.getType())
                .setCoverURL(movie.getCoverURL())
                .setMainActors(movie.getMainActors());
    }

    //Getters
    public String getRunTime() {return this.runTime;}
    public String getName() {return this.name;}
    public String getYear() {return this.year;}
    public String getCoverURL() {return this.coverURL;}
    public String getRating() {return this.rating;}
    public String getPlot() {return this.plot;}
    public String getMainActors() {return this.mainActors;}
    public String getType() {return this.type;}

    //Setters return MovieBuilder for method chaining
    public MovieBuilder setRunTime(String runTime) {this.runTime = runTime; return this;}
    public MovieBuilder setName(String name) {this.name = name; return this;}
    public MovieBuilder setYear(String year) {this.year = year; return this;}
    public MovieBuilder setCoverURL(String coverURL) {this.coverURL = coverURL; return this;}
    public MovieBuilder setRating(String rating) {this.rating = rating; return this;}
    public MovieBuilder setPlot(String plot) {this.plot = plot; return this;}
    public MovieBuilder setMainActors(String mainActors) {this.mainActors = mainActors; return this;}
    public MovieBuilder setType(String type) {this.type = type; return this;}

    //Build function
    public Movie build() {
        return new Movie(this);
    }

}