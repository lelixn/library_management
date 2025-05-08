import java.util.*;

class Book {
    String bookId;
    String title;
    boolean isAvailable;
    int borrowCount;

    Book(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.isAvailable = true;
        this.borrowCount = 0;
    }
}

public class LibrarySystem {
    Map<String, Book> books = new HashMap<>();
    Map<String, List<String>> userBorrowedBooks = new HashMap<>();

    public void addBook(String bookId, String title) {
        if (books.containsKey(bookId)) {
            System.out.println("Book ID already exists.");
            return;
        }
        books.put(bookId, new Book(bookId, title));
        System.out.println("Book " + bookId + " added.");
    }

    public void borrowBook(String userId, String bookId) {
        if (!books.containsKey(bookId)) {
            System.out.println("Book does not exist.");
            return;
        }

        Book book = books.get(bookId);
        if (!book.isAvailable) {
            System.out.println("Book " + bookId + " is not available.");
            return;
        }

        List<String> borrowed = userBorrowedBooks.getOrDefault(userId, new ArrayList<>());
        if (borrowed.size() >= 3) {
            System.out.println("User " + userId + " cannot borrow more than 3 books.");
            return;
        }

        if (borrowed.contains(bookId)) {
            System.out.println("User already borrowed this book.");
            return;
        }

        borrowed.add(bookId);
        userBorrowedBooks.put(userId, borrowed);
        book.isAvailable = false;
        book.borrowCount++;
        System.out.println("User " + userId + " borrowed " + bookId + ".");
    }

    public void returnBook(String userId, String bookId) {
        if (!userBorrowedBooks.containsKey(userId) || !userBorrowedBooks.get(userId).contains(bookId)) {
            System.out.println("User " + userId + " did not borrow " + bookId + ".");
            return;
        }

        userBorrowedBooks.get(userId).remove(bookId);
        books.get(bookId).isAvailable = true;
        System.out.println("User " + userId + " returned " + bookId + ".");
    }

    public void getTopBorrowedBooks() {
        System.out.println("Top 3 Most Borrowed Books:");
        books.values().stream()
            .sorted((b1, b2) -> Integer.compare(b2.borrowCount, b1.borrowCount))
            .limit(3)
            .forEach(book -> System.out.println(book.title + " - Borrowed " + book.borrowCount + " times"));
    }

    public static void main(String[] args) {
        LibrarySystem lib = new LibrarySystem();

        lib.addBook("B101", "The Hobbit");
        lib.addBook("B102", "1984");
        lib.addBook("B103", "Dune");

        lib.borrowBook("U01", "B101");
        lib.borrowBook("U01", "B102");
        lib.returnBook("U01", "B101");
        lib.borrowBook("U01", "B101");

        lib.borrowBook("U02", "B101");
        lib.returnBook("U01", "B101");
        lib.borrowBook("U02", "B101");

        lib.getTopBorrowedBooks();
    }
}
