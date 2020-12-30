import com.mongodb.*;
import models.*;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    Socket socket;

    public static MongoClient mongoClient;
    public static DB database;
    public static DBCollection authors;
    public static DBCollection books;
    public static DBCollection genres;
    public static DBCollection publishers;
    public static DBCollection users;

    ServerThread(Socket socket) {
        this.socket = socket;

        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("privlib");
        authors = database.getCollection("authors");
        books = database.getCollection("books");
        genres = database.getCollection("genres");
        publishers = database.getCollection("publishers");
        users = database.getCollection("users");
    }

    public void run() {

//        Genre genre = new Genre();
//        genre.setType("Fiction");
//        genre.setName("Action");
//        genre.setDescription("About gangsters");
//        genres.insert(Converter.convertGenre(genre));
//        genre.setType("Fiction");
//        genre.setName("SF");
//        genre.setDescription("About aliens");
//        genres.insert(Converter.convertGenre(genre));
//
//        Publisher publisher = new Publisher();
//        publisher.setName("wydawnictwo1");
//        publisher.setDateOfFoundation(LocalDate.of(1999,02,02));
//        publisher.setDescription("opis");
//        publishers.insert(Converter.convertPublisher(publisher));
//        publisher.setDescription("opis");
//        publisher.setDateOfFoundation(LocalDate.of(1999,02,02));
//        publisher.setName("wydawnictwo2");
//        publishers.insert(Converter.convertPublisher(publisher));
//
//        Author author = new Author();
//        author.setFirstName("imie1");
//        author.setLastName("nazwisko1");
//        author.setGender("Male");
//        author.setCountry("Poland");
//        author.setBiography("biografia1");
//        authors.insert(Converter.convertAuthor(author));
//
//        author.setBiography("biografia2");
//        author.setCountry("Poland");
//        author.setGender("Male");
//        author.setLastName("nazwisko2");
//        author.setFirstName("imie2");
//        authors.insert(Converter.convertAuthor(author));
//
//        User user = new User("test", "test", "imie", "nazwisko", "Poland", "Male");
//        users.insert(Converter.convertUser(user));
//        user = new User("test2", "test2", "imie2", "nazwisko2", "Poland2", "Male2");
//        users.insert(Converter.convertUser(user));
//
//        Book book = new Book();
//        book.setTitle("ksiazka");
//        book.setAuthor("imie1 nazwisko1");
//        book.setGenre("Action");
//        book.setPublisher("wydawnictwo1");
//        book.setPublishDate(LocalDate.of(1999,02,02));
//        book.setReturnDate(null);
//        book.setLanguage("polish");
//        book.setDescription("opis");
//
//        addBook(book, "test");
//
//        Book book1 = new Book();
//        book1.setTitle("ksiazka1");
//        book1.setAuthor("imie1 nazwisko1");
//        book1.setGenre("SF");
//        book1.setPublisher("wydawnictwo2");
//        book1.setPublishDate(LocalDate.of(1999,02,02));
//        book1.setReturnDate(LocalDate.of(2021,02,02));
//        book1.setLanguage("polish");
//        book1.setDescription("opis");
//
//        addBook(book1, "test");


        try {
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//            String typeOfConnection = inputStream.readObject().toString();



//            switch (typeOfConnection) {
//                case "PUT login": {
//                    User user = (User) inputStream.readObject();
//                    boolean isConnectionValid = validLoginData(user);
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(isConnectionValid);
//                    break;
//                }
//                case "GET user data": {
//                    String username = (String) inputStream.readObject();
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getUser(username));
//                    break;
//                }
//                case "GET favourite genre": {
//                    String username = (String) inputStream.readObject();
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getFavouriteGenre(username));
//                    break;
//                }
//                case "GET favourite author": {
//                    String username = (String) inputStream.readObject();
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getFavouriteAuthor(username));
//                    break;
//                }
//                case "POST books": {
//                    Book book = (Book) inputStream.readObject();
//                    String username = (String) inputStream.readObject();
//                    addBook(book, username);
//                    break;
//                }
//                case "POST register": {
//                    User user = (User) inputStream.readObject();
//                    addUser(user);
//                    break;
//                }
//                case "POST personal data": {
//                    User user = (User) inputStream.readObject();
//                    updateUser(user);
//                    break;
//                }
//                case "POST new genre": {
//                    Genre genre = (Genre) inputStream.readObject();
//                    addGenre(genre);
//                    break;
//                }
//                case "POST new publisher": {
//                    Publisher publisher = (Publisher) inputStream.readObject();
//                    addPublisher(publisher);
//                    break;
//                }
//                case "POST new author": {
//                    Author author = (Author) inputStream.readObject();
//                    addAuthor(author);
//                    break;
//                }
//                case "GET all books": {
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getAllBooks());
//                    break;
//                }
//
//                case "GET user books": {
//                    String username = (String) inputStream.readObject();
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getBooks(username));
//                    break;
//                }
//                case "GET genres list": {
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getAllGenres());
//                    break;
//                }
//                case "GET publishers list": {
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getAllPublishers());
//                    break;
//                }
//                case "GET authors list": {
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    outputStream.writeObject(getAllAuthors());
//                    break;
//                }
//                default:
//                    System.out.println("Type of connection is not correct.");
//                    break;
//            }

            socket.close();

        } catch (IOException e) {
            System.out.println("Client closed connection.");
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllGenres() {
        DBCursor cursor = genres.find();
        ArrayList<String> genresList = new ArrayList<>();

        while (cursor.hasNext()) {
            BasicDBObject genreObj = (BasicDBObject) cursor.next();
            String name = genreObj.getString("name");
            genresList.add(name);
        }
        return genresList;
    }

    private ArrayList<String> getAllPublishers() {
        DBCursor cursor = publishers.find();
        ArrayList<String> publishersList = new ArrayList<>();

        while (cursor.hasNext()) {
            BasicDBObject publisherObj = (BasicDBObject) cursor.next();
            String name = publisherObj.getString("name");
            publishersList.add(name);
        }
        return publishersList;
    }

    private ArrayList<String> getAllAuthors() {
        DBCursor cursor = authors.find();
        ArrayList<String> authorsList = new ArrayList<>();

        while (cursor.hasNext()) {
            BasicDBObject authorObj = (BasicDBObject) cursor.next();
            String firstName = authorObj.getString("firstName");
            String lastName = authorObj.getString("lastName");
            authorsList.add(firstName+" "+lastName);
        }
        return authorsList;
    }

    private User getUser(String username) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        DBCursor cursor = users.find(query);

        User user = null;
        while (cursor.hasNext()) {
            BasicDBObject userObj = (BasicDBObject) cursor.next();
            String userName = userObj.getString("username");
            String password = userObj.getString("password");
            String firstName = userObj.getString("firstName");
            String lastName = userObj.getString("lastName");
            String country = userObj.getString("country");
            String gender = userObj.getString("gender");
            user = new User(userName, password, firstName, lastName, country, gender);

            ArrayList<String> books = (ArrayList<String>) userObj.get("books");
            if(books == null)
                user.setBooks(new ArrayList<>());
            else
                user.setBooks(books);
        }
        return user;
    }

    private Author getAuthor(String authorName) {
        String[] parts = authorName.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];

        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> partsQuery = new ArrayList<BasicDBObject>();
        partsQuery.add(new BasicDBObject("firstName", firstName));
        partsQuery.add(new BasicDBObject("lastName", lastName));
        query.put("$and", partsQuery);

        DBCursor cursor = authors.find(query);

        Author author = null;
        while (cursor.hasNext()) {
            BasicDBObject authorObj = (BasicDBObject) cursor.next();
            author = new Author();
            author.setFirstName(authorObj.getString("firstName"));
            author.setLastName(authorObj.getString("lastName"));
            author.setCountry(authorObj.getString("country"));
            author.setGender(authorObj.getString("gender"));
            author.setBiography(authorObj.getString("biography"));

            ArrayList<String> books = (ArrayList<String>) authorObj.get("books");
            if(books == null)
                author.setBooks(new ArrayList<>());
            else
                author.setBooks(books);
        }
        return author;
    }

    private void updateUser(User user) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", user.getUsername());
        User dbUser = getUser(user.getUsername());

        dbUser.getBooks().addAll(user.getBooks());
        user.setBooks(dbUser.getBooks());
        database.getCollection("users").update(query, Converter.convertUser(user));
    }

    private void updateAuthor(Author author) {
        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> parts = new ArrayList<BasicDBObject>();
        parts.add(new BasicDBObject("firstName", author.getFirstName()));
        parts.add(new BasicDBObject("lastName", author.getLastName()));
        query.put("$and", parts);
        Author dbAuthor = getAuthor(author.getFirstName()+" "+author.getLastName());
        dbAuthor.getBooks().addAll(author.getBooks());
        author.setBooks(dbAuthor.getBooks());
        database.getCollection("authors").update(query, Converter.convertAuthor(author));
    }

    /*
    private String getFavouriteGenre(String username) {
        try {
            Statement stmt = connection.createStatement();

            String query = "select b.id_genre, count(*) quantity from books b " +
                    "NATURAL JOIN user_book ub " +
                    "NATURAL JOIN users u WHERE u.username='" + username + "' " +
                    "GROUP BY b.id_genre ORDER BY quantity DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            int genreId = 0;
            while (rs.next()) {
                genreId = rs.getInt("id_genre");
            }
            if(genreId == 0)
                return "no genre";
            else
                return getGenreName(genreId);
        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }

    private String getFavouriteAuthor(String username) {
        try {
            Statement stmt = connection.createStatement();

            String query = "select ab.id_author, count(*) quantity from author_book ab " +
                    "NATURAL JOIN user_book ub " +
                    "NATURAL JOIN users u WHERE u.username='" + username + "' " +
                    "GROUP BY ab.id_author ORDER BY quantity DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            int authorId = 0;
            while (rs.next()) {
                authorId = rs.getInt("id_author");
            }
            if(authorId == 0)
                return "no author";
            else
                return getAuthorName(authorId);
        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }
*/
    /*
    private ArrayList<Book> getBooks(String username) {
        try {
            Statement stmt = connection.createStatement();
            String query = "select b.title, ab.id_author, b.id_genre, b.publish_date, b.status from books b " +
                    "natural join author_book ab " +
                    "natural join user_book ub " +
                    "natural join users u where u.username='" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            Book book;
            ArrayList<Book> books = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = getAuthorName(rs.getInt("id_author"));
                String genre = getGenreName(rs.getInt("id_genre"));
                LocalDate publishDate = rs.getDate("publish_date").toLocalDate();
                String status = rs.getString("status");
                book = new Book(title, author, genre, publishDate, status);
                book.setOwner(username);
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }
*/
    /*
    private ArrayList<Book> getAllBooks() {
        try {
            Statement stmt = connection.createStatement();
            String query = "select b.title, ab.id_author, b.id_genre, u.username, ub.date_added from books b " +
                    "natural join author_book ab " +
                    "natural join user_book ub " +
                    "natural join users u";
            ResultSet rs = stmt.executeQuery(query);
            Book book;
            ArrayList<Book> books = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = getAuthorName(rs.getInt("id_author"));
                String genre = getGenreName(rs.getInt("id_genre"));
                String owner = rs.getString("username");
                LocalDate dateAdded = rs.getDate("date_added").toLocalDate();

                book = new Book(title, author, genre, owner, dateAdded);
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }
*/

    private void addBook(Book book, String username) {

        String[] genres = {getGenreId(book.getGenre())};
        String[] publishers = {getPublisherId(book.getPublisher())};
        if (book.getReturnDate() == null){
            book.setStatus("own");
        }
        else{
            book.setStatus("borrowed");
        }
        book.setDateAdded(LocalDate.now());
        book.setGenres(genres);
        book.setPublishers(publishers);
        books.insert(Converter.convertBook(book));

        ArrayList<String> books = new ArrayList<>();
        books.add(getBookId(book.getTitle()));

        User user = getUser(username);
        user.setBooks(books);
        updateUser(user);

        Author author = getAuthor(book.getAuthor());
        author.setBooks(books);
        updateAuthor(author);
    }

    private void addUser(User user) {
        users.insert(Converter.convertUser(user));
    }

    private void addGenre(Genre genre) {
        genres.insert(Converter.convertGenre(genre));
    }

    private void addPublisher(Publisher publisher) {
        publishers.insert(Converter.convertPublisher(publisher));
    }

    private void addAuthor(Author author) {
        authors.insert(Converter.convertAuthor(author));
    }

    private Boolean validLoginData(User user) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", user.getUsername());
        DBCursor cursor = users.find(query);
        String dbPassword = "";
        while (cursor.hasNext()) {
            BasicDBObject userObj = (BasicDBObject) cursor.next();
            dbPassword = userObj.getString("password");
        }
        return user.getPassword().equals(dbPassword);
    }

    /*
    private int getUserId(String username) {
        try {
            Statement stmt = connection.createStatement();
            String query = "select id_user from users where username='" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            int id = -1;
            while (rs.next()) {
                id = rs.getInt("id_user");
            }

            return id;

        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return -1;
    }
*/
    private String getBookId(String title) {

        BasicDBObject query = new BasicDBObject();
        query.put("title", title);
        DBCursor cursor = books.find(query);

        String id = "";
        while (cursor.hasNext()) {
            BasicDBObject bookObj = (BasicDBObject) cursor.next();
            id = bookObj.get("_id").toString();
        }
        return id;
    }

