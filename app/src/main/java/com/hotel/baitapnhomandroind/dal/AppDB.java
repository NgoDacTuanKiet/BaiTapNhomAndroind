package com.hotel.baitapnhomandroind.dal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hotel.baitapnhomandroind.dal.dao.MovieDao;
import com.hotel.baitapnhomandroind.dal.dao.ShowtimeDao;
import com.hotel.baitapnhomandroind.dal.dao.TheaterDao;
import com.hotel.baitapnhomandroind.dal.dao.TicketDao;
import com.hotel.baitapnhomandroind.dal.dao.UserDao;
import com.hotel.baitapnhomandroind.entities.Movie;
import com.hotel.baitapnhomandroind.entities.Showtime;
import com.hotel.baitapnhomandroind.entities.Theater;
import com.hotel.baitapnhomandroind.entities.Ticket;
import com.hotel.baitapnhomandroind.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class},
        version = 5, 
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract TheaterDao theaterDao();
    public abstract ShowtimeDao showtimeDao();
    public abstract TicketDao ticketDao();

    private static volatile AppDB INSTANCE;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDB.class,
                                    "movie_db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            seedData();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            seedData();
        }
    };

    private static void seedData() {
        databaseWriteExecutor.execute(() -> {
            AppDB database = INSTANCE;
            if (database == null) return;

            if (database.movieDao().getAllSync().isEmpty()) {
                // Sample User
                User user = new User();
                user.username = "admin";
                user.password = "123";
                database.userDao().insert(user);

                // Sample Movies
                String[][] movieData = {
                        {"Avengers: Endgame", "Siêu anh hùng Marvel", "180"},
                        {"The Dark Knight", "Người dơi đối đầu Joker", "152"},
                        {"Interstellar", "Khám phá vũ trụ", "169"},
                        {"Inception", "Kẻ đánh cắp giấc mơ", "148"}
                };
                for (String[] data : movieData) {
                    Movie m = new Movie();
                    m.title = data[0];
                    m.description = data[1];
                    m.duration = Integer.parseInt(data[2]);
                    database.movieDao().insert(m);
                }

                // Sample Theaters
                String[][] theaterData = {
                        {"CGV Vincom Center", "Bà Triệu, Hà Nội"},
                        {"Lotte Cinema Landmark", "Phạm Hùng, Hà Nội"},
                        {"BHD Star Discovery", "Cầu Giấy, Hà Nội"},
                        {"Galaxy Cinema Mipec", "Long Biên, Hà Nội"}
                };
                for (String[] data : theaterData) {
                    Theater t = new Theater();
                    t.name = data[0];
                    t.location = data[1];
                    database.theaterDao().insert(t);
                }

                // Sample Showtimes
                List<Movie> movies = database.movieDao().getAllSync();
                List<Theater> theaters = database.theaterDao().getAllSync();
                if (!movies.isEmpty() && !theaters.isEmpty()) {
                    for (Theater t : theaters) {
                        for (Movie m : movies) {
                            Showtime s = new Showtime();
                            s.movieId = m.id;
                            s.theaterId = t.id;
                            s.showDate = "2023-12-25";
                            s.showTime = "19:00";
                            database.showtimeDao().insert(s);
                            
                            Showtime s2 = new Showtime();
                            s2.movieId = m.id;
                            s2.theaterId = t.id;
                            s2.showDate = "2023-12-26";
                            s2.showTime = "20:30";
                            database.showtimeDao().insert(s2);
                        }
                    }
                }
            }
        });
    }
}
