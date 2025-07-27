import entities.*;
import storage.SearchService;
import storage.StorageManager;

import java.util.List;
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
                User user = new SearchService<User>().findUserByName(name);
                if (user!=null && (roleInput.equals("admin")&&user instanceof Admin
                        || roleInput.equals("regular")&&user instanceof RegularUser)) {
                    if (role == Role.ADMIN) {
                        runAdminMenu((Admin)user);
                    } else {
                        runRegularMenu(sm, (RegularUser) user);
                    }
                    break;
                }
                else {
                    System.out.println("User not found. Enter another name:");
                }
            }
        }
    }

    private static void runAdminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book Author");
            System.out.println("3. Edit Book Genre");
            System.out.println("4. Edit Book Available Copies");
            System.out.println("5. Delete Book");
            System.out.println("6. Register User");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();
            StorageManager sm = StorageManager.getStorageManager();
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
                    admin.addBook(title, author, genre, copies);
                    System.out.println("Book added successfully.");
                    break;

                case "2":
                    String titleForAuthor = ask("Book title: ");
                    Book bookForAuthor = sm.findBookByTitle(titleForAuthor);
                    if (bookForAuthor != null) {
                        admin.editBookAuthor(bookForAuthor.getId(), ask("New Author: "));
                        System.out.println("Author name edited successfully.");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case "3":
                    String titleForGenre = ask("Book title: ");
                    Book bookForGenre = sm.findBookByTitle(titleForGenre);
                    if (bookForGenre != null) {
                        admin.editBookGenre(bookForGenre.getId(), ask("New Genre: "));
                        System.out.println("Genre edited successfully.");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case "4":
                    String titleForCopies = ask("Book title: ");
                    Book bookForCopies = sm.findBookByTitle(titleForCopies);
                    if (bookForCopies != null) {
                        try {
                            int newCopies = Integer.parseInt(ask("New Available Copies: "));
                            admin.editBookAvailableCopies(bookForCopies.getId(), newCopies);
                            System.out.println("Available copies edited successfully.");
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong format.");
                        }
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case "5":
                    String titleToDelete = ask("Book title to delete: ");
                    Book bookToDelete = sm.findBookByTitle(titleToDelete);
                    if (bookToDelete != null) {
                        admin.deleteBook(bookToDelete.getId());
                        System.out.println("Book deleted successfully.");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case "6":
                    String userName = ask("User Name: ");
                    String userRole = ask("Role [admin/regular]: ").toLowerCase();
                    admin.addUser(userName, userRole.equals("admin") ? Role.ADMIN : Role.REGULAR);
                    System.out.println("User added successfully.");
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }

    }

    private static void runRegularMenu(StorageManager sm, RegularUser rUser) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Books");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    String bookTitleToBorrow = ask("Enter Book title to borrow: ");
                    Book bookBorrowed = sm.findBookByTitle(bookTitleToBorrow);
                    if (bookBorrowed==null) {
                        System.out.println("Book not found.");
                        break;
                    }

                    try {
                        rUser.borrowBook(bookBorrowed.getId());
                    } catch (Exception e) {
                        System.out.println("Book already borrowed.");
                        break;
                    }
                    System.out.println("Book borrowed successfully.");
                    break;

                case "2":
                    String bookTitleToReturn = ask("Enter Book title to return: ");
                    Book bookReturned = sm.findBookByTitle(bookTitleToReturn);
                    if (bookReturned==null) {
                        System.out.println("Book not found.");
                        break;
                    }
                    try {
                        rUser.returnBook(bookReturned.getId());
                    } catch (Exception e) {
                        System.out.println("Book is not borrowed.");
                        break;
                    }
                    System.out.println("Book returned successfully.");
                    break;

                case "3":
                    List<Book> books = rUser.getAllBooks();
                    for (Book book : books) {
                        System.out.println("\""+book.getTitle()+"\" by "+ book.getAuthor()+", Genre: "+
                                    book.getGenre());
                    }
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
