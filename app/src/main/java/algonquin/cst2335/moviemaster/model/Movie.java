package algonquin.cst2335.moviemaster.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "year")
    protected String year;

    @ColumnInfo(name = "coverURL")
    protected String coverURL;

    @ColumnInfo(name = "rating")
    protected String rating;

    @ColumnInfo(name = "runTime")
    protected String runTime;

    @ColumnInfo(name = "plot")
    protected String plot;

    @ColumnInfo(name = "mainActors")
    protected String mainActors;

    @ColumnInfo(name = "type")
    protected String type;

    public Movie(String name, String year, String coverURL, String rating, String plot, String runTime, String mainActors, String type) {
        this.name = name;
        this.year = year;
        this.coverURL = coverURL;
        this.rating = rating;
        this.plot = plot;
        this.runTime = runTime;
        this.mainActors = mainActors;
        this.type = type;
    }

    public Movie(MovieBuilder builder) {
        this.name = builder.getName();
        this.year = builder.getYear();
        this.coverURL = builder.getCoverURL();
        this.rating = builder.getRating();
        this.runTime = builder.getRunTime();
        this.plot = builder.getPlot();
        this.mainActors = builder.getMainActors();
        this.type = builder.getType();
    }

    public String getCoverURL() {return this.coverURL;}
    public String getName() {return this.name;}
    public String getYear() {return this.year;}
    public String getRating() {return this.rating;}
    public String getPlot() {return this.plot;}
    public String getMainActors() {return this.mainActors;}
    public String getRunTime() {return this.runTime;}
    public String getType() {return this.type;}
    public int getId() {return this.id;}

}

