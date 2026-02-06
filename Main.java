import java.time.LocalDateTime;
import java.util.*;
import java.time.*;

interface ParkingOperations {

    void allotSlot(String carNumber);   // for ENTRY
    void exitCar(String carNumber);     // for EXIT
    long calculateFine(long hours);     // fine logic
}


class Car {
    String carNumber;
    LocalDateTime entryTime;

    Car(String carNumber) {
        this.carNumber = carNumber;
        this.entryTime = LocalDateTime.now();
    }
}

class ParkingSlot {
    ArrayList<Car> cars = new ArrayList<>();

    boolean hasSpace() {
        return cars.size() < 10;
    }

    void parkCar(Car car) {
        cars.add(car);
    }

    void removeCar(Car car) {
        cars.remove(car);
    }
}
class ParkingInfo {
    int floor;
    int slot;
    Car car;

    ParkingInfo(int floor, int slot, Car car) {
        this.floor = floor;
        this.slot = slot;
        this.car = car;
    }
}


class ParkingSystem implements ParkingOperations {

    ParkingSlot[][] parking = new ParkingSlot[3][10];
    HashMap<String, ParkingInfo> map = new HashMap<>();

    ParkingSystem() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 10; j++) {
                parking[i][j] = new ParkingSlot();
            }
        }
    }
    public void allotSlot(String carNumber) {

        if(map.containsKey(carNumber)) {
            System.out.println("Car already parked!");
            return;
        }

        Car car = new Car(carNumber);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 10; j++) {

                if(parking[i][j].hasSpace()) {

                    parking[i][j].parkCar(car);
                    map.put(carNumber, new ParkingInfo(i, j, car));

                    System.out.println("Car Parked!");
                    System.out.println("Floor: " + (i+1) + " Slot: " + (j+1));
                    return;
                }
            }
        }

        System.out.println("Parking Full!");
    }
    public void exitCar(String carNumber) {

        if(!map.containsKey(carNumber)) {
            System.out.println("Car not found!");
            return;
        }

        ParkingInfo info = map.get(carNumber);
        Car car = info.car;

        LocalDateTime exitTime = LocalDateTime.now();
        long hours = Duration.between(car.entryTime, exitTime).toHours();

        System.out.println("Car stayed: " + hours + " hours");

        long fine = calculateFine(hours);

        if(fine > 0)
            System.out.println("Fine: ₹" + fine);
        else
            System.out.println("No Fine");

        parking[info.floor][info.slot].removeCar(car);
        map.remove(carNumber);

        System.out.println("Car removed successfully");
    }
    public long calculateFine(long hours) {
        if(hours > 8) {
            return (hours - 8) * 50;
        }
        return 0;
    }
}


public class Main {
    public static void main(String[] args) {

        ParkingOperations system = new ParkingSystem();
        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\nEnter Car Number:");
            String carNo = sc.nextLine();

            System.out.println("Enter Action (ENTRY/EXIT):");
            String action = sc.nextLine();

            if(action.equalsIgnoreCase("ENTRY")) {
                system.allotSlot(carNo);
            }
            else if(action.equalsIgnoreCase("EXIT")) {
                system.exitCar(carNo);
            }
            else {
                System.out.println("Invalid Action!");
            }
        }
    }
}
