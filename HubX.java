
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Product {

    int id; 
    String name;
    double price; 
    String category;
    int stock;

    public Product(int id, String name, double price, String category, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public Product() {
    }

    @Override
    public String toString() {

        return String.format(
                "| %-5d | %-25s | %-15s | %-10.2f | %-5d |",
                id,
                name,
                category,
                price,
                stock
        );
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        // Basic validation is often added here
        if (price >= 0) {
            this.price = price;
        }
    }

    // Getter and Setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and Setter for stock
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
        }
    }

}

class Node2 {

    Node2 next, prev;
    Product data;

    Node2(Product data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

class Order {

    int id;
    int quantity;
    // Getter for id

    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", quantity=" + quantity + "}";
    }
}

class Inventory {

    Node2 head = null;
    Node2 end = null;
    java.util.Stack<Node2> nodeHistory = new java.util.Stack<>();
    List<Order> orList = new ArrayList<>();

    Queue<Order> queue = new ArrayDeque<>();

    public void insertAtEnd(Product p) {
        Node2 node = new Node2(p);
        if (head == null) {
            head = node;
            end = node;
        } else {
            Node2 temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
            node.prev = temp;
            end = node;
        }

    }

    public void deleteById(int id) {
        Node2 temp = head;

        while (temp != null) {

            if (temp.data.id == id) {
                nodeHistory.push(temp);
                if (temp == head) {

                    if (head.next == null) {

                        head = null;
                        end = null;

                    } else {

                        head = head.next;
                        head.prev = null;
                    }
                    // head = temp.next;
                    // end=temp.next;

                    // if (head != null) {
                    // }
                } else {

                    if (temp.next != null) {
                        temp.next.prev = temp.prev;

                    } else {
                        end = temp.prev;
                    }
                    temp.prev.next = temp.next;

                }

            }
            temp = temp.next;
            System.out.println("Product Deleted Successfully..");
        }
        if (temp == null) {
            System.out.println("Product is not present \"" + id + "\" this id.");
        }

    }

    public void forwardShow() {

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-5s |\n",
                "ID", "Product Name", "Category", "Price", "Stock");
        System.out.println("----------------------------------------------------------------------------");
        Node2 temp = head;

        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }

        System.out.println("----------------------------------------------------------------------------");
    }

    public void backWardShow() {

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-5s |\n",
                "ID", "Product Name", "Category", "Price", "Stock");
        System.out.println("----------------------------------------------------------------------------");

        Node2 temp = end;

        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.prev;
        }

        System.out.println("----------------------------------------------------------------------------");
    }

    public void undoLast() {
        Node2 pop_item = nodeHistory.pop();
        // jodi first
        if (pop_item.prev == null) {
            pop_item.next = head;
            if (head != null) {
                head.prev = pop_item;
            }
            head = pop_item;
        } else if (pop_item.next == null) {
            // jodi end a thake
            end.next = pop_item;
            pop_item.prev = end;
            end = pop_item;
        } else {
            // jodi middle a thake
            pop_item.prev.next = pop_item;
            pop_item.next.prev = pop_item;
        }
    }

    public Node2 searchbyId(int id) {
        Node2 temp = head;
        int position = 0;
        while (temp != null) {
            position++;
            if (temp.data.id == id) {
                return temp;
            }

            temp = temp.next;
        }
        if (position > 0) {
            System.out.println("Id not found..");
        }
        if (position == 0) {
            System.out.println("List is empty...");
        }
        return temp;
    }

    // if(low>=high){
    // return;
    // }
    // int pivot = high;
    // int i=low-1;
    // for(int j=low;j<high;j++){
    // if(arr[j]<arr[pivot]){
    // i++;
    // int temp=arr[i];
    // arr[i]=arr[j];
    // arr[j]=temp;
    // }
    // }
    // i++;
    // int temp=arr[i];
    // arr[i]=arr[pivot];
    // arr[pivot]=temp;
    // pivot=i;
    // quick(arr, low, pivot-1);
    // quick(arr, pivot+1, high);
    public void sortbyPrice(Node2 low, Node2 high) {

        if (low == null || high == null || low == high) {
            return;
        }

        Double pivotPrice = high.data.getPrice();
        Node2 i = low.prev;

        for (Node2 j = low; j.next != null; j = j.next) {
            if (j.data.getPrice() > pivotPrice) {
                if (i == null) {
                    i = low;
                } else {
                    i = i.next;
                }
                Product temp = i.data;
                i.data = j.data;
                j.data = temp;

            }
        }

        if (i == null) {
            i = low;
        } else {
            i = i.next;
        }
        Product temp = i.data;
        i.data = high.data;
        high.data = temp;
        Node2 pivot = i;
        if (pivot != low) {
            sortbyPrice(low, pivot.prev);
        }
        if (pivot != high) {
            sortbyPrice(pivot.next, high);
        }

    }

    public void takeOrder(int id, int Quantity) {
        Order or = new Order();
        or.setId(id);
        or.setQuantity(Quantity);
        orList.add(or);

    }

    public void addOrderQueue() {
        queue.addAll(orList);

    }
    public void processShiping(){

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {           
            System.out.println(queue.poll().toString());
            System.out.println("Order shippded");
             // Baki code ekhane likhbi
              }, 30, TimeUnit.SECONDS);

}
}

// class HubXTimer {
//     public void scheduleTask() {
//         ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

//         System.out.println("Order place holo...");

