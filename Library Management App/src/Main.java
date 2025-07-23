import entities.Role;
import entities.User;
import entities.Book;
import storage.StorageManager;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        StorageManager sm;

        while (true) {
            try {
                sm = StorageManager.getStorageManager();
                break;
            }
            catch (Exception e) {
                System.out.println("Application is not configured properly.");
            }
        }

        System.out.println("=== Welcome to Library Management Console ===");

        while (true) {
            System.out.print("\nLogin as [admin/regular/exit]: ");
            String roleInput = scanner.nextLine().trim().toLowerCase();

            if (roleInput.equals("exit")) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            }

            Role role = null;
            if (roleInput.equals("admin")) {
                role = Role.ADMIN;
            } else if (roleInput.equals("regular")) {
                role = Role.REGULAR;
            } else {
                System.out.println("Invalid role. Try again.");
                continue;
            }

            while (true) {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                User user = sm.findUserByName(name, role==Role.ADMIN ? "admin" : "regular");
                if (user!=null) {
                    if (role == Role.ADMIN) {
                        runAdminMenu(sm);
                    } else {
                        runRegularMenu(sm, user.getId());
                    }
                    break;
                }
                else {
                    System.out.println("User not found. Enter another name:");
                }
            }
        }
    }

    private static void runAdminMenu(StorageManager sm) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book Title");
            System.out.println("3. Edit Book Author");
            System.out.println("4. Edit Book Genre");
            System.out.println("5. Edit Book Available Copies");
            System.out.println("6. Delete Book");
            System.out.println("7. Register User");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Available Copies: ");
                    int copies = Integer.parseInt(scanner.nextLine());
                    sm.addBook(UUID.randomUUID().toString(), title, author, genre, copies);
                    break;

                case "2":
                    System.out.print("Book ID: ");
                    sm.editBookTitle(scanner.nextLine(), ask("New Title: "));
                    break;

                case "3":
                    System.out.print("Book ID: ");
                    sm.editBookAuthor(scanner.nextLine(), ask("New Author: "));
                    break;

                case "4":
                    System.out.print("Book ID: ");
                    sm.editBookGenre(scanner.nextLine(), ask("New Genre: "));
                    break;

                case "5":
                    System.out.print("Book ID: ");
                    String bookId = scanner.nextLine();
                    int newCopies = Integer.parseInt(ask("New Available Copies: "));
                    sm.editBookAvailableCopies(bookId, newCopies);
                    break;

                case "6":
                    sm.deleteBook(ask("Book ID to delete: "));
                    break;

                case "7":
                    String userName = ask("User Name: ");
                    String userRole = ask("Role [admin/regular]: ").toLowerCase();
                    String userId = UUID.randomUUID().toString();
                    sm.addUser(userId, userName, userRole.equals("admin") ? "admin" : "regular");
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void runRegularMenu(StorageManager sm, String userId) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    String bookIdToBorrow = ask("Enter Book title to borrow: ");
                    Book bookBorrowed = sm.findBookByTitle(bookIdToBorrow);
                    sm.addBorrow(userId, bookBorrowed.getId());
                    break;

                case "2":
                    String bookIdToReturn = ask("Enter Book title to return: ");
                    Book bookReturned = sm.findBookByTitle(bookIdToReturn);
                    sm.deleteBorrow(userId, bookReturned.getId());
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