/*
    private int getAuthorId(String Name) {
        String[] parts = Name.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];
        try {
            Statement stmt = connection.createStatement();
            String query = "select id_author from authors where first_name='" + firstName + "' and last_name='" + lastName + "'";
            ResultSet rs = stmt.executeQuery(query);
            int id = -1;
            while (rs.next()) {
                id = rs.getInt("id_author");
            }

            return id;

        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return -1;
    }
*/
    private String getGenreId(String genre) {
        BasicDBObject query = new BasicDBObject();
        query.put("name", genre);
        DBCursor cursor = genres.find(query);
        String id = "";
        while (cursor.hasNext()) {
            BasicDBObject genreObj = (BasicDBObject) cursor.next();
            id = genreObj.get("_id").toString();
        }
        return id;
    }

    private String getPublisherId(String publisher) {
        BasicDBObject query = new BasicDBObject();
        query.put("name", publisher);
        DBCursor cursor = publishers.find(query);
        String id = "";
        while (cursor.hasNext()) {
            BasicDBObject publisherObj = (BasicDBObject) cursor.next();
            id = publisherObj.get("_id").toString();
        }
        return id;
    }
/*
    private String getGenreName(int id) {
        try {
            Statement stmt = connection.createStatement();
            String query = "select name from genres where id_genre=" + id;
            ResultSet rs = stmt.executeQuery(query);
            String name = null;
            while (rs.next()) {
                name = rs.getString("name");
            }

            return name;

        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }

    private String getAuthorName(int id) {
        try {
            Statement stmt = connection.createStatement();
            String query = "select first_name, last_name from authors where id_author=" + id;
            ResultSet rs = stmt.executeQuery(query);
            String firstName = null;
            String lastName = null;
            while (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }

            return firstName+" "+lastName;

        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return null;
    }

 */
}