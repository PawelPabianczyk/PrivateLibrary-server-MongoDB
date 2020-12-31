import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import models.*;

public class Converter {

    public static DBObject convertGenre(Genre genre) {
        return new BasicDBObject("type", genre.getType())
                .append("name", genre.getName())
                .append("description", genre.getDescription());
    }

    public static DBObject convertPublisher(Publisher publisher) {
        return new BasicDBObject("name", publisher.getName())
                .append("dayOfFoundation", publisher.getDateOfFoundation())
                .append("description", publisher.getDescription());
    }

    public static DBObject convertUser(User user){
        return new BasicDBObject("username", user.getUsername())
                .append("password", user.getPassword())
                .append("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("gender", user.getGender())
                .append("country", user.getCountry())
                .append("books", user.getBooks());
    }

    public static DBObject convertAuthor(Author author){
        return new BasicDBObject("firstName", author.getFirstName())
                .append("lastName", author.getLastName())
                .append("gender", author.getGender())
                .append("country", author.getCountry())
                .append("biography", author.getBiography())
                .append("books", author.getBooks());
    }

    public static DBObject convertBook(Book book){
        return new BasicDBObject("title", book.getTitle())
                .append("authors", book.getAuthors())
                .append("genres", book.getGenres())
                .append("publishers", book.getPublishers())
                .append("language", book.getLanguage())
                .append("description", book.getDescription())
                .append("publishDate", book.getPublishDate())
                .append("returnDate", book.getReturnDate())
                .append("status", book.getStatus())
                .append("dateAdded", book.getDateAdded())
                .append("owners", book.getOwners());
    }
}
