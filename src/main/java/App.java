import java.util.Scanner;

public class App {

    private String storage_path;

    public static void main(String[] args) {

        System.out.println("Please choose:");
        System.out.println("1. Cloud storage");
        System.out.println("2. Local storage");

        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch(option){
            case 1:
                try {
                    Cloud_Implementation_Test.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Local_Implementation_Test.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Wrong option!");
        }

    }

}
