package com.firstgroup.project.controller;

import com.firstgroup.project.Exceptions.*;
import com.firstgroup.project.dataBase.DBService;
import com.firstgroup.project.hotels.Hotel;
import com.firstgroup.project.hotels.Room;
import com.firstgroup.project.hotels.User;
import com.firstgroup.project.loginService.LoginController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sonikb on 22.04.2017.
 */
public class ConsoleHelper {
    Controller controller = new Controller();
    LoginController loginController = new LoginController();

    public static void main(String[] args) {
        ConsoleHelper consoleHelper = new ConsoleHelper();
//        consoleHelper.mainMenu();
        consoleHelper.loginService();
    }


    public void loginService() {
        System.out.println("Что бы войти в систему создайте профиль или выполните вход с существуещего!");
        System.out.println("\n1. * Зарегистрироваться!");
        System.out.println("2. * Войти!");

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        if (!"1".equals(line) && !"2".equals(line)) {
            System.out.println("Не верная команда, повторите попытку!\n");
            loginService();
        }
        if ("1".equals(line)) {
            regUser();
            mainMenu();
        } else {
            enterToSystem();
        }
    }

    private void enterToSystem() {
        System.out.println("***Вход в систему***");
        String email;
        String password;
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Введите email: ");
            email = sc.nextLine();
            System.out.println("Введите password: ");
            password = sc.nextLine();
            if (loginController.loginUser(email, password)) {
                System.out.println("Вход выполнен " + loginController.getDbService().getDataBase().getCurrentUser().getName() + "\n");
                mainMenu();
            }
        } catch (IncorrectEmail | IncorrectPassword ex) {
            System.out.println(ex.getMessage());
            enterToSystem();
        }
    }

    private void regUser() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Введите Ваше имя");
            String name = sc.nextLine();
            System.out.println("Введите Вашу фамилию");
            String secondName = sc.nextLine();
            System.out.println("Введите Ваш email");
            String email = sc.nextLine();
            System.out.println("Введите PASSWORD");
            String password = sc.nextLine();

            User user = loginController.registerUser(name, secondName, email, password);
            System.out.println("Пользователь " + user.getEmail() + " успешно зарегистрирован!\n");
        } catch (UserAlreadyExist e) {
            System.out.println(e.getMessage());
            regUser();
        }
    }

    private void deleteUser() {
        System.out.println("Cписок пользователей----------------------------------------\n");
        Set<Map.Entry<String, User>> entries = controller.getDbService().getDataBase().getUserMap().entrySet();
        int count = 1;
        for (Map.Entry<String, User> entry : entries) {
            System.out.println(count++ + "." + " " + entry.getValue());
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("\nУкажите email пользователя которого вы хотите удалить");
        System.out.println("\nДля возврата в меню введите \"0\"");
        String email = sc.nextLine();
        if ("0".equals(email))mainMenu();
        try {
            System.out.println("Пользователь " + controller.deleteUser(email).getEmail() + " удалён!\n");
        } catch (UserNotCreated | CantDeleteCurrentUser ex) {
            System.out.println(ex.getMessage());
            deleteUser();
        }
        int count2 = 1;
        for (Map.Entry<String, User> entry : entries) {
            System.out.println(count2++ + " " + entry.getValue());
        }
        mainMenu();


    }

    public void mainMenu() {

        System.out.println("-->Система бронирования отелей<--" + "\n");

        System.out.println("1. * Добавить отель");
        System.out.println("2. * Редактировать данные отеля");
        System.out.println("3. * Добавить комнату в отель");
        System.out.println("4. * Редактировать данные комнаты");
        System.out.println("5. * Удалить комнату из отеля");
        System.out.println("6. * Удалить отель");
        System.out.println("7. * Зарегистрировать пользователя");
        System.out.println("8. * Редактировать данные пользователя");
        System.out.println("9. * Удалить пользователя");
        System.out.println("10. * Поиск отеля по имени");
        System.out.println("11. * Поиск отеля по городу");
        System.out.println("12. * Поиск комнаты по отелю");
        System.out.println("13. * Бронирование комнаты на имя пользователя");
        System.out.println("14. * Отмена бронирования комнаты" + "\n");
        System.out.println("0. * ВЫХОД");
        chooseTheOperation();
    }

    private void chooseTheOperation() {
        System.out.println("Введите номер операции которую вы хотите произвести!!!");
        try {
            Scanner sc = new Scanner(System.in);
            switch (sc.nextInt()) {
                case 1:
                    System.out.println("\n***** Добавление отеля в базу данных *****\n");
                    addHotel();
                    break;
                case 2:
                    System.out.println("\n***** Редактирование данных отеля *****\n");
                    break;
                case 3:
                    System.out.println("\n***** Добавление комнаты в отель *****\n");
                    addRoom();
                    break;
                case 4:
                    System.out.println("\n***** Редактирование данных комнаты *****\n");
                    break;
                case 5:
                    System.out.println("\n***** Удалиение комнат из отеля *****\n");
                    deleteRoom();
                    break;
                case 6:
                    System.out.println("\n***** Удаление отелей *****\n");
                    deleteHotel();
                    break;
                case 7:
                    System.out.println("\n***** Регистрация пользователей *****\n");
                    break;
                case 8:
                    System.out.println("\n***** Редактирование данных пользователя *****\n");
                    break;
                case 9:
                    System.out.println("\n***** Удаление пользователей *****\n");
                    deleteUser();
                    break;
                case 10:
                    System.out.println("\n***** Поиск отеля по имени *****\n");
                    break;
                case 11:
                    System.out.println("\n***** Поиск отеля по городу *****\n");
                    break;
                case 12:
                    System.out.println("\n***** Поиск комнаты по отелю *****\n");
                    break;
                case 13:
                    System.out.println("\n***** Бронирование комнаты на имя пользователя *****\n");
                    break;
                case 14:
                    System.out.println("\n***** Отмена бронирования комнаты *****\n");
                    break;
                case 0:
                    System.out.println("\n***** Программа завершена, все изменения сохранены *****\n");
                    DBService.save();
                    sc.close();
                    break;
                default:
                    System.out.println("Не верный номер операции! Повторите попытку!" + " \nДля выхода нажмите \"0\"");
                    chooseTheOperation();
            }
        } catch (IOException e) {
            e.printStackTrace();              //TODO create some exception
        } catch (NoSuchElementException n) {
            mainMenu();
        }
    }

    private void addHotel() throws IOException {
        Hotel hotel = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Укажите название отеля:");
        String hotelName = scanner.nextLine();
        System.out.println("Укажите название города:");
        String cityName = scanner.nextLine();
        System.out.println("Для создания отеля необходимо создать хотя бы одну комнату!");
        System.out.println("Укажите количество спальных мест в номере:");
        int roomPersons = scanner.nextInt();
        System.out.println("Укажите цену номера в грн:");
        double roomPrice = scanner.nextDouble();
        System.out.println("Укажите дату когда номер будет доступен в формате year.mm.dd");
        scanner.nextLine();
        String dateAvailableFrom = scanner.nextLine();
        try {
            hotel = controller.addHotel(hotelName, cityName, roomPersons, roomPrice, dateAvailableFrom);
        } catch (HotelAlreadyExist r) {
            r.getMessage();
            addHotel();
        }
        System.out.println(hotel.getHotelName() + " успешно сохранен!");
        mainMenu();
    }

    private void addRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите отель в котором вы хотите добавить комнату:");
        int count = 1;
        List<String> hotelNames = controller.getDbService().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        int hotelIndex = scanner.nextInt() - 1;
        Hotel hotel = controller.getDbService().getDataBase().getHotelList().get(hotelIndex);
        System.out.println("**** Добавление комнат в отель " + hotel.getHotelName() + " ****");
        System.out.println("Укажите количество спальных мест в номере:");
        int roomPersons = scanner.nextInt();
        System.out.println("Укажите цену номера в грн:");
        double roomPrice = scanner.nextDouble();
        System.out.println("Укажите дату когда номер будет доступен в формате year.mm.dd");
        scanner.nextLine();
        String dateAvailableFrom = scanner.nextLine();
        controller.addRoom(hotelIndex, roomPersons, roomPrice, dateAvailableFrom);
        System.out.println("Комната сохранена в отель " + hotel.getHotelName());
//        controller.getDbService().getDataBase().getHotelList().get(hotelIndex).getRoomList().stream().forEach(System.out::println); // Testing, after remove
        mainMenu();
    }

    private void deleteHotel() {
        System.out.println("Список отелей : ");
        int count = 1;
        List<String> hotelNames = controller.getDbService().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());

        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        System.out.println("Введите имя отеля которий ви хотите удалить...");
        Scanner in = new Scanner(System.in);
        String hotelName = in.nextLine();
        controller.deleteHotel(hotelName);


        System.out.println("Список отелей после удаления: ");
        List<String> hotelNamesWhenWeDeleteHotel = controller.getDbService().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        int count1 = 1;
        for (String hotel : hotelNamesWhenWeDeleteHotel) {
            System.out.println(count1++ + ". * " + hotel);
        }
        mainMenu();
    }

    private void deleteRoom() {
        System.out.println("Виберете отель из которого ви хотите удалить комануту: ");
        int count = 1;
        List<String> hotelNames = controller.getDbService().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Введите имя отеля из которого ви хотите удалить:");
        String hotelName = in.nextLine();
        Hotel hotel = controller.getDbService().getDataBase().getHotelList().stream()
                .filter(hotel1 -> hotel1.getHotelName().equals(hotelName)).findFirst().get();
        System.out.println("**** Удаление комнат в отеле " + hotel.getHotelName() + " ****");
        List<Room> roomList = new ArrayList<>(hotel.getRoomList());
        count = 1;
        for (Room room : roomList) {
            System.out.println(count++ + ". * " + room);
        }
        controller.deleteRoom();
    }
}