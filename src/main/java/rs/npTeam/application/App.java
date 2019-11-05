package rs.npTeam.application;

import model.User;
import model.UserPriority;
import models.Local_Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App
{
    public static void main(String[] args) {

        Local_Storage.getInstance();

/*        System.out.println("Welcome!\nConnect to drive(1) or a local(2) storage?");
        Scanner sc = new Scanner(System.in);

        int answer = 0;
        if(sc.hasNextInt()) {
            answer = sc.nextInt();
        } else {
            System.out.println("Invalid input");
            return;
        }

        if(answer == 1) {
            System.out.println("Connecting to drive..");
        } else if(answer == 2) {
            System.out.println("Connecting to local..");
        } else {
            System.out.println("answer aerror");
        }*/

    }
}
