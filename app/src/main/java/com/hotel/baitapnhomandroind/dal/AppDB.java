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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class},
        version = 4, // Tăng lên 4 để reset DB hoàn toàn
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

            // Sử dụng getAllSync() để kiểm tra đồng bộ trong thread background
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
            }
        });
    }
}