//         // Task-ta schedule kora
//         scheduler.schedule(() -> {
//             System.out.println("30 minute por: Order process shuru holo.");
//             // Baki code ekhane likhbi
//         }, 30, TimeUnit.MINUTES);

//         System.out.println("Baki system kintu cholche, freeze hoy ni!");
        
//         // Sheshe scheduler bondho kora dorkar
//         scheduler.shutdown();
//     }
// }


public class HubX {

    public static Product userInput() {

    Scanner sc = new Scanner(System.in);

    int id;
    while (true) {

        System.out.print("Enter Product ID: ");

        if (sc.hasNextInt()) {

            id = sc.nextInt();

            if (id > 0) {
                break;
            } else {
                System.out.println("ID must be positive...");
            }

        } else {

         System.out.println("Invalid Input! Enter integer only...");
            sc.next();
        }
    }

    sc.nextLine();

    String name;

    while (true) {

        System.out.print("Enter Product Name: ");

        name = sc.nextLine();

        if (!name.trim().isEmpty()) {
            break;
        } else {
            System.out.println("Product name cannot be empty...");
        }
    }
    double price;
    while (true) {

        System.out.print("Enter Product Price: ");

        if (sc.hasNextDouble()) {

            price = sc.nextDouble();

            if (price >= 0) {
                break;
            } else {
                System.out.println("Price cannot be negative...");
            }

        } else {

            System.out.println("Invalid Input! Enter numbers only...");
            sc.next();
        }
    }

    sc.nextLine();

    String category;

    while (true) {

        System.out.print("Enter Product Category: ");

        category = sc.nextLine();

        if (!category.trim().isEmpty()) {
            break;
        } else {
            System.out.println("Category cannot be empty...");
        }
    }

    int stock;

    while (true) {

        System.out.print("Enter Product Stock: ");

        if (sc.hasNextInt()) {

            stock = sc.nextInt();

            if (stock >= 0) {
                break;
            } else {
                System.out.println("Stock cannot be negative...");
            }

        } else {

            System.out.println("Invalid Input! Enter integer only...");
            sc.next();
        }
    }
    Product p = new Product(id, name, price, category, stock);

    return p;
}

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Order or = new Order();
        Inventory in = new Inventory();
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                       HUBX SMART WAREHOUSE                         ║");
            System.out.println("╠═════════════════════════════╦══════════════════════════════════════╣");
            System.out.println("║ 1 -> Add Product            ║ 6 -> Search Product by ID            ║");
            System.out.println("║ 2 -> Delete Product by ID   ║ 7 -> Sort Products by Price          ║");
            System.out.println("║ 3 -> Display Products       ║ 8 -> Add Shipping Order              ║");
            System.out.println("║ 4 -> Display Backward       ║ 9 -> Exit                            ║");
            System.out.println("║ 5 -> Undo Last Delete       ║                                      ║");
            System.out.println("╚═════════════════════════════╩══════════════════════════════════════╝");
            System.out.print("--:: Enter your choice  = > ");

            int ch = sc.nextInt();

            switch (ch) {

                case 1:

                    System.out.print("How many products you want to add: ");
                    int n = sc.nextInt();

                    for (int i = 1; i <= n; i++) {

                        Product p = userInput();

                        in.insertAtEnd(p);
                    }

                    System.out.println("  Product added successfully!!");
                    break;

                case 2:
                    System.out.print("Enter Product ID to delete: ");
                    int id2 = sc.nextInt();
                    in.deleteById(id2);
                    break;

                case 3:
                    in.forwardShow();
                    break;

                case 4:
                    in.backWardShow();
                    break;

                case 5:
                    in.undoLast();
                    break;

                case 6:
                    System.out.println("Enter Id for search : ");
                    int id = sc.nextInt();
                    Node2 iddd = in.searchbyId(id);
                    if (iddd != null) {
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-5s |\n",
                                "ID", "Product Name", "Category", "Price", "Stock");
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.println(iddd.data);
                        System.out.println("----------------------------------------------------------------------------");
                    } else {
                        System.out.println("Product Not Found...");
                    }
                    break;
                case 7:
                    if(in.head==null){
                         System.out.println("Product not present..");
                    }
                    in.sortbyPrice(in.head, in.end);
                    System.out.println("Product sorted successfully..");

                    break;

                case 8:
                    boolean flag = true;
                    while (flag) {

                        System.out.println("1:Add Item  2:Exit");
                        System.out.print("Enter choice:");
                        int ch2 = sc.nextInt();

                        switch (ch2) {
                            case 1:
                                System.out.println("Enter your product Id: ");
                                int id3 = sc.nextInt();
                                Node2 iddd2 = in.searchbyId(id3);
                                if (iddd2 != null) {
                                    System.err.println("Enter your Product Quantity: ");
                                    int quantity = sc.nextInt();
                                    if (iddd2.data.stock < quantity) {
                                        System.out.println(
                                                "Order not Placed.    Only " + iddd2.data.stock + " Availevel");
                                    } else {
                                        in.takeOrder(id3, quantity);

                                        iddd2.data.stock -= quantity;
                                    }
                                } else {
                                    System.out.println("No product Found...");
                                }
                                in.processShiping();
                                break;
                            case 2:
                                in.addOrderQueue();
                                in.orList.clear();
                                flag = false;
                                break;

                        }
                    }
                    break;
                case 9:
                    System.out.println("Exiting system...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }

    }
}
