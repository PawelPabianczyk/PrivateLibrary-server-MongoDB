import com.mongodb.*;
import models.*;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String typeOfConnection = inputStream.readObject().toString();

            switch (typeOfConnection) {
                case "PUT login": {
                    User user = (User) inputStream.readObject();
                    boolean isConnectionValid = validLoginData(user);
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(isConnectionValid);
                    break;
                }
                case "GET user data": {
                    String username = (String) inputStream.readObject();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    User userDB = getUser(username);
                    User userClient = new User(
                            (String)userDB.getUsername(),
                            (String)userDB.getPassword(),
                            (String)userDB.getFirstName(),
                            (String)userDB.getLastName(),
                            (String)userDB.getCountry(),
                            (String)userDB.getGender()
                            );

                    outputStream.writeObject(userClient);
                    break;
                }
                case "GET favourite genre": {
                    String username = (String) inputStream.readObject();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getFavouriteGenre(username));
                    break;
                }
                case "GET favourite author": {
                    String username = (String) inputStream.readObject();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getFavouriteAuthor(username));
                    break;
                }
                case "POST books": {
                    Book book = (Book) inputStream.readObject();
                    String username = (String) inputStream.readObject();
                    addBook(book, username);
                    break;
                }
                case "POST register": {
                    User user = (User) inputStream.readObject();
                    addUser(user);
                    break;
                }
                case "POST personal data": {
                    User user = (User) inputStream.readObject();
                    updateUser(user);
                    break;
                }
                case "POST new genre": {
                    Genre genre = (Genre) inputStream.readObject();
                    addGenre(genre);
                    break;
                }
                case "POST new publisher": {
                    Publisher publisher = (Publisher) inputStream.readObject();
                    addPublisher(publisher);
                    break;
                }
                case "POST new author": {
                    Author author = (Author) inputStream.readObject();
                    addAuthor(author);
                    break;
                }
                case "GET all books": {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getAllBooks());
                    break;
                }

                case "GET user books": {
                    String username = (String) inputStream.readObject();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getBooks(username));
                    break;
                }
                case "GET genres list": {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getAllGenres());
                    break;
                }
                case "GET publishers list": {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getAllPublishers());
                    break;
                }
                case "GET authors list": {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(getAllAuthors());
                    break;
                }
                default:
                    System.out.println("Type of connection is not correct.");
                    break;
            }

            socket.close();

        } catch (IOException | ClassNotFoundException e) {
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
            authorsList.add(firstName + " " + lastName);
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
            if (books == null)
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
            if (books == null)
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

        ArrayList<String> booksDB = dbUser.getBooks();
        ArrayList<String> booksUser = user.getBooks();

        if(booksUser != null){
            booksDB.addAll(booksUser);
        }
        user.setBooks(booksDB);
        database.getCollection("users").update(query, Converter.convertUser(user));
    }

    private void updateAuthor(Author author) {
        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> parts = new ArrayList<BasicDBObject>();
        parts.add(new BasicDBObject("firstName", author.getFirstName()));
        parts.add(new BasicDBObject("lastName", author.getLastName()));
        query.put("$and", parts);

        Author dbAuthor = getAuthor(author.getFirstName() + " " + author.getLastName());
        dbAuthor.getBooks().addAll(author.getBooks());
        author.setBooks(dbAuthor.getBooks());
        database.getCollection("authors").update(query, Converter.convertAuthor(author));
    }

    private String getFavouriteGenre(String username) {
        ArrayList<Book> usersBook = getBooks(username);

        if (usersBook.isEmpty())
            return "no genre";

        ArrayList<String> genres = new ArrayList<>();

        for (Book book :
                usersBook) {
            genres.add(book.getGenre());
        }

        return mostCommon(genres);
    }

    private String getFavouriteAuthor(String username) {
        ArrayList<Book> usersBook = getBooks(username);

        if (usersBook.isEmpty())
            return "no author";

        ArrayList<String> authors = new ArrayList<>();

        for (Book book :
                usersBook) {
            authors.add(book.getAuthor());
        }

        return mostCommon(authors);
    }

    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    private ArrayList<Book> getBooks(String username) {
        ArrayList<Book> booksObj = new ArrayList<>();
        User user = getUser(username);
        ArrayList<String> booksId = user.getBooks();
        for (String bookId : booksId) {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(bookId));
            DBCursor cursor = books.find(query);
            Book book = null;
            while (cursor.hasNext()) {
                BasicDBObject bookObj = (BasicDBObject) cursor.next();
                book = new Book();

                book.setTitle(bookObj.getString("title"));

                List<BasicDBObject> authorIDs = (List<BasicDBObject>) bookObj.get("authors");
                String idAuthor = String.valueOf(authorIDs.get(0));
                book.setAuthor(getAuthorName(idAuthor));

                List<BasicDBObject> genreIDs = (List<BasicDBObject>) bookObj.get("genres");
                String idGenre = String.valueOf(genreIDs.get(0));
                book.setGenre(getGenreName(idGenre));

                book.setPublishDate(bookObj.getDate("publishDate").toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
                book.setStatus(bookObj.getString("status"));
                booksObj.add(book);
            }
        }
        return booksObj;
    }

    private ArrayList<Book> getAllBooks() {
        ArrayList<Book> booksObj = new ArrayList<>();

        DBCursor cursor = books.find();

        Book book = null;

        while (cursor.hasNext()) {
            BasicDBObject bookObj = (BasicDBObject) cursor.next();
            book = new Book();
            book.setTitle(bookObj.getString("title"));

            List<BasicDBObject> authorIDs = (List<BasicDBObject>) bookObj.get("authors");
            String idAuthor = String.valueOf(authorIDs.get(0));
            book.setAuthor(getAuthorName(idAuthor));

            List<BasicDBObject> genreIDs = (List<BasicDBObject>) bookObj.get("genres");
            String idGenre = String.valueOf(genreIDs.get(0));
            book.setGenre(getGenreName(idGenre));

            List<BasicDBObject> ownerIDs = (List<BasicDBObject>) bookObj.get("owners");
            String idOwner = String.valueOf(ownerIDs.get(0));
            book.setOwner(getUserName(idOwner));


            book.setDateAdded(bookObj.getDate("dateAdded").toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            booksObj.add(book);
        }

        return booksObj;
    }

    private synchronized void addBook(Book book, String username) {
        String[] genres = {getGenreId(book.getGenre())};
        String[] publishers = {getPublisherId(book.getPublisher())};
        String[] authors = {getAuthorId(book.getAuthor())};
        String[] owners = {getUserId(username)};
        if (book.getReturnDate() == null) {
            book.setStatus("own");
        } else {
            book.setStatus("borrowed");
        }
        book.setDateAdded(LocalDate.now());
        book.setGenres(genres);
        book.setPublishers(publishers);
        book.setAuthors(authors);
        book.setOwners(owners);
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

    private String getUserId(String username) {

        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        DBCursor cursor = users.find(query);

        String id = "";
        while (cursor.hasNext()) {
            BasicDBObject userObj = (BasicDBObject) cursor.next();
            id = userObj.get("_id").toString();
        }
        return id;
    }

    private String getAuthorId(String authorName) {
        String[] parts = authorName.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];

        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> partsQuery = new ArrayList<BasicDBObject>();
        partsQuery.add(new BasicDBObject("firstName", firstName));
        partsQuery.add(new BasicDBObject("lastName", lastName));
        query.put("$and", partsQuery);

        DBCursor cursor = authors.find(query);

        String id = "";
        while (cursor.hasNext()) {
            BasicDBObject authorObj = (BasicDBObject) cursor.next();
            id = authorObj.get("_id").toString();
        }
        return id;
    }

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

    private String getGenreName(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCursor cursor = genres.find(query);
        String genreName = "";
        while (cursor.hasNext()) {
            BasicDBObject genreObj = (BasicDBObject) cursor.next();
            genreName = genreObj.get("name").toString();
        }
        return genreName;
    }

    private String getPublisherName(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCursor cursor = publishers.find(query);
        String publisherName = "";
        while (cursor.hasNext()) {
            BasicDBObject publisherObj = (BasicDBObject) cursor.next();
            publisherName = publisherObj.get("name").toString();
        }
        return publisherName;
    }

    private String getAuthorName(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCursor cursor = authors.find(query);
        String firstName = "";
        String lastName = "";
        while (cursor.hasNext()) {
            BasicDBObject authorObj = (BasicDBObject) cursor.next();
            firstName = authorObj.getString("firstName");
            lastName = authorObj.getString("lastName");
        }
        return firstName + " " + lastName;
    }

    private String getUserName(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCursor cursor = users.find(query);
        String username = "";
        while (cursor.hasNext()) {
            BasicDBObject userObj = (BasicDBObject) cursor.next();
            username = userObj.getString("username");
        }
        return username;
    }
}