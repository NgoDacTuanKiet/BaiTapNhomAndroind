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
        version = 2, // Tăng version lên 2 để trigger reset database
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
                            .fallbackToDestructiveMigration() // Sẽ xóa và tạo lại DB khi tăng version
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
            databaseWriteExecutor.execute(() -> {
                AppDB database = INSTANCE;

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

                // Sample Theaters (8 rạp)
                String[][] theaterData = {
                        {"CGV Vincom Center", "Bà Triệu, Hà Nội"},
                        {"Lotte Cinema Landmark", "Phạm Hùng, Hà Nội"},
                        {"BHD Star Discovery", "Cầu Giấy, Hà Nội"},
                        {"Galaxy Cinema Mipec", "Long Biên, Hà Nội"},
                        {"Beta Cinemas Mỹ Đình", "Nam Từ Liêm, Hà Nội"},
                        {"Cinestar Quốc Thanh", "Quận 1, TPHCM"},
                        {"BHD Star Bitexco", "Quận 1, TPHCM"},
                        {"Galaxy Nguyễn Du", "Quận 1, TPHCM"}
                };
                for (String[] data : theaterData) {
                    Theater t = new Theater();
                    t.name = data[0];
                    t.location = data[1];
                    database.theaterDao().insert(t);
                }
            });
        }
    };
}
