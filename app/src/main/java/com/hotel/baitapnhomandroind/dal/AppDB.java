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
        version = 1,
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {

    // DAO
    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract TheaterDao theaterDao();
    public abstract ShowtimeDao showtimeDao();
    public abstract TicketDao ticketDao();

    // Singleton
    private static volatile AppDB INSTANCE;

    // Thread chạy background
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AppDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDB.class,
                                    "movie_db"
                            )
                            .fallbackToDestructiveMigration() // reset db khi đổi version
                            .addCallback(roomCallback) // thêm data mẫu
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback để insert data mẫu khi tạo DB lần đầu
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                AppDB database = INSTANCE;

                // ===== Sample Users =====
                User user = new User();
                user.username = "admin";
                user.password = "123";
                database.userDao().insert(user);

                // ===== Sample Movies =====
                Movie m1 = new Movie();
                m1.title = "Avengers";
                m1.description = "Marvel movie";
                m1.duration = 120;
                database.movieDao().insert(m1);

                Movie m2 = new Movie();
                m2.title = "Batman";
                m2.description = "DC movie";
                m2.duration = 110;
                database.movieDao().insert(m2);

                // ===== Sample Theater =====
                Theater t1 = new Theater();
                t1.name = "CGV";
                t1.location = "Hà Nội";
                database.theaterDao().insert(t1);

                // NOTE: vì insert async nên nếu cần chính xác ID → phải query lại
            });
        }
    };
}